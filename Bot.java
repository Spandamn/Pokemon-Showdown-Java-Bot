import java.io.*;
import java.net.*;

public class Bot {
	//private static CountDownLatch latch;
		//socket.send("Hello Server!");
	Config conf;
	WS socket;
	String[] formats;
	Commands com;
	Bot (Config conf) {
		this.conf = conf;
		com = new Commands(this);
		try {
			socket = new WS(new URI("ws://" + InetAddress.getByName(conf.server).getHostAddress() + ":" + conf.port + "/showdown/websocket"), this); //51.79.72.237:8000/showdown/websocket
			socket.connect();
		} catch (URISyntaxException use) {
			System.err.println("Error: Invalid URI");
		} catch (UnknownHostException uhe) {
			System.err.println("Error: Invalid Servername or Port");
		}
	}

	public void setFormats (String s) {
		String[] f = s.split("|");
		formats = new String[f.length - 2];
		for (int i = 2; i < f.length; i++) {
			System.out.println(f[i]);
			formats[i - 2] = f[i];
		}
	}

	public void handleMessage (String s) {
		String[] ms = s.split("\n");
		System.out.println("Message length: " + ms.length);
		//for (int i = 0; i < ms.length; i++) {
			/*if (ms[i].startsWith("|formats|")) {
				setFormats(ms[i]);
			}*/
			//System.out.println(ms[i]);
		//}
		String forms[] = ms[1].split("|");
		for (int i = 0; i < forms.length; i++) System.out.println(forms[i]);
	}

	public void sendToServer (String s) {
		this.socket.send(s);
	}
}