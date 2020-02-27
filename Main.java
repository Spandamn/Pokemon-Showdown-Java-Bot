import java.net.*;
import org.java_websocket.client.WebSocketClient;
import java.io.IOException;

public class Main {
	String[] formats;

	public static void main (String args[]) throws IOException, URISyntaxException {
		IO io = new IO();
		Config conf =  new Config();
		Bot bot = new Bot(conf);
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
}
