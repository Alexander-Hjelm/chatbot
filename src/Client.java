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
		s = new Socket("localhost", 4444);
		startThread();
	}

	@Override
	public void run() {
		while (true) {
			//Listen for messages from server
			
			try {
				
				streamIn = new DataInputStream(s.getInputStream());
				String xml = streamIn.readUTF();
				
				XmlParser xmlParser = new XmlParser();
				Message msg = xmlParser.xmlStringToMessage(xml);
				
				UI.updateMessageArea(msg);
				
				
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	} 
	

	
	
	@Override
	public void send(Message msg) {
		try {
			XmlParser xmlParser = new XmlParser();
			String xml = xmlParser.MessageToXmlString(msg);
			
			streamOut = new DataOutputStream(s.getOutputStream());
			streamOut.writeUTF(xml);
			streamOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void setUI(ChatUI UI) {
		this.UI = UI;
	}
}
