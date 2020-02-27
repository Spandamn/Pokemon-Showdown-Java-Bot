import java.net.*;
import java.util.*;
import java.lang.reflect.*;

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
			String fnName = methods[i].toString();
			if (fnName.startsWith("_")) cmdCount++;
		}
		cmdList = new String[cmdCount];
		cmdCount = 0;
		for (int i = 0; i < methods.length; i++) {
			String fnName = methods[i].toString();
			if (fnName.startsWith("_")) {
				cmdList[cmdCount++] = fnName.substring(1);
			}
		}
	}

	public void callCommand (String cmd, String user, String mess, Room room) {
		Method send;
		Object ob;
		Class<?>[] paramTypes = {String.class, String.class};
		try {
			if (room == null) {
				ob = bot;
				send = bot.getClass().getMethod("sendPM", paramTypes);
			} else {
				ob = room;
				send = room.getClass().getMethod("send", paramTypes);
			}
			if (IO.searchArr(cmdList, cmd) < 0) {
				send.invoke(ob, user, "Error; Command not found");
				return;
			}
			Class<?>[] params = {String.class, String.class, Method.class, Object.class};
			Method com = this.getClass().getMethod(cmd, params);
			com.invoke(user, mess, send, ob);
		} catch (NoSuchMethodException nsme) {
			System.out.println("Oops! Command went wrong.");
		} catch (IllegalAccessException iae) {
			System.out.println("Oops! Something went wrong.");
		} catch (InvocationTargetException ite) {
			System.out.println("Oops! Something went wrong.");
		}
	}

	public void _help (String user, String mess, Method send, Object ob) throws IllegalAccessException, InvocationTargetException {
		send.invoke(ob, user, "List of available commands: " + IO.join(cmdList, ", "));
	}
}