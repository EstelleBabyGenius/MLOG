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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
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
	private static final long STANDARD_MOUSE_LOGGER_SLEEP_TIME = 1000L;
	private static final String WINDOWS_LOOK_AND_FEEL = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
	private static final String RUNNING_LABEL = "RUNNING";
	private static final String NOT_RUNNING_LABEL = "NOT RUNNING";
	private static JButton generateMapButton;
	private static JLabel runningIndicatorLabel;
	private static JLabel totalRuntimeLabel;

	/**
	 * Creates the main window and runs the program.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MLog();
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
	private void generateMap(HashMap<PixelPoint, Integer> pixelMap) {
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
					int radius = i;
					int alpha = 255/i;
					System.out.println(alpha);
					pixelTimeMapImage.setColor(new Color(255, 0, 0, alpha));
					pixelTimeMapImage.fillOval((int)(pixel.getX() - radius/2), (int)(pixel.getY() - radius/2), radius, radius);
				}
			}
		}
		try {
			ImageIO.write(pixelTimeMapBuffer, "png",new File("C:\\Users\\Filip\\Desktop\\HeatMap.png"));

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
		this.setSize(220, 300);
		this.setVisible(true);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setNativeLookAndFeel();

		JPanel panel = new JPanel();
		panel.setLayout(null);
		getContentPane().add(panel);

		JButton startButton = new JButton("START");
		startButton.setBounds(50, 30, 110, 30);
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				mouseLogger.start();
				if (mouseLogger.isRunning()) {
					runningIndicatorLabel.setText(RUNNING_LABEL);
					generateMapButton.setEnabled(false);
				}
			}
		});
		panel.add(startButton);

		JButton pauseButton = new JButton("PAUSE");
		pauseButton.setBounds(50, 70, 110, 30);
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					mouseLogger.pause();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (!mouseLogger.isRunning()) {
					runningIndicatorLabel.setText(NOT_RUNNING_LABEL);
					generateMapButton.setEnabled(true);
				}
			}
		});
		panel.add(pauseButton);

		generateMapButton = new JButton("GENERATE MAP");
		generateMapButton.setBounds(50, 110, 110, 30);
		generateMapButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				generateMap(mouseLogger.getPixelTimeLog());
			}
		});
		panel.add(generateMapButton);

		JButton printLogButton = new JButton("PRINT");
		printLogButton.setBounds(50, 150, 110, 30);
		printLogButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				mouseLogger.printLog();
			}
		});
		panel.add(printLogButton);

		runningIndicatorLabel = new JLabel(NOT_RUNNING_LABEL);
		runningIndicatorLabel.setBounds(0, 200, this.getWidth(), 30);
		runningIndicatorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(runningIndicatorLabel);

		totalRuntimeLabel = new JLabel("TOTAL RUNTIME: 00:00:00");
		totalRuntimeLabel.setBounds(0, 220, this.getWidth(), 30);
		totalRuntimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(totalRuntimeLabel);
	}
}
