import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlParser {

	private MyData myData;
	
	public XmlParser(MyData myData) {
		this.myData = myData;
	}
	
	public Message xmlStringToMessage (String xml) {
		//build a Message from an xml string
		Document xmlDoc = null;
		try { 
			xmlDoc = buildXMLDocumentFromString(xml);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			return new Message("ERROR: XML error at sender. Message is not shown", "System", myData.color, true);
		}
		
		xmlDoc.getElementsByTagName("text").item(0).getTextContent();
		String text = xmlDoc.getElementsByTagName("text").item(0).getTextContent();
		String sender = xmlDoc.getElementsByTagName("sender").item(0).getTextContent();
		String colorStr = xmlDoc.getElementsByTagName("color").item(0).getTextContent();
		
		//check to see if there's an <disconnect\> tag. Assume connected, but if tag exist, change status. 
		Node connectionNode = xmlDoc.getElementsByTagName("disconnect").item(0);
		boolean connected = true;
		if(!(connectionNode == null)){
			connected = false;
		}
		
		
		//tried to find attributes, got fatal error.
//		NodeList nl = (NodeList) xmlDoc.getElementsByTagName("disconnect");
//		String connectionStatus = nl.item(0).getAttributes().getNamedItem("status").getNodeValue();
		
		
		Color color = buildColorFromString(colorStr);
		
		//de-ecsape necessary fields here
		text = deEscapeXMLChars(text);
		sender = deEscapeXMLChars(sender);
				
		return new Message(text, sender, color, connected);
	}

	public String MessageToXmlString (Message message) {
		//build an xml String from a message
		
		//escape necessary fields here, to handle usage of XML-specific characters
		message.text = escapeXMLChars(message.text);
		message.sender = escapeXMLChars(message.sender);
		
		
		String retStr = 
				"<message>"
					+ "<text>"
						+ message.text
					+ "</text>"
					+ "<sender>"
						+ message.sender
					+ "</sender>"
					+ "<color>"
						+ message.color.toString()
					+ "</color>"
				+ "</message>"
					;
		
		//add disconnected tag if message contains connected = false.
		if(!message.connected){
			int strLen = retStr.length();
			retStr = retStr.substring(0, strLen - 10) + "<disconnect/>" + retStr.substring(strLen - 10, strLen);
		}
		
		return  retStr;
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
	
}
