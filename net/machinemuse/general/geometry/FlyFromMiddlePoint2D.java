package net.machinemuse.general.geometry;

public class FlyFromMiddlePoint2D extends Point2D {
	protected long spawnTime;
	protected float timeTo;

	public FlyFromMiddlePoint2D(float x, float y, float timeTo) {
		super(x, y);
		spawnTime = System.currentTimeMillis();
		this.timeTo = timeTo;
	}

	public FlyFromMiddlePoint2D(Point2D target, float timeTo) {
		this(target.x(), target.y(), timeTo);
	}

	@Override
	public float x() {
		return doRatio(x);
	}

	@Override
	public float y() {
		return doRatio(y);
	}

	public float doRatio(float val) {
		long elapsed = System.currentTimeMillis() - spawnTime;
		float ratio = elapsed / timeTo;
		if (ratio > 1.0F) {
			return val;
		} else {
			return val * ratio;
		}
	}
}
