package machinemuse.powersuits.gui;

import java.util.List;

import machinemuse.general.geometry.Doodler;
import machinemuse.general.geometry.Point2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

/**
 * Extends the Clickable class to add a clickable ItemStack - note that this
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

	public ClickableItem(ItemStack item, Point2D pos, int inventorySlot) {
		super(pos);
		this.inventorySlot = inventorySlot;
		this.item = item;
	}

	public ItemStack getItem() {
		return item;
	}

	@Override
	public boolean hitBox(int x, int y, MuseGui gui) {
		boolean hitx = Math.abs(x - gui.absX(getPosition().x())) < offsetx;
		boolean hity = Math.abs(y - gui.absY(getPosition().y())) < offsety;
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
	public void draw(RenderEngine engine, MuseGui gui) {

		Doodler.drawItemAt(
				(int) gui.absX(getPosition().x()) - offsetx,
				(int) gui.absY(getPosition().y()) - offsety,
				gui, item);
	}
}
