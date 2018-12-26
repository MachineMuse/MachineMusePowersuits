package net.machinemuse.numina.gui.scrollable;

import net.machinemuse.numina.utils.math.geometry.MusePoint2D;
import net.machinemuse.numina.utils.math.geometry.MuseRelativeRect;
import net.machinemuse.numina.gui.clickable.ClickableSlider;

public class ScrollableSlider extends ScrollableRectangle {
    ClickableSlider slider;

    public ScrollableSlider(ClickableSlider slider, MuseRelativeRect relativeRect) {
        super(relativeRect);
        this.slider = slider;
    }

    public ScrollableSlider(ClickableSlider slider, double left, double top, double right, double bottom) {
        super(left, top, right, bottom);
        this.slider = slider;
    }

    public ScrollableSlider(ClickableSlider slider, double left, double top, double right, double bottom, boolean growFromMiddle) {
        super(left, top, right, bottom, growFromMiddle);
        this.slider = slider;
    }

    public ScrollableSlider(ClickableSlider slider, MusePoint2D ul, MusePoint2D br) {
        super(ul, br);
        this.slider = slider;
    }

    public double getValue() {
        return slider.getValue();
    }

    public void setValue(double value) {
        slider.setValue(value);
    }

    public ClickableSlider getSlider() {
        return slider;
    }

    public boolean hitBox(double x, double y) {
        return slider.hitBox(x, y);
    }

    @Override
    public void draw() {
        slider.draw();
    }
}