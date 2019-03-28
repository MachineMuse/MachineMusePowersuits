package net.machinemuse.numina.client.gui.clickable;

import net.machinemuse.numina.client.gui.IClickable;
import net.machinemuse.numina.math.geometry.MusePoint2D;
import net.machinemuse.numina.client.render.MuseRenderer;

import java.util.List;

public class ClickableLabel implements IClickable {
    protected String label;
    protected MusePoint2D position;
    protected int mode;

    public ClickableLabel(String label, MusePoint2D position) {
        this.label = label;
        this.position = position;
        this.mode = 1;
    }

    public ClickableLabel(String label, MusePoint2D position, int mode) {
        this.label = label;
        this.position = position;
        this.mode = mode;
    }

    public ClickableLabel setMode(int mode) {
        this.mode = mode;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    // fixme: don't think this is actually working as intended
    @Override
    public void draw() {
        if (mode == 0)
            MuseRenderer.drawLeftAlignedStringString(this.label, position.getX(), position.getY() - 4);
        if (mode == 1)
            MuseRenderer.drawCenteredString(this.label, position.getX(), position.getY() - 4);
        if (mode == 2)
            MuseRenderer.drawRightAlignedString(this.label, position.getX(), position.getY() - 4);
    }

    @Override
    public boolean hitBox(double x, double y) {
        if (label == null || label.isEmpty())
            return false;

        MusePoint2D radius = new MusePoint2D(MuseRenderer.getStringWidth(label) / 2 + 2, 6);
        boolean hitx = Math.abs(position.getX() - x) < radius.getX();
        boolean hity = Math.abs(position.getY() - y) < radius.getY();
        return hitx && hity;
    }

    @Override
    public List<String> getToolTip() {
        return null;
    }

    @Override
    public void move(double x, double y) {
        this.position.setX(x);
        this.position.setY(y);
    }

    @Override
    public MusePoint2D getPosition() {
        return position;
    }
}
