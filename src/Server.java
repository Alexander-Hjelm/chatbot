import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server extends CommunicationsHandler {

	private int port;
	private ServerSocket server;
	private Socket socket;
	private DataInputStream streamIn;
	private DataOutputStream streamOut;
	private ChatUI UI;
	private Thread t;
	private MyData myData;

public Server(int portIn, MyData myData) throws IOException {
	port = portIn;
	startServer();
} 	
	
	public void startServer() throws IOException {
		this.myData = myData;
		this.server = new ServerSocket((int) port);
		//Wait for an incoming connection	
		socket = server.accept();
		startThread();
	}
	
	public void stopServer() throws IOException {
		server.close();
	}
	
	@Override
	public void run() {
		while (true) {
			//Listen for messages from client
			
			
			try {
				
				
				
				streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				String xml = streamIn.readUTF();
				
				XmlParser xmlParser = new XmlParser(myData);
				Message msg = xmlParser.xmlStringToMessage(xml);
				UI.updateMessageArea(msg);
				
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			
		}
	}	
	
	@Override
	public void send(Message msg) {
		try {
			XmlParser xmlParser = new XmlParser(myData);
			String xml = xmlParser.MessageToXmlString(msg);
			
			streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			streamOut.writeUTF(xml);
			streamOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void setUI(ChatUI UI) {
		this.UI = UI;
	}

	@Override
	public void exit() {
		

			
			try {
				if(!socket.isClosed()){
					socket.close();
				}
				server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(0);
			

		
		
	}

	@Override
	protected void startThread() {
		this.t = new Thread(this);
		t.start();
		
	}
}
