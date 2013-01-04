package net.machinemuse.general.geometry;

public class FlyFromPointToPoint2D extends Point2D {
	protected Point2D prev;
	protected long spawnTime;
	protected float timeTo;

	public FlyFromPointToPoint2D(float x, float y, float x2, float y2,
			float timeTo) {
		super(x2, y2);
		prev = new Point2D(x, y);
		spawnTime = System.currentTimeMillis();
		this.timeTo = timeTo;
	}

	public FlyFromPointToPoint2D(Point2D prev, Point2D target, float timeTo) {
		this(prev.x(), prev.y(), target.x(), target.y(), timeTo);
	}

	@Override
	public float x() {
		return doRatio(prev.x, x);
	}

	@Override
	public float y() {
		return doRatio(prev.y, y);
	}

	public float doRatio(float val1, float val2) {
		long elapsed = System.currentTimeMillis() - spawnTime;
		float ratio = elapsed / timeTo;
		if (ratio > 1.0F) {
			return val2;
		} else {
			return val2 * ratio + val1 * (1 - ratio);
		}
	}
}
