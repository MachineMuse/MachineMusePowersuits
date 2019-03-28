package net.machinemuse.numina.client.gui.scrollable;

import net.machinemuse.numina.math.geometry.MusePoint2D;
import net.machinemuse.numina.math.geometry.MuseRect;
import net.machinemuse.numina.math.geometry.MuseRelativeRect;

public class ScrollableRectangle extends MuseRelativeRect {
    public ScrollableRectangle(MuseRelativeRect relativeRect) {
        super(relativeRect.left(), relativeRect.top(), relativeRect.right(), relativeRect.bottom());
    }

    public ScrollableRectangle(double left, double top, double right, double bottom) {
        super(left, top, right, bottom);
    }

    public ScrollableRectangle(double left, double top, double right, double bottom, boolean growFromMiddle) {
        super(left, top, right, bottom, growFromMiddle);
    }

    public ScrollableRectangle(MusePoint2D ul, MusePoint2D br) {
        super(ul, br);
    }

    public void draw() {
    }

    public MuseRect getBorder() {
        return this;
    }
}
