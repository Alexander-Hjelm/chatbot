
public class User {
	
	
	public String name;
	public String adress;
//	public Socket socket; for later.
	public String key;
	public boolean aes;
	
	public User(String name, String adress, String key, boolean aes) {
		this.name = name;
		this.adress = adress;
		this.key = key;
		this.aes = aes;
	}
}
