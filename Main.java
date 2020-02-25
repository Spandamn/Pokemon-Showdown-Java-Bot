import java.io.*;
import java.net.*;
import javax.websocket.*;
import java.util.*;
import org.glassfish.tyrus.client.ClientManager;

public class Main {
	private static CountDownLatch latch;
	String[] formats;

	public static void main (String args[]) throws IOException {
		IO io = new IO();
		Config conf =  new Config();
		latch = new CountDownLatch(1);
 
		ClientManager client = ClientManager.createClient();
		try {
			client.connectToServer(Main.class, new URI("wss://" + conf.server + "/showdown/websocket"));
			latch.await();
 
		} catch (DeploymentException | URISyntaxException | InterruptedException e) {
			throw new RuntimeException(e);
		}
		/*self.ws = websocket.create_connection('wss://'+config.server+'/showdown/websocket')
		self.contime = time.time()
		msg = self.ws.recv()
		msg = msg.split('\n')[1]
		self.formats = []
		for m in msg.split('|'):
			if m[1:4] == 'Gen':
				self.formats.append(m.split(',')[0])
		msg = self.ws.recv()
		r = requests.post('https://play.pokemonshowdown.com/action.php',data = {'act':'login','name':self.usr['nickname'],'pass':self.usr['password'],'challstr':msg[10:]})
		self.ws.send('|/trn '+self.usr['nickname']+',0,'+json.loads(r.__dict__['_content'][1:])['assertion'])
		self.ws.send('|/avatar '+self.usr['avatar'])
		for room in config.rooms:
			self.ws.send('|/j '+room)*/
	}

	@OnOpen
	public void onOpen(Session session) {
		try {
			session.getBasicRemote().sendText("start");
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
 
	@OnMessage
	public String onMessage(String message, Session session) {
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		try {
			logger.info("Received ...." + message);
			String userInput = bufferRead.readLine();
			return userInput;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
 
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info(String.format("Session %s close because of %s", session.getId(), closeReason));
	}
}