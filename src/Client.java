import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {

	private int destinationPort;
	private ChatUI userInter;

	public Client(int portIn, ChatUI ui) {
		destinationPort = portIn;
		userInter = ui;
		
	}
	
	public void connect(String Adress, int port) throws UnknownHostException, IOException {
		Socket s = new Socket(Adress, port);
		
		
	} 
	
}
