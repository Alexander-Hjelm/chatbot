import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlParser {

	private MyData myData;
	private EncryptionHandler encryptionHandler;
	private User user = null;
	
	public XmlParser(MyData myDataIn) {
		this.myData = myDataIn;
		
	}
	
	
	//method related to run in client/server.
	public Message xmlStringToMessage (String xml) {

		//build a Message from an xml string
		Document xmlDoc = null;
		try { 
			xmlDoc = buildXMLDocumentFromString(xml);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			Message outMsg = new Message("ERROR: XML error at sender. Message is not shown", "System", myData.color);
			//outMsg.setDisconnectType(false);
			return outMsg;
		}
		
		
		Node connectionNode;
		
		//check to see if there's an <disconnect\> tag. Assume connected, but if tag exist, change status. 
		connectionNode = xmlDoc.getElementsByTagName("disconnect").item(0);
		boolean isDisconnectType = false;
		if(!(connectionNode == null)){
			isDisconnectType = true;
		}
		
		//check to see if there's an <keyrequest\> tag. Assume connected, but if tag exist, change status. 
		connectionNode = xmlDoc.getElementsByTagName("keyrequest").item(0);
		boolean isKeyRequestType = false;
		if(!(connectionNode == null)){
			isKeyRequestType = true;
		}
		
		//check to see if there's an <keyresponse\> tag. Assume connected, but if tag exist, change status. 
		connectionNode = xmlDoc.getElementsByTagName("keyresponse").item(0);
		String key = null;
		boolean aes = false;	//Encryption method
		boolean isKeyResponseType = false;
		if(!(connectionNode == null)){
			isKeyResponseType = true;
			Element keyElem = (Element) xmlDoc.getElementsByTagName("keyresponse").item(0);
			key = keyElem.getAttribute("key");//Extract key from the attribute of keyresponse
			
			//set encryption method
			if(keyElem.getAttribute("type").equals("aes")) {
				aes = true;
			}
			
		}
		
		//check to see if there's an <filerequest\> tag. Assume connected, but if tag exist, change status. 
		connectionNode = xmlDoc.getElementsByTagName("filerequest").item(0);
		long size = 0;
		String fileName = null;
		boolean isFileRequestType = false;
		if(!(connectionNode == null)){
			isFileRequestType = true;
			Element fileElem = (Element) xmlDoc.getElementsByTagName("filerequest").item(0);
			fileName = fileElem.getAttribute("name");	//Extract file name from message
			size = Long.parseLong(fileElem.getAttribute("size"));	//Extract file size from message
		}
		
		//check to see if there's an <fileresponse\> tag. Assume connected, but if tag exist, change status. 
		connectionNode = xmlDoc.getElementsByTagName("fileresponse").item(0);
		boolean isFileResponseType = false;
		boolean reply = false;
		int port = 0;
		if(!(connectionNode == null)){
			isFileResponseType = true;
			Element fileElem = (Element) xmlDoc.getElementsByTagName("fileresponse").item(0);
			//Set port for file transfer
			port = Integer.parseInt(fileElem.getAttribute("port"));
			if (fileElem.getAttribute("reply").equals("yes")) {
				//Message contained the reply yes
				reply = true;
			}
		}
		
		
		

		
		MessageType messageType = MessageType.STANDARD;
		if (isDisconnectType) {
			messageType = MessageType.DISCONNECT;
		} else if (isKeyRequestType){
			messageType = MessageType.KEYREQUEST;
		} else if (isKeyResponseType){
			messageType = MessageType.KEYRESPONSE;
		} else if (isFileRequestType){
			messageType = MessageType.FILEREQUEST;
		} else if (isFileResponseType){
			messageType = MessageType.FILERESPONSE;
		}
		
		
//		decrypt
		if((messageType != MessageType.KEYRESPONSE) && (messageType != MessageType.KEYREQUEST)){
			encryptionHandler = new EncryptionHandler(myData.key, myData.aes);
			Element encrypted = (Element) xmlDoc.getElementsByTagName("encrypted").item(0);
			NodeList encryptedChildren = encrypted.getChildNodes();
			for (int i = 0; i < encryptedChildren.getLength(); i++) {
				
				
				String stringToBeDecrypted = encryptedChildren.item(i).getTextContent();
				
				String plainText = "";
				try {
					plainText = encryptionHandler.decrypt(stringToBeDecrypted);
				} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException
						| NoSuchPaddingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				encryptedChildren.item(i).setTextContent(plainText);
				
			}
		}
		
		//make stuff for msg
		
		Element msg = (Element) xmlDoc.getElementsByTagName("message").item(0);
		String sender = msg.getAttribute("sender");
		
		
		Element textElem = (Element) xmlDoc.getElementsByTagName("text").item(0);
		String colorStr = textElem.getAttribute("color");
		
		String text = textElem.getTextContent();
		Color color = buildColorFromString(colorStr);
		
		//de-ecsape necessary fields here
		text = deEscapeXMLChars(text);
		sender = deEscapeXMLChars(sender);
		
		
		

		
		Message outMsg;
		//Only add key to message if it was set before
		if(isKeyResponseType) {
			//This is a key response message
			outMsg = new Message(text, sender, color, messageType, key, aes);
		} else if(isFileRequestType) {
			//This is a file request message
			outMsg = new Message(text, sender, color, messageType, fileName, size);
		} else if(isFileResponseType) {
			//This is a file response message
			outMsg = new Message(text, sender, color, messageType, reply, port);
		} else if(isKeyRequestType) {
			//This is a standard message
			outMsg = new Message(text, sender, color, messageType);
		} else {		
			//This is a standard message
			outMsg = new Message(text, sender, color, messageType);
		} 
		
		return outMsg;
	}
	
	
	//method related to send in client/server.
	public String MessageToXmlString (Message message) {
		//build an xml String from a message
		
		//escape necessary fields here, to handle usage of XML-specific characters
		message.text = escapeXMLChars(message.text);
		message.sender = escapeXMLChars(message.sender);
	
		Document xmlDoc = null;
		try {
			xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		//template
		Element msgElem = xmlDoc.createElement("message");
		Element encryptedElem = xmlDoc.createElement("encrypted");
		Element textElem = xmlDoc.createElement("text");
		textElem.setTextContent(message.text);
		
		//build tree
		xmlDoc.appendChild(msgElem);
		msgElem.appendChild(encryptedElem);
		encryptedElem.appendChild(textElem);
	
		//set attributes
		msgElem.setAttribute("sender", message.sender);
		textElem.setAttribute("color", message.color.toString());
		
		
		//add disconnected tag if message contains connected = false.
		if(message.messageType == MessageType.DISCONNECT){
			msgElem.appendChild(xmlDoc.createElement("disconnect"));
		}
		
		if(message.messageType == MessageType.KEYREQUEST) {
			Element keyReqElem = xmlDoc.createElement("keyrequest");
//			keyElem.setAttribute("type", arg1);
			keyReqElem.setTextContent(message.text);
			msgElem.appendChild(keyReqElem);
		}
		
		if(message.messageType == MessageType.KEYRESPONSE) {
			//Build type string
			String type = "";
			if (myData.aes) {
				type = "aes";
			} else {
				type = "caesar";
			}
			Element keyElem = xmlDoc.createElement("keyresponse");
			keyElem.setAttribute("key", myData.key);
			keyElem.setAttribute("type", type);
			keyElem.setTextContent(message.text);
			msgElem.appendChild(keyElem);
			
		}
		
		if(message.messageType == MessageType.FILEREQUEST) {
			Element fileReqElem = xmlDoc.createElement("filerequest");
			fileReqElem.setAttribute("size", String.valueOf(message.fileSize));
			fileReqElem.setAttribute("type", message.fileName);
			msgElem.appendChild(fileReqElem);
		}
		
		if(message.messageType == MessageType.FILERESPONSE) {
			//Set response boolean
			String reply = "no";
			if (message.fileReply) {
				reply = "yes";
			}
			int port = message.port;
			Element fileElem = xmlDoc.createElement("fileresponse");
			fileElem.setAttribute("reply", reply);
			fileElem.setAttribute("port", Integer.toString(port));
			msgElem.appendChild(fileElem);
			System.out.println(fileElem.getAttribute("reply"));
			
		}
		
		//encrypt
		if((message.messageType != MessageType.KEYRESPONSE) && (message.messageType != MessageType.KEYREQUEST)){
			encryptionHandler = new EncryptionHandler(user.key, user.aes);
			Element encrypted = (Element) xmlDoc.getElementsByTagName("encrypted").item(0);
			NodeList encryptedChilds = encrypted.getChildNodes();
			for (int i = 0; i < encryptedChilds.getLength(); i++) {
				
				
				String stringToBeEncrypted = encryptedChilds.item(i).getTextContent();
				
				String plainText = "";
				try {
					plainText = encryptionHandler.encrypt(stringToBeEncrypted);
				} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException
						| NoSuchPaddingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				encryptedChilds.item(i).setTextContent(plainText);
				
			}
		}
		
		
		
		String xmlString = "";
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(xmlDoc), new StreamResult(writer));
			xmlString = writer.getBuffer().toString().replaceAll("\n|\r", "");
		} catch (IllegalArgumentException | TransformerFactoryConfigurationError | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
				
		return xmlString;
	}
	
	private Document buildXMLDocumentFromString(String xml) throws SAXException, IOException, ParserConfigurationException {
		//parse an xml string into a org.w3c.dom.Document
		DocumentBuilder newDocumentBuilder;
		//From StackOverflow
		newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document parse = newDocumentBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
		return parse;
	}
	
	private String escapeXMLChars(String inputStr) {
		String outStr = inputStr;
		outStr = outStr.replace("&", "&amp;");	//Needs to be first
		outStr = outStr.replace("<", "&lt;");
		outStr = outStr.replace(">", "&gt;");
		outStr = outStr.replace("\"", "&quot;");
		outStr = outStr.replace("'", "&apos;");
		return outStr;
	}
	
	private String deEscapeXMLChars(String inputStr) {
		String outStr = inputStr;
		outStr = outStr.replace("&amp;", "&");	//Needs to be first
		outStr = outStr.replace("&lt;", "<");
		outStr = outStr.replace("&gt;", ">");
		outStr = outStr.replace("&quot;", "\"");
		outStr = outStr.replace("&apos;", "'");
		return outStr;
	}
	
	private Color buildColorFromString(String colorStr) {
		int r;
		int g;
		int b;
		
		int indexR = colorStr.indexOf("r=");
		int indexG = colorStr.indexOf("g=");
		int indexB = colorStr.indexOf("b=");
		
		r = Integer.parseInt(colorStr.substring(indexR + 2, colorStr.indexOf(",", indexR) ));
		g = Integer.parseInt(colorStr.substring(indexG + 2, colorStr.indexOf(",", indexG) ));	
		b = Integer.parseInt(colorStr.substring(indexB + 2, colorStr.indexOf("]", indexB) ));
		
		return new Color(r,g,b);
	}
	
//	to add private boolean checkNode(string tocheck){
//	retBoolean = false;
//	if(}

	public void setUser(User receiverUser) {
		this.user = receiverUser;
		
	}
	
}
