import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server extends CommunicationsHandler {

	private int port;
	private ServerSocket socket; 
	private DataOutputStream streamOut;

public Server(int portIn) throws IOException {
	port = portIn;
	
	startServer();
	
} 	
	
	public void startServer() throws IOException {
		this.socket = new ServerSocket((int) port);
		startThread();
	}
	
	public void stopServer() throws IOException {
		socket.close();
	}
	
	@Override
	public void run() {
		while (true) {
			try {
			Socket ssClient = socket.accept();
			if (ssClient.isConnected()) {
				System.out.println("hej");
			}
			
			streamOut = new DataOutputStream(ssClient.getOutputStream());
			streamOut.writeUTF("AEF=)");
			streamOut.flush();
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}	
	
	@Override
	public void send(String msg) {
		
	}
}
