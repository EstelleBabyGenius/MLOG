package mlog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

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
	private static final long STANDARD_MOUSE_LOGGER_SLEEP_TIME = 1000L;
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
	public void drawMap(HashMap<PixelPoint, Integer> pixelMap) {
		// TODO: Add implementation
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
				//drawMap(mouseLogger.getPixelTimeLog());
			}
		});
		panel.add(drawButton);
	}
}
