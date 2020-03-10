import java.io.*;
import org.json.*;

public class Config {
	String nick, pass, server;
	String[] rooms, devs;
	int port, avatar;
	char[] comchars, ranks; // Actually character arrays; but JSON will import them as strings
	public Config (FileReader f) {
		JSONObject lgdt;
		try {
			String file = "";
			BufferedReader read = new BufferedReader(r);
			String line =  "";
			while ((line = read.readLine()) != null) file += line + "\n";
			lgdt = new JSONObject (file);
			nick = lgdt.getString("nickname");
			pass = lgdt.getString("password");
			avatar = lgdt.getInt("avatar");
			JSONArray rooms = (JSONArray) lgdt.get("rooms");
			JSONArray devs = (JSONArray) lgdt.get("devs");
			server = lgdt.getString("server");
			port = lgdt.getInt("port");
			JSONArray comchars = (JSONArray) lgdt.get("comchars");
			JSONArray ranks = (JSONArray) lgdt.get("ranks");
			this.rooms  = new String[rooms.length()];
			for (int i = 0; i < rooms.length(); i++) {
				this.rooms[i] = rooms.getString(i);
			}
			this.devs  = new String[devs.length()];
			for (int i = 0; i < devs.length(); i++) {
				this.devs[i] = devs.getString(i);
			}
			IO.println("Devs: " + String.join(", ", this.devs));
			this.comchars  = new char[comchars.length()];
			for (int i = 0; i < comchars.length(); i++) {
				this.comchars[i] = comchars.getString(i).charAt(0);
			}
			this.ranks  = new char[ranks.length()];
			for (int i = 0; i < ranks.length(); i++) {
				this.ranks[i] = ranks.getString(i).charAt(0);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (JSONException jsone) {
			System.err.println("Error: Cant read JSON file");
		}
		IO.println("Config loaded");
	}

	public Config () {
		IO io = new IO();
		io.println("Please enter the username: ");
		this.nick = io.getLine();
		io.println("Please enter the password: ");
		this.pass = io.getLine();
		io.println("Please enter the avatar number: ");
		this.avatar = io.getInt();
		io.println("Please enter the room to join:");
		this.rooms = new String[1];
		this.rooms[0] = io.getLine();
		io.println("Please enter the developer username on PS:");
		this.devs = new String[1];
		this.devs[0] = io.getLine();
		io.println("Please enter the server:");
		this.server = io.getLine();
		io.println("Please enter the port");
		this.port = io.getInt();
		this.port = this.port == 0 ? 8000 : this.port;
		io.println("Please enter the command character");
		this.comchars = new char[1];
		this.comchars[0] = io.getChar();
		this.ranks = new char[] {'+','%','@','★','*','#', '&','~'};
	}
}
