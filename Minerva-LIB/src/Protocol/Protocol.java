package Protocol;

import Application.Application;
import Application.ClientApplication;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import Application.ServerApplication;
import java.util.zip.DataFormatException;
import javax.management.modelmbean.XMLParseException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

/**
 * El protocolo, en mayor o menor medida, funciona exactamente igual Necesitamos abrir conexiones (toctoc), recibir mensajes y enviar mensajes Manejamos tipos de mensaje (para definir el tipo de
 * protocolo), la app y el mensaje en si.
 *
 * @author fj
 */
public class Protocol {

    private final MessageType messageType;
    private final Application app;
    private final Message message;

    public Protocol(MessageType messageType, Application app) {
        this.messageType = messageType;
        this.app = app;
        this.message = null;
    }

    public Protocol(Message message, Application app) {
        this.messageType = message.getType();
        this.app = app;
        this.message = message;
    }

    // Client-side comms
    public boolean tocTocRequest() throws ProtocolTocotocException {
        Message message;
        ClientApplication app = (ClientApplication) this.app;
        Socket clientSocket;

        message = new Message(messageType, app.getAppType(), 
                new ClientRequest("", app.getClientID()).toJSONString());

        try {
            clientSocket = new Socket(app.getRemoteHost(), app.getRemotePort());
            app.setClientSocket(clientSocket);
            //clientSocket.setSoTimeout(2000);
            Message.sendXML(message, clientSocket.getOutputStream());
            
            try {
                message = Message.getFromSocket(clientSocket);
                
            } catch (FactoryConfigurationError ex) {
                Logger.getLogger(Protocol.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(Protocol.class.getName()).log(Level.SEVERE, null, ex);
            } catch (XMLParseException ex) {
                Logger.getLogger(Protocol.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NullPointerException ex) {
                Logger.getLogger(Protocol.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DataFormatException ex) {
                Logger.getLogger(Protocol.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (message.isValidRemoteHost(InterlocutorType.SERVER, app.getServerID())) {

                return true;
            }
        } catch (IOException ex) {
            log(Level.SEVERE, ex);
            throw new ProtocolTocotocException("Al realizar la conexión con servidor");
        }
        return false;
    }

    public void sendClientRequest(String data) throws ProtocolException {
        ClientApplication app = (ClientApplication) this.app;
        Socket clientSocket = app.getClientSocket();
        Message message = new Message(messageType, app.getAppType(), data);
        try {
            Message.sendXML(message, clientSocket.getOutputStream());
        } catch (IOException ex) {
            log(Level.SEVERE, ex);
            throw new ProtocolException("Al enviar solicitud al servidir");
        }
    }

    public ServerResponse getServerResponse() throws ProtocolException {
        ClientApplication app = (ClientApplication) this.app;
        Socket clientSocket = app.getClientSocket();
        try {
            Message message = null;
            try {
                message = Message.getFromSocket(clientSocket);
            } catch (FactoryConfigurationError ex) {
                Logger.getLogger(Protocol.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(Protocol.class.getName()).log(Level.SEVERE, null, ex);
            } catch (XMLParseException ex) {
                Logger.getLogger(Protocol.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NullPointerException ex) {
                Logger.getLogger(Protocol.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DataFormatException ex) {
                Logger.getLogger(Protocol.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (message.isValidRemoteHost(InterlocutorType.SERVER, app.getServerID())) {
                ServerResponse response = ServerResponse.fromJSONString(message.getContent());
                return response;
            }
        } catch (IOException ex) {
            log(Level.SEVERE, ex);
            throw new ProtocolException("Al recibir solicitud del servidor!");
        }
        return null;
    }

    // Server-side comms
    public Message totTocResponse(Message message, Socket socket) throws ProtocolTocotocException {
        ServerApplication app = (ServerApplication) this.app;
        boolean client = message.isValidRemoteHost(InterlocutorType.CLIENT, app.getClientID());
        
        if (client) {
            message = Message.getInstance(app.getAppType(),
                    messageType, new ServerResponse("", !app.isRunning(), app.getSoftwareID()));
            try {
                Message.sendXML(message, socket.getOutputStream());
                return message;
            } catch (IOException ex) {
                log(Level.SEVERE, ex);
                throw new ProtocolTocotocException("Al recibir petición del cliente");
            }
        }
        log(Level.WARNING, "Solicitud de tocToc, malformadada: [" + message.toString() + "]");

        return null;
    }

    public Message readClientRequest(Socket socket) throws ProtocolException {
        boolean client;
        ServerApplication app = (ServerApplication) this.app;
        try {
            Message message = null;
            try {
                message = Message.getFromSocket(socket); // Obtenemos un nuevo mensge leído del socket
            } catch (FactoryConfigurationError ex) {
                Logger.getLogger(Protocol.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(Protocol.class.getName()).log(Level.SEVERE, null, ex);
            } catch (XMLParseException ex) {
                Logger.getLogger(Protocol.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NullPointerException ex) {
                Logger.getLogger(Protocol.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DataFormatException ex) {
                Logger.getLogger(Protocol.class.getName()).log(Level.SEVERE, null, ex);
            }
            client = message.isValidRemoteHost(InterlocutorType.CLIENT, app.getClientID());
            
            if (client) {            
                return message;
            }
        } catch (IOException ex) {
            log(Level.SEVERE, ex);
            throw new ProtocolException("Al recibir petición del cliente");
        }
        log(Level.WARNING, "Solicitud no válida [" + message.toString() + "]");
        return null;
    }

    public void sendServerResponse(ServerResponse response, Socket socket) throws ProtocolException {
        ServerApplication app = (ServerApplication) this.app;
        Message message = Message.getInstance(app.getAppType(), messageType, response);
        try {

            Message.sendXML(message, socket.getOutputStream());
        } catch (IOException ex) {
            log(Level.SEVERE, ex);
            throw new ProtocolException("Al enviar la respuesta al cliente [" + message.toString() + "]");
        }
    }

    private void log(Level level, Object obj) {
        if (obj.getClass().isInstance(String.class)) {
            Logger.getLogger(getClass().getName()).log(level, (String) obj);
        } else {
            Logger.getLogger(getClass().getName()).log(level, null, obj);
        }
    }

}
