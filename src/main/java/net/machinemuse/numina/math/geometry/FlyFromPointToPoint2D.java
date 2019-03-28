package net.machinemuse.numina.math.geometry;

public class FlyFromPointToPoint2D extends MusePoint2D {
    protected final MusePoint2D prev;
    protected final long spawnTime;
    protected final double timeTo;

    public FlyFromPointToPoint2D(double x, double y, double x2, double y2,
                                 double timeTo) {
        super(x2, y2);
        prev = new MusePoint2D(x, y);
        spawnTime = System.currentTimeMillis();
        this.timeTo = timeTo;
    }

    public FlyFromPointToPoint2D(MusePoint2D prev, MusePoint2D target, double timeTo) {
        this(prev.getX(), prev.getY(), target.getX(), target.getY(), timeTo);
    }

    @Override
    public double getX() {
        return doRatio(prev.x, x);
    }

    @Override
    public double getY() {
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