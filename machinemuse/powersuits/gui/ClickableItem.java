package machinemuse.powersuits.gui;

import java.util.List;

import machinemuse.general.geometry.Point2D;
import net.minecraft.item.ItemStack;

public class ClickableItem extends Clickable {
	public static final Point2D offset = new Point2D(8, 8);
	private ItemStack item;

	public ClickableItem(ItemStack item, Point2D pos) {
		super(pos);
		this.item = item;
	}

	@Override
	public boolean hitBox(float x, float y) {
		boolean hitx = Math.abs(x - getPosition().getX()) < offset.getX();
		boolean hity = Math.abs(y - getPosition().getY()) < offset.getY();
		return hitx && hity;
	}

	@Override
	public List<String> getToolTip() {
		return item.getTooltip(null, false);
	}

}
