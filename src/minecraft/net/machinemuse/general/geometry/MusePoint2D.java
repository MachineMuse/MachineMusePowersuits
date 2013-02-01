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
public class MusePoint2D {
	protected double x;
	protected double y;

	public MusePoint2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public MusePoint2D(MusePoint2D p) {
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

	public MusePoint2D plus(MusePoint2D b) {
		return new MusePoint2D(x() + b.x(), y() + b.y());
	}

	public MusePoint2D minus(MusePoint2D b) {
		return new MusePoint2D(x() - b.x(), y() - b.y());
	}

	public MusePoint2D times(double scalefactor) {
		return new MusePoint2D(x() * scalefactor, y() * scalefactor);
	}

	public boolean equals(MusePoint2D other) {
		return this.x() == other.x() && this.y() == other.y();
	}

	public double distance() {
		return Math.sqrt(x() * x() + y() * y());
	}

	public double distanceTo(MusePoint2D position) {
		return Math.sqrt(distanceSq(position));
	}

	public double distanceSq(MusePoint2D position) {
		double xdist = position.x() - this.x();
		double ydist = position.y() - this.y();
		return xdist * xdist + ydist * ydist;
	}

	public MusePoint2D normalize() {
		double distance = distance();
		return new MusePoint2D(x() / distance, y() / distance);
	}

	public MusePoint2D midpoint(MusePoint2D target) {
		return new MusePoint2D((this.x() + target.x()) / 2, (this.y() + target.y()) / 2);
	}

	public MusePoint2D copy() {
		return new MusePoint2D(this.x(), this.y());
	}
}
