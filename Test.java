import java.io.*;
import java.util.*;
import java.lang.reflect.*;

public class Test {
	public static void main (String args[]) throws IOException, NoSuchMethodException {
		IO io = new IO ();
		Class[] paramTypes = {String.class, String.class};
			Method send = Room.class.getMethod("send", paramTypes);
	}
}