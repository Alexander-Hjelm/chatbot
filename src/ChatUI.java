import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;




public class ChatUI extends JFrame{

	private CommunicationsHandler communicationsHandler;

	private JPanel panel;
	private JTextPane messageArea;
	private JScrollPane messageScrollPane;
	private JLabel titleLabel; 
	private JLabel otherNamesLabel;
	private JTextField myMessagePane;
	
	
	private JButton sendButton;
	private JButton exitButton;
	private JButton sendFileButton;
	private JButton newChatButton;
	private JFileChooser fileChooser = new JFileChooser();
	private FileReceiverUI fileReceiverUI;
	private JProgressBar progressBar = new JProgressBar(0, 100);
	
	private MyData myData;
	
	
	
	public ChatUI(CommunicationsHandler communicationsHandlerIn, MyData myDataIn) {
		super("Chat Window");
		this.communicationsHandler = communicationsHandlerIn;
		this.myData = myDataIn;

		panel = new JPanel();
		buttonAction();
		createAndShowGUI(panel);
		
		// tell communicationsHandler, client or server that this ChatUI is their UI:
//		communicationsHandler.setUI(this);
		
		//Send Key request AFTER UI has been set, otherwise we get a NullPointerException on ChatUI
//		communicationsHandler.sendKeyRequest();
		//moved to server/client

				

	}
	
	

	
	
	private void buttonAction() {
		sendButton = new JButton("send");
		exitButton = new JButton("exit");;
		sendFileButton = new JButton("send file");
		newChatButton = new JButton("new chat");
		
		
		sendButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	           	 sendMessage();
	         }
	      });
		
		
		
		exitButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	            try {
					communicationsHandler.exit();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
	            
	         }
	      });
		
		sendFileButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 //Show a file chooser GUI
	             int returnVal = fileChooser.showOpenDialog(panel);

	             if (returnVal == JFileChooser.APPROVE_OPTION) {
	            	 File file = fileChooser.getSelectedFile();
	                 communicationsHandler.sendFileRequest(file, myMessagePane.getText());
	                 

	                 
//	         		 Check for xml errors,  \o/
//	                 XmlParser xmlParser = new XmlParser(myData);
//	                 xmlParser.xmlStringToMessage(xmlParser.MessageToXmlString(new Message(myData.userName, myMessagePane.getText(), myData.color)));
//	         		
	         		myMessagePane.setText("");
	             }
	         }
	      });
		
		newChatButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 // add listener stuff, some get data from myMessagePane for this button.
	         }
	      });
		
	}

	private void createAndShowGUI(JPanel panel) {
		
		messageArea = new JTextPane();
		messageArea.setEditable(false);
		
		messageScrollPane = new JScrollPane(messageArea);
		titleLabel = new JLabel(myData.communicationType + ": " + myData.userName); 
		otherNamesLabel  = new JLabel();
		myMessagePane = new JTextField();

		
		
		//make myMessagePane listen to enters and send message if pressed. might better be defined elsewhere
		myMessagePane.addKeyListener(new KeyListener() {
			
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void keyPressed(KeyEvent e) {
		        if (e.getKeyCode()==KeyEvent.VK_ENTER){
		        	sendMessage();
		        }
		    

		    }
		});
		
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
        c.gridy = 3;
        c.gridx = 1;
        c.weightx = 1;
        panel.add(sendFileButton,c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 3;
        c.gridx = 0;
        c.weightx = 1;
        panel.add(progressBar,c);
        
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
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        this.addWindowListener( new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
            	
            	try {
					communicationsHandler.exit();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	
                JFrame frame = (JFrame)e.getSource();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
	  
	    //Add contents to the window.
		this.add(panel);
	
	    //Display the window.
		this.pack();
		this.setVisible(true);
		
		
	}
	
	public void showFileReceiverUI(String fileName, long fileSize) {
		fileReceiverUI = new FileReceiverUI(communicationsHandler, fileName, fileSize);
	}
	
	private void sendMessage() {
		// might want to build XML-message here, or it could be constructed in client/server with the string sent.
		String text = myMessagePane.getText();
		Message msg = new Message(text, myData.userName, myData.color);
		
		communicationsHandler.send(msg);
//							  _   _
//		Check for xml errors,  \o/
//		XmlParser xmlParser = new XmlParser(myData); 
//		String outText = xmlParser.xmlStringToMessage(xmlParser.MessageToXmlString(msg)).text;
//		msg.text = outText;
		
		myMessagePane.setText("");
		updateMessageArea(msg);

	}
	
	public void updateMessageArea(Message msg) {
		//Set text color
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, msg.color);

        int len = messageArea.getDocument().getLength();
    	String appendStr = msg.sender + ": " + msg.text + "\n";
        
    	//Print text in MessageArea
        try {
			messageArea.getDocument().insertString(len, appendStr, aset);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
//		this.messageArea.append(msg.sender + ": " + msg.text + "\n");
	}

	public void setProgressBarFill (int i) {
		progressBar.setValue(i);
	}
	
}
