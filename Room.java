import java.net.*;
import java.io.*;
public class Room {
	String id, type, title;
	String[] userlist;
	Bot bot;
	char authrank;
	public Room (String initstring, Bot bot) {
		this.bot = bot;
		String[] initlist = initstring.split("\n");
		id = IO.toId(initlist[0]);
		type = initlist[1].split("\\|")[2];
		title = initlist[2].split("\\|")[2];
		userlist = initlist[3].split("\\|")[2].split(",");
		authrank = '+';
	}

	void updateUL (String opts) {
		String[] options = opts.split("\\|");
		if (options[1].equalsIgnoreCase("J")) {
			userlist = IO.arrayMod(userlist, "add" + options[2]);
		} else if (options[1].equalsIgnoreCase("L")) {
			userlist = IO.arrayMod(userlist, "del" + options[2]);
		} else if (options[1].equalsIgnoreCase("N")) {
			userlist = IO.arrayMod(userlist, "del" + options[3]);
			userlist = IO.arrayMod(userlist, "add" + options[2]);
		}
	}

	void send (String user, String message) {
		if (user.length() == 0 || user.charAt(0) == ' ') {
			bot.sendPM(user, message);
		}
		bot.sendToServer(this.id + "|" + message);
	}

	void send (String message) {
		bot.sendToServer(this.id + "|" + message);
	}
}