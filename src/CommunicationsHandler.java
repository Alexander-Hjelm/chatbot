import javax.swing.JFrame;

public abstract class CommunicationsHandler implements Runnable{

//	private ChatUI chatUi;

	public abstract void run();
	public abstract void send(String message);
	public abstract void setUI(ChatUI UI);
	public abstract void exit();
	protected abstract void startThread();
	
	
	
}
