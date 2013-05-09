package mlog;

import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

/**
 * TODO
 * 
 * @author Filip Östermark
 * @version 2013-05-10
 */
public class MouseLogger implements Runnable {

	private PointerInfo pointer;
	private HashMap<PixelPoint, Integer> pixelTimeLog;
	private volatile boolean running = true;

	private final long SLEEP_TIME_MILLIS = 1000L;
	private final boolean DEBUG = true;

	@Override
	public void run() {
		while (true) {
			while (!running) {
				Thread.yield();
			}
			this.pointer = MouseInfo.getPointerInfo();
			PixelPoint currentPointerLocation = new PixelPoint(this.pointer.getLocation());
			this.updatePixelTime(currentPointerLocation);
			
			if (DEBUG) {
				int mouseX = (int) currentPointerLocation.getX();
				int mouseY = (int) currentPointerLocation.getY();
				System.out.println("{ " + mouseX + " ; " + mouseY + " }");
			}
			
			try {
				Thread.sleep(this.SLEEP_TIME_MILLIS);
			} catch (InterruptedException ie) {
				// TODO
			}
		}
	}

	/**
	 * TODO
	 */
	public MouseLogger() {
		this.running = false;
		this.pixelTimeLog = new HashMap<PixelPoint, Integer>();
	}

	/**
	 * TODO
	 */
	public void start() {
		this.running = true;
	}

	/**
	 * TODO
	 * @throws InterruptedException
	 */
	public void pause() throws InterruptedException
	{
		this.running = false;
	}
	
	/**
	 * TODO
	 */
	public void printLog() {
		if (DEBUG) {
			System.out.println("---MOUSE LOG---");
		}
		Set<Entry<PixelPoint, Integer>> loggedPositionListKeySet = this.pixelTimeLog.entrySet();
		Iterator<Entry<PixelPoint, Integer>> it = loggedPositionListKeySet.iterator();
		while (it.hasNext()) {
			Entry<PixelPoint, Integer> entry = it.next();
			System.out.println("PIXEL: " + entry.getKey() + " , TIME: " + entry.getValue());
		}
		if (DEBUG) {
			System.out.println("---------------");
		}
	}
	
	/**
	 * TODO
	 * @return
	 */
	public HashMap<PixelPoint, Integer> getPixelTimeLog() {
		return this.pixelTimeLog;
	}
	
	/**
	 * TODO
	 */
	private void updatePixelTime(PixelPoint pixel) {
		Integer currentPixelTime;
		if ((currentPixelTime = pixelTimeLog.get(pixel)) != null) {
			this.pixelTimeLog.put(pixel, currentPixelTime + 1);
		} else {
			this.pixelTimeLog.put(pixel, 1);
		}
	}
}
