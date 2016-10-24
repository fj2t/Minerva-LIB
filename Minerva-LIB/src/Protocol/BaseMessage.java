package Protocol;

import Xml.XML;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * Estas funciones siguen siendo utiles, de modo que se mantienen
 *
 * @author fj
 */
public abstract class BaseMessage {

    /**
     * Simplifica el uso de <code>this.getClass().getSimpleName()</code>
     *
     * @return Un String con el nombre simple de la clase.
     */
    public final String getClassName () {
        return getClass().getSimpleName();
    }

    /**
     * Enviar un mensaje...
     *
     * @param msg Mensaje a enviar.
     * @param out Por donde se envía.
     */
    public static void sendXML ( Message msg, OutputStream out ) {
        XML xml = new XML();   
        
        xml.getDocument().appendChild( msg.toXML( xml.getDocument() ) );
        
        try {
            xml.write( out );
        } catch ( TransformerException ex ) {
            Logger.getLogger( BaseMessage.class.getName() ).log( Level.SEVERE, null, ex );
        } catch ( IOException ex ) {
            Logger.getLogger( BaseMessage.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

    /**
     * Método que retorna un nodo xml que representa al objeto actual para
     * insertarlo en <b>{@link #parent parent}</b>.
     *
     *
     * @param parent Rama xml ({@link org.w3c.dom.Node Node}), donde insertar el
     *               nodo creado.
     *
     * @return el nodo creado.
     */
    public abstract Element toXML ( Node parent );

}
