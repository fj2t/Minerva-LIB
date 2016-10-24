package Application;

import Protocol.InterlocutorType;

/**
 * Clase padre con informacion relevante sobre la aplicacion Se mantiene por dos
 * motivos: -Primero, porque es utilizado por varias funciones del protocolo
 * -Segundo, porque guarda informacion igualmente util, como los Ids, Typo de
 * aplicacion, etc
 *
 * @author fj
 */
public abstract class Application {
    private final String clientID;
    private final String serverID;
    private final InterlocutorType appType;

    public Application ( String clienteID, String serverID, InterlocutorType appType ) {
        this.clientID = clienteID;
        this.serverID = serverID;
        this.appType = appType;
    }

    public String getClientID () {
        return clientID;
    }

    public String getServerID () {
        return serverID;
    }

    public InterlocutorType getAppType () {
        return appType;
    }

    public abstract String getSoftwareID ();

}
