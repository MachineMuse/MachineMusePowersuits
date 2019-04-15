/**
 *
 */
package net.machinemuse.powersuits.gui.tinker.clickable;

import net.machinemuse.numina.client.gui.clickable.Clickable;
import net.machinemuse.numina.client.render.MuseRenderer;
import net.machinemuse.numina.math.Colour;
import net.machinemuse.numina.math.geometry.DrawableMuseRect;
import net.machinemuse.numina.math.geometry.MusePoint2D;

import java.util.List;

/**
 * @author MachineMuse
 */
public class ClickableButton extends Clickable {
    protected String label;
    protected MusePoint2D radius;
    protected DrawableMuseRect rect;
    protected boolean enabled;
    protected boolean visible = true;

    public ClickableButton(String label, MusePoint2D position, boolean enabled) {
        this.label = label;
        this.position = position;
        this.radius = new MusePoint2D(MuseRenderer.getStringWidth(label) / 2 + 2, 6);
        this.rect = new DrawableMuseRect(
                position.getX() - radius.getX(),
                position.getY() - radius.getY(),
                position.getX() + radius.getX(),
                position.getY() + radius.getY(),
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
        if (visible) {
            Colour topcolour;
            Colour bottomcolour;
            if (isEnabled()) {
                topcolour = new Colour(0.5F, 0.6F, 0.8F, 1);
                bottomcolour = new Colour(0.3F, 0.3F, 0.3F, 1);
            } else {
                topcolour = new Colour(0.8F, 0.3F, 0.3F, 1);
                bottomcolour = new Colour(0.8F, 0.6F, 0.6F, 1);
            }
            this.rect.setLeft(position.getX() - radius.getX());
            this.rect.setTop(position.getY() - radius.getY());
            this.rect.setRight(position.getX() + radius.getX());
            this.rect.setBottom(position.getY() + radius.getY());
            this.rect.setOutsideColour(topcolour);
            this.rect.setInsideColour(bottomcolour);
            this.rect.draw();
            MuseRenderer.drawCenteredString(this.label, position.getX(),
                    position.getY() - 4);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see machinemuse.powersuits.gui.Clickable#hitBox(int, int,
     * machinemuse.powersuits.gui.MuseGui)
     */
    @Override
    public boolean hitBox(double x, double y) {
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

    public boolean isEnabled() {
        return enabled;
    }

    public ClickableButton setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void enable() {
        this.enabled = true;
    }

    public void buttonOn() {
        this.enable();
        this.show();
    }

    public void buttonOff() {
        this.disable();
        this.hide();
    }

    public void disable() {
        this.enabled = false;
    }

    public void show() {
        this.visible = true;
    }

    public void hide() {
        this.visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public ClickableButton setLable(String label) {
        this.label = label;
        return this;
    }

    public String getLabel() {
        return label;
    }
}
