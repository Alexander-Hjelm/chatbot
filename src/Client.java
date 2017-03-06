import java.awt.Color;
import java.awt.Frame;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
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
	private User serverUser = null;
	private FileServer fileServer;
	private FileClient fileClient;
	private int bufferSize = 50;

	private boolean listeningForKeyResponse = false;
	private boolean listeningForFileResponse = false;
	
	private long fileRequestSendTime;
	private long keyRequestSendTime;
	private boolean isRunning;
	
	
	public Client(String adress, int portIn, MyData myDataIn) throws UnknownHostException, IOException {
		this.myData = myDataIn;
		this.destinationPort = portIn;
		
		this.UI = new ChatUI(this, myData);
		connect(adress, destinationPort);
		
		//if connect worked, a server is up.
		this.serverUp = true;
		this.isRunning = true;
		
	}

	public void sendKeyRequest() {
		//First thing: send key request message
		send(new Message("{Key Request}", myData.userName, myData.color, MessageType.KEYREQUEST));
		
		//Enable listening for key response message
		keyRequestSendTime = System.currentTimeMillis();
		listeningForKeyResponse = true;
	}
	
	public void connect(String adress, int port) throws UnknownHostException, IOException {
		try {
			socket = new Socket(adress, port);
			startThread();
		} catch (Exception e) {
			System.out.println("Trouble connecting to server. Closing program");
			exit();
		}

	}

	@Override
	public void run() {
		
		while (isRunning) {
			//Listen for messages from server
			
			try {
				streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				String xml = streamIn.readUTF();
				
				XmlParser xmlParser = new XmlParser(myData);
				Message msg = xmlParser.xmlStringToMessage(xml);
				
				UI.updateMessageArea(msg);
				handleMessageType(msg);
				Thread.sleep(500);
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//Evaluate if we should stop listening for incoming response messages
			if (listeningForFileResponse && fileRequestSendTime + 60000 < System.currentTimeMillis()) {
				listeningForFileResponse = false;
			}
			if (listeningForKeyResponse && keyRequestSendTime + 60000 < System.currentTimeMillis()) {
				listeningForKeyResponse = false;
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
			sendKeyRequest();
		}
		
		//Key response message
		else if (msg.messageType == MessageType.KEYRESPONSE && listeningForKeyResponse) {
			//Store sender in Users
			String adress = (((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()).toString().replace("/","");
			adress = adress.replace("localhost", "");
			serverUser  = new User(msg.sender, adress, msg.key, msg.aes );
		}
		
		//File request message
		else if (msg.messageType == MessageType.FILEREQUEST) {
			//serverUser is always known for client and it can be accessed with ease later on, so no need to get it now. See sendFileResponse(..)
			int destinationIndex = 0;
			UI.showFileReceiverUI(msg.fileName, msg.fileSize, destinationIndex);
		}
		
		//File response message
		else if (msg.messageType == MessageType.FILERESPONSE && listeningForFileResponse) {
			if(msg.fileReply) {
				UI.updateMessageArea(new Message("Reciever has accepted your file", "System", Color.BLACK));
				//wait for connection and send file once connection has been established
				fileServer.startServer(msg.port);
			} else {
				UI.updateMessageArea(new Message("Reciever did not accepted your file", "System", Color.BLACK));
			}
		}
	}

	@Override
	public void send(Message msg) {
		try {
			//Build xml-string and encrypt
			XmlParser xmlParser = new XmlParser(myData);
			if(serverUser != null){
				xmlParser.setUser(serverUser);
			}
			String xml = xmlParser.MessageToXmlString(msg);

			//Write xml-string to socket
			streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			streamOut.writeUTF(xml);
			streamOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public void exit() throws IOException {
		t = null;
		isRunning = false;
		try {
			//wait for server to realize client is gone.
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//if connected to server.
		if(serverUp) {
			//Send disconnect message
			Message exitMsg = new Message("has logged off.", myData.userName, myData.color, MessageType.DISCONNECT);
			send(exitMsg);
		}
		

		socket.close();
		UI.dispose();
		
		// if no windows are currently up, exit normally
		if (Frame.getFrames().length == 0) {
			System.exit(0);
		}
	
	}

	@Override
	protected void startThread() {
		this.t = new Thread(this);
		t.start();
		
		
	}

	public void sendFileRequest(File file, String text, User destinationUser) {
		if (file.exists()) {
			// Never use destinationUser
			Message fileRequestMessage = new Message(text, myData.userName, myData.color, MessageType.FILEREQUEST, file.getName(), file.length());
			send(fileRequestMessage);
			// Initialize file server class
			fileServer = new FileServer(file, UI, bufferSize, serverUser);
			
			//Enable listening for file response message
			fileRequestSendTime = System.currentTimeMillis();
			listeningForFileResponse = true;
		}
	}
	
	@Override
	public void sendFileResponse(boolean reply, int port, String additionalText, String fileName, long fileSize, int destinationUserIndex) {
		Message fileResponseMessage = new Message(additionalText, myData.userName, myData.color, MessageType.FILERESPONSE, reply, port);
		send(fileResponseMessage);
		// If yes, Initialize file client class, recieve file at once
		if (reply) {
			fileClient = new FileClient(serverUser.adress, port, fileName, fileSize, UI, bufferSize, myData);
		}
	}

}
