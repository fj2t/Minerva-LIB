package utils;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author fj
 */
public abstract class MyUtils {

    public static void center( Container wnd ) {
        Container parent = wnd.getParent();
        Point formLocation;
        if ( wnd.getClass().getSuperclass() == JFrame.class ) {
            Toolkit t = wnd.getToolkit();
            Dimension screenSize = t.getScreenSize();
            Insets insets = t.getScreenInsets( wnd.getGraphicsConfiguration() );
            formLocation = new Point( 0, 0 );
            formLocation.x = ( screenSize.width - insets.left - insets.right - wnd.getWidth() ) / 2 ;
            formLocation.y = ( screenSize.height - insets.top - insets.bottom - wnd.getHeight() ) / 2;
        } else {
            formLocation = parent.getLocation();
            formLocation.x += ( parent.getWidth() - wnd.getWidth() ) / 2 ;
            formLocation.y += ( parent.getHeight() - wnd.getHeight() ) / 2;
        }
        wnd.setLocation( formLocation );
    }

}
