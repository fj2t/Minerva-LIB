package Application;

import Protocol.InterlocutorType;

/**
 *
 * @author fj
 */
public class ServerApplication extends Application {
    private final int gamePort;
    private String dataBaseServer;
    private String dataBaseUser;
    private String databasePass;
    private boolean runServer;

    public ServerApplication ( String serverID, int gamePort, String clienteID ) {
        super( clienteID, serverID, InterlocutorType.SERVER );
        this.gamePort = gamePort;
        runServer = true;
    }   

    public int getGamePort () {
        return gamePort;
    }

    @Override
    public String getSoftwareID () {
        return getServerID();
    }

    public boolean isRunning () {
        return runServer;
    }

    public void setExecution ( boolean execution ) {
        this.runServer = execution;
    }

    public String getDataBaseServer () {
        return dataBaseServer;
    }

    public void setDataBaseServer ( String dataBaseServer ) {
        this.dataBaseServer = dataBaseServer;
    }

    public String getDataBaseUser () {
        return dataBaseUser;
    }

    public void setDataBaseUser ( String dataBaseUser ) {
        this.dataBaseUser = dataBaseUser;
    }

    public String getDatabasePass () {
        return databasePass;
    }

    public void setDatabasePass ( String databasePass ) {
        this.databasePass = databasePass;
    }

}
