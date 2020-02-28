import java.net.*;
import org.java_websocket.client.WebSocketClient;
import java.io.IOException;

public class Main {
	String[] formats;

	public static void main (String args[]) throws IOException, URISyntaxException {
		IO io = new IO();
		Config conf =  new Config();
		Bot bot = new Bot(conf);
	}
}
