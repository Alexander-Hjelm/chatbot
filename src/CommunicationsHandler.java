public abstract class CommunicationsHandler implements Runnable{
	
	//protected constructor -> can only be instantiated by server and client
	protected CommunicationsHandler(){}

	public abstract void run();
	public abstract void send(Message msg);
	public abstract void setUI(ChatUI UI);
	public abstract void exit();
	protected abstract void startThread();
	
}