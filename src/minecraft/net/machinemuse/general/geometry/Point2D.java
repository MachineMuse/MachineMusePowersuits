package net.machinemuse.general.geometry;

/**
 * Base class for Points. The main reason for this is to have a
 * pass-by-reference coordinate with getter/setter functions so that points with
 * more elaborate behaviour can be implemented - such as for open/close
 * animations.
 * 
 * @author MachineMuse
 * 
 */
public class Point2D {
	protected double x;
	protected double y;

	public Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Point2D(Point2D p) {
		this(p.x, p.y);
	}

	public double x() {
		return x;
	}

	public double y() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public Point2D plus(Point2D b) {
		return new Point2D(x + b.x, y + b.y);
	}

	public Point2D minus(Point2D b) {
		return new Point2D(x - b.x, y - b.y);
	}

	public Point2D times(double scalefactor) {
		return new Point2D(x * scalefactor, y * scalefactor);
	}
}
