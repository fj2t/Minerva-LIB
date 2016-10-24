package Protocol;

import Xml.XML;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import javax.management.modelmbean.XMLParseException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Estructura basica del mensaje XML que vamos a mandar, y a tratar Han habido 
 * cambios con el hundir la flota en toda la estructura del mensaje: Antes, el 
 * mensaje tenia:
 * 
 * <Message><Interlocutor><Data>Content</Data></Interlocutor></Mensaje>.
 *
 * Ahora, he eliminado etiquetas inutiles en mi opinion La etiqueta Interlocutor 
 * a pasado a ser un atributo de Message, Interlocutor Type Data a dejado de existir, 
 * y es directamente un json, Content.
 * 
 * Si hay que pasar datos como contraseñas, o usuarios, se enviaran en el momento 
 * necesario, y no tendremos siempre unos datos flotando en todos los mensajes con 
 * valores nulos.
 *
 * La nueva estructura es:
 * <Message type=ord(MessageType) interlocutor="ord(InterlocutorType)">Content</Message>.
 *
 * @author fj
 */
public class Message extends BaseMessage {

    private final MessageType type;
    private final InterlocutorType interlocutor;
    private String content; // será un JSON

    /**
     * Este es el constructor del mensaje....
     *
     * @param type Enumeración con el tipo del mensaje
     * @param interlocutor Enumeración con el tipo de interlocutor.
     * @param content JSON con el contenido del mensaje.
     */
    public Message(MessageType type, InterlocutorType interlocutor, String content) {
        this.type = type;
        this.interlocutor = interlocutor;
        this.content = content;
    }

    /**
     * Constructor a partir de un elemento XML.
     *
     * @param xml Elemento XML
     */
    private Message(XML xml) throws NullPointerException, DataFormatException {

        Node node = xml.getRootElement();
        if (node == null) {
            throw new NullPointerException();
        }

        try {
            int intType = Integer.parseInt(node.getAttributes().
                    getNamedItem("type").getNodeValue());
            this.type = MessageType.fromOrdinal(intType);
        } catch (NullPointerException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            throw new DataFormatException("Atributo \"type\" inexistente.");
        }
        try {
            int intInterlocutorType = Integer.parseInt(node.getAttributes().
                    getNamedItem("interlocutor").getNodeValue());
            this.interlocutor = InterlocutorType.fromOrdinal(intInterlocutorType);
        } catch (NullPointerException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            throw new DataFormatException("Atributo \"interlocutor\" inexistente.");
        }
        this.content = xml.getContent();
    }

    public String getStringType() {
        return String.format("%02d", type.ordinal());
    }

    public MessageType getType() {
        return type;
    }

    public String getStringInterlocutorType() {
        return String.format("%02d", interlocutor.ordinal());
    }

    public InterlocutorType getInterlocutorType() {
        return interlocutor;
    }

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        if (content.startsWith("<![CDATA[")) {
            content = content.substring(9, content.lastIndexOf("]]>"));
        }
        this.content = content;
    }

    /**
     *
     * @param parent
     *
     * @return
     */
    @Override
    public Element toXML(Node parent) {
        Element element = ((Document) parent).createElement(getClass().getSimpleName());
        element.setAttribute("type", getStringType());
        element.setAttribute("interlocutor", getStringInterlocutorType());
        element.appendChild(((Document) parent).createCDATASection(getContent()));
        setContent(element.getTextContent());
        return element;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        String str;

        str = "<" + getClass().getSimpleName() + " type=\"" + getStringType() + " interlocutor=\"" + getStringInterlocutorType() + "\">";
        str += content;
        str += "</" + getClass().getSimpleName() + ">";
        return str;
    }

    /**
     * Método que retorna una instancia de tipo BaseMessage desde la cadena XML.
     *
     * @param strXML Cadena XML que representa al objeto que quiero instaciar.
     *
     * @return La nueva instancia
     *
     * @throws javax.management.modelmbean.XMLParseException
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws java.util.zip.DataFormatException
     */
    static public Message getInstanceFromXMLString(String strXML)
            throws FactoryConfigurationError, ParserConfigurationException,
            XMLParseException, NullPointerException, DataFormatException {
        XML xml = XML.fromString(strXML);
        Message newMessage = new Message(xml);
        return newMessage;
    }

    public boolean isValidRemoteHost(InterlocutorType hostType, String hostId) {
        String jsonString = content;
        String softId = "";
        switch (hostType) {
            case CLIENT:  {          
                ClientRequest request = ClientRequest.fromJSONString(jsonString);
                softId = request.getSoftwareID();
                break;
            }
            case SERVER: {
                ServerResponse request = ServerResponse.fromJSONString(jsonString);
                softId = request.getSoftwareID();
                break;
            }
            default:
                return false;
        }
        return getInterlocutorType() == hostType && softId.equals(hostId);
    }

    public static Message getInstance(InterlocutorType hostType, MessageType type, ServerResponse response) {
        return new Message(type, hostType, response.toJSONString());
    }

    /**
     *
     * @param socket
     *
     * @return
     *
     * @throws IOException
     * @throws FactoryConfigurationError
     * @throws ParserConfigurationException
     * @throws XMLParseException
     * @throws NullPointerException
     * @throws DataFormatException
     */
    public static Message getFromSocket(Socket socket) throws IOException, FactoryConfigurationError, ParserConfigurationException, XMLParseException, NullPointerException, DataFormatException {
        char[] buffer = new char[2500];
        
        String leido = "";
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        do {
            bufferedReader.read(buffer);
            leido += new String(buffer);

        } while (bufferedReader.ready());

        return Message.getInstanceFromXMLString(leido.trim());
    }

}
