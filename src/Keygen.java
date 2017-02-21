
public class Keygen {
	
	public String generateCaesarKey() {
		int key = (int) (Math.random() * 10000);
		return Integer.toString(key);
	}

	//dummy
	public String generateAesKey(){
		return "";
		
	}
}
