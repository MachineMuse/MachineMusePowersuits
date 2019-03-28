package net.machinemuse.numina.client.gui.scrollable;

import net.machinemuse.numina.client.gui.clickable.ClickableLabel;
import net.machinemuse.numina.math.geometry.MusePoint2D;
import net.machinemuse.numina.math.geometry.MuseRelativeRect;

public class ScrollableLabel extends ScrollableRectangle {
    ClickableLabel label;
    boolean enabled = true;

    public ScrollableLabel(String text, MuseRelativeRect relativeRect) {
        super(relativeRect);
        this.label = new ClickableLabel(text, new MusePoint2D(relativeRect.centerx(), relativeRect.centery()));
    }

    public ScrollableLabel(ClickableLabel label, MuseRelativeRect relativeRect) {
        super(relativeRect);
        this.label = label;
    }

    public ScrollableLabel(ClickableLabel label, double left, double top, double right, double bottom) {
        super(left, top, right, bottom);
        this.label = label;
    }

    public ScrollableLabel(ClickableLabel label, double left, double top, double right, double bottom, boolean growFromMiddle) {
        super(left, top, right, bottom, growFromMiddle);
        this.label = label;
    }

    public ScrollableLabel(ClickableLabel label, MusePoint2D ul, MusePoint2D br) {
        super(ul, br);
        this.label = label;
    }

    public ScrollableLabel setMode(int mode) {
        this.label = this.label.setMode(mode);
        return this;
    }

    public ScrollableLabel setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setText(String text) {
        label.setLabel(text);
    }

    public boolean hitbox(double x, double y) {
        return enabled && label.hitBox(x, y);
    }

    @Override
    public void draw() {
        if (enabled)
            label.draw();
    }
}