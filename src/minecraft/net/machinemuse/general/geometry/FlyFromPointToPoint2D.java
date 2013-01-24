package net.machinemuse.general.geometry;

public class FlyFromPointToPoint2D extends MusePoint2D {
	protected MusePoint2D prev;
	protected long spawnTime;
	protected double timeTo;

	public FlyFromPointToPoint2D(double x, double y, double x2, double y2,
			double timeTo) {
		super(x2, y2);
		prev = new MusePoint2D(x, y);
		spawnTime = System.currentTimeMillis();
		this.timeTo = timeTo;
	}

	public FlyFromPointToPoint2D(MusePoint2D prev, MusePoint2D target, double timeTo) {
		this(prev.x(), prev.y(), target.x(), target.y(), timeTo);
	}

	@Override
	public double x() {
		return doRatio(prev.x, x);
	}

	@Override
	public double y() {
		return doRatio(prev.y, y);
	}

	public double doRatio(double val1, double val2) {
		long elapsed = System.currentTimeMillis() - spawnTime;
		double ratio = elapsed / timeTo;
		if (ratio > 1.0F) {
			return val2;
		} else {
			return val2 * ratio + val1 * (1 - ratio);
		}
	}
}
