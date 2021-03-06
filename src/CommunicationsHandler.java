import java.io.File;
import java.io.IOException;

public abstract class CommunicationsHandler implements Runnable{
	
	//protected constructor -> can only be instantiated by server and client
	protected CommunicationsHandler(){}

	public abstract void run();
	public abstract void send(Message msg);
	public abstract void sendFileRequest(File file, String additionalText, User destinationUser);
	public abstract void sendFileResponse(boolean reply, int port, String additionalText, String fileName, long fileSize, int destinationUserIndex);
	public abstract void exit() throws IOException;
	protected abstract void startThread();
	
}