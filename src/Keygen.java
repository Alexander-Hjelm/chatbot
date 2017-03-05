import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Keygen {
	//Generate random keys for each available crypto
	
	public String generateCaesarKey() {
		int key = (int) (Math.random() * 10000);
		return Integer.toString(key);
	}

	public String generateAesKey() throws NoSuchAlgorithmException{
		SecretKey secKey = KeyGenerator.getInstance("AES").generateKey();
		String stringKey = Base64.getEncoder().encodeToString(secKey.getEncoded());
		return stringKey;
		
	}
}
