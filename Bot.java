import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.reflect.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import org.apache.http.client.methods.*;

public class Bot {
	private boolean isLoggedIn = false;
	Config conf;
	WS socket;
	String[] formats;
	Commands com;
	JSONObject lgdt;
	Room[] rooms;
	Bot (Config conf) {
		this.conf = conf;
		com = new Commands(this);
		try {
			socket = new WS(new URI("ws://" + InetAddress.getByName(conf.server).getHostAddress() + ":" + conf.port + "/showdown/websocket"), this); //51.79.72.237:8000/showdown/websocket
			socket.connect();
		} catch (URISyntaxException use) {
			System.err.println("Error: Invalid URI");
		} catch (UnknownHostException uhe) {
			System.err.println("Error: Invalid Servername or Port");
		}
		rooms = new Room[1];
	}

	public void setFormats (String s) {
		String[] f = s.split("\\|");
		formats = new String[f.length - 2];
		for (int i = 2; i < f.length; i++) {
			System.out.println(f[i]);
			formats[i - 2] = f[i];
		}
	}

	public void handleMessage (String s) {
		String[] ms = s.split("\n");
		System.out.println("Message length: " + ms.length);
		Room curRoom = null;
		if (s.startsWith(">")) {
			if (ms[1].startsWith("|init|")) {
				if (rooms[0] == null) {
					rooms[0] = new Room(s, this);
					curRoom = rooms[0];
				} else {
					rooms = Arrays.copyOf(rooms, rooms.length + 1);
					rooms[rooms.length - 1] = new Room(s, this);
					curRoom = rooms[rooms.length - 1];
				}
			}
		} else if (s.startsWith("|init|")) {
			if (rooms[0] == null) {
				rooms[0] = new Room(">lobby\n" + s, this);
				curRoom = rooms[0];
			} else {
				rooms = Arrays.copyOf(rooms, rooms.length + 1);
				rooms[rooms.length - 1] = new Room(">lobby\n" + s, this);
				curRoom = rooms[rooms.length - 1];
			}
		} /*else {
			curRoom = new Room(">lobby\n|init|" + s, this);
		}*/
		for (int i = 0; i < ms.length; i++) {
			if (ms[i].startsWith("|pm|")) {
				String bd[] = ms[i].split("\\|");
				this.parseChatMessage(bd[2], bd[4], null);
			} else if (ms[i].startsWith("|formats|")) {
				setFormats(ms[i]);
			} else if (ms[i].startsWith("|challstr|")) {
				this.login(ms[i].substring(10));
			} else if (curRoom != null && (ms[i].startsWith("|J|") || ms[i].startsWith("|L|") || ms[i].startsWith("|N|"))) {
				curRoom.updateUL(ms[i]);
			} else if (curRoom != null && (ms[i].startsWith("|c:|") || ms[i].startsWith("|c|"))) {
				String bd[] = ms[i].split("\\|");
				this.parseChatMessage(bd[3], bd[4], curRoom);
			}
			//System.out.println(ms[i]);
		}
	}

	public void login (String s) {
		try {
			String jsonop = "{\"act\":\"login\",\"name\":\"" + this.conf.nick + "\",\"pass\":\"" + this.conf.pass + "\",\"challstr\":\"" + s + "\"}";
			URL UrlObj = new URL("http://play.pokemonshowdown.com/action.php");
			HttpURLConnection connection = (HttpURLConnection) UrlObj.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json; utf-8");
			connection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(jsonop);
			try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				System.out.println(response.toString());
				JSONParser jp = new JSONParser();
				lgdt = (JSONObject) jp.parse(response.toString().substring(1));
			} finally {
				wr.flush();
				wr.close();
			}wr.flush();
				wr.close();
		}
		catch (Exception e) {
			System.err.println("Something went wrong.");
			return;
		}
		if (lgdt != null) {
			this.sendToServer("|/trn " + conf.nick + ",0," + lgdt.get("assertation"));
			this.isLoggedIn = true;
			this.sendToServer("|/avatar " + conf.avatar);
			for (int i = 0; i < conf.rooms.length; i++) {
				this.sendToServer("|/join " + conf.rooms[i]);
			}
		}
	}

	public void sendToServer (String s) {
		this.socket.send(s);
	}

	public Room getRoom (String s) {
		if (this.rooms.length <= 0) return null;
		for (int i = 0; i < this.rooms.length; i++) {
			if (this.rooms[i].id.equals(IO.toId(s))) return this.rooms[i];
		}
		return null;
	}

	public void sendPM (String user, String message) {
		this.sendToServer("|/w " + user + ", " + message);
	}

	public void parseChatMessage (String user, String message, Room r) {
		char rank = user.charAt(0);
		boolean isCommand = false;
		for (int i = 0; i < conf.comchars.length; i++) {
			if (message.startsWith(conf.comchars[i] + "")) isCommand = true;
		}
		if (r != null && !isCommand) return;
		if (!isCommand) {
			this.sendPM(user, "Hey! I am " + this.conf.nick + ". My command characters are: " + IO.join(this.conf.comchars, ", ") + " (Use the help command with a command character like .help to get help on commands)");
			return;
		}
		com.callCommand(message.substring(1).split(" ")[0], user, message.substring(message.indexOf(" ") + 1), r);
	}
}