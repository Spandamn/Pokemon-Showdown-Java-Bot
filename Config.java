import java.io.*;
import org.json.*;

public class Config {
	String nick, pass, server;
	String[] rooms, devs;
	int port, avatar;
	char[] comchars, ranks; // Actually character arrays; but JSON will import them as strings
	public Config () {
		JSONObject lgdt;
		try {
			String file = "";
			BufferedReader read = new BufferedReader(new FileReader("details.json"));
			String line =  "";
			while ((line = read.readLine()) != null) file += line + "\n";
			lgdt = new JSONObject (file);
			nick = lgdt.getString("nickname");
			pass = lgdt.getString("password");
			avatar = lgdt.getInt("avatar");
			//System.out.println(lgdt.get("rooms").getClass().getName());
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
			this.comchars  = new char[comchars.length()];
			for (int i = 0; i < comchars.length(); i++) {
				this.comchars[i] = comchars.getString(i).charAt(0);
			}
			this.ranks  = new char[ranks.length()];
			for (int i = 0; i < ranks.length(); i++) {
				this.ranks[i] = ranks.getString(i).charAt(0);
			}
		} catch (IOException e) {}
		catch (JSONException jsone) {
			System.err.println("Error: Cant read JSON file");
		}
	}
}
