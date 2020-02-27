import java.net.*;
import java.io.*;
public class Room {
	String id, type, title;
	String[] userlist;
	Room (String initstring) {
		String[] initlist = initstring.split("\n");
		id = IO.toId(initlist[0]);
		type = initlist[1].split("|")[2];
		title = initlist[2].split("|")[2];

		userlist = initlist[3].split("|")[2].split(",");
	}

	void updateUL (String opts) {
		String[] options = opts.split("|");
		if (options[1].equalsIgnoreCase("J")) {
			userlist = IO.arrayMod(userlist, "add" + options[2]);
		} else if (options[1].equalsIgnoreCase("L")) {
			userlist = IO.arrayMod(userlist, "del" + options[2]);
		} else if (options[1].equalsIgnoreCase("N")) {
			userlist = IO.arrayMod(userlist, "del" + options[3]);
			userlist = IO.arrayMod(userlist, "add" + options[2]);
		}
	}

	public static void main (String args[]) throws IOException, UnknownHostException {
		Config c = new Config();
		System.out.println(c.server);
		System.out.println(c.port);
		System.out.println(InetAddress.getByName(c.server));
	}
}
