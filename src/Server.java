import java.io.DataInputStream;
import java.io.DataOutputStream;
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

public Server(int portIn) throws IOException {
	port = portIn;
	startServer();
} 	
	
	public void startServer() throws IOException {
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
				
				streamIn = new DataInputStream(socket.getInputStream());
				
				
				String msgIn = streamIn.readUTF();
				
				
				UI.updateMessageArea(msgIn + "\n");
				
				
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}	
	
	@Override
	public void send(String msg) {
		try {
			
			
			streamOut = new DataOutputStream(socket.getOutputStream());
			streamOut.writeUTF(msg);
			streamOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void setUI(ChatUI UI) {
		this.UI = UI;
	}
}
