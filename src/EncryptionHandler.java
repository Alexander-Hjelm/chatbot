import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class EncryptionHandler {
	
	private String thisKey = null;
	private int thisShift;
	private int byteRange = 256;
	
	public EncryptionHandler(boolean aes){
		if(aes){
			//this.thisKey = generateAesKey()
		} 
		else {
			this.thisShift = generateCaesarKey(); 
		}
		
		
	}
	
	private int generateCaesarKey() {
		int key = (int) Math.random() * 10000;
		return key;
	}

	//dummy
	private String generateAesKey(){
		return "";
		
	}
	
	
	public String getHexFromByte(byte[] b){
	    return String.format("%x", new BigInteger(1, b));
	}
	
	public String getStringFromHex(String hexString){
		byte[] b = new BigInteger(hexString,16).toByteArray();
		String retStr = new String(b, StandardCharsets.UTF_8);	
		return retStr;
	}
	
	private String encryptCaesar(String inputString) {
//		
		byte[] b = inputString.getBytes(StandardCharsets.UTF_8);	
		
		for (int i = 0; i < b.length; i++) {
			

			int current = b[i] & 0xff;
			System.out.println("currentint " + current);
			int shifted = ((current + thisShift) % byteRange);
			b[i] = (byte) shifted;
		}
		
		String outputString = getHexFromByte(b);
		
		return(outputString);
	}
	
	private String decryptCaesar(String hexString) {
		
		byte[] b = new BigInteger(hexString,16).toByteArray();
		
		for (int i = 0; i < b.length; i++) {
			
			int current = b[i] & 0xff;
			int shifted = ((current - thisShift) % byteRange);
			if (shifted < 0) {
				shifted += byteRange;
				}
			
			b[i] = (byte) shifted;
		}
		
		String outputString = new String(b, StandardCharsets.UTF_8);	
		return(outputString);
	}
	

}