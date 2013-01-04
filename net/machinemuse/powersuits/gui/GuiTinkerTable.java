package net.machinemuse.powersuits.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.FlyFromPointToPoint2D;
import net.machinemuse.general.geometry.MuseRenderer;
import net.machinemuse.general.geometry.Point2D;
import net.machinemuse.powersuits.gui.clickable.ClickableButton;
import net.machinemuse.powersuits.gui.clickable.ClickableItem;
import net.machinemuse.powersuits.gui.clickable.ClickableTinkerAction;
import net.machinemuse.powersuits.gui.frame.IGuiFrame;
import net.machinemuse.powersuits.gui.frame.ItemInfoFrame;
import net.machinemuse.powersuits.item.ItemUtils;
import net.machinemuse.powersuits.network.MusePacket;
import net.machinemuse.powersuits.network.MusePacketTinkerRequest;
import net.machinemuse.powersuits.tinker.TinkerAction;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.network.Player;

/**
 * The gui class for the TinkerTable block.
 * 
 * @author MachineMuse
 * 
 */
public class GuiTinkerTable extends MuseGui {
	protected EntityClientPlayerMP player;
	protected List<ClickableItem> itemButtons;
	protected int selectedItemStack = -1;
	protected List<ClickableTinkerAction> tinkeringButtons;
	protected int selectedTinkerAction = -1;
	protected List<ItemStack> workingUpgradeCost;
	protected List<ItemStack> workingDowngradeRefund;
	protected ClickableButton applyTinkerButton;
	protected IGuiFrame statsFrame;
	protected boolean refresh = true;

	/**
	 * Constructor. Takes a player as an argument.
	 * 
	 * @param player
	 */
	public GuiTinkerTable(EntityClientPlayerMP player) {
		this.player = player;
		this.loadItems();

		this.xSize = 256;
		this.ySize = 226;
	}

	/**
	 * Add the buttons (and other controls) to the screen.
	 */
	@Override
	public void initGui()
	{
		super.initGui();
	}

	/**
	 * Add the clickables for the modular items available
	 * 
	 */
	public void loadItems() {
		itemButtons = new ArrayList<ClickableItem>();
		List<Integer> slots = ItemUtils
				.getModularItemSlotsInInventory(player.inventory);
		if (slots.size() > 0) {
			List<Point2D> points = this.pointsInLine(slots.size(),
					new Point2D(absX(-0.9F), absY(0.9F)),
					new Point2D(absX(-0.9F), absY(-0.9F)));

			Iterator<Integer> slotiterator = slots.iterator();
			Iterator<Point2D> pointiterator = points.iterator();

			for (int slot : slots) {
				ClickableItem clickie = new ClickableItem(
						player.inventory.getStackInSlot(slot),
						// Fly from middle over 200 ms
						new FlyFromPointToPoint2D(
								new Point2D(absX(0), absY(0)),
								pointiterator.next(), 200),
						slot);
				itemButtons.add(clickie);
			}
		}
	}

	protected void loadTinkersList(ClickableItem itemClicked) {
		statsFrame = new ItemInfoFrame(
				new Point2D(absX(0f), absY(-0.9f)),
				new Point2D(absX(0.9f), absY(0.9f)),
				Colour.LIGHTBLUE.withAlpha(0.8),
				Colour.DARKBLUE.withAlpha(0.8),
				itemClicked.getItem());
		tinkeringButtons = new ArrayList();
		List<TinkerAction> workingTinkers = ItemUtils
				.getValidTinkersForItem(player, itemClicked
						.getItem());
		if (workingTinkers.size() > 0) {
			List<Point2D> points = this.pointsInLine(workingTinkers.size(),
					new Point2D(absX(-0.7F), absY(-0.9F)),
					new Point2D(absX(-0.7F), absY(0.9F)));
			Iterator<Point2D> pointiter = points.iterator();
			for (TinkerAction tinker : workingTinkers) {
				tinkeringButtons.add(new ClickableTinkerAction(tinker,
						pointiter
								.next()));
			}
		}
	}

	// public void drawNthItem(ItemStack stack, int n) {
	// // TBI
	// // draw item
	// // draw a button if it's moddable
	// }
	//

	public void drawSelection() {
		if (selectedItemStack != -1) {
			MuseRenderer.drawCircleAround(
					itemButtons.get(selectedItemStack).getPosition().x(),
					itemButtons.get(selectedItemStack).getPosition().y(),
					10);
		}

		if (selectedTinkerAction != -1) {
			MuseRenderer
					.drawCircleAround(
							tinkeringButtons.get(selectedTinkerAction)
									.getPosition().x(),
							tinkeringButtons.get(selectedTinkerAction)
									.getPosition().y(),
							10);

		}

	}

	// public void drawLayout(AugLayout layout) {
	//
	// }
	//

