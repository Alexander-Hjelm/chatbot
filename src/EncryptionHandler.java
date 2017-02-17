
public class EncryptionHandler {
	
	private String thisKey = null;
	private int thisShift;
	
	public EncryptionHandler(boolean aes){
		if(aes){
			//this.thisKey = generateAesKey()
		} 
		else {
			this.thisShift = generateCaesarKey(); 
		}
		
		
	}
	
	private int generateCaesarKey() {
		// TODO Auto-generated method stub
		return 0;
	}

	//dummy
	private String generateAesKey(){
		return "";
		
	}
//	
//	public String encrypt(String message) {
//		if(!thisKey.equals(null)) {
//			return "";
//		}
//		else {
//			
//		}
//	}
//	

}

//
//+ encrypt(String message): 
//	   String
//	+ decrypt(String message):
//	   String 
//	+ generateAesKey()