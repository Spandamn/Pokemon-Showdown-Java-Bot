import java.io.*;
import java.net.*;
import java.lang.*;
import java.nio.ByteBuffer;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.*;
import org.java_websocket.handshake.ServerHandshake;


public class WS extends WebSocketClient {
	Bot bot;
	boolean logReply = false;
	public WS(URI serverUri, Draft draft) {
		super(serverUri, draft);
	}

	public WS(URI serverURI, Bot bot) {
		super(serverURI);
		this.bot = bot;
	}

	@Override
	public void onOpen(ServerHandshake hd) {
		System.out.println("New connection opened");
		//System.out.println(message);
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("closed with exit code " + code + " additional info: " + reason);
	}

	@Override
	public void onMessage(String message) {
		if (this.logReply) {
			this.logReply = false;
			IO.println(message);
		}
		bot.handleMessage(message);
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
