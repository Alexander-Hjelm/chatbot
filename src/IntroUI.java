import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.swing.BorderFactory;
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
		
		panel = new JPanel();

		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints(); 

	    FlowLayout titleLayout = new FlowLayout();
	    titleLayout.setAlignment(FlowLayout.CENTER);
	    JPanel titlePanel = new JPanel(titleLayout);
	    titlePanel.add(titleLabel);	    
		
		c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 0.5;
        panel.add(titlePanel,c);
		
		clientServerButtonGroup = new ButtonGroup();
		clientServerButtonGroup.add(clientRadioButton);
		clientServerButtonGroup.add(serverRadioButton);
		clientRadioButton.setSelected(true);
		
		GridLayout clientServerButtonLayout = new GridLayout(0, 5);
        JPanel clientServerPanel = new JPanel(clientServerButtonLayout);
        clientServerPanel.setBorder(BorderFactory.createTitledBorder("Run as client or server?"));
        
        clientServerPanel.add(clientRadioButton);
        clientServerPanel.add(radioButtonClientLabel);
        clientServerPanel.add(new JLabel("           "));	//Whitespace
        clientServerPanel.add(serverRadioButton);
		clientServerPanel.add(radioButtonServerLabel);
		
		c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 1;
        c.gridx = 0;
        c.weightx = 0.5;
        panel.add(clientServerPanel,c);
		
		cryptoButtonGroup = new ButtonGroup();
		cryptoButtonGroup.add(aesRadioButton);
		cryptoButtonGroup.add(caesarRadioButton);
		caesarRadioButton.setSelected(true);
		
		GridLayout cryptoButtonLayout = new GridLayout(0, 2);
		JPanel cryptoButtonPanel = new JPanel(cryptoButtonLayout);
		cryptoButtonPanel.setBorder(BorderFactory.createTitledBorder("Crypto:"));
		
		cryptoButtonPanel.add(aesRadioButton);
		cryptoButtonPanel.add(radioButtonAesLabel);
		cryptoButtonPanel.add(caesarRadioButton);
		cryptoButtonPanel.add(radioButtonCaesarLabel);
		
		c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 2;
        c.gridx = 0;
        c.weightx = 0.5;
        panel.add(cryptoButtonPanel,c);

		connectButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 //Create a new client or server, and assign that to a new ChatUI
	        	 //ChatUI now has control
	        	 createChatUI();
	         }
	      });
		
		exitButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	 			dispose();
				
				// if no windows are currently up, exit normally
				if (Frame.getFrames().length == 0) {
					System.exit(0);
				}
	         }
	      });

		GridLayout namePanesLayout = new GridLayout(2, 2);
		JPanel namePanesPanel = new JPanel(namePanesLayout);
		namePanesPanel.setBorder(BorderFactory.createTitledBorder(""));
		
		namePanesPanel.add(nameLabel);
		namePanesPanel.add(namePane);
		
		c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 3;
        c.gridx = 0;
        c.weightx = 0.5;
        panel.add(namePanesPanel,c);
		
		GridLayout inputPanesLayout = new GridLayout(2, 2);
		JPanel inputPanesPanel = new JPanel(inputPanesLayout);
		inputPanesPanel.setBorder(BorderFactory.createTitledBorder("Connection details:"));
		
		inputPanesPanel.add(adressLabel);
		inputPanesPanel.add(adressPane);
		inputPanesPanel.add(portLabel);
		inputPanesPanel.add(portPane);

		c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 4;
        c.gridx = 0;
        c.weightx = 0.5;
        panel.add(inputPanesPanel,c);
		
        colorChooser.setColor(Color.BLACK);
        
		c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 5;
        c.gridx = 0;
        c.weightx = 0.5;
        panel.add(colorChooser,c);	
		
		GridLayout jButtonLayout = new GridLayout(1, 0);
		JPanel jButtonPanel = new JPanel(jButtonLayout);
		jButtonPanel.add(connectButton);
		jButtonPanel.add(exitButton);
		
		c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 6;
        c.gridx = 0;
        c.weightx = 0.5;
        panel.add(jButtonPanel,c);

		
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
			
			// if no windows are currently up, exit normally
			if (Frame.getFrames().length == 0) {
				System.exit(0);
			}
		
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
