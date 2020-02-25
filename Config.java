import java.io.*;

public class Config {
	String nick, pass, avatar, server;
	String[] rooms, devs;
	char[] comchars, ranks;
	public Config () throws IOException {
		FileReader conf;
		BufferedReader br = null;
		try {
			conf = new FileReader("details.txt");
			br = new BufferedReader(conf);
			String nextL;
			while ((nextL = br.readLine()) != null) {
				if (nextL.startsWith("nickname")) {
					nick = nextL.split(":")[1];
				} else if (nextL.startsWith("password")) {
					pass = nextL.split(":")[1];
				} else if (nextL.startsWith("avatar")) {
					avatar = nextL.split(":")[1];
				} else if (nextL.startsWith("rooms")) {
					rooms = nextL.split(":")[1].split(",");
				} else if (nextL.startsWith("devs")) {
					devs = nextL.split(":")[1].split(",");
				} else if (nextL.startsWith("server")) {
					server = nextL.split(":")[1];
				} else if (nextL.startsWith("comchars")) {
					comchars = nextL.split(":")[1].toCharArray();
				} else if (nextL.startsWith("ranks")) {
					ranks = nextL.split(":")[1].toCharArray();
				}
			}
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}

	/*public static void main (String args[]) throws IOException {
		Config c = new Config();
		System.out.println("Nickname: " + c.nick);
		System.out.println("Password: " + c.pass);
		System.out.println("Avatar: " + c.avatar);
		System.out.println("Rooms: " + String.join(", ", c.rooms));
		System.out.println("Devs: " + String.join(", ", c.devs));
		System.out.println("Server: " + c.server);
		System.out.println("Command Characters: '" + c.join(c.comchars, "', '") + "'");
		System.out.println("Ranks: '" + c.join(c.ranks, "', '") + "'");
	}*/

	String join (char[] arr, String s) {
		String n = "";
		for (int i = 0; i < arr.length; i++) {
			if (i == arr.length - 1) break;
			n = n + arr[i] + s;
		}
		return n;
	}
}