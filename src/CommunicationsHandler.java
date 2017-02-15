
public class CommunicationsHandler implements Runnable{

	private ChatUI chatUi;

	public void run() {
		// Override this
	}
	
	public void send(String message) {
		// Override this
	}
	
	public void setUI(ChatUI UI) {
		// Override this
	}
	
	protected void startThread() {
		Thread t = new Thread(this);
		t.start();
	}
	
}
