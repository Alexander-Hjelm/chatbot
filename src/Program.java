import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class Program{

	private ArrayList<User> userList;
	
	
	public static void main(String[] args) throws UnknownHostException, IOException {
	
		System.setProperty("sun.net.useExclusiveBind", "false");	// Allows reusing ports once the original socket has been closed
		IntroUI introUI = new IntroUI();
		
	}

}
