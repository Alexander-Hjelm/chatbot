import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

	private int port;
	private String adress;
	private ServerSocket socket; 
	private ChatUI userInter;

public Server(int portIn, String adressIn, ChatUI ui ) {
	port = portIn;
	adress = adressIn;
	userInter = ui;
} 	
	
	public void startServer() throws IOException {
		this.socket = new ServerSocket((int) port);
	}
	
	public void stopServer() throws IOException {
		socket.close();
	}
	
	
	
	
}
