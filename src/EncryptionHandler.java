import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
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
	
	private String thisKey = null;
	private int byteRange = 256;
	private MyData myData;
	
	public EncryptionHandler(MyData myDataIn){
		
		this.myData = myDataIn;	
		
		
	}

	//ciphers
	//caesar/shift-cipher:
	public String encryptCaesar(String inputString) {
	
		int key = Integer.parseInt(thisKey);
		
		byte[] b = inputString.getBytes(StandardCharsets.UTF_8);	
		
		for (int i = 0; i < b.length; i++) {
			

			int current = b[i] & 0xff;
			System.out.println("currentint " + current);
			int shifted = ((current + key) % byteRange);
			b[i] = (byte) shifted;
		}
		
		String encryptedHexString = DatatypeConverter.printHexBinary(b);
		
		return(encryptedHexString);
	}
	
	public String caesarDecrypt(String encryptedHexString) {
		
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
		
		String outputString = new String(b, StandardCharsets.UTF_8);	
		return(outputString);
	}
	
	
	
	
	//aes:
	
	
	private SecretKey aesStringToKey(String stringKey){
        byte[] encodedKey     = Base64.getDecoder().decode(stringKey);
        return new SecretKeySpec(encodedKey, "AES");
	}
	
	
	public String aesEncrypt(String inputString, String stringKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException{
		SecretKey secKey = aesStringToKey(stringKey);
        
        byte[] b = inputString.getBytes(StandardCharsets.UTF_8);
        
        // encrypt the bytes of inputstring
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] bytesEncrypted = aesCipher.doFinal(b);
        
        //make to hex-string
        String aesHexString = DatatypeConverter.printHexBinary(bytesEncrypted);
		return aesHexString;
	} 
	
	
	
	
	public String aesDecrypt(String encryptedHexString, String stringKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException{
		SecretKey secKey = aesStringToKey(stringKey);
		
		//hex to bytes
		byte[] b = DatatypeConverter.parseHexBinary(encryptedHexString);
		
		
		//decrypt bytes
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        byte[] bytesDecrypted = aesCipher.doFinal(b);

		
		
		//interpret bytes. gg
		String plainText = new String(bytesDecrypted, StandardCharsets.UTF_8);
		return plainText;
	}
		
}
	

