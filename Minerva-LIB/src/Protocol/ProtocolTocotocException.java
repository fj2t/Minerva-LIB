package Protocol;

/**
 *
 * @author fj
 */
public class ProtocolTocotocException extends ProtocolException {

    /**
     * Creates a new instance of <code>ProtocolTocotocExcepction</code> without
     * detail message.
     */
    public ProtocolTocotocException () {
        super( "(TocToc)" );
    }

    /**
     * Constructs an instance of <code>ProtocolTocotocExcepction</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ProtocolTocotocException ( String msg ) {
        super( "(TocToc): " + msg );
    }

}
