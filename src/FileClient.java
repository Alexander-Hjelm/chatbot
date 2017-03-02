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
	
	public FileClient(String addr, int port, String fileName, long fileSize) {
		this.fileName = fileName;
		this.fileSize = fileSize;
		
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


	        byte[] bytes = new byte[(int) fileSize];

	        int count;
	        try {
				while ((count = streamIn.read(bytes)) > 0) {
					fileStreamOut.write(bytes, 0, count);
				}
	        } catch (IOException e) {
				e.printStackTrace();
			}
	        
	        try{
		        fileStreamOut.close();
		        streamIn.close();
		        socket.close();
	        } catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}