import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
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
	private MyData myData;

	

	public Client(String adress, int portIn, MyData myData) throws UnknownHostException, IOException {
		this.myData = myData;
		destinationPort = portIn;
		
		//redundant method at the moment?
		connect(adress, destinationPort);
	}
	
	public void connect(String adress, int port) throws UnknownHostException, IOException {
		socket = new Socket(adress, port);
		startThread();
	}

	@Override
	public void run() {
		while (true) {
			//Listen for messages from server
			
			try {
			
				
				
				streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				String xml = streamIn.readUTF();
				
				XmlParser xmlParser = new XmlParser(myData);
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
			XmlParser xmlParser = new XmlParser(myData);
			String xml = xmlParser.MessageToXmlString(msg);

			streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
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
		Message exitMsg = new Message("has logged off.", myData.userName, myData.color, false);
		send(exitMsg);
		
		
		System.exit(0);
		
		
		
	}

	@Override
	protected void startThread() {
		this.t = new Thread(this);
		t.start();
		
		
	}
}
