package net.machinemuse.numina.math.geometry;

public class SpiralPointToPoint2D extends MusePoint2D {
    protected final MusePoint2D center;
    protected final long spawnTime;
    protected final double timeTo;
    protected final boolean outwards;
    protected final double spiral = Math.PI;
    protected double radius;
    protected double rotation;

    public SpiralPointToPoint2D(double x, double y,
                                double x2, double y2,
                                double timeTo, boolean outwards) {
        super(x2, y2);
        center = new MusePoint2D(x, y);
        this.radius = this.distanceTo(center);
        this.rotation = Math.atan2(y2 - y, x2 - x);
        spawnTime = System.currentTimeMillis();
        this.timeTo = timeTo;
        this.outwards = outwards;
    }

    public SpiralPointToPoint2D(double x, double y,
                                double x2, double y2,
                                double timeTo) {
        this(x, y, x2, y2, timeTo, true);
    }

    public SpiralPointToPoint2D(MusePoint2D center,
                                MusePoint2D target,
                                double timeTo, boolean outwards) {
        this(center.getX(), center.getY(), target.getX(), target.getY(), timeTo, outwards);
    }

    public SpiralPointToPoint2D(MusePoint2D center,
                                MusePoint2D target,
                                double timeTo) {
        this(center, target, timeTo, true);
    }

    public SpiralPointToPoint2D(MusePoint2D center,
                                double radius, double rotation,
                                double timeTo, boolean outward) {
        this(center.getX(), center.getY(), radius * Math.cos(rotation), radius * Math.sin(rotation), timeTo, outward);
        this.radius = radius;
        this.rotation = rotation;
    }

    public SpiralPointToPoint2D(MusePoint2D center,
                                double radius, double rotation,
                                double timeTo) {
        this(center, radius, rotation, timeTo, true);
    }

    private double getRatio() {
        long elapsed = System.currentTimeMillis() - spawnTime;
        double ratio = elapsed / timeTo;
        if (ratio > 1.0F) {
            ratio = 1.0;
        }
        if (outwards) {
            return ratio;
        } else {
            return 1 - ratio;
        }
    }

    private double getTheta() {
        return rotation + (spiral * (1 - getRatio()));
    }

    @Override
    public double getX() {
        //getX = r × cos(θ)
        return center.getX() + (radius * getRatio()) * Math.cos(getTheta());
    }

    @Override
    public double getY() {
        //getY = r × sin(θ)
        return center.getY() + (radius * getRatio()) * Math.sin(getTheta());
    }
}

//    return ((3 * Math.PI / 2) - ((2 * Math.PI * modeNum) / totalModes) + (2 * Math.PI * ((1 - (frame / numFrames))));
//                   ^--------rotation----------------^