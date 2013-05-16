/**
 *
 */
package net.machinemuse.general.gui.clickable;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.DrawableMuseRect;
import net.machinemuse.general.geometry.MusePoint2D;
import net.machinemuse.utils.render.MuseRenderer;

import java.util.List;

/**
 * @author MachineMuse
 */
public class ClickableButton extends Clickable {
    protected String label;
    protected MusePoint2D radius;
    protected DrawableMuseRect rect;
    protected boolean enabled;

    public ClickableButton(String label, MusePoint2D position, boolean enabled) {
        this.label = label;
        this.position = position;
        this.radius = new MusePoint2D(MuseRenderer.getStringWidth(label) / 2 + 2, 6);
        this.rect = new DrawableMuseRect(
                position.x() - radius.x(),
                position.y() - radius.y(),
                position.x() + radius.x(),
                position.y() + radius.y(),
                new Colour(0.5F, 0.6F, 0.8F, 1),
                new Colour(0.3F, 0.3F, 0.3F, 1)
        );
        this.setEnabled(enabled);
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
        Colour topcolour;
        Colour bottomcolour;
        if (isEnabled()) {
            topcolour = new Colour(0.5F, 0.6F, 0.8F, 1);
            bottomcolour = new Colour(0.3F, 0.3F, 0.3F, 1);
        } else {
            topcolour = new Colour(0.8F, 0.3F, 0.3F, 1);
            bottomcolour = new Colour(0.8F, 0.6F, 0.6F, 1);
        }
        this.rect.setLeft(position.x() - radius.x());
        this.rect.setTop(position.y() - radius.y());
        this.rect.setRight(position.x() + radius.x());
        this.rect.setBottom(position.y() + radius.y());
        this.rect.setOutsideColour(topcolour);
        this.rect.setInsideColour(bottomcolour);
        this.rect.draw();
        MuseRenderer.drawCenteredString(this.label, position.x(),
                position.y() - 4);
    }

    /*
     * (non-Javadoc)
     *
     * @see machinemuse.powersuits.gui.Clickable#hitBox(int, int,
     * machinemuse.powersuits.gui.MuseGui)
     */
    @Override
    public boolean hitBox(double x, double y) {
        boolean hitx = Math.abs(position.x() - x) < radius.x();
        boolean hity = Math.abs(position.y() - y) < radius.y();
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

    public boolean isEnabled() {
        return enabled;
    }

    public ClickableButton setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

}
