import java.io.*;
import java.net.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Bot {
	private boolean isLoggedIn = false;
	Config conf;
	WS socket;
	String[] formats;
	Commands com;
	JSONObject lgdt;
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
		for (int i = 0; i < ms.length; i++) {
			if (ms[i].startsWith("|formats|")) {
				setFormats(ms[i]);
			} else if (ms[i].startsWith("|challstr|")) {
				this.login(ms[i].substring(10));
			}
			//System.out.println(ms[i]);
		}
	}

	public void login (String s) {
		/*JSONObject json = new JSONObject();
		json.put("act","login");
		json.put("name", this.conf.nick);
		json.put("pass", this.conf.pass);
		json.put("challstr", s);*/
		try {
			String jsonop = "{\"act\":\"login\",\"name\":\"" + this.conf.nick + "\",\"pass\":\"" + this.conf.pass + "\",\"challstr\":\"" + s + "\"}";
			URL UrlObj = new URL("http://play.pokemonshowdown.com/action.php");
			HttpURLConnection connection = (HttpURLConnection) UrlObj.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json; utf-8");
			connection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(jsonop);
			wr.flush();
			wr.close();
			try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				System.out.println(response.toString());
				JSONParser jp = new JSONParser();
				lgdt = (JSONObject) jp.parse(response.toString().substring(1));
			}
		}
		catch (Exception e) {
			System.err.println("Something went wrong.");
			return;
		}
		if (lgdt != null) {
			this.sendToServer("|/trn " + conf.nick + ",0," + lgdt.get("assertation"));
			this.sendToServer("|/avatar " + conf.avatar);
			for (int i = 0; i < conf.rooms.length; i++) {
				this.sendToServer("|/join " + conf.rooms[i]);
			}
		}
	}

	public void sendToServer (String s) {
		this.socket.send(s);
	}
}