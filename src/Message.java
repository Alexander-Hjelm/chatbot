import java.awt.Color;

public class Message {
	public String text;
	public String sender;
	public Color color;
	public String key;
	public boolean aes;
	public String fileName;
	public int fileSize;
	
	public MessageType messageType;
	
	public Message(String text, String sender, Color color) {
		this.text = text;
		this.sender = sender;
		this.color = color;
		this.messageType = MessageType.STANDARD;	//Assume standard message type if nothing else is given.
	}
	
	public Message(String text, String sender, Color color, MessageType messageType) {
		//Standard
		this.text = text;
		this.sender = sender;
		this.color = color;
		this.messageType = messageType;
	}
	
	public Message(String text, String sender, Color color, MessageType messageType, String key, boolean aes) {
		//Key response
		this.text = text;
		this.sender = sender;
		this.color = color;
		this.messageType = messageType;
		this.key = key;
		this.aes = aes;
	}
	
	public Message(String text, String sender, Color color, MessageType messageType, String fileName, int fileSize) {
		//File request
		this.text = text;
		this.sender = sender;
		this.color = color;
		this.messageType = messageType;
		this.fileName = fileName;
		this.fileSize = fileSize;
	}
	
}