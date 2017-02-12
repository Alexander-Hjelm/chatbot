import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;




public class IntroUI extends JFrame{

	private JPanel panel;

	//Panel elements
	private JLabel titleLabel = new JLabel("Chat Program");
	private JLabel nameLabel = new JLabel("Name / alias:");
	private JLabel portLabel  = new JLabel("Port:");
	private JLabel adressLabel = new JLabel("Adress:");
	
	private JTextPane namePane = new JTextPane();
	
	
//	+ adressLabel: JLabel
//	+ portLabel: JLabel
//	+ adressPane: JTextPane
//	+ portPane: JTextPane
//	+ connectButton: JButton
//	+ exitButton: JButton
//	+ clientButton: JRadioButton
//	+ serverButton: JRadioButton
//	+ clientServerButtonGroup: ButtonGroup
//	+ aesButton: JRadioButton
//	+ caesarButton: JRadioButton
//	+ encryptionGroup: ButtonGroup
//	+ toggleClientServerUI()
	
	public IntroUI() {
		super("Intro Window");
		
		GridLayout gridLayout = new GridLayout(0, 1);

		panel = new JPanel(gridLayout);
		
		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		titlePanel.add(titleLabel);
		
		JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		namePanel.add(nameLabel);
		namePanel.add(namePane);
		
		panel.add(titlePanel);
		panel.add(namePanel);
		
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
