import java.io.*;
import java.util.*;
import java.lang.reflect.*;

public class Test {
	public static void main (String args[]) throws IOException {
		IO io = new IO ();
		Method m[] = io.getClass().getDeclaredMethods();
		System.out.println(m[0].getDeclaringClass());
	}
}