import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class EncryptionHandler {
	
	private String thisKey;
	private int byteRange = 256;
	private boolean aes;
	
	public EncryptionHandler(String keyIn, boolean aesIn){
		
		this.thisKey = keyIn;	
		this.aes = aesIn;
		
	}
	
	public String encrypt(byte[] byteInput) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException{
		//Evaluate cipher and encrypt
		if(aes){
			return aesEncrypt(byteInput);
		}
		else{
			return caesarEncrypt(byteInput);
		}
	}
	
	public byte[] decrypt(String inputString) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException{
		//Evaluate cipher and decrypt
		if(aes){
			return aesDecrypt(inputString);
		}
		else{
			return caesarDecrypt(inputString);
		}
	}

	//ciphers
	//caesar/shift-cipher:
	private String caesarEncrypt(byte[] byteInput) {
	
		int key = Integer.parseInt(thisKey);
		
		for (int i = 0; i < byteInput.length; i++) {
			

			int current = byteInput[i] & 0xff;
			int shifted = ((current + key) % byteRange);
			byteInput[i] = (byte) shifted;
		}
		
		String encryptedHexString = DatatypeConverter.printHexBinary(byteInput);
		
		return(encryptedHexString);
	}
	
	private byte[] caesarDecrypt(String encryptedHexString) {
		
		int key = Integer.parseInt(thisKey);
		
		byte[] b = DatatypeConverter.parseHexBinary(encryptedHexString);
		
		for (int i = 0; i < b.length; i++) {
			
			int current = b[i] & 0xff;
			int shifted = ((current - key) % byteRange);
			if (shifted < 0) {
				shifted += byteRange;
				}
			
			b[i] = (byte) shifted;
		}
		
		return b;
	}
	
	
	
	
	//aes:
	private SecretKey aesStringToKey(String stringKey){
        byte[] encodedKey     = Base64.getDecoder().decode(stringKey);
        return new SecretKeySpec(encodedKey, "AES");
	}
	
	
	private String aesEncrypt(byte[] byteInput) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException{
		SecretKey secKey = aesStringToKey(thisKey);
        
        // encrypt the bytes of inputstring
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] bytesEncrypted = aesCipher.doFinal(byteInput);
        
        //make to hex-string
        String encryptedHexString = DatatypeConverter.printHexBinary(bytesEncrypted);
		return encryptedHexString;
	} 
	
	
	
	
	private byte[] aesDecrypt(String encryptedHexString) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException{
		SecretKey secKey = aesStringToKey(thisKey);
		
		//hex to bytes
		byte[] b = DatatypeConverter.parseHexBinary(encryptedHexString);
		
		
		//decrypt bytes
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        byte[] bytesDecrypted = aesCipher.doFinal(b);
		return bytesDecrypted;
	}
		
}
	

