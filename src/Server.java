import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Server extends CommunicationsHandler {

	private int port;
	private ServerSocket server;
	private Socket socket;
	private DataInputStream streamIn;
	private DataOutputStream streamOut;
	private ChatUI UI;
	private Thread t;
	private MyData myData;
	private boolean clientsConnected;
	private ArrayList<User> clientUsers = new ArrayList<User>();

public Server(int portIn, MyData myData) throws IOException {
	port = portIn;
	this.myData = myData;
	startServer();
} 	
	
	public void startServer() throws IOException {
		
		this.server = new ServerSocket((int) port);
		socket = server.accept();
		clientsConnected = true;
		startThread();
		
		//First thing: send key request message
		send(new Message("{Key Request}", myData.userName, myData.color, MessageType.KEYREQUEST));
	}
	
	public void stopServer() throws IOException {
		server.close();
	}
	
	@Override
	public void run() {

		
		while (t != null && clientsConnected) {
			//Listen for messages from client
			//Wait for an incoming connection	
			
			try {
				streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				String xml = streamIn.readUTF();
				
				XmlParser xmlParser = new XmlParser(myData);
				Message msg = xmlParser.xmlStringToMessage(xml);
				UI.updateMessageArea(msg);
				handleMessageType(msg);
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}	
	
	private void handleMessageType(Message msg) throws IOException {
		//Disconnect message
		if(msg.messageType == MessageType.DISCONNECT){
			clientsConnected = false;
			t = null;
		}
		
		//Key request message
		else if (msg.messageType == MessageType.KEYREQUEST) {
			//Send key response.
			send(new Message("{Key Response}", myData.userName, myData.color, MessageType.KEYRESPONSE, myData.key, myData.aes));
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
	public void exit() throws IOException {
		//kill thread, see while loop in run.
		t = null;
		
		//if clients are connected:
		if(clientsConnected){
			Message exitMsg = new Message(" - the server has logged off. Closing program.", myData.userName, myData.color, MessageType.DISCONNECT);
			send(exitMsg);
		}

		System.exit(0);
	}


	@Override
	protected void startThread() {
		this.t = new Thread(this);
		t.start();
		
	}
}
