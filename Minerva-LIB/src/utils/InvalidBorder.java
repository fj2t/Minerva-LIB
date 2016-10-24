package utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.TextField;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 *
 * @author fj
 */
public class InvalidBorder implements Border {
    private final Color c;
    private final Insets insets;
    private final boolean opaque;
    
    public InvalidBorder( Color c ) {
        super();
        this.c = c;
        JTextField tmpTextField = new JTextField();
        insets = tmpTextField.getBorder().getBorderInsets( tmpTextField );
        opaque = tmpTextField.getBorder().isBorderOpaque();
    }
    
    @Override
    public void paintBorder( Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor( Color.red );
        g.drawRect(x, y, width, height);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return insets;
    }

    @Override
    public boolean isBorderOpaque() {
        return opaque;
    }
}
