import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class FileServer implements Runnable{
	//Sends files over a separate thread and socket
	
	private File file;
	private ServerSocket server;
	private Socket socket;
	private DataOutputStream streamOut;
	private FileInputStream fileStreamIn;
	private Thread t;
	private ChatUI chatUI;
	private int bufferSize;
	private User destinationUser;
	
	public FileServer(File file, ChatUI chatUI, int bufferSize, User destinationUser) {
		this.file = file;
		this.chatUI = chatUI;
		this.bufferSize = bufferSize;
		this.destinationUser = destinationUser;
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
			//Instantiate streams
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

			//Disable send file button
			chatUI.toggleSendFileButton();
			
			//Encrypt file
			EncryptionHandler encryptionHandler = new EncryptionHandler(destinationUser.key, destinationUser.aes);
			
			
	        byte[] bytes = new byte[bufferSize];
	        int currentBytes = 0;
	        try {
				while (fileStreamIn.read(bytes) != -1){
					String encryptedHex = "";
					try {
						//Bytes to hex
						encryptedHex = encryptionHandler.encrypt(bytes);
						//Write bytes to socket
						streamOut.writeUTF(encryptedHex);
					} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException
							| NoSuchAlgorithmException | NoSuchPaddingException | IOException e1) {
						e1.printStackTrace();
					}
					
					//Update progressbar on chatUI
					currentBytes += bufferSize;
					double frac = (double) currentBytes / (double) file.length();
					int progressBarFill = (int) (frac * 100);
					chatUI.setProgressBarFill(progressBarFill);
					
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	        
			//Enable send file button
			chatUI.toggleSendFileButton();
	        
	        try {
		        fileStreamIn.close();
		        streamOut.close();
		        socket.close();
		        server.close();
	        } catch (IOException e) {
				e.printStackTrace();
			}
	        
	        if (server.isClosed()) {
	        	//Stop thread
	        	return;
	        }
		}

}



