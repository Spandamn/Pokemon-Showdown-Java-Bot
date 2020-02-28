//package pokemon.bot;
import java.io.*;
import java.net.*;
import java.lang.*;
import java.nio.ByteBuffer;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.*;
import org.java_websocket.handshake.ServerHandshake;


public class WS extends WebSocketClient {
	public static void main (String[] args) throws IOException, URISyntaxException {
		
	}

	public WS(URI serverUri, Draft draft) {
		super(serverUri, draft);
	}

	public WS(URI serverURI) {
		super(serverURI);
	}

	@Override
	public void onOpen(ServerHandshake hd) {
		System.out.println("new connection opened");
		//System.out.println(message);
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("closed with exit code " + code + " additional info: " + reason);
	}

	@Override
	public void onMessage(String message) {
		System.out.println("received message: " + message);
	} 

	@Override
	public void onMessage(ByteBuffer message) {
		System.out.println("received ByteBuffer");
	}

	@Override
	public void onError(Exception ex) {
		System.err.println("an error occurred:" + ex);
	}
}