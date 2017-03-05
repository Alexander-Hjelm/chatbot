import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class FileClient implements Runnable{
	//Accepts incoming files over a separate thread and socket
	
	private Socket socket;
	private DataInputStream streamIn;
	private FileOutputStream fileStreamOut;
	private Thread t;
	private String fileName;
	private long fileSize;
	private ChatUI chatUI;
	private int bufferSize;
	private MyData myData;
	
	public FileClient(String addr, int port, String fileName, long fileSize, ChatUI chatUI, int bufferSize, MyData myDataIn) {
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.chatUI = chatUI;
		this.bufferSize = bufferSize;
		this.myData = myDataIn;
		
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
			//Instantiate streams
			try {
				streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			File file = new File(fileName);
			EncryptionHandler encryptionHandler = new EncryptionHandler(myData.key, myData.aes);
            try {
				fileStreamOut = new FileOutputStream(file);
				
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

			//Disable send file button
			chatUI.toggleSendFileButton();

	        byte[] bytes = new byte[bufferSize];
	        int currentBytes = 0;
	        String encryptedHex;
	        try {
				while ((currentBytes) < fileSize) {
					//decrypt file
					encryptedHex = streamIn.readUTF();
					try {
						bytes = encryptionHandler.decrypt(encryptedHex);
					} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException
							| NoSuchAlgorithmException | NoSuchPaddingException e) {
						e.printStackTrace();
					}
					
					//Write to file
					fileStreamOut.write(bytes);
					
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
	        	//Stop thread
	        	return;
	        }
		}
	}
}