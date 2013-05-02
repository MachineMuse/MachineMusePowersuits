package net.machinemuse.general.geometry;

public class MuseRect {
    MusePoint2D ul;
    MusePoint2D wh;

    public MuseRect(double left, double top, double right, double bottom, boolean growFromMiddle) {
        ul = new MusePoint2D(left, top);
        wh = new MusePoint2D(right - left, bottom - top);
        if (growFromMiddle) {
            MusePoint2D center = ul.plus(wh.times(0.5));
            this.ul = new FlyFromPointToPoint2D(center, ul, 200);
            this.wh = new FlyFromPointToPoint2D(new MusePoint2D(0, 0), wh, 200);
        }
    }

    public MuseRect(double left, double top, double right, double bottom) {
        this(left, top, right, bottom, false);
    }

    public MuseRect(MusePoint2D ul, MusePoint2D br) {
        this.ul = ul;
        this.wh = br.minus(ul);
    }

    public double left() {
        return ul.x();
    }

    public double right() {
        return ul.x() + wh.x();
    }

    public double top() {
        return ul.y();
    }

    public double bottom() {
        return ul.y() + wh.y();
    }

    public double width() {
        return wh.x();
    }

    public double height() {
        return wh.y();
    }

    public MuseRect setLeft(double value) {
        ul.x = value;
        return this;
    }

    public MuseRect setRight(double value) {
        wh.x = value - ul.x();
        return this;
    }

    public MuseRect setTop(double value) {
        ul.y = value;
        return this;
    }

    public MuseRect setBottom(double value) {
        wh.y = value - ul.y();
        return this;
    }

    public MuseRect setWidth(double value) {
        wh.x = value;
        return this;
    }

    public MuseRect setHeight(double value) {
        wh.y = value;
        return this;
    }

    public boolean equals(MuseRect other) {
        return ul.equals(other.ul) && wh.equals(other.wh);
    }

    public boolean containsPoint(double x, double y) {
        return x > left() && x < right() && y > top() && y < bottom();
    }

    public double centerx() {
        return (left() + right()) / 2.0;
    }
    public double centery() {
        return (top() + bottom()) / 2.0;
    }
}
