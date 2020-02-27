import java.io.*;
import java.util.*;
/**
 * IO class: Input and Output for Primitive data types
 * Written by Spandan
 * Pass a FileReader object to the constructer to read Files
 */

public class IO {
	static BufferedReader in;

	IO () {
		in = new BufferedReader(new InputStreamReader(System.in));
	}

	IO (FileReader f) {
		in = new BufferedReader(f);
	}

	// Input
	String getLine () {
		try {
			return in.readLine();
		} catch (IOException ioe) {}
		return "";
	}

	int getInt () {
		try {
			return Integer.parseInt(in.readLine());
		} catch (NumberFormatException n) {
			System.err.println("Input mismatch: Returning default value 0");
			return 0;
		} catch (IOException ioe) {}
		return 0;
	}

	float getFloat () {
		try {
			return Float.parseFloat(in.readLine());
		} catch (NumberFormatException n) {
			System.err.println("Input mismatch: Returning default value 0");
			return 0.0f;
		} catch (IOException ioe) {}
		return 0.0f;
	}

	double getDouble () {
		try {
			return Double.parseDouble(in.readLine());
		} catch (NumberFormatException n) {
			System.err.println("Input mismatch: Returning default value 0");
			return 0.0;
		} catch (IOException ioe) {}
		return 0.0;
	}

	short getShort () {
		try {
			return Short.parseShort(in.readLine());
		} catch (NumberFormatException n) {
			System.err.println("Input mismatch: Returning default value 0");
			return 0;
		} catch (IOException ioe) {}
		return 0;
	}

	long getLong () {
		try {
			return Long.parseLong(in.readLine());
		} catch (NumberFormatException n) {
			System.err.println("Input mismatch: Returning default value 0");
			return 0;
		} catch (IOException ioe) {}
		return 0;
	}

	char getChar () {
		try {
			return in.readLine().charAt(0);
		} catch (IOException ioe) {}
		return '\0';
	}

	byte getByte () {
		try {
			return Byte.parseByte(in.readLine());
		} catch (NumberFormatException n) {
			System.err.println("Input mismatch: Returning default value 0");
			return 0;
		} catch (IOException ioe) {}
		return 0;
	}

	boolean getBool () {
		try {
			return Boolean.parseBoolean(in.readLine());
		} catch (NumberFormatException n) {
			System.err.println("Input mismatch: Returning default value false");
			return false;
		} catch (IOException ioe) {}
		return false;
	}

	// Output functions

	void print (String s) {
		System.out.print(s);
	}

	void println (String s) {
		System.out.println(s);
	}

	void println (int s) {
		System.out.println(s);
	}

	void println (float s) {
		System.out.println(s);
	}

	void println (double s) {
		System.out.println(s);
	}

	void println (long s) {
		System.out.println(s);
	}

	void println (short s) {
		System.out.println(s);
	}

	void println (byte s) {
		System.out.println(s);
	}

	void println (boolean s) {
		System.out.println(s);
	}

	void println (char s) {
		System.out.println(s);
	}

	public static String toId (String text) {
		String newStr = "";
		for (int i = 0; i < text.length(); i++) {
			int c = (int)text.charAt(i);
			if (c >= 65 && c <= 90 || c >= 97 && c <= 122 || c >= ((int)'0') && c <= ((int)'9')) {
				newStr += (char)c;
			}
		}
		return newStr;
	}

	public static String[] arrayMod (String arr[], String opt) {
		String[] arrr = Arrays.copyOf(arr, arr.length);
		if (opt.startsWith("del")) {
			arrr = new String[arr.length - 1];
			int j = 0;
			for (int i = 0; i < arr.length; i++) {
				if (arr[i].equalsIgnoreCase(opt.substring(3))) {
					continue;
				}
				arrr[j] = arr[i];
				j++;
			}
		} else if (opt.startsWith("add")) {
			arrr = Arrays.copyOf(arr, arr.length + 1);
			arrr[arr.length] = opt.substring(3);
		}
		return arrr;
	}
}
