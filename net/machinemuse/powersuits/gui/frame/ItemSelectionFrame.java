package net.machinemuse.powersuits.gui.frame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.FlyFromPointToPoint2D;
import net.machinemuse.general.geometry.MuseRenderer;
import net.machinemuse.general.geometry.Point2D;
import net.machinemuse.powersuits.gui.clickable.ClickableItem;
import net.machinemuse.powersuits.item.ItemUtils;
import net.minecraft.entity.player.EntityPlayer;

public class ItemSelectionFrame extends ScrollableFrame {
	protected List<ClickableItem> itemButtons;
	protected int selectedItemStack = -1;
	protected EntityPlayer player;
	protected List<Point2D> itemPoints;

	public ItemSelectionFrame(Point2D topleft, Point2D bottomright,
			Colour borderColour, Colour insideColour, EntityPlayer player) {
		super(topleft, bottomright, borderColour, insideColour);
		this.player = player;
		List<Integer> slots = ItemUtils
				.getModularItemSlotsInInventory(player.inventory);
		loadPoints(slots.size());
		loadItems();
	}

	private void loadPoints(int num) {
		double centerx = (topleft.x() + bottomright.x()) / 2;
		double centery = (topleft.y() + bottomright.y()) / 2;
		itemPoints = new ArrayList();
		List<Point2D> targetPoints = MuseRenderer.pointsInLine(num,
				new Point2D(centerx, bottomright.y()),
				new Point2D(centerx, topleft.y()));
		for (Point2D point : targetPoints) {
			// Fly from middle over 200 ms
			itemPoints.add(new FlyFromPointToPoint2D(
					new Point2D(centerx, centery),
					point, 200));
		}
	}

	private void loadItems() {
		itemButtons = new ArrayList<ClickableItem>();
		double centerx = (topleft.x() + bottomright.x()) / 2;
		double centery = (topleft.y() + bottomright.y()) / 2;
		List<Integer> slots = ItemUtils
				.getModularItemSlotsInInventory(player.inventory);
		if (slots.size() > itemPoints.size()) {
			loadPoints(slots.size());
		}
		if (slots.size() > 0) {
			Iterator<Point2D> pointiterator = itemPoints.iterator();

			for (int slot : slots) {
				ClickableItem clickie = new ClickableItem(
						player.inventory.getStackInSlot(slot),
						pointiterator.next(), slot);
				itemButtons.add(clickie);
			}
		}
	}

	@Override
	public void draw() {
		loadItems();
		drawBackground();
		drawItems();
		drawSelection();
	}

	private void drawBackground() {
		MuseRenderer.drawFrameRect(topleft, bottomright, borderColour,
				insideColour, 0, 4);
	}

	private void drawItems() {
		for (ClickableItem item : itemButtons) {
			item.draw();
		}
	}

	private void drawSelection() {
		if (selectedItemStack != -1) {
			MuseRenderer.drawCircleAround(
					Math.floor(itemButtons.get(selectedItemStack).getPosition()
							.x()),
					Math.floor(itemButtons.get(selectedItemStack).getPosition()
							.y()),
					10);
		}
	}

	public ClickableItem getSelectedItem() {
		if (itemButtons.size() > selectedItemStack && selectedItemStack != -1) {
			return itemButtons.get(selectedItemStack);
		} else {
			return null;
		}
	}

	@Override
	public void onMouseDown(int x, int y) {
		int i = 0;
		for (ClickableItem item : itemButtons) {
			if (item.hitBox(x, y)) {
				selectedItemStack = i;
				break;
			} else {
				i++;
			}
		}
	}

	@Override
	public List<String> getToolTip(int x, int y) {
		int itemHover = -1;
		int i = 0;
		for (ClickableItem item : itemButtons) {
			if (item.hitBox(x, y)) {
				itemHover = i;
				break;
			} else {
				i++;
			}
		}
		if (itemHover > -1) {
			return itemButtons.get(itemHover).getToolTip();
		} else {
			return null;
		}
	}

}
