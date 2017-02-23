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
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class FileReceiverUI extends JFrame {

	private CommunicationsHandler communicationsHandler;
	
	private JPanel panel;
	
	private JButton yesButton;
	private JButton noButton;
	private JTextField portTextField;
	private JTextField optionalMessageTextField;
	
	public FileReceiverUI(CommunicationsHandler communicationsHandler) {
		super("Accept incoming file?");
		this.communicationsHandler = communicationsHandler;
		panel = new JPanel();
		
		buttonAction();
		
		portTextField = new JTextField("port");
		optionalMessageTextField = new JTextField("Optional message");
		
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints(); 
		
		// add stuff to panel then add panel to frame.
		c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 0.5;
        panel.add(portTextField,c);
		
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.gridx = 1;
        c.weightx = 1;
        panel.add(optionalMessageTextField, c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 1;
        c.gridx = 0;
        c.weightx = 1;
        panel.add(yesButton,c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 1;
        c.gridx = 1;
        c.weightx = 1;
        panel.add(noButton,c);
        		
		
	    //Create and set up the window.
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	  
	    //Add contents to the window.
		this.add(panel);
	
	    //Display the window.
		this.pack();
		this.setVisible(true);
		
		
	}
	
	private void buttonAction() {
		yesButton = new JButton("Yes");
		noButton = new JButton("No");
		
		yesButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	           	 communicationsHandler.sendFileResponse(true, optionalMessageTextField.getText());
	           	 dispose();
	         }
	      });
		
		noButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 communicationsHandler.sendFileResponse(false, optionalMessageTextField.getText());
	        	 dispose();
	         }
	      });

	}

}
