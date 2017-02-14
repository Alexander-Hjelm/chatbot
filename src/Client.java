import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client extends CommunicationsHandler{

	private int destinationPort;
	private DataInputStream streamIn;
	private Socket s;

	

	public Client(int portIn) throws UnknownHostException, IOException {
		destinationPort = portIn;
//		userInter = ui;
		s = new Socket("localhost", 4444);
		connect("localhost", destinationPort);
	}
	
	public void connect(String Adress, int port) throws UnknownHostException, IOException {
		startThread();
	}

	@Override
	public void run() {
		while (true) {
			//Listen for messages from server
			
			try {
				
				streamIn = new DataInputStream(s.getInputStream());
				System.out.println(streamIn.readUTF());
				
				
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	} 
	
	@Override
	public void send(String msg) {
		
	}
}
