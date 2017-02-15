import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client extends CommunicationsHandler{

	private int destinationPort;
	private Socket s;
	private DataInputStream streamIn;
	private DataOutputStream streamOut;
	private ChatUI UI;

	

	public Client(int portIn) throws UnknownHostException, IOException {
		destinationPort = portIn;
		
		//redundant method at the moment?
		connect("localhost", destinationPort);
	}
	
	public void connect(String Adress, int port) throws UnknownHostException, IOException {
		s = new Socket("84.216.231.127", 4444);
		startThread();
	}

	@Override
	public void run() {
		while (true) {
			//Listen for messages from server
			
			try {
				
				streamIn = new DataInputStream(s.getInputStream());
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
			
			
			streamOut = new DataOutputStream(s.getOutputStream());
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
