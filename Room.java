import java.net.*;
import java.io.*;
import java.util.*;

public class Room {
	String id, type, title;
	String[] userlist;
	Bot bot;
	char defaultAuthRank;
	public Room (String initstring, Bot bot) {
		this.bot = bot;
		String[] initlist = initstring.split("\n");
		id = IO.toId(initlist[0]);
		type = initlist[1].split("\\|")[2];
		title = initlist[2].split("\\|")[2];
		userlist = initlist[3].split("\\|")[2].split(",");
		char defaultAuthRank = '+';
		IO.println("Room " + this.title + " initialized");
	}

	public void updateUL (String opts) {
		String[] options = opts.split("\\|");
		if (options[1].equalsIgnoreCase("J")) {
			this.userlist = IO.arrayMod(this.userlist, "add" + options[2]);
		} else if (options[1].equalsIgnoreCase("L")) {
			if (options[2].equals(IO.toId(options[2]))) {
				// User has logged out
				this.userLogout(options[2]);
				return;
			} else if (IO.indexOf(this.userlist, options[2]) < 0) {
				// User apparently does not exist in userlist
				return;
			}
			this.userlist = IO.arrayMod(userlist, "del" + options[2]);
		} else if (options[1].equalsIgnoreCase("N")) {
			if (options[2].endsWith("@!")) {
				//User is busy/away
				return;
			}
			this.userlist = IO.arrayMod(userlist, "del" + options[3]);
			this.userlist = IO.arrayMod(userlist, "add" + options[2]);
		}
	}

	void allowCommands (char c) {
		defaultAuthRank = c;
	}

	public void send (String user, String message) {
		if ((user.length() == 0 || user.charAt(0) == ' ') && !bot.hasUserAuth(user, defaultAuthRank)) {
			bot.sendPM(user, message);
			return;
		}
		bot.sendToServer(this.id + "|" + message);
	}

	public void send (String message) {
		bot.sendToServer(this.id + "|" + message);
	}

	public void userLogout (String user) {
		for (int i = 0; i < this.userlist.length; i++) {
			if (user.equals(IO.toId(this.userlist[i]))) {
				this.userlist = IO.arrayMod(this.userlist, this.userlist[i]);
				return;
			}
		}
	}
}