	/**
	 * Draws the background layer for the GUI.
	 */
	public void drawBackground() {
		this.drawDefaultBackground(); // Shading on the world view
		this.drawRectangularBackground(); // The window rectangle
	}

	/**
	 * Called every frame, draws the screen!
	 */
	@Override
	public void drawScreen(int x, int y, float z) {
		super.drawScreen(x, y, z);
		if (refresh) {
			refresh = false;
			loadItems();
			if (selectedItemStack != -1
					&& selectedItemStack < itemButtons.size()) {
				loadTinkersList(itemButtons.get(selectedItemStack));
			}
			refreshUpgrades();
		}
		drawBackground();
		drawClickables(this.itemButtons);
		drawSelection();
		if (statsFrame != null) {
			statsFrame.draw();
		}
		drawClickables(this.tinkeringButtons);
		drawApplyTinkerFrame();
		drawToolTip();
	}

	/**
	 * Draws the tinkering cost, buttons, and labels.
	 */
	public void drawApplyTinkerFrame() {
		if (workingUpgradeCost != null && workingUpgradeCost.size() > 0) {
			MuseRenderer.drawString("Cost:", absX(-0.7F),
					absY(0.7F));
			List<Point2D> points = this.pointsInLine(workingUpgradeCost.size(),
					new Point2D(absX(-0.4F), absY(0.7F)),
					new Point2D(absX(-0.8F), absY(0.7F)));
			Iterator<Point2D> pointiter = points.iterator();
			for (ItemStack item : workingUpgradeCost) {
				Point2D next = pointiter.next();
				MuseRenderer.drawItemAt(next.x(), next.y(),
						item);
			}
			applyTinkerButton.draw();
		}

	}

	/**
	 * Clear all the UI stuff that's there.
	 */
	protected void clearSelections() {
		this.selectedTinkerAction = -1;
		this.workingUpgradeCost = null;
		this.workingDowngradeRefund = null;
		this.applyTinkerButton = null;
	}

	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int x, int y, int button)
	{
		if (button == 0) // Left Mouse Button
		{
			int itemClicked = hitboxClickables(x, y, this.itemButtons);
			int augClicked = hitboxClickables(x, y, this.tinkeringButtons);
			if (itemClicked != -1) {
				clearSelections();
				this.selectedItemStack = itemClicked;
				loadTinkersList(itemButtons.get(itemClicked));
			} else if (augClicked != -1) {
				this.selectedTinkerAction = augClicked;
				refreshUpgrades();
			} else if (applyTinkerButton != null
					&& applyTinkerButton.isEnabled()
					&& applyTinkerButton.hitBox(x, y)) {
				doTinker();
			}
		}
	}

	/**
	 * Performs all the functions associated with the upgrade button. This
	 * requires communicating with the server.
	 */
	private void doTinker() {
		if (ItemUtils.hasInInventory(workingUpgradeCost, player.inventory)) {
			// ItemUtils.deleteFromInventory(workingUpgradeCost,
			// player.inventory);
			// workingAugmentation.upgrade();
			MusePacket newpacket = new MusePacketTinkerRequest(
					(Player) player,
					itemButtons.get(selectedItemStack).inventorySlot,
					tinkeringButtons.get(selectedTinkerAction).getAction()
							.getName()
					);
			player.sendQueue.addToSendQueue(newpacket.getPacket250());
			// player.sendQueue.sendPacket();
		}
	}

	/**
	 * Updates the apply tinker button.
	 */
	private void refreshUpgrades() {
		if (selectedTinkerAction != -1
				&& tinkeringButtons.size() > selectedTinkerAction) {
			this.workingUpgradeCost =
					tinkeringButtons.get(selectedTinkerAction).getAction()
							.getCosts();
			if (workingUpgradeCost != null) {
				this.applyTinkerButton = new ClickableButton("Apply",
						new Point2D(-.4F, 0.9F),
						new Point2D(0.20F, 0.05F), true);
				if (ItemUtils.hasInInventory(workingUpgradeCost,
						player.inventory)) {
					applyTinkerButton.setEnabled(true);
				} else {
					applyTinkerButton.setEnabled(false);
				}
			}
		}
	}

	/**
	 * @return
	 */
	@Override
	protected List<String> getToolTip(int x, int y) {
		List<String> hitTip = null;
		int itemHover = hitboxClickables(x, y, this.itemButtons);
		if (itemHover > -1) {
			hitTip = itemButtons.get(itemHover).getToolTip();
		}
		int augHover = hitboxClickables(x, y, this.tinkeringButtons);
		if (augHover > -1) {
			hitTip = tinkeringButtons.get(augHover).getToolTip();
		}
		return hitTip;
	}

	@Override
	public void refresh() {
		refresh = true;
	}
}
