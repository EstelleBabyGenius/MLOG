package mlog;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * TODO
 * 
 * @author Filip Östermark
 * @version 2013-05-10
 */
public class PictureBox extends JPanel{
	
	private int x, y, width, height;
	private BufferedImage image;
	
	/**
	 * TODO
	 * 
	 * @param image
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public PictureBox(BufferedImage image, int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.image = image;
	}
	
	/**
	 * TODO
	 */
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, x, y, null);
    }
}
