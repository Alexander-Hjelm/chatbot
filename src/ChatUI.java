import javax.swing.JFrame;
import javax.swing.JPanel;




public class ChatUI extends JFrame{

	private JPanel panel;
	
	public ChatUI() {
		super("Chat Window");
	
		
		
		
		
		
		panel = new JPanel();
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
