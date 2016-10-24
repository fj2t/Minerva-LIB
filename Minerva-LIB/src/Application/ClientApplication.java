package Application;

import java.net.Socket;
import Protocol.InterlocutorType;

/**
 *
 * @author fj
 */
public abstract class ClientApplication extends Application {

    private final int remotePort;
    private final String remoteHost;
    private Socket clientSocket;
    private String sessionId;
    private String matchId;

    public ClientApplication(String clienteID, String remoteHost, int remotePort, String serverID) {
        super(clienteID, serverID, InterlocutorType.CLIENT);
        this.remotePort = remotePort;
        this.remoteHost = remoteHost;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getMatchId() {
        return matchId;
    }

    @Override
    public String getSoftwareID() {
        return getClientID();
    }

    public abstract String doLogin(String userName, String password, Object dlgLogin);
   
}
