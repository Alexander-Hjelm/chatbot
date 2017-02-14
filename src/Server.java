import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server extends CommunicationsHandler {

	private int port;
	private ServerSocket server;
	private Socket socket;
	private DataOutputStream streamOut;

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
		}
	}	
	
	@Override
	public void send(String msg) {
		try {
			
			
			streamOut = new DataOutputStream(socket.getOutputStream());
			streamOut.writeUTF("AEF=)");
			streamOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
