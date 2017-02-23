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
    double valueInternal = 0;
    MusePoint2D pos;
    double width;
    String name;
    DrawableMuseRect insideRect;
    DrawableMuseRect outsideRect;
    final int cornersize = 3;

    public ClickableSlider(MusePoint2D pos, double width, String name) {
        this.pos = pos;
        this.width = width;
        this.name = name;
        this.position = pos;
        this.insideRect = new DrawableMuseRect(position.x() - width / 2.0 - cornersize, position.y() + 8, 0, position.y() + 16, Colour.LIGHTBLUE, Colour.ORANGE);
        this.outsideRect = new DrawableMuseRect(position.x() - width / 2.0 - cornersize, position.y() + 8, position.x() + width / 2.0 + cornersize, position.y() + 16, Colour.LIGHTBLUE, Colour.DARKBLUE);
    }

    public String name() {
        return this.name;
    }

    @Override
    public void draw() {
        MuseRenderer.drawCenteredString(name, position.x(), position.y());
        this.insideRect.setRight(position.x() + width * (value() - 0.5) + cornersize);
        this.outsideRect.draw();
        this.insideRect.draw();
    }

    @Override
    public boolean hitBox(double x, double y) {
        return Math.abs(position.x() - x) < width / 2 &&
                Math.abs(position.y() + 12 - y) < 4;
    }

    public double value() {
        return valueInternal;
    }

    public void setValueByX(double x) {
        double v = (x - pos.x()) / width + 0.5;
        valueInternal = MuseMathUtils.clampDouble(v, 0, 1);
    }

    public void setValue(double v) {
        valueInternal = v;
    }
}
