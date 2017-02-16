import javax.swing.JFrame;

public class CommunicationsHandler implements Runnable{

	private ChatUI chatUi;

	public void run() {
		// Override this
	}
	
	public void send(Message msg) {
		// Override this
	}
	
	public void setUI(ChatUI UI) {
		// Override this
		
	}
	
	public int exit() {
		// Override this
		return ((int) JFrame.EXIT_ON_CLOSE);
	}
	
	
	
	protected void startThread() {
		Thread t = new Thread(this);
		t.start();
	}
	
	
	
}
