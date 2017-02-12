import java.awt.Panel;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Program{

	private ArrayList<User> userList;
	
//	private static JFrame chatFrame;
//	


//	private static JFrame buildChatFrame() {
//		JFrame frame = new JFrame();
//		chatFrame.setTitle("Chat Window");
//		return new JFrame();
//	}
//	
//	private static JFrame buildIntroFrame() {
//		return new JFrame();
//	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		chatFrame = buildChatFrame();
//		createAndShowGUI(chatFrame);
		IntroUI introUI = new IntroUI();
		ChatUI chatUI = new ChatUI();
	}

}
