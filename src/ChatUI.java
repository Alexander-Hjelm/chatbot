import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		buttonAction();
		createAndShowGUI(panel);
	}
	
	private void buttonAction() {
		sendButton = new JButton("send");
		exitButton = new JButton("exit");;
		sendFileButton = new JButton("send file");
		newChatButton = new JButton("new chat");
		
		
		sendButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	            // add listener stuff, some get data from myMessagePane for this button.
	         }
	      });
		
		exitButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	            System.exit(0);
	         }
	      });
		
		sendFileButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	            // open sendFileUI, open fileChooser etc. 
	         }
	      });
		
		newChatButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	            // add listener stuff, some get data from myMessagePane for this button.
	         }
	      });
		
	}

	private void createAndShowGUI(JPanel panel) {
		
		messageArea = new JTextArea();
		titleLabel = new JLabel("MSN 2017"); 
		otherNamesLabel  = new JLabel();
		myMessagePane = new JTextPane();

		
		
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints(); 
		
		
		
	    //Create and set up the window.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  
	    //Add contents to the window.
		this.add(panel);
	
	    //Display the window.
		this.pack();
		this.setVisible(true);
	}
	

}
