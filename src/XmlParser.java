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
		Document xmlDoc = buildXMLDocumentFromString(xml);
		
		xmlDoc.getElementsByTagName("text").item(0).getTextContent();
		String text = xmlDoc.getElementsByTagName("text").item(0).getTextContent();
		String sender = xmlDoc.getElementsByTagName("sender").item(0).getTextContent();;
		
		return new Message(text, sender);
	}
	
	public String MessageToXmlString (Message message) {
		//build an xml String from a message
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
	
	private Document buildXMLDocumentFromString(String xml) {
		//parse an xml string into a org.w3c.dom.Document
		DocumentBuilder newDocumentBuilder;
		try {
			//From StackOverflow
			newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document parse = newDocumentBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
			return parse;
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	public void test() {
//		Message message = xmlStringToMessage("<message><text>fuk aff</text><sender>Viktor</sender></message>");
//		System.out.println(message.text);
//		System.out.println(message.sender);
//		
//		String xml = MessageToXmlString(new Message("ty same", "Alexader"));
//		System.out.println(xml);
//		
//		message = xmlStringToMessage(xml);
//		System.out.println(message.text);
//		System.out.println(message.sender);
//	}
}
