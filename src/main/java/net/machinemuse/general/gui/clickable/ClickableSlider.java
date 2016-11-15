package net.machinemuse.general.gui.clickable;

import net.machinemuse.numina.general.MuseMathUtils;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.numina.geometry.DrawableMuseRect;
import net.machinemuse.numina.geometry.MusePoint2D;
import net.machinemuse.utils.render.MuseRenderer;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:08 AM, 06/05/13
 *
 * Ported to Java by lehjr on 10/19/16.
 */
public class ClickableSlider extends Clickable {
    private final MusePoint2D pos;
    private final double width;
    private final String name;
    private final int cornersize;
    private final DrawableMuseRect insideRect;
    private final DrawableMuseRect outsideRect;
    private double valueInternal;

    public ClickableSlider(final MusePoint2D pos, final double width, final String name) {
        this.pos = pos;
        this.width = width;
        this.name = name;
        super.position = pos;
        this.cornersize = 3;
        this.insideRect = new DrawableMuseRect(this.position.x() - width / 2.0 - this.cornersize(), this.position.y() + 8, 0.0, this.position.y() + 16, Colour.LIGHTBLUE, Colour.ORANGE);
        this.outsideRect = new DrawableMuseRect(this.position.x() - width / 2.0 - this.cornersize(), this.position.y() + 8, this.position.x() + width / 2.0 + this.cornersize(), this.position.y() + 16, Colour.LIGHTBLUE, Colour.DARKBLUE);
        this.valueInternal = 0.0;
    }

    public MusePoint2D pos() {
        return this.pos;
    }

    public double width() {
        return this.width;
    }

    public String name() {
        return this.name;
    }

    public int cornersize() {
        return this.cornersize;
    }

    public DrawableMuseRect insideRect() {
        return this.insideRect;
    }

    public DrawableMuseRect outsideRect() {
        return this.outsideRect;
    }

    @Override
    public void draw() {
        MuseRenderer.drawCenteredString(this.name(), this.position.x(), this.position.y());
        this.insideRect().setRight(this.position.x() + this.width() * (this.value() - 0.5) + this.cornersize());
        this.outsideRect().draw();
        this.insideRect().draw();
    }

    @Override
    public boolean hitBox(final double x, final double y) {
        return Math.abs(this.position.x() - x) < this.width() / 2 && Math.abs(this.position.y() + 12 - y) < 4;
    }

    public double valueInternal() {
        return this.valueInternal;
    }

    public void valueInternal_$eq(final double x$1) {
        this.valueInternal = x$1;
    }

    public double value() {
        return this.valueInternal();
    }

    public void setValueByX(final double x) {
        final double v = (x - this.pos().x()) / this.width() + 0.5;
        this.valueInternal = (MuseMathUtils.clampDouble(v, 0.0, 1.0));
    }

    public void setValue(final double v) {
        this.valueInternal_$eq(v);
    }
}