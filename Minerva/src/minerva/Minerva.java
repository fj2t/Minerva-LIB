package minerva;

import Application.ClientApplication;
import GUI.MainFrame;
import Protocol.Data.ClientLoginData;
import Protocol.ClientRequest;
import Protocol.MessageType;
import Protocol.Protocol;
import Protocol.ProtocolException;
import Protocol.ProtocolTocotocException;
import Protocol.ServerResponse;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author fj
 */
public class Minerva extends ClientApplication implements ActionListener {

    private JFrame mainFrame;

    public Minerva(String clientID, String remoteHost, int remotePort, String serverID) {
        super(clientID, remoteHost, remotePort, serverID);
    }

    public static void main(String[] args) {
        final int remotePort = 42321;
        final String remoteHost = "localhost";
        final String clientID = "Minerva Cliente V 0.1";
        final String serverID = "Minerva Server V 0.1";
        final Minerva application = new Minerva(clientID, remoteHost, remotePort, serverID);
        application.setSessionId(null);

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mainForm;
                mainForm = new MainFrame(application);
                application.setMainFrame(mainForm);
                mainForm.setVisible(true);
            }
        });
    }

    /**
     * Petición al servidor para el login.
     *
     * @param userName
     * @param password
     * @param dlgLogin
     * @return devuelve el sessionId si todo ha sido correcto
     * @throws ProtocolException
     * @trowos ProtocolTocotocException
     */
    @Override
    public String doLogin(String userName, String password, Object dlgLogin) {
        String sessionId = null;
        setSessionId(null);
        Protocol login = new Protocol(MessageType.LOGIN, this);
        System.out.println("1");
        try {
            if (login.tocTocRequest()) {
                System.out.println("2");
                try {
                    login.sendClientRequest(new ClientRequest(new ClientLoginData(userName, password).toJSONString(), this.getSoftwareID()).toJSONString());
                    System.out.println("3");
                    ServerResponse response = login.getServerResponse();
                    switch (response.getResponseError()) {
                        case NO_ERROR: {
                            sessionId = response.getResponse();
                            System.out.println(sessionId);
                            break;
                        }
                        case CREATE_SESSION: {
                            // Informar al cliente de que ha pasado algo muy chungo
                            JOptionPane.showMessageDialog((JDialog) dlgLogin, "Ha pasado algo muy chungo!", "Horror!", JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                        case BAD_AUTH: {
                            // Credenciales no válidas
                            JOptionPane.showMessageDialog((JDialog) dlgLogin, "Credenciales no válidas", "Horror!", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        }
                    }
                } catch (ProtocolException ex) {
                    Logger.getLogger(Minerva.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("No true");
            }
        } catch (ProtocolTocotocException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }

        setSessionId(sessionId);
        return sessionId;
    }  
    

    public void doAck() {
        if (getSessionId() == null) {
            return;
        }
        Protocol ack = new Protocol(MessageType.ACK, this);
        try {
            if (ack.tocTocRequest()) {
                try {
                    ack.sendClientRequest(new ClientRequest(getSessionId(), this.getSoftwareID()).toJSONString());
                    ServerResponse response = ack.getServerResponse();
                    switch (response.getResponseError()) {
                        case NO_ERROR: {
                            break;
                        }
                        case CREATE_SESSION: {
                            // Informar al cliente de que ha pasado algo muy chungo
                            JOptionPane.showMessageDialog((JFrame) mainFrame, "Ha pasado algo muy chungo!", "Horror!", JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                        /* case INVALID_SESSION: {
                         // Credenciales no válidas
                         JOptionPane.showMessageDialog((JFrame) mainFrame, "Sesión no válida!", "Horror!", JOptionPane.ERROR_MESSAGE);
                         Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Seesión no válida en ACK");
                         System.exit(3);
                         break;
                         }*/
                    }
                } catch (ProtocolException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (ProtocolTocotocException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("ACK")) {
            doAck();
        }
    }

    public void setMainFrame(JFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

}