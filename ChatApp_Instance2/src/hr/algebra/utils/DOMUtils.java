package hr.algebra.utils;

import hr.algebra.model.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

public class DOMUtils {
    private static final String FILE_NAME = "chat.xml";

    public static void saveChat(ObservableList<Message> messages) {
        try {
            Document document = createDocument();
            messages.forEach(message -> {
                if (!message.isImage())
                    document.getDocumentElement().appendChild(
                            createMessageElement(message, document)
                    );
            });
            saveDocument(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Document createDocument() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation domImplementation = builder.getDOMImplementation();
        return domImplementation.createDocument(null, FILE_NAME, null);
    }

    private static Node createMessageElement(Message message, Document document) {
        Element element = document.createElement("message");
        element.appendChild(createElement(document, "idMessage", String.valueOf(message.getIdMessage())));
        element.appendChild(createElement(document, "messageContent", new String(message.getMessageContent())));
        element.appendChild(createElement(document, "fromId", String.valueOf(message.getFromId())));
        element.appendChild(createElement(document, "toId", String.valueOf(message.getToId())));
        element.appendChild(createElement(document, "time", message.getTime().toString()));
        element.appendChild(createElement(document, "isImage", String.valueOf(message.isImage())));
        return element;
    }

    private static Node createElement(Document document, String tag, String content) {
        Element element = document.createElement(tag);
        element.appendChild(document.createTextNode(content));
        return element;
    }

    private static void saveDocument(Document document) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(document), new StreamResult(new File(DOMUtils.FILE_NAME)));
    }

    public static List<Message> loadMessages() {
        List<Message> messages = FXCollections.observableArrayList();
        try {
            Document document = createDocument(new File(FILE_NAME));
            NodeList nodes = document.getElementsByTagName("message");
            for (int i = 0; i < nodes.getLength(); i++) {
                messages.add(processMessageNode((Element) nodes.item(i)));
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return messages;
    }

    private static Message processMessageNode(Element element) {
        return new Message(
                Integer.parseInt(element.getElementsByTagName("idMessage").item(0).getTextContent()),
                element.getElementsByTagName("messageContent").item(0).getTextContent().getBytes(),
                Integer.parseInt(element.getElementsByTagName("fromId").item(0).getTextContent()),
                Integer.parseInt(element.getElementsByTagName("toId").item(0).getTextContent()),
                Timestamp.valueOf(element.getElementsByTagName("time").item(0).getTextContent()),
                Boolean.getBoolean(element.getElementsByTagName("isImage").item(0).getTextContent())
        );
    }

    private static Document createDocument(File file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(file);
    }
}
