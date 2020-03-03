import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.reflect.*;
import org.json.*;
import org.apache.http.client.methods.*;

public class Bot {
	private boolean isLoggedIn = false;
	Config conf;
	WS socket;
	String[] formats;
	Commands com;
	JSONObject lgdt;
	Room[] rooms;
	String username, id;
	char defaultAuthRank;
	String lastM = "";
	Bot () {
		this.conf = new Config();
		com = new Commands(this);
		try {
			socket = new WS(new URI("ws://" + InetAddress.getByName(conf.server).getHostAddress() + ":" + conf.port + "/showdown/websocket"), this);
			socket.connect();
		} catch (URISyntaxException use) {
			System.err.println("Error: Invalid URI");
		} catch (UnknownHostException uhe) {
			System.err.println("Error: Invalid Servername or Port");
		}
		this.rooms = new Room[1];
		this.defaultAuthRank = '~';
	}

	public void setFormats (String s) {
		String[] f = s.split("\\|");
		formats = new String[f.length - 2];
		for (int i = 2; i < f.length; i++) {
			formats[i - 2] = f[i];
		}
		IO.println("Received Formats: " + formats.length);
	}

	public void handleMessage (String s) {
		String[] ms = s.split("\n");
		Room curRoom = this.getRoom("lobby");
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
				IO.println("Joined room " + curRoom.title);
				return;
			}
			curRoom = this.getRoom(IO.toId(ms[0]));
		} else if (s.startsWith("|init|")) {
			if (rooms[0] == null) {
				rooms[0] = new Room(">lobby\n" + s, this);
				curRoom = rooms[0];
			} else {
				rooms = Arrays.copyOf(rooms, rooms.length + 1);
				rooms[rooms.length - 1] = new Room(">lobby\n" + s, this);
				curRoom = rooms[rooms.length - 1];
			}
			System.out.println("Joined room " + curRoom.title);
			return;
		}
		for (int i = 0; i < ms.length; i++) {
			if (ms[i].length() == 0) {
				continue;
			} else if (ms[i].startsWith("|challstr|")) {
				this.login(ms[i].substring(10));
			} else if (ms[i].startsWith("|formats|")) {
				this.setFormats(ms[i]);
			} else if (ms[i].startsWith("|pm|")) {
				String bd[] = ms[i].split("\\|");
				this.parseChatMessage(bd[2], bd[4], null);
			} else if (ms[i].startsWith("|updateuser|")) {
				String bd[] = ms[i].split("\\|");
				this.username = bd[2];
				this.id = IO.toId(bd[2]);
				IO.println("Username is now: " + this.username);
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
				lgdt = new JSONObject (response.toString().substring(1));
			} finally {
				wr.flush();
				wr.close();
			}
		}
		catch (Exception e) {
			System.err.println("Something went wrong.");
			e.printStackTrace();
			return;
		}
		if (lgdt != null) {
			try {
				this.sendToServer("|/trn " + conf.nick + ",0," + lgdt.getString("assertion"));
			} catch (JSONException jsone) {
				System.err.println("Cant read JSON.");
				return;
			}
			this.isLoggedIn = true;
			System.out.println("Succesfully logged in.");
			System.out.println("Command Characters: " + IO.join(conf.comchars, " "));
			this.sendToServer("|/avatar " + conf.avatar);
			for (int i = 0; i < conf.rooms.length; i++) {
				this.sendToServer("|/join " + conf.rooms[i]);
			}
		}
	}

	public void sendToServer (String s) {
		if (lastM.equals(s)) return;
		this.socket.send(s);
	}

	public Room getRoom (String s) {
		if (this.rooms.length <= 0) return null;
		for (int i = 0; i < this.rooms.length; i++) {
			if (this.rooms[i] != null && this.rooms[i].id.equals(IO.toId(s))) return this.rooms[i];
		}
		return null;
	}

	public void sendPM (String user, String message) {
		this.sendToServer("|/w " + user + ", " + message);
	}

	public void parseChatMessage (String user, String message, Room r) {
		if (IO.toId(user) == this.id) return;
		char rank = user.charAt(0);
		boolean isCommand = false;
		for (int i = 0; i < conf.comchars.length; i++) {
			if (message.startsWith(conf.comchars[i] + "")) isCommand = true;
		}
		if (!isCommand && r == null) {
			this.sendPM(user, "Hey! I am " + this.conf.nick + ". My command characters are: " + IO.join(this.conf.comchars, ", ") + " (Use the help command with a command character like " + this.conf.comchars[0] + "help to get help on commands)");
			return;
		}
		if (!isCommand) return;
		com.callCommand(message.substring(1).split(" ")[0], user, message.substring(message.indexOf(" ") + 1), r);
	}

	public boolean hasUserAuth (String username, char requiredRank) {
		if (requiredRank == '\0') requiredRank = this.defaultAuthRank;
		String userid = IO.toId(username);
		if (IO.indexOf(conf.devs, userid) >= 0) return true;
		if (IO.indexOf(conf.ranks, username.charAt(0)) >= IO.indexOf(conf.ranks, requiredRank)) return true;
		return false;
	}

	public void killProcess () {
		this.socket.close();
	}

	public boolean reloadCommands () {
		try {
			Commands com = new Commands(this);
			this.com = com;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		IO.println("Commands reloaded");
		return true;
	}

	public boolean reloadConfig () {
		try {
			Config conf = new Config();
			this.conf = conf;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		IO.println("Config reloaded");
		return true;
	}

	public void restartProcess() {
		this.socket.close();
		this.reloadConfig();
		this.reloadCommands();
		try {
			socket = new WS(new URI("ws://" + InetAddress.getByName(conf.server).getHostAddress() + ":" + conf.port + "/showdown/websocket"), this);
			socket.connect();
		} catch (URISyntaxException use) {
			System.err.println("Error: Invalid URI");
		} catch (UnknownHostException uhe) {
			System.err.println("Error: Invalid Servername or Port");
		}
		this.rooms = new Room[1];
		this.defaultAuthRank = '~';
	}
}