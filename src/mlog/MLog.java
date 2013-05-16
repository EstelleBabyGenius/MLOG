package mlog;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * A small Java application using the Swing library, for drawing heat maps from mouse positions over time.
 * 
 * TODO: The timer needs some fixing.
 * 
 * @author Filip Östermark
 * @version 2013-05-16
 */
public class MLog extends JFrame{

	private static final long serialVersionUID = 7865377280283371025L;
	private static volatile int secondsRun, minutesRun, hoursRun;
	private static MouseLogger mouseLogger;
	private static MapGenerator mapGenerator;
	private static Color mapColor;
	private static Timer timer;
	private static HashMap<Integer, String> mapTypeIndices;
	private static final int INITIAL_WINDOW_WIDTH = 220;
	private static final int INITIAL_WINDOW_HEIGHT = 300;
	private static final int EXPANDED_WINDOW_WIDTH = INITIAL_WINDOW_WIDTH + 260;
	private static final long STANDARD_MOUSE_LOGGER_SLEEP_TIME = 1000L;
	private static final String WINDOWS_LOOK_AND_FEEL = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
	private static final String RUNNING_LABEL = "RUNNING";
	private static final String NOT_RUNNING_LABEL = "NOT RUNNING";
	private static final Color STANDARD_MAP_COLOR = Color.GREEN;
	private static JButton startButton;
	private static JButton pauseButton;
	private static JButton generateMapButton;
	private static JButton doneButton;
	private static JButton backButton;
	private static JComboBox mapTypeDropDown;
	private static ColorPickerPanel colorPickerPanel;
	private static JLabel mapTypeLabel;
	private static JLabel runningIndicatorLabel;
	private static JLabel totalRuntimeLabel;
	private static JLabel mapElementColorLabel;

	/**
	 * Creates the main window and runs the program.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MLog();
			}
		});
		ExecutorService executor = Executors.newCachedThreadPool();
		mapGenerator = new MapGenerator();
		mouseLogger = new MouseLogger(STANDARD_MOUSE_LOGGER_SLEEP_TIME);
		executor.execute(mouseLogger);
	}

	/**
	 * Creates a new MLog object and initializes its GUI.
	 */
	public MLog() {
		mapTypeIndices = new HashMap<Integer, String>();
		mapTypeIndices.put(0, MapGenerator.MAP_TYPE_DOTMAP);
		mapColor = STANDARD_MAP_COLOR;

		initUI();
		initTimer();
	}

	/**
	 * @return	The screen resolution.
	 */
	private Dimension getScreenResolution() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

