package net.machinemuse.powersuits.gui.tinker.clickable;

import net.machinemuse.numina.api.gui.IClickable;
import net.machinemuse.numina.utils.math.geometry.MusePoint2D;
import net.machinemuse.numina.utils.render.MuseRenderer;

import java.util.List;

public class ClickableLabel implements IClickable {
    protected String label;
    protected MusePoint2D position;
    public static final int offsetx = 8;

    public ClickableLabel(String label, MusePoint2D position) {
        this.label = label;
        this.position = position;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * machinemuse.powersuits.gui.Clickable#draw(net.minecraft.client.renderer
     * .RenderEngine, machinemuse.powersuits.gui.MuseGui)
     */
    @Override
    public void draw() {
        MuseRenderer.drawCenteredString(this.label, position.getX(),
                position.getY() - 4);
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

    /*
     * (non-Javadoc)
     *
     * @see machinemuse.powersuits.gui.Clickable#getToolTip()
     */
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
