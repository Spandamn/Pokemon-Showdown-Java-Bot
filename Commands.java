import java.net.*;
import java.util.*;
import java.lang.reflect.*;
/*
 * Bot commands file
 * all command function names should start with a _.
 * Each command method takes parameters (String user, String message, Method m, Object ob)
 * Method m is the method dedicated for the command to call depending on Rooms, ranks, etc.
 * Object ob is equal to a Room object if command was called in a Room.
 */
public class Commands {
	Bot bot;
	String[] cmdList;
	private Method[] methods;
	Commands (Bot bot) {
		this.bot = bot;
		Class thisClass = Commands.class;
		methods = thisClass.getDeclaredMethods();
		int cmdCount = 0;
		for (int i = 0; i < methods.length; i++) {
			String fnName = methods[i].getName();
			IO.println(fnName);
			if (fnName.startsWith("_")) cmdCount++;
		}
		cmdList = new String[cmdCount];
		cmdCount = 0;
		for (int i = 0; i < methods.length; i++) {
			String fnName = methods[i].getName();
			if (fnName.startsWith("_")) {
				cmdList[cmdCount++] = fnName.substring(1);
			}
		}
	}

	public void callCommand (String cmd, String user, String mess, Room room) {
		IO.println("In callCommand() method");
		Method send;
		Class[] paramTypes = {String.class, String.class};
		String sendType = "PM";
		try {
			System.out.println("In try block");
			if (room == null) {
				send = bot.getClass().getMethod("sendPM", paramTypes);
			} else {
				IO.println("Room exists. getting room method send");
				sendType = "room";
				send = room.getClass().getMethod("send", paramTypes);
			}
			if (IO.indexOf(cmdList, IO.toId(cmd)) < 0) {
				IO.println("Command not found. Invoking send");
				send.invoke(sendType.equals("PM") ? bot : room, user, "Error; Command not found");
				return;
			}
			Class[] params = {String.class, String.class, Method.class, Room.class};
			IO.println("Declared class paramaters, getting command");
			Method com = this.getClass().getMethod("_" + cmd, params);
			IO.println("Successfully got command, invoking");
			com.invoke(com, user, mess, send, sendType.equals("PM") ? null : room);
			IO.println("Command successful");
		} catch (NoSuchMethodException nsme) {
			System.out.println("Oops! Command went wrong.");
			nsme.printStackTrace();
		} catch (IllegalAccessException iae) {
			System.out.println("Oops! Something went wrong.");
		} catch (InvocationTargetException ite) {
			System.out.println("Oops! Something went wrong.");
		} catch (Exception e) {
			IO.println(e.toString());
			e.printStackTrace();
		}
	}

	public void _help (String user, String mess, Method send, Room ob) throws IllegalAccessException, InvocationTargetException {
		send.invoke(ob == null ? this.bot : ob, user, "List of available commands: " + IO.join(cmdList, ", "));
	}

	public void _changeAuthRank (String user, String mess, Method send, Room ob) throws IllegalAccessException, InvocationTargetException {
		if (mess.length() >= 1 || mess.length() == 0) {
			send.invoke(ob == null ? this.bot : ob, user, "Error: Invalid rank");
		}
		char defaultAuthRank = ob == null ? this.bot.defaultAuthRank : ob.defaultAuthRank;
		if (!bot.hasUserAuth(user, defaultAuthRank)) return;
		if (ob == null) {
			bot.defaultAuthRank = mess.charAt(0);
			send.invoke(bot, user, "Successful.");
			return;
		}
		ob.allowCommands(mess.charAt(0));
		send.invoke(ob, user, "Successful!");
	}
}