import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.ArrayList;

public class Server extends CommunicationsHandler {

	private int port;
	private ServerSocket server;
//	private Socket socket;
	private DataInputStream streamIn;
	private DataOutputStream streamOut;
	private ChatUI UI;
	private Thread connectionThread;
	private MyData myData;
	private boolean clientsConnected;
	private ArrayList<User> clientUsers = new ArrayList<User>();
	private ArrayList<Thread> threadPool = new ArrayList<Thread>();
	private ArrayList<Socket> socketPool = new ArrayList<Socket>();
	private FileServer fileServer;
	private FileClient fileClient;

public Server(int portIn, MyData myData) throws IOException {
	port = portIn;
	this.myData = myData;
	startServer();
} 	
	
	public void startServer() throws IOException {
		
		this.server = new ServerSocket((int) port);
		startThread();
	}
	
	@Override
	public void sendKeyRequest() {
		//First thing: send key request message
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		send(new Message("{Key Request}", myData.userName, myData.color, MessageType.KEYREQUEST));
	}
	
	public void stopServer() throws IOException {
		server.close();
	}
	
	@Override
	public void run() {

		
		while (connectionThread != null) {
			//Wait for an incoming connection
			

				
				try {
					Socket s = server.accept();
					clientsConnected = true;
					
					Thread listenerThread = new Thread(new MessageListener(s));
					listenerThread.start();
					threadPool.add(listenerThread);
					socketPool.add(s);
					clientUsers.add(null);
					
					Thread.sleep(500);
					
					
					
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// kill thread to make it behave as before. remove this later.
//				connectionThread = null;
				
				
				
				

				

			
		}
	}	
	

	
	@Override
	public void send(Message msg) {
		//loop through users and send to all.
		for (int i = 0; i < clientUsers.size(); i++) {

			User receiverUser = clientUsers.get(i);
			Socket receiverSocket = socketPool.get(i);
			msg = handleOutMsg(msg,receiverUser);
			sendToUser(msg,receiverUser,receiverSocket);

		}
	}
	
	private void sendToUser(Message msg, User user, Socket s){
		try {
			XmlParser xmlParser = new XmlParser(myData);
			if (user != null) {
				xmlParser.setUser(user);
			}
			String xml = xmlParser.MessageToXmlString(msg);
			streamOut = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
			streamOut.writeUTF(xml);
			streamOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	//check if this message is the first, if so make it a keyrequest.
	private Message handleOutMsg(Message msgIn, User user){
		if(user == null && msgIn.messageType != MessageType.KEYRESPONSE){
			return new Message(msgIn.text, myData.userName, myData.color, MessageType.KEYREQUEST);
		} 
		return msgIn;
	}
	
	@Override
	public void setUI(ChatUI UI) {
		this.UI = UI;
	}

	@Override
	public void exit() throws IOException {
		//kill thread, see while loop in run.
		connectionThread = null;
		for (int j = 0; j <threadPool.size(); j++) {
			threadPool.set(j, null);
		}
		//if clients are connected:
		if(clientsConnected){
			Message exitMsg = new Message(" - the server has logged off. Closing program.", myData.userName, myData.color, MessageType.DISCONNECT);
			send(exitMsg);
		}

		System.exit(0);
	}


	@Override
	protected void startThread() {
		this.connectionThread = new Thread(this);
		connectionThread.start();
		
	}
	
	@Override
	public void sendFileRequest(File file, String additionalText) {
		if (file.exists()) {
			Message fileRequestMessage = new Message(additionalText, myData.userName, myData.color, MessageType.FILEREQUEST, file.getName(), file.length());
			send(fileRequestMessage);
			// Initialize file server class
			fileServer = new FileServer(file, UI);
		}
	}
	
	@Override
	public void sendFileResponse(boolean reply, int port, String additionalText, String fileName, long fileSize) {
		Message fileResponseMessage = new Message(additionalText, myData.userName, myData.color, MessageType.FILERESPONSE, reply, port);
		send(fileResponseMessage);
		// If yes, Initialize file client class, recieve file at once
		if (reply) {
			//made this ugly with clientusers.get(0).address. needs fixing
			fileClient = new FileClient(clientUsers.get(0).adress, port, fileName, fileSize, UI);	//Change single client user for later
		}
	}
	
	
	private class MessageListener implements Runnable{
		private Socket listenSocket;
		private MessageListener(Socket socketIn) {
			listenSocket = socketIn;
		}
		@Override
		public void run() {
			
			
			while (true){
				try {
					//Listen for messages from client
					streamIn = new DataInputStream(new BufferedInputStream(listenSocket.getInputStream()));
					String xml = streamIn.readUTF();
					XmlParser xmlParser = new XmlParser(myData);
					Message msg = xmlParser.xmlStringToMessage(xml);
					UI.updateMessageArea(msg);
					handleMessageType(msg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			
		}
		
		private void handleMessageType(Message msg) throws IOException {
			//Disconnect message
			if(msg.messageType == MessageType.DISCONNECT){
				clientsConnected = false;
//				connectionThread = null;
			}
			
			//Key request message
			else if (msg.messageType == MessageType.KEYREQUEST) {
				//Send key response.
				Message msgOut = new Message("{Key Response}", myData.userName, myData.color, MessageType.KEYRESPONSE, myData.key, myData.aes);
				int userIndex = socketPool.indexOf(listenSocket);
				sendToUser(msgOut, clientUsers.get(userIndex), listenSocket);
			}
			
			//Key response message
			else if (msg.messageType == MessageType.KEYRESPONSE) {
				//Store sender in Users
				String adress = (((InetSocketAddress) listenSocket.getRemoteSocketAddress()).getAddress()).toString().replace("/","");
				adress = adress.replace("localhost", "");
				int userIndex = socketPool.indexOf(listenSocket);
				clientUsers.set(userIndex, new User(msg.sender, adress, msg.key, msg.aes ));
			}
			
			//File request message
			else if (msg.messageType == MessageType.FILEREQUEST) {
				UI.showFileReceiverUI(msg.fileName, msg.fileSize);
			}
			
			//File response message
			else if (msg.messageType == MessageType.FILERESPONSE) {
				if(msg.fileReply) {
					UI.updateMessageArea(new Message("Reciever has accepted your file.", "System", Color.BLACK));
					//wait for connection and send file once connection has been established
					fileServer.startServer(msg.port);
				} else {
					UI.updateMessageArea(new Message("Reciever did not accepted your file.", "System", Color.BLACK));
				}
			}
		}
		
			
		
		
	}
	
}
