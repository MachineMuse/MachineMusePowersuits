package net.machinemuse.numina.math.geometry;

public class MuseRelativeRect extends MuseRect {
    protected MuseRect belowme;
    protected MuseRect aboveme;
    protected MuseRect leftofme;
    protected MuseRect rightofme;

    public MuseRelativeRect(double left, double top, double right, double bottom) {
        super(left, top, right, bottom);
    }

    public MuseRelativeRect(double left, double top, double right, double bottom, boolean growFromMiddle) {
        super(left, top, right, bottom, growFromMiddle);
    }

    public MuseRelativeRect(MusePoint2D ul, MusePoint2D br) {
        super(ul, br);
    }

    public double left() {
        if (rightofme != null) {
            return rightofme.right();
        }
        return ul.getX();
    }

    public double right() {
        if (leftofme != null) {
            return leftofme.left();
        }
        return left() + wh.getX();
    }

    public double top() {
        if (belowme != null) {
            return belowme.bottom();
        }
        return ul.getY();
    }

    public double bottom() {
        if (aboveme != null) {
            return aboveme.top();
        }
        return top() + wh.getY();
    }

    public MuseRelativeRect setBelow(MuseRect belowme) {
        this.belowme = belowme;
        return this;
    }

    public MuseRelativeRect setAbove(MuseRect aboveme) {
        this.aboveme = aboveme;
        return this;
    }

    public MuseRelativeRect setLeftOf(MuseRect leftofme) {
        this.leftofme = leftofme;
        return this;
    }

    public MuseRelativeRect setRightOf(MuseRect rightofme) {
        this.rightofme = rightofme;
        return this;
    }
}