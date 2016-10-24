package Protocol.threads;

import Application.ServerApplication;
import Protocol.Message;
import com.mysql.jdbc.Connection;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Base de los hilos de proceso de protocolos.
 *
 * @author fj
 */
public abstract class BaseThreads extends Thread {
    protected final Socket socket;
    protected final Message message;
    protected final ServerApplication application;

    /**
     * Constructor base.
     *
     * @param socket      Socket donde se ha recibido el toctoc.
     * @param message     Message del toctoc.
     * @param application ServerApplication que lo ha recibido.
     */
    public BaseThreads ( Socket socket, Message message, ServerApplication application ) {
        this.socket = socket;
        this.message = message;
        this.application = application;
    }

    /**
     * Método de conexión a la base de datos (se puede mejorar muuucho).
     *
     * @return El objeto sqlConnection.
     *
     * @throws ClassNotFoundException El Driver MySQL no está disponible.
     * @throws SQLException           No se ha podido realizar la conexión.
     */
    protected Connection connectDB () throws ClassNotFoundException, SQLException {
        Connection connection;

        Class.forName( "com.mysql.jdbc.Driver" );
        connection = ( Connection ) DriverManager.
                getConnection( application.getDataBaseServer(), application.getDataBaseUser(), application.getDatabasePass() );
        return connection;
    }

}
