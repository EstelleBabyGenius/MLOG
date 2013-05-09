package mlog;

import java.awt.Point;

public class PixelPoint extends Point {
	
	public PixelPoint(int x, int y) {
		this.setLocation(x, y);
	}
	
	public PixelPoint(Point point) {
		this.setLocation(point);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj.getClass().getName().equals(this.getClass().getName())) {
			PixelPoint otherPixelPoint = (PixelPoint) obj;
			if (this.getX() == otherPixelPoint.getX() && this.getY() == otherPixelPoint.getY()) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "{ " + this.getX() + " ; " + this.getY() + " }";
	}
}
