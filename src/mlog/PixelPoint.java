package mlog;

import java.awt.Point;

/**
 * Represents a point representing a pixel on the screen.
 * 
 * @author Filip Östermark
 * @version 2013-05-11
 */
public class PixelPoint extends Point {

	private static final long serialVersionUID = 9129011606536238109L;

	/**
	 * Creates a new PixelPoint with the given coordinates.
	 * 
	 * @param x	The x coordinate of the PixelPoint
	 * @param y	The y coordinate of the PixelPoint
	 */
	public PixelPoint(int x, int y) {
		super(x, y);
	}
	
	/**
	 * Creates a new PixelPoint at the given Point.
	 * 
	 * @param point	The Point that sets the location of the PixelPoint
	 */
	public PixelPoint(Point point) {
		this.setLocation(point);
	}
	
	/**
	 * Two PixelPoints are considered equal if their x and y coordinates respectively are equal.
	 * 
	 * @return	True if the objects are equal, false if not.
	 */
	@Override
	public boolean equals(Object obj) {
		// Check whether or not two PixelPoints are being compared
		if (obj.getClass().getName().equals(this.getClass().getName())) {
			PixelPoint otherPixelPoint = (PixelPoint) obj;
			if (this.getX() == otherPixelPoint.getX() && this.getY() == otherPixelPoint.getY()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return	A String representation of the PixelPoint.
	 */
	@Override
	public String toString() {
		return "{ " + this.getX() + " ; " + this.getY() + " }";
	}
}
