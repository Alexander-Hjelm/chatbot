import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


public class FileUserSelectorUI extends JFrame{

	private File file;
	private String messageText;
	private CommunicationsHandler communicationsHandler;
	private User user = null;
	
	//Swing elements
	private JPanel panel;
	private ButtonGroup buttonGroup;
	private JButton sendButton;
	
	public FileUserSelectorUI(File file, String messageText, CommunicationsHandler communicationsHandler) {
		this.file = file;
		this.messageText = messageText;
		this.communicationsHandler = communicationsHandler;
		
		//Swing
		GridLayout layout = new GridLayout(0, 2);
		panel = new JPanel(layout);
		buttonGroup = new ButtonGroup();
		
		JPanel radioButtonPanel = new JPanel();
		//Build radio buttons based on users
		for (User u : ((Server) communicationsHandler).getClientUsers()) {
			JRadioButton radioButton = new JRadioButton(u.name);
			buttonGroup.add(radioButton);
			radioButtonPanel.add(radioButton);
		}
				
		sendButton = new JButton("send");
		sendButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	           	 sendMessage();
	           	 dispose();
	         }
	      });
		
		panel.add(radioButtonPanel);
		panel.add(sendButton);
		
	    //Add contents to the window.
		this.add(panel);
	
	    //Display the window.
		this.pack();
		this.setVisible(true);
		
	}

	public void sendMessage() {
		//Get index of selected button
		int index = 0;
		int count = 0;
		for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                index = count;
            }
            count++;
        }
		
		//Send file request
		user = ((Server) communicationsHandler).getClientUsers().get(index);
		communicationsHandler.sendFileRequest(file, messageText, user);
	}
	
}

