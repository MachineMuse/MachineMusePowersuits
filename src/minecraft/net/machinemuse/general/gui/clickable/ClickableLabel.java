package net.machinemuse.general.gui.clickable;

import net.machinemuse.general.geometry.MusePoint2D;
import net.machinemuse.utils.render.MuseRenderer;

import java.util.List;

public class ClickableLabel implements IClickable {
    protected String label;
    protected MusePoint2D position;

    public ClickableLabel(String label, MusePoint2D position) {
        this.label = label;
        this.position = position;
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
        MuseRenderer.drawCenteredString(this.label, position.x(),
                position.y() - 4);
    }

    @Override
    public boolean hitBox(double x, double y) {
        return false;
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
