import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class FileServer implements Runnable{

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
			
			StringBuilder fileStringBuilder = new StringBuilder();
			int ch;
			try {
				while((ch = fileStreamIn.read()) != -1){
					fileStringBuilder.append((char)ch);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			String fileString = fileStringBuilder.toString();
			String outStr = "";
			try {
				outStr = encryptionHandler.encrypt(fileString);
			} catch (InvalidKeyException | IllegalBlockSizeException
					| BadPaddingException | NoSuchAlgorithmException
					| NoSuchPaddingException e1) {
				e1.printStackTrace();
			}
			//outStr is now the encrypted file string

			InputStream stream = new ByteArrayInputStream(outStr.getBytes(StandardCharsets.UTF_8));
	        byte[] bytes = new byte[bufferSize];
	        int currentBytes = 0;
	        int count;
	        try {
				while ((count = stream.read(bytes)) > 0) {
					streamOut.write(bytes, 0, count);
					
					//Fill progress bar on ChatUI
					currentBytes += bufferSize;
					double frac = (double) currentBytes/ (double) file.length();
					int progressBarFill = (int) (frac * 100);
					chatUI.setProgressBarFill(progressBarFill);
				}
	        } catch (IOException e) {
				e.printStackTrace();
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
	        	return;
	        }
		}

}



