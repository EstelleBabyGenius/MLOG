package mlog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * Handles generation of maps from mouse coordinates over time.
 * 
 * @author Filip Östermark
 * @version 2013-05-16
 */
public class MapGenerator {

	public final static String MAP_TYPE_DOTMAP = "DOTMAP";
	public final static String MAP_TYPE_LINEMAP = "LINEMAP";

	/**
	 * Standard constructor for the map generator.
	 */
	public MapGenerator() { }

	/**
	 * Generates a map (from mouse coordinates over time) of the specified type.
	 * 
	 * @param resolution	The user's screen resolution
	 * @param pixelMap		A HashMap of mouse coordinates over time
	 * @param elementColor	The color of the map elements
	 * @param mapType		The type of map to be generated
	 * @param filePath			The output picture file
	 */
	public void generateMap(Dimension resolution, HashMap<PixelPoint, Integer> pixelMap, Color elementColor, String mapType, String filePath) {
		if (mapType.equals(MAP_TYPE_DOTMAP)) {
			this.generateDotMap(resolution, pixelMap, elementColor, filePath);
		}
		else if (mapType.equals(MAP_TYPE_LINEMAP)) {
			this.generateLineMap(resolution, pixelMap, elementColor, filePath);
		} else {
			System.err.println("Invalid map type. The map type parameter was invalid and the map could not be generated.");
		}
	}

	/**
	 * Generates a dot map of mouse pointer positions over time.
	 * 
	 * @param resolution	The user's screen resolution
	 * @param pixelMap		A HashMap of mouse pointer positions over time
	 * @param dotColor		The color of the dots
	 * @param filePath		The file path to save the image at
	 */
	public void generateDotMap(Dimension resolution, HashMap<PixelPoint, Integer> pixelMap, Color dotColor, String filePath) {
		int screenWidth = (int)resolution.getWidth();
		int screenHeight = (int)resolution.getHeight();

		BufferedImage pixelTimeMapBuffer = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D pixelTimeMapImage = pixelTimeMapBuffer.createGraphics();
		pixelTimeMapImage.setColor(Color.BLACK);
		pixelTimeMapImage.fillRect(0, 0, screenWidth, screenHeight); // Set background color
		pixelTimeMapImage.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Generate all the dots on the picture
		synchronized(pixelMap) {
			for (PixelPoint pixel : pixelMap.keySet()) {
				for (int i = pixelMap.get(pixel); i > 0; i--) {
					int radius = i;
					int alpha = 255/i;
					pixelTimeMapImage.setColor(new Color(dotColor.getRed(), dotColor.getGreen(), dotColor.getBlue(), alpha));
					pixelTimeMapImage.fillOval((int)(pixel.getX() - radius), (int)(pixel.getY() - radius), 2*radius, 2*radius);
				}
			}
		}

		this.saveImage(filePath, pixelTimeMapBuffer);
	}

	/**
	 * TODO
	 */
	public void generateLineMap(Dimension resolution, HashMap<PixelPoint, Integer> pixelMap, Color lineColor, String filePath) {
		int screenWidth = (int)resolution.getWidth();
		int screenHeight = (int)resolution.getHeight();
		BufferedImage pixelTimeMapBuffer = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D pixelTimeMapImage = pixelTimeMapBuffer.createGraphics();
		pixelTimeMapImage.setColor(Color.BLACK);
		pixelTimeMapImage.fillRect(0, 0, screenWidth, screenHeight); // Set background color
		pixelTimeMapImage.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Generate all the dots on the picture
		synchronized(pixelMap) {
			PixelPoint[] pixelArray = pixelMap.keySet().toArray(new PixelPoint[0]);
			for (int i = 0; i < pixelArray.length; i++) {
				for (int j = i + 1; j < pixelArray.length; j++) {
					double distance = Math.sqrt(Math.pow(pixelArray[i].getX() - pixelArray[j].getX(), 2) + Math.pow(pixelArray[i].getY() - pixelArray[j].getY(), 2));
					int alpha = 255/((int)Math.pow(distance, 2)/10000 + 1);
					if (distance < 800 && distance > 20 && alpha >= 20) {
						pixelTimeMapImage.setColor(new Color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), alpha));
						pixelTimeMapImage.drawLine((int)pixelArray[i].getX(), (int)pixelArray[i].getY(), (int)pixelArray[j].getX(), (int)pixelArray[j].getY());
					}
				}
			}
		}

		this.saveImage(filePath, pixelTimeMapBuffer);
	}

	/**
	 * Draws a single gradient dot.
	 * 
	 * @param pixel		The position of the dot
	 * @param radius	The radius of the dot
	 * @param color		The color of the dot
	 * @param g2d		The Graphics2D object to draw the dot on
	 */
	public static void drawGradientDot(PixelPoint pixel, int radius, Color color, Graphics2D g2d) {
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		for (int i = radius; i > 0; i--) {
			int alpha = 255/i;
			g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
			g2d.fillOval((int)(pixel.getX() - i), (int)(pixel.getY() - i), 2*i, 2*i);
		}
	}

	/**
	 * TODO
	 * @param filePath
	 * @param image
	 */
	private void saveImage(String filePath, BufferedImage bufferedImage) {
		try {
			if (!filePath.endsWith(".png")) {
				filePath += ".png";
			}
			ImageIO.write(bufferedImage, "png", new File(filePath));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("The map could not be saved. Try specifying another path to save to.");
		}
	}
}
