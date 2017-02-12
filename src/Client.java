import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client implements Runnable{

	private int destinationPort;
	private ChatUI userInter;
	private DataInputStream streamIn;
	private Socket s;

	

	public Client(int portIn) throws UnknownHostException, IOException {
		destinationPort = portIn;
//		userInter = ui;
		s = new Socket("localhost", 4444);
		connect("localhost", destinationPort);
		
		
	}
	
	public void connect(String Adress, int port) throws UnknownHostException, IOException {


		
		Thread t = new Thread(this);
		t.start();
		
		
	}

	public void run() {
		while (true) {

			
			try {
				
				streamIn = new DataInputStream(s.getInputStream());
				System.out.println(streamIn.readUTF());
				
				
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	} 
	
	public void send(String msg) {
		
	}
}
