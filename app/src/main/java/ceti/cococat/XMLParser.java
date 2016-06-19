package ceti.cococat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by law on 19/06/16.
 */
public class XMLParser{
    private Document document;

    public XMLParser() {}
    public XMLParser(String content) {
        try {
            this.document =  DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(new StringReader(content)));
        }
        catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (org.xml.sax.SAXException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int[] getPartidas() {
        int[] partidas;
        NodeList nodes = this.document.getElementsByTagName("partida");
        partidas = new int[nodes.getLength()];
        for(int i=nodes.getLength()-1; i>=0; i--)
            partidas[i] = Integer.parseInt(nodes.item(i).getTextContent());
        return partidas;
    }

    public void createPartidas(int status){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            Element root = document.createElement("partidas");
            document.appendChild(root);
            Element partida;
            partida = document.createElement("partida");
            partida.setTextContent(String.valueOf(status));
            root.appendChild(partida);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addPartida(int status){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Element root = document.getDocumentElement();
        Element partida;
        partida = document.createElement("partida");
        partida.setTextContent(String.valueOf(status));
        root.appendChild(partida);
    }

    public String getDocumentString(){
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"yes");
            StringWriter writer = new StringWriter();
            trans.transform(new DOMSource(document), new StreamResult(writer));
            String out = writer.getBuffer().toString();
            return out;
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
