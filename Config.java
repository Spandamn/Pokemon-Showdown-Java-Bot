import java.io.*;

public class Config {
	String nick, pass, avatar, server;
	String[] rooms, devs;
	int port;
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
				} else if (nextL.startsWith("port")) {
					port = Integer.parseInt(nextL.split(":")[1]);
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
}
