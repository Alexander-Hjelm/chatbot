import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XmlParser {

	public Message xmlStringToMessage (String xml) {
		//build a Message from an xml string
		Document xmlDoc = null;
		try { 
			xmlDoc = buildXMLDocumentFromString(xml);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			return new Message("ERROR: XML error at sender. Message is not shown", "System");
		}
		
		xmlDoc.getElementsByTagName("text").item(0).getTextContent();
		String text = xmlDoc.getElementsByTagName("text").item(0).getTextContent();
		String sender = xmlDoc.getElementsByTagName("sender").item(0).getTextContent();;
		
		//de-ecsape necessary fields here
		text = deEscapeXMLChars(text);
		sender = deEscapeXMLChars(sender);
				
		return new Message(text, sender);
	}
	
	public String MessageToXmlString (Message message) {
		//build an xml String from a message
		
		//escape necessary fields here, to handle usage of XML-specific characters
		message.text = escapeXMLChars(message.text);
		message.sender = escapeXMLChars(message.sender);
		
		return  "<message>"
					+ "<text>"
						+ message.text
					+ "</text>"
					+ "<sender>"
						+ message.sender
					+ "</sender>"
				+ "</message>"
				;
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
	
}
