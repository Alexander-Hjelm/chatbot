import java.awt.Color;

public class Message {
	public String text;
	public String sender;
	public Color color;
	
	public boolean isDisconnectType;
	public boolean isKeyRequestType;
	public boolean isKeyResponseType;
	
	public Message(String text, String sender, Color color) {
		this.text = text;
		this.sender = sender;
		this.color = color;
	}
	
	public void setDisconnectType( boolean boolIn ) {
		isDisconnectType = boolIn;
	}
	
	public void setKeyRequestType( boolean boolIn ) {
		isDisconnectType = boolIn;
	}
	
	public void setKeyResponseType( boolean boolIn ) {
		isDisconnectType = boolIn;
	}
}