	/**
	 * Sets the look and feel of the application if run on a Windows system.
	 */
	private void setNativeLookAndFeel() {
		try {
			if (UIManager.getSystemLookAndFeelClassName().equals(WINDOWS_LOOK_AND_FEEL)) {
				// Set Windows look and feel
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
		} catch (Exception e) { 
			e.printStackTrace();
			System.err.println("The look and feel could not be set; leaving it at the default setting.");
		}
	}

	/**
	 * Shows or hides the settings for map generation.
	 * 
	 * @param panel	The JPanel to expand or shrink
	 * @param show	Show if true, hide if not
	 */
	private void setMapSettingsVisible(boolean show) {
		if (show) {
			this.setSize(EXPANDED_WINDOW_WIDTH, this.getHeight());
			startButton.setEnabled(false);
		} else {
			this.setSize(INITIAL_WINDOW_WIDTH, INITIAL_WINDOW_HEIGHT);
			startButton.setEnabled(true);
		}

		/* Some old code for animated expansion of the window.
		 * It is not currently used because it causes the settings
		 * part of the window is fully expanded, which arguably does
		 * not look good. This code (if updated) will be used if this
		 * problem is fixed.
		 * 
		 * for (int i = 0; i < 7; i++) {
			if (show) {
				this.setSize((int)(this.getWidth() + Math.pow(i, 2) + 2), this.getHeight());
			} else {
				this.setSize((int)(this.getWidth() - Math.pow(i, 2) - 2), this.getHeight());
			}
			try {
				Thread.sleep(20);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (int i = 7; i > 0; i--) {
			if (show) {
				this.setSize((int)(this.getWidth() + Math.pow(i, 2) + 2), this.getHeight());
			} else {
				this.setSize((int)(this.getWidth() - Math.pow(i, 2) - 2), this.getHeight());
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		 */


	}

	/**
	 * Initializes the timer counting the total time the logger has run.
	 */
	private void initTimer() {
		ActionListener timerTask = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				secondsRun++;

				if (secondsRun == 60) {
					secondsRun = 0;
					minutesRun++;
				}
				if (minutesRun == 60) {
					minutesRun = 0;
					hoursRun++;
				}
				String secondsRunString = Integer.toString(secondsRun);
				String minutesRunString = Integer.toString(minutesRun);
				String hoursRunString = Integer.toString(hoursRun);
				if (secondsRun < 10) {
					secondsRunString = "0" + secondsRun;
				}
				if (minutesRun < 10) {
					minutesRunString = "0" + minutesRun;
				}
				if (hoursRun < 10) {
					hoursRunString = "0" + hoursRun;
				}
				totalRuntimeLabel.setText("TOTAL RUNTIME: " + hoursRunString + ":" + minutesRunString + ":" + secondsRunString);
			}
		};
		timer = new Timer(1000, timerTask);
	}

	/**
	 * Initializes the GUI.
	 */
	private void initUI() {

		/******************************
		 *### MAIN MENU COMPONENTS ###*
		 ******************************/

		this.setTitle("MLOG");
		this.setSize(INITIAL_WINDOW_WIDTH, INITIAL_WINDOW_HEIGHT);
		this.setVisible(true);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setNativeLookAndFeel();

		// Create a JPanel with an overridden paintComponent method.
		// This is so that a separator can be drawn between the standard panel
		// and the map generator settings panel.
		final JPanel panel = new JPanel() {
			private static final long serialVersionUID = 2266868192394712142L;
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);

				// Draw separator on the right edge of the program window
				g.setColor(new Color(190, 190, 190));
				g.drawLine(INITIAL_WINDOW_WIDTH, 30, INITIAL_WINDOW_WIDTH, INITIAL_WINDOW_HEIGHT - 60);
			}
		};
		panel.setLayout(null);
		getContentPane().add(panel);

		// The button that starts the logging of mouse coordinates
		startButton = new JButton("START");
		startButton.setBounds(50, 30, 110, 30);
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				mouseLogger.start();
				if (mouseLogger.isRunning()) {
					runningIndicatorLabel.setText(RUNNING_LABEL);
					runningIndicatorLabel.setForeground(new Color(0, 180, 0));
					generateMapButton.setEnabled(false);
					startButton.setEnabled(false);
					pauseButton.setEnabled(true);
					timer.start();
				}
			}
		});
		panel.add(startButton);

		// The button that pauses the logging of mouse coordinates
		pauseButton = new JButton("PAUSE");
		pauseButton.setBounds(50, 70, 110, 30);
		pauseButton.setEnabled(false);
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
					runningIndicatorLabel.setForeground(Color.RED);
					generateMapButton.setEnabled(true);
					startButton.setEnabled(true);
					pauseButton.setEnabled(false);
					timer.stop();
				}
			}
		});
		panel.add(pauseButton);

		// The button that shows the menu for generating maps
		generateMapButton = new JButton("GENERATE MAP");
		generateMapButton.setBounds(50, 110, 110, 30);
		generateMapButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				generateMapButton.setEnabled(false);
				setMapSettingsVisible(true);
			}
		});
		panel.add(generateMapButton);

		// A temporary button for debugging; prints all the logged coordinates on the standard output stream
		JButton printLogButton = new JButton("PRINT");
		printLogButton.setBounds(50, 150, 110, 30);
		printLogButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				mouseLogger.printLog();
			}
		});
		panel.add(printLogButton);

		// A label to indicate whether or the mouse is being logged
		runningIndicatorLabel = new JLabel(NOT_RUNNING_LABEL);
		runningIndicatorLabel.setBounds(0, 200, this.getWidth(), 30);
		runningIndicatorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		runningIndicatorLabel.setForeground(Color.RED);
		panel.add(runningIndicatorLabel);

		// A label that shows the total time that the user has been logging the mouse
		totalRuntimeLabel = new JLabel("TOTAL RUNTIME: 00:00:00");
		totalRuntimeLabel.setBounds(0, 220, this.getWidth(), 30);
		totalRuntimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(totalRuntimeLabel);


		/****************************************
		 *### MAP GENERATION MENU COMPONENTS ###*
		 ****************************************/

		// A label that tells the user how to choose what type of map to generate
		mapTypeLabel = new JLabel("MAP TYPE:");
		mapTypeLabel.setBounds(240, 5, 215, 30);
		panel.add(mapTypeLabel);

		// The drop down menu to pick which type of map to generate
		mapTypeDropDown = new JComboBox();
		for (int i = 0; i < mapTypeIndices.size(); i++) {
			mapTypeDropDown.addItem(mapTypeIndices.get(i));
		}
		mapTypeDropDown.setBounds(240, 30, 215, 30);
		panel.add(mapTypeDropDown);

		// TODO
		mapElementColorLabel = new JLabel("MAP ELEMENT COLOR:");
		mapElementColorLabel.setBounds(240, 90, 150, 50);
		panel.add(mapElementColorLabel);

		// Create a color chooser panel component
		colorPickerPanel = new ColorPickerPanel(Color.BLACK, mapColor);
		colorPickerPanel.setBounds(405, 90, 50, 50);
		colorPickerPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		colorPickerPanel.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				colorPickerPanel.showDialog("Map element color");
			}
		});
		panel.add(colorPickerPanel);

		// The button that lets the user generate and save maps
		doneButton = new JButton("SAVE");
		doneButton.setBounds(300, 225, 110, 30);
		doneButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG", "png");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showSaveDialog(MLog.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					switch (mapTypeDropDown.getSelectedIndex()) {
					case 0:
						mapGenerator.generateMap(getScreenResolution(), mouseLogger.getPixelTimeLog(), colorPickerPanel.getForegroundColor(), mapTypeIndices.get(0), chooser.getSelectedFile().getAbsolutePath());
						break;
					default:
						break;
					}
				}
			}
		});
		panel.add(doneButton);

		// The button to hide the map generation menu
		backButton = new JButton("<");
		backButton.setBounds(240, 225, 50, 30);
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setMapSettingsVisible(false);
				generateMapButton.setEnabled(true);
			}
		});
		panel.add(backButton);
	}
}
