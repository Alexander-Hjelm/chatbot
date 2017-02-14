
public class CommunicationsHandler implements Runnable{

	private ChatUI chatUi;

	public void run() {
		// Override this in
		
	}
	
	public void send(String message) {
		
	}
	
	protected void startThread() {
		Thread t = new Thread(this);
		t.start();
	}
	
}
