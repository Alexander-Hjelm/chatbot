import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer implements Runnable{

	private File file;
	private ServerSocket server;
	private Socket socket;
	private DataInputStream streamIn;
	private FileOutputStream fileStreamOut;
	private Thread t;
	
	public FileServer(File file) {
		this.file = file;
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

		while (t != null) {

			try {
				streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			try {
	            fileStreamOut = new FileOutputStream(this.file);
	        } catch (FileNotFoundException ex) {
	            System.out.println("File not found. ");
	        }

	        byte[] bytes = new byte[(int) file.length()];

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
		        server.close();
	        } catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
	
}
