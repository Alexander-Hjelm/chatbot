import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;




public class ChatUI extends JFrame{


	private Server server;
	private Client client;
	private JPanel panel;
	private JTextArea messageArea;
	private JLabel titleLabel; 
	private JLabel otherNamesLabel;
	private JTextPane myMessagePane;
	private JButton sendButton;
	private JButton exitButton;
	private JButton sendFileButton;
	private JButton newChatButton;
	private JFileChooser fileChooser;
	private FileReceiverUI fileReceiverUI;
	private SendFileUI sendFileUI;
	
	
	public ChatUI(Server serverIn, Client clientIn) {
		super("Chat Window");
		if (serverIn != null) {
			server = serverIn;
		}
		
		if (clientIn != null) {
			client = clientIn;
		}
		
		
		
	
		
		
		
		
		
		panel = new JPanel();
		
		
		
		
		createAndShowGUI(panel);
	}
	
	private void createAndShowGUI(JPanel panel) {
	    //Create and set up the window.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  
	    //Add contents to the window.
		this.add(panel);
	
	    //Display the window.
		this.pack();
		this.setVisible(true);
	}
	

}
