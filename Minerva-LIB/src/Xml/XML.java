package Xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import javax.management.modelmbean.XMLParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import Protocol.Message;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Funcion para manejar XMLs Se mantiene la clase exactamente igual. Otra cosa
 * es que queramos usar XML
 *
 * @author fj
 */
public class XML {
    private Element rootElement;
    private Document document;

    public XML ( String strXML ) throws XMLParseException, ParserConfigurationException, FactoryConfigurationError {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;

            builder = factory.newDocumentBuilder();
            document = builder.parse( new InputSource( new StringReader( strXML ) ) );

            document.setXmlVersion( "1.0" );
            rootElement = document.getDocumentElement();
        } catch ( SAXException | IOException ex ) {
            Logger.getLogger( this.getClass().getName() ).log( Level.SEVERE, null, ex );
            throw new XMLParseException( "Cadena XML es inválida en el constructor." );
        }
    }

    public XML () {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch ( ParserConfigurationException ex ) {
            Logger.getLogger( Message.class.getName() ).log( Level.SEVERE, null, ex );
        }
        document = builder.getDOMImplementation().createDocument( null, null, null );
        document.setXmlVersion( "1.0" );
    }

    private ByteArrayOutputStream transform () throws TransformerException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty( OutputKeys.METHOD, "xml" );
        transformer.setOutputProperty( OutputKeys.CDATA_SECTION_ELEMENTS, "data" );
        //transformer.setOutputProperty( OutputKeys.INDENT, "yes" );
        DOMSource source = new DOMSource( document );
        transformer.transform( source, new StreamResult( outputStream ) );
        return outputStream;
    }

    public void write ( OutputStream out ) throws TransformerException, IOException {
        out.write( this.transform().toByteArray() );
        out.flush();
    }

    public Element getRootElement () {
        return rootElement;
    }

    public Document getDocument () {
        return document;
    }

    @Override
    public String toString () {
        String strResult = "";
        try {
            strResult = new String( this.transform().toByteArray() );
        } catch ( TransformerException ex ) {
            Logger.getLogger( XML.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return strResult + "\n";
    }

    static public XML fromString ( String strXML ) throws FactoryConfigurationError, XMLParseException, ParserConfigurationException {
        return new XML( strXML );
    }

    /**
     * Obtiene el contenido del mesaje del XML.
     *
     * @return Cadena dentro del CDATA.
     *
     * @throws DataFormatException En caso de que la etiqueta CDATA no exista.
     */
    public String getContent () throws DataFormatException {
        String content = rootElement.getTextContent().trim();
//        if ( content.equals( "" ) ) {
//            return "";
//        }
//        if ( !content.startsWith( "<![CDATA[" ) ) {
//            throw new DataFormatException( "Error en contenido, etiqueta CDATA inexistente" );
//        }
        return content;
        //return content.substring( 9, content.lastIndexOf( "]]>" ) ).trim();
    }

}
