package net.machinemuse.general.gui.clickable;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MusePoint2D;
import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Extends the Clickable class to add a clickable IC2ItemTest - note that this
 * will be a button that looks like the item, not a container slot.
 *
 * @author MachineMuse
 */
public class ClickableItem extends Clickable {
    public static final int offsetx = 8;
    public static final int offsety = 8;
    public static RenderItem itemRenderer;
    public int inventorySlot;
    protected ItemStack item;

    public ClickableItem(ItemStack item, MusePoint2D pos, int inventorySlot) {
        super(pos);
        this.inventorySlot = inventorySlot;
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }

    @Override
    public boolean hitBox(double x, double y) {
        boolean hitx = Math.abs(x - getPosition().x()) < offsetx;
        boolean hity = Math.abs(y - getPosition().y()) < offsety;
        return hitx && hity;
    }

    @Override
    public List<String> getToolTip() {
        return item.getTooltip(Minecraft.getMinecraft().thePlayer, false);
    }

    /**
     * Draws the specified itemstack at the *relative* coordinates x,y. Used
     * mainly in clickables.
     */
    @Override
    public void draw() {
        MuseRenderer.drawItemAt(
                getPosition().x() - offsetx,
                getPosition().y() - offsety, item);
        if (inventorySlot > 35 || Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem() == item) {
            MuseRenderer.drawString("e", getPosition().x() + 3, getPosition().y() + 1, Colour.DARKGREEN);
        }
    }
}
