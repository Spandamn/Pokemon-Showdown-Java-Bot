public class Room {
	String id, type, title;
	String[] userlist;
	Room (String initstring) {
		String[] initlist = String.split("\n", initstring);
		id = IO.toId(initlist[0]);
		type = String.split("|", initlist[1])[2];
		title = String.split("|", initlist[2])[2];
		userlist = String.split(",", String.split("|", initlist[3])[2]);
	}

	void updateUL (String opts) {
		String[] options = String.split("|", opts);
		if (options[1].equalsIgnoreCase("J")) {
			userlist = IO.arrayMod(userlist, "add" + options[2]);
		} else if (options[1].equalsIgnoreCase("L")) {
			userlist = IO.arrayMod(userlist, "del" + options[2]);
		} else if (options[1].equalsIgnoreCase("N")) {
			userlist = IO.arrayMod(userlist, "del" + options[3]);
			userlist = IO.arrayMod(userlist, "add" + options[2]);
		}
	}
}