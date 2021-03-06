package mlog;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.AbstractButton;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * A custom look for the Java Swing JButton.
 * 
 * @author Filip Ístermark
 * @version 2013-05-28
 */
public class CustomButtonUI extends BasicButtonUI {
	
	private Font font;
	private boolean enabled;
	
	/**
	 * Creates a new CustomButtonUI.
	 */
	public CustomButtonUI() {
		super();
		this.font = new Font(Font.DIALOG, Font.BOLD, 12);
		this.enabled = true;
	}
	
	/**
	 * Paints the text on the button.
	 */
    @Override
    protected void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text) {
    	Graphics2D g2d = (Graphics2D) g;
    	if (this.enabled) {
    		g2d.setColor(Color.WHITE);
    	} else {
    		g2d.setColor(new Color(95, 95, 95));
    	}
    	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	g2d.setFont(font);
    	g2d.drawString(text, (int)textRect.getX(), (int)textRect.getY() + 13);
    }
    
    /**
     * Custom enabling method for the button.
     * 
     * @param b	The button is enabled if b is true, false if not
     */
    public void setEnabled(boolean b) {
    	this.enabled = b;
    }
}
