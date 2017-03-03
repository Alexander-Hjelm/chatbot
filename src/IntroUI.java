import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
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
	private JColorChooser colorChooser = new JColorChooser();

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
		clientRadioButton.setSelected(true);
		
		cryptoButtonGroup = new ButtonGroup();
		cryptoButtonGroup.add(aesRadioButton);
		cryptoButtonGroup.add(caesarRadioButton);
		caesarRadioButton.setSelected(true);
		
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
		
		exitButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 System.exit(0);
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
		
		panel.add(colorChooser);
		colorChooser.setColor(Color.BLACK);
		
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
		String address = adressPane.getText();	// only for client, use "localhost" for testing
		Color color = colorChooser.getColor();
		
		//default to port 4444 the port-string is no good. 
		int port;
		try {
			port = Integer.parseInt(portPane.getText());
		} catch (NumberFormatException e1) {
			port = 4444;
//			e1.printStackTrace();
		}

		//Encryption stuff
		
		String key = null;
		boolean aes;
		Keygen keygen = new Keygen();
		
		// Set encryption method based on user selection
		if (aesRadioButton.isSelected()) {
			aes = true;
			try {
				key = keygen.generateAesKey();
			} catch (NoSuchAlgorithmException e) {
				System.out.println("Trouble generating AES-key");
				e.printStackTrace();
			}
		}
		else {
			aes = false;
			key = keygen.generateCaesarKey();
		}
		
		MyData myData;
		Server server;
		Client client;
		
		
		try {
			if (serverRadioButton.isSelected()) {
				myData = new MyData(userName,address,key,aes,"Server", color);
				server = new Server(port, myData);
//				chatUI = new ChatUI(new Server(port, myData), myData);
			}
			else if (clientRadioButton.isSelected()) {
				myData = new MyData(userName,address,key,aes,"Client", color);
				client = new Client(address, port, myData);
//				chatUI = new ChatUI(new Client(address, port, myData), myData);
			}
			
			this.dispose();
		
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
