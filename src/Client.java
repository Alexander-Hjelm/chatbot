import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client extends CommunicationsHandler{

	private int destinationPort;
	private Socket socket = null;
	private DataInputStream streamIn;
	private DataOutputStream streamOut;
	private ChatUI UI;
	private Thread t;
	private MyData myData;
	private boolean serverUp;
	private User serverUser;

	public Client(String adress, int portIn, MyData myData) throws UnknownHostException, IOException {
		this.myData = myData;
		destinationPort = portIn;
		
		//redundant method at the moment?
		connect(adress, destinationPort);
		
		//if connect worked, a server is up.
		serverUp = true;
	}
	
	@Override
	public void sendKeyRequest() {
		//First thing: send key request message
		send(new Message("{Key Request}", myData.userName, myData.color, MessageType.KEYREQUEST));
	}
	
	public void connect(String adress, int port) throws UnknownHostException, IOException {
		try {
			socket = new Socket(adress, port);
		} catch (Exception e) {
			System.out.println("Trouble connecting to server. Closing program");
			exit();
		}
		startThread();
	}

	@Override
	public void run() {
		
		while (t != null) {
			//Listen for messages from server
			
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
			serverUp = false;
			System.out.println("Server down.");
			exit();
		}
		
		//Key request message
		else if (msg.messageType == MessageType.KEYREQUEST) {
			//Send key response.
			send(new Message("{Key Response}", myData.userName, myData.color, MessageType.KEYRESPONSE, myData.key, myData.aes));
		}
		
		//Key response message
		else if (msg.messageType == MessageType.KEYRESPONSE) {
			//Store sender as User
			serverUser = new User(msg.sender, socket.getInetAddress().getHostAddress(), msg.key, msg.aes );
		}
		
		//File request message
		else if (msg.messageType == MessageType.FILEREQUEST) {
			UI.showFileReceiverUI();
		}
		
		//File response message
		else if (msg.messageType == MessageType.FILERESPONSE) {

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
		t = null;
		
		//if connected to server.
		if(serverUp) {
			Message exitMsg = new Message("has logged off.", myData.userName, myData.color, MessageType.DISCONNECT);
			send(exitMsg);
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
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

	@Override
	public void sendFileRequest(File file, String text) {
		if (file.exists()) {
			Message fileRequestMessage = new Message(text, myData.userName, myData.color, MessageType.FILEREQUEST, file.getName(), file.length());
			send(fileRequestMessage);
		}
	}
	
	@Override
	public void sendFileResponse(boolean reply, String additionalText) {
		Message fileResponseMessage = new Message(additionalText, myData.userName, myData.color, MessageType.FILERESPONSE, reply);
		send(fileResponseMessage);
	}
	
}
