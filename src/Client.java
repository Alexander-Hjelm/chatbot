import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client extends CommunicationsHandler{

	private int destinationPort;
	private Socket socket;
	private DataInputStream streamIn;
	private DataOutputStream streamOut;
	private ChatUI UI;
	private Thread t;

	

	public Client(int portIn) throws UnknownHostException, IOException {
		destinationPort = portIn;
		
		//redundant method at the moment?
		connect("localhost", destinationPort);
	}
	
	public void connect(String Adress, int port) throws UnknownHostException, IOException {
		socket = new Socket("localhost", 4444);
		startThread();
	}

	@Override
	public void run() {
		while (true) {
			//Listen for messages from server
			
			try {

				streamIn = new DataInputStream(socket.getInputStream());
				String xml = streamIn.readUTF();
				
				XmlParser xmlParser = new XmlParser();
				Message msg = xmlParser.xmlStringToMessage(xml);
				
				UI.updateMessageArea(msg);

				//if server has closed socket:
				if(socket.isClosed()){
					t.interrupt();
					exit();
				}

				
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

			streamOut = new DataOutputStream(socket.getOutputStream());
			streamOut.writeUTF(xml);

			streamOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void setUI(ChatUI UI) {
		this.UI = UI;
	}

	@Override
	public void exit() {
		
		try {
			socket.close();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	protected void startThread() {
		this.t = new Thread(this);
		t.start();
		
		
	}
}
