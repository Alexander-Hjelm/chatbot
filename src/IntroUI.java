import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;




public class IntroUI extends JFrame{

	private JPanel panel;

	
	
	public IntroUI() {
		super("Intro Window");
		
		GridLayout gridLayout = new GridLayout(0, 2);
		
		panel = new JPanel(gridLayout);
		
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
