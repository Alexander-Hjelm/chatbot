import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;




public class ChatUI extends JFrame{

	private CommunicationsHandler communicationsHandler;

	private JPanel panel;
	private JTextArea messageArea;
	private JScrollPane messageScrollPane;
	private JLabel titleLabel; 
	private JLabel otherNamesLabel;
	
	// changed to JTextField.
//	private JTextPane myMessagePane;
	private JTextField myMessagePane;
	
	
	private JButton sendButton;
	private JButton exitButton;
	private JButton sendFileButton;
	private JButton newChatButton;
	private JFileChooser fileChooser;
	private FileReceiverUI fileReceiverUI;
	private SendFileUI sendFileUI;
	private String typeUserName;
	
	
	public ChatUI(CommunicationsHandler communicationsHandlerIn, String typeUserNameIn) {
		super("Chat Window");
		this.communicationsHandler = communicationsHandlerIn;
		this.typeUserName = typeUserNameIn;
		
		// tell communicationsHandler, client or server that this is their UI:
		communicationsHandler.setUI(this);
				
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
	        	 String text = myMessagePane.getText();
	        	 
	        	 communicationsHandler.send(text);
	        	 myMessagePane.setText("");
	        	 updateMessageArea("You: " + text + "\n");

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
		messageArea.setEditable(false);
		
		messageScrollPane = new JScrollPane(messageArea);
		titleLabel = new JLabel(typeUserName); 
		otherNamesLabel  = new JLabel();
		myMessagePane = new JTextField();

		
		
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints(); 
		
		// add stuff to panel then add panel to frame.
		c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.gridx = 1;
        c.weightx = 0.5;
        panel.add(titleLabel,c);
		
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 2;
        c.gridx = 0;
        c.weightx = 1;
        panel.add(myMessagePane, c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 2;
        c.gridx = 1;
        c.weightx = 1;
        panel.add(sendButton,c);
        
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 1;
        panel.add(exitButton,c);
        
        
		
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        panel.add(messageScrollPane,c);
		
		
		
	    //Create and set up the window.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  
	    //Add contents to the window.
		this.add(panel);
	
	    //Display the window.
		this.pack();
		this.setVisible(true);
	}
	
	public void updateMessageArea(String msg) {
		this.messageArea.append(msg);
		
	}
	

}
