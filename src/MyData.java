import java.awt.Color;

public class MyData {
	protected String userName;
	protected String address;
	protected String key;
	protected boolean aes;
	protected String communicationType;
	protected Color color;
	
	
	public MyData(String userNameIn, String addressIn, String keyIn, boolean aesIn, String communicationTypeIn, Color colorIn) {
		userName = userNameIn;
		address = addressIn;
		key = keyIn;
		aes = aesIn;
		communicationType = communicationTypeIn;
		color = colorIn;
	}
	
}

