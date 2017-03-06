import java.awt.Color;
import java.awt.Frame;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends CommunicationsHandler {

	private int port;
	private ServerSocket server;
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
	private int bufferSize = 50;
	private boolean isRunning;

	private boolean listeningForKeyResponse = false;
	private boolean listeningForFileResponse = false;

	private long fileRequestSendTime;
	private long keyRequestSendTime;

	public Server(int portIn, MyData myDataIn) throws IOException {
		port = portIn;
		this.myData = myDataIn;
		this.UI = new ChatUI(this, myData);
		startServer();

	}

	public void startServer() throws IOException {

		this.server = new ServerSocket((int) port);
		startThread();
	}

	public void sendKeyRequest(Socket s, User user) {
		// First thing: send key request message
		Message requestMsg = new Message("{Key Request}", myData.userName, myData.color, MessageType.KEYREQUEST);
		sendToUser(requestMsg, user, s);

		// Enable listening for key response message
		keyRequestSendTime = System.currentTimeMillis();
		listeningForKeyResponse = true;
	}

	public void stopServer() throws IOException {
		server.close();
	}

	@Override
	public void run() {

		while (connectionThread != null) {
			// Wait for an incoming connection

			try {
				Socket s = server.accept();
				clientsConnected = true;

				Thread listenerThread = new Thread(new MessageListener(s));

				listenerThread.start();
				threadPool.add(listenerThread);
				socketPool.add(s);
				User connectedUser = null;
				clientUsers.add(connectedUser);
				sendKeyRequest(s, connectedUser);

				System.out.println(threadPool.size());

			} catch (IOException e) {
				e.printStackTrace();
			}

			// Evaluate if we should stop listening for incoming response
			// messages
			if (listeningForFileResponse && fileRequestSendTime + 60000 < System.currentTimeMillis()) {
				listeningForFileResponse = false;
			}
			if (listeningForKeyResponse && keyRequestSendTime + 60000 < System.currentTimeMillis()) {
				listeningForKeyResponse = false;
			}

		}
	}

	@Override
	public void send(Message msg) {
		// loop through users and send to all.
		for (int i = 0; i < clientUsers.size(); i++) {

			User receiverUser = clientUsers.get(i);
			Socket receiverSocket = socketPool.get(i);
			sendToUser(msg, receiverUser, receiverSocket);

		}
	}

	private void sendToUser(Message msg, User user, Socket s) {
		try {
			// Build xml-string and encrypt
			XmlParser xmlParser = new XmlParser(myData);
			if (user != null) {
				xmlParser.setUser(user);
			}
			if (s != null) {
				String xml = xmlParser.MessageToXmlString(msg);
				streamOut = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
				streamOut.writeUTF(xml);
				streamOut.flush();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void exit() throws IOException {
		// kill thread, see while loop in run.
		connectionThread = null;
		for (int j = 0; j < threadPool.size(); j++) {
			threadPool.set(j, null);
		}
		// if clients are connected:
		if (clientsConnected) {
			Message exitMsg = new Message(" - the server has logged off. Closing program.", myData.userName,
					myData.color, MessageType.DISCONNECT);
			send(exitMsg);
		}

		isRunning = false;
		//wait for listeners to know it's bye time. 
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UI.dispose();
		stopServer();
		

		// if no windows are currently up, exit normally
		if (Frame.getFrames().length == 0) {
			System.exit(0);
		}
	}

	@Override
	protected void startThread() {
		this.connectionThread = new Thread(this);
		connectionThread.start();

	}

	public void sendFileRequest(File file, String additionalText, User destinationUser) {
		if (file.exists()) {
			int userIndex = clientUsers.indexOf(destinationUser);
			fileServer = new FileServer(file, UI, bufferSize, destinationUser);
			Message fileRequestMessage = new Message(additionalText, myData.userName, myData.color,
					MessageType.FILEREQUEST, file.getName(), file.length());
			sendToUser(fileRequestMessage, destinationUser, socketPool.get(userIndex));
			// Initialize file server class
			fileServer = new FileServer(file, UI, bufferSize, destinationUser);

			// Enable listening for file response message
			fileRequestSendTime = System.currentTimeMillis();
			listeningForFileResponse = true;
		}
	}

	@Override
	public void sendFileResponse(boolean reply, int port, String additionalText, String fileName, long fileSize,
			int userIndex) {
		Message fileResponseMessage = new Message(additionalText, myData.userName, myData.color,
				MessageType.FILERESPONSE, reply, port);
		sendToUser(fileResponseMessage, clientUsers.get(userIndex), socketPool.get(userIndex));
		// If yes, Initialize file client class, recieve file at once
		if (reply) {
			// made this ugly with clientusers.get(0).address. needs fixing
			fileClient = new FileClient(clientUsers.get(userIndex).adress, port, fileName, fileSize, UI, bufferSize,
					myData); // Change single client user for later
		}
	}

	public ArrayList<User> getClientUsers() {
		return clientUsers;
	}

	private class MessageListener implements Runnable {
		private Socket listenSocket;
		private DataInputStream streamIn;

		private MessageListener(Socket socketIn) {
			listenSocket = socketIn;
			if (listenSocket != null) {
				isRunning = true;
			}

		}

		public void run() {

			while (isRunning) {
				try {
					// Listen for messages from client
					streamIn = new DataInputStream(new BufferedInputStream(listenSocket.getInputStream()));
					String xml = streamIn.readUTF();
					XmlParser xmlParser = new XmlParser(myData);
					Message msg = xmlParser.xmlStringToMessage(xml);
					UI.updateMessageArea(msg);
					handleMessageType(msg);
					Thread.sleep(500);
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

		private void handleMessageType(Message msg) throws IOException {
			// Disconnect message
			if (msg.messageType == MessageType.DISCONNECT) {

				Message msgOut = msg;
				msgOut.messageType = MessageType.STANDARD;
				updateOtherUsers(msgOut);

				// kill this thread
				isRunning = false;

				// check sendToUser why this:
				socketPool.set(socketPool.indexOf(listenSocket), null);

			}

			// Key request message
			else if (msg.messageType == MessageType.KEYREQUEST) {
				// Send key response.
				Message msgOut = new Message("{Key Response}", myData.userName, myData.color, MessageType.KEYRESPONSE,
						myData.key, myData.aes);
				int userIndex = socketPool.indexOf(listenSocket);
				sendToUser(msgOut, clientUsers.get(userIndex), listenSocket);
			}

			// Key response message
			else if (msg.messageType == MessageType.KEYRESPONSE && listeningForKeyResponse) {
				// Store sender in Users
				String adress = (((InetSocketAddress) listenSocket.getRemoteSocketAddress()).getAddress()).toString()
						.replace("/", "");
				adress = adress.replace("localhost", "");
				int userIndex = socketPool.indexOf(listenSocket);
				clientUsers.set(userIndex, new User(msg.sender, adress, msg.key, msg.aes));
			}

			// File request message
			else if (msg.messageType == MessageType.FILEREQUEST) {
				int userIndex = socketPool.indexOf(listenSocket);
				UI.showFileReceiverUI(msg.fileName, msg.fileSize, userIndex);
			}

			// File response message
			else if (msg.messageType == MessageType.FILERESPONSE && listeningForFileResponse) {
				if (msg.fileReply) {
					UI.updateMessageArea(new Message("Reciever has accepted your file.", "System", Color.BLACK));
					// wait for connection and send file once connection has
					// been established
					fileServer.startServer(msg.port);
				} else {
					UI.updateMessageArea(new Message("Reciever did not accepted your file.", "System", Color.BLACK));
				}
			} else {
				updateOtherUsers(msg);
			}
		}

		private void updateOtherUsers(Message msg) {
			// Send message to all clients other than the sender of the last
			// message
			int senderIndex = socketPool.indexOf(listenSocket);
			for (int i = 0; i < clientUsers.size(); i++) {
				if (i != senderIndex) {
					User receiverUser = clientUsers.get(i);
					Socket receiverSocket = socketPool.get(i);
					sendToUser(msg, receiverUser, receiverSocket);
				}
			}

		}

	}

}
