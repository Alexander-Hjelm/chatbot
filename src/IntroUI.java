import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;




public class IntroUI extends JFrame{

	private JPanel panel;
	private MyData myData;

	//Panel elements
	private JLabel titleLabel = new JLabel("Chat Program");
	private JLabel nameLabel = new JLabel("Name / alias:");
	private JLabel portLabel  = new JLabel("Port:");
	private JLabel adressLabel = new JLabel("Adress:");
	private JLabel radioButtonClientLabel  = new JLabel("Client");
	private JLabel radioButtonServerLabel = new JLabel("Server");
	private JLabel radioButtonAesLabel  = new JLabel("Aes encryption");
	private JLabel radioButtonCaesarLabel = new JLabel("Caesar encryption");
	
	

	private JTextPane namePane = new JTextPane();
	private JTextPane adressPane = new JTextPane();	
	private JTextPane portPane = new JTextPane();	
	
	private ButtonGroup clientServerButtonGroup;
	private ButtonGroup cryptoButtonGroup;

	private JRadioButton clientRadioButton = new JRadioButton();
	private JRadioButton serverRadioButton = new JRadioButton();
	private JRadioButton aesRadioButton = new JRadioButton();
	private JRadioButton caesarRadioButton = new JRadioButton();
	
	private JButton connectButton = new JButton("Connect");
	private JButton exitButton = new JButton("Exit");
	
	public IntroUI() {
		super("Intro Window");
		
		GridLayout gridLayout = new GridLayout(0, 2);

		panel = new JPanel(gridLayout);
		
		//Empty layout
		JPanel emptyPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		
		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		titlePanel.add(titleLabel);
		
		clientServerButtonGroup = new ButtonGroup();
		clientServerButtonGroup.add(clientRadioButton);
		clientServerButtonGroup.add(serverRadioButton);
		
		cryptoButtonGroup = new ButtonGroup();
		cryptoButtonGroup.add(aesRadioButton);
		cryptoButtonGroup.add(caesarRadioButton);
		
		GridLayout cryptoButtonLayout = new GridLayout(0, 2);
		JPanel cryptoButtonPanel = new JPanel(cryptoButtonLayout);
		
		cryptoButtonPanel.add(aesRadioButton);
		cryptoButtonPanel.add(radioButtonAesLabel);
		cryptoButtonPanel.add(caesarRadioButton);
		cryptoButtonPanel.add(radioButtonCaesarLabel);
		
		GridLayout jButtonLayout = new GridLayout(0, 1);
		JPanel jButtonPanel = new JPanel(jButtonLayout);
		jButtonPanel.add(connectButton);
		jButtonPanel.add(exitButton);

		connectButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 //Create a new client or server, and assign that to a new ChatUI
	        	 //ChatUI now has control
	        	 createChatUI();
	         }
	      });
		
		panel.add(titlePanel);
		panel.add(emptyPanel);
		
		panel.add(nameLabel);
		panel.add(namePane);
		
		panel.add(clientRadioButton);
		panel.add(radioButtonClientLabel);
		panel.add(serverRadioButton);
		panel.add(radioButtonServerLabel);

		panel.add(adressLabel);
		panel.add(adressPane);
		
		panel.add(portLabel);
		panel.add(portPane);
		
		panel.add(cryptoButtonPanel);
		panel.add(jButtonPanel);
		
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
	
	private void createChatUI() {
		//Create a chatUI and assign to it a server or client depending on user selection
		ChatUI chatUI;
		
		//for myData:
		String userName = namePane.getText();
		String address = adressPane.getText();
		// might be too early to set key, unless ceasar-encrypt?
		String key = "temp";
		// ought to be set to true/false in listeners to the encryption-buttons.
		boolean aes = true;
		
		
		
		MyData myData = new MyData(userName, "", "", false, "");
		
		try {
			if (serverRadioButton.isSelected()) {
				myData = new MyData(userName,address,key,aes,"Server");
				chatUI = new ChatUI(new Server(4444), myData);
			}
			else if (clientRadioButton.isSelected()) {
				myData = new MyData(userName,address,key,aes,"Client");
				chatUI = new ChatUI(new Client(4444), myData);
			}
			
			this.dispose();
		
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
