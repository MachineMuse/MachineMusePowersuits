package net.machinemuse.general.gui.clickable;

import net.machinemuse.general.geometry.MusePoint2D;

import java.util.List;

/**
 * Defines a generic clickable item for a MuseGui.
 *
 * @author MachineMuse
 */
public abstract class Clickable implements IClickable {
    protected MusePoint2D position;

    public Clickable() {
        position = new MusePoint2D(0, 0);
    }

    public Clickable(MusePoint2D point) {
        position = point;
    }

    @Override
    public MusePoint2D getPosition() {
        return position;
    }

    public void setPosition(MusePoint2D position) {
        this.position = position;
    }

    @Override
    public void move(double x, double y) {
        this.position.setX(x);
        this.position.setY(y);
    }

    @Override
    public List<String> getToolTip() {
        return null;
    }
}
