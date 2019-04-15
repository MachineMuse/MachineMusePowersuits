package net.machinemuse.powersuits.gui.tinker.clickable;

import net.machinemuse.numina.client.gui.clickable.Clickable;
import net.machinemuse.numina.client.render.MuseRenderer;
import net.machinemuse.numina.math.Colour;
import net.machinemuse.numina.math.geometry.MusePoint2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
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

    public ClickableItem(@Nonnull ItemStack item, MusePoint2D pos, int inventorySlot) {
        super(pos);
        this.inventorySlot = inventorySlot;
        this.item = item;
    }

    @Nonnull
    public ItemStack getItem() {
        return item;
    }

    @Override
    public boolean hitBox(double x, double y) {
        boolean hitx = Math.abs(x - getPosition().getX()) < offsetx;
        boolean hity = Math.abs(y - getPosition().getY()) < offsety;
        return hitx && hity;
    }

    @Override
    public List<String> getToolTip() {
        return item.getTooltip(Minecraft.getMinecraft().player, ITooltipFlag.TooltipFlags.NORMAL);
    }

    /**
     * Draws the specified itemstack at the *relative* coordinates getX,getY. Used
     * mainly in clickables.
     */
    @Override
    public void draw() {
        MuseRenderer.drawItemAt(
                getPosition().getX() - offsetx,
                getPosition().getY() - offsety, item);
        if (inventorySlot > 35 || Minecraft.getMinecraft().player.inventory.getCurrentItem() == item) {
            MuseRenderer.drawString("e", getPosition().getX() + 3, getPosition().getY() + 1, Colour.DARKGREEN);
        }
    }
}
