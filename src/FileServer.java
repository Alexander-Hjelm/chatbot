import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer implements Runnable{

	private File file;
	private ServerSocket server;
	private Socket socket;
	private DataOutputStream streamOut;
	private FileInputStream fileStreamIn;
	private Thread t;
	private ChatUI chatUI;
	private int bufferSize;
	
	public FileServer(File file, ChatUI chatUI, int bufferSize) {
		this.file = file;
		this.chatUI = chatUI;
		this.bufferSize = bufferSize;
	}

	public void startServer(int port) {
		try {
			this.server = new ServerSocket(port);
			socket = server.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
		startThread();
	}
	
	protected void startThread() {
		this.t = new Thread(this);
		t.start();
	}
	
	@Override
	public void run() {

			try {
				streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			try {
				fileStreamIn = new FileInputStream(file);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

	        byte[] bytes = new byte[(int) file.length()];

	        int count;
	        int maxCount = 0;
	        try {
				while ((count = fileStreamIn.read(bytes)) > 0) {
					streamOut.write(bytes, 0, count);
					
					//Fill progress bar on ChatUI
					maxCount = Math.max(maxCount, count);
					int progressBarFill = (maxCount - count)/maxCount;
					chatUI.setProgressBarFill(progressBarFill);
				}
	        } catch (IOException e) {
				e.printStackTrace();
			}
	        
	        try {
		        fileStreamIn.close();
		        streamOut.close();
		        socket.close();
		        server.close();
	        } catch (IOException e) {
				e.printStackTrace();
			}
	        
	        if (server.isClosed()) {
	        	return;
	        }
		}

}



