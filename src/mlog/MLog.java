package mlog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * A small Java application using the Swing library, for drawing heat maps from mouse positions over time.
 * 
 * @author Filip Östermark
 * @version 2013-05-10
 */
public class MLog extends JFrame{

	private static MouseLogger mouseLogger;
	private static final long STANDARD_MOUSE_LOGGER_SLEEP_TIME = 500L;
	private static final String WINDOWS_LOOK_AND_FEEL = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

	/**
	 * Creates the main window and runs the program.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MLog window = new MLog();
				window.setVisible(true);
				window.setResizable(true);
			}
		});
		mouseLogger = new MouseLogger(STANDARD_MOUSE_LOGGER_SLEEP_TIME);
		mouseLogger.run();
	}

	/**
	 * Creates a new MLog object and initializes its GUI.
	 */
	public MLog() {
		initUI();
	}

	/**
	 * Draws a heat map of mouse pointer positions over time.
	 * 
	 * @param pixelMap	A HashMap of mouse pointer positions over time
	 */
	private void drawMap(HashMap<PixelPoint, Integer> pixelMap) {
		Dimension screenResolution = this.getScreenResolution();
		int screenWidth = (int)screenResolution.getWidth();
		int screenHeight = (int)screenResolution.getHeight();

		BufferedImage pixelTimeMapBuffer = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D pixelTimeMapImage = pixelTimeMapBuffer.createGraphics();

		pixelTimeMapImage.setBackground(Color.BLACK);
		pixelTimeMapImage.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		synchronized(pixelMap) {
			for (PixelPoint pixel : pixelMap.keySet()) {
				for (int i = pixelMap.get(pixel); i > 0; i--) {
					int radius = i*5;
					int alpha = 255/i;
					System.out.println(alpha);
					pixelTimeMapImage.setColor(new Color(255, 0, 0, alpha));
					pixelTimeMapImage.fillOval((int)(pixel.getX() - radius/2), (int)(pixel.getY() - radius/2), radius, radius);
				}
			}
		}
		try {
			ImageIO.write(pixelTimeMapBuffer, "png",new File("C:\\Users\\Filip\\Desktop\\out2.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return	The screen resolution.
	 */
	private Dimension getScreenResolution() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

	/**
	 * Sets the look and feel of the application run on a Windows system.
	 */
	private void setNativeLookAndFeel() {
		try {
			if (UIManager.getSystemLookAndFeelClassName().equals(WINDOWS_LOOK_AND_FEEL)) {
				// Set cross-platform Java L&F (also called "Metal")
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
		} catch (Exception e) { 
			// TODO
		}

	}

	/**
	 * Initializes the GUI.
	 */
	private void initUI() {
		this.setTitle("MLOG");
		this.setSize(800, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setNativeLookAndFeel();

		JPanel panel = new JPanel();
		panel.setLayout(null);
		getContentPane().add(panel);

		JButton pauseButton = new JButton("PAUSE");
		pauseButton.setBounds(50, 60, 80, 30);
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					mouseLogger.pause();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		panel.add(pauseButton);

		JButton startButton = new JButton("START");
		startButton.setBounds(200, 60, 80, 30);
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				mouseLogger.start();
			}
		});
		panel.add(startButton);

		JButton printLogButton = new JButton("PRINT");
		printLogButton.setBounds(400, 60, 80, 30);
		printLogButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				mouseLogger.printLog();
			}
		});
		panel.add(printLogButton);

		JButton drawButton = new JButton("DRAW");
		drawButton.setBounds(200, 150, 80, 30);
		drawButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				drawMap(mouseLogger.getPixelTimeLog());
			}
		});
		panel.add(drawButton);
	}
}
