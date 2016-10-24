package Protocol;

/**
 *
 * @author fj
 */
public class ProtocolException extends Exception {

    /**
     * Creates a new instance of <code>ProtocolException</code> without detail
     * message.
     */
    public ProtocolException () {
        super( "Error de protocolo" );
    }

    /**
     * Constructs an instance of <code>ProtocolException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ProtocolException ( String msg ) {
        super( "Error de protocolo: " + msg );
    }

}
