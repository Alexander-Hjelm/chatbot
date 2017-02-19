import java.awt.Color;

public class Message {
	public String text;
	public String sender;
	public Color color;
	public boolean connected;
	
	public Message(String text, String sender, Color color, boolean connected) {
		this.text = text;
		this.sender = sender;
		this.color = color;
		this.connected = connected;
	}
}
