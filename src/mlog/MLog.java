package mlog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

/**
 * A small Java application using the Swing library, for drawing heat maps from mouse positions over time.
 * 
 * TODO: The timer needs fixing.
 * 
 * @author Filip Östermark
 * @version 2013-05-10
 */
public class MLog extends JFrame{

	private static volatile int secondsRun, minutesRun, hoursRun;
	private static MouseLogger mouseLogger;
	private static MapGenerator mapGenerator;
	private static Timer timer;
	private static final long STANDARD_MOUSE_LOGGER_SLEEP_TIME = 1000L;
	private static final String WINDOWS_LOOK_AND_FEEL = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
	private static final String RUNNING_LABEL = "RUNNING";
	private static final String NOT_RUNNING_LABEL = "NOT RUNNING";
	private static JButton startButton;
	private static JButton pauseButton;
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
		ExecutorService executor = Executors.newCachedThreadPool();
		mapGenerator = new MapGenerator();
		mouseLogger = new MouseLogger(STANDARD_MOUSE_LOGGER_SLEEP_TIME);
		executor.execute(mouseLogger);
	}

	/**
	 * Creates a new MLog object and initializes its GUI.
	 */
	public MLog() {
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
				// Set cross-platform Java L&F (also called "Metal")
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
		} catch (Exception e) { 
			// TODO
		}
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

		generateMapButton = new JButton("GENERATE MAP");
		generateMapButton.setBounds(50, 110, 110, 30);
		generateMapButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				mapGenerator.generateDotMap(getScreenResolution(), mouseLogger.getPixelTimeLog(), new File("C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\DotMap.png"));
				//mapGenerator.generateDotMap(getScreenResolution(), mouseLogger.getPixelTimeLog(), new File("DotMap.png"));
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
		runningIndicatorLabel.setForeground(Color.RED);
		panel.add(runningIndicatorLabel);

		totalRuntimeLabel = new JLabel("TOTAL RUNTIME: 00:00:00");
		totalRuntimeLabel.setBounds(0, 220, this.getWidth(), 30);
		totalRuntimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(totalRuntimeLabel);
	}
}
