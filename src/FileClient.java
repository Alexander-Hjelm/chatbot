import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class FileClient implements Runnable{

	private Socket socket;
	private DataInputStream streamIn;
	private FileOutputStream fileStreamOut;
	private Thread t;
	private String fileName;
	private long fileSize;
	private ChatUI chatUI;
	private int bufferSize;
	
	public FileClient(String addr, int port, String fileName, long fileSize, ChatUI chatUI, int bufferSize) {
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.chatUI = chatUI;
		this.bufferSize = bufferSize;
		
		try {
			socket = new Socket(addr, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
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

		while (t != null) {

			try {
				streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			File file = new File(fileName);

            try {
				fileStreamOut = new FileOutputStream(file);
				
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

			//Disable send file button
			chatUI.toggleSendFileButton();

	        byte[] bytes = new byte[bufferSize];
	        int currentBytes = 0;
	        int count;
	        try {
				while ((count = streamIn.read(bytes)) > 0) {
					fileStreamOut.write(bytes, 0, count);
					
					//Fill progress bar on ChatUI
					currentBytes += bufferSize;
					double frac = (double) currentBytes/ (double) fileSize;
					int progressBarFill = (int) (frac * 100);
					chatUI.setProgressBarFill(progressBarFill);
				}
	        } catch (IOException e) {
				e.printStackTrace();
			}
	        
			//Enable send file button
			chatUI.toggleSendFileButton();
	        
	        try{
		        fileStreamOut.close();
		        streamIn.close();
		        socket.close();
	        } catch (IOException e) {
				e.printStackTrace();
			}
	        
	        if (socket.isClosed()) {
	        	return;
	        }
		}
	}
}