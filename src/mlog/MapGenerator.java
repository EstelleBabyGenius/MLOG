package mlog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 * TODO
 * @author Filip Östermark
 * @version 2013-05-14
 */
public class MapGenerator {
	
	public final static String MAP_TYPE_DOTMAP = "DOTMAP";
	
	/**
	 * TODO
	 */
	public MapGenerator() { }
	
	public void generateMap(Dimension resolution, HashMap<PixelPoint, Integer> pixelMap, File file, String mapType) {
		if (mapType.equals(MAP_TYPE_DOTMAP)) {
			this.generateDotMap(resolution, pixelMap, file);
		} else {
			System.out.println("INVALID MAP TYPE!");
		}
	}
	
	/**
	 * Generates a dot map of mouse pointer positions over time.
	 * 
	 * @param pixelMap	A HashMap of mouse pointer positions over time
	 */
	public void generateDotMap(Dimension resolution, HashMap<PixelPoint, Integer> pixelMap, File file) {
		int screenWidth = (int)resolution.getWidth();
		int screenHeight = (int)resolution.getHeight();

		BufferedImage pixelTimeMapBuffer = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D pixelTimeMapImage = pixelTimeMapBuffer.createGraphics();

		pixelTimeMapImage.setBackground(Color.BLACK);
		pixelTimeMapImage.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		synchronized(pixelMap) {
			for (PixelPoint pixel : pixelMap.keySet()) {
				for (int i = pixelMap.get(pixel); i > 0; i--) {
					int radius = i;
					int alpha = 255/i;
					pixelTimeMapImage.setColor(new Color(100, 255, 0, alpha));
					pixelTimeMapImage.fillOval((int)(pixel.getX() - radius), (int)(pixel.getY() - radius), 2*radius, 2*radius);
				}
			}
		}
		try {
			ImageIO.write(pixelTimeMapBuffer, "png", file);

		} catch (IOException e) {
			// TODO
			e.printStackTrace();
		}
	}
}
