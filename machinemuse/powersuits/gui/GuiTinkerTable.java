package machinemuse.powersuits.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import machinemuse.general.geometry.Colour;
import machinemuse.general.geometry.Doodler;
import machinemuse.general.geometry.FlyFromMiddlePoint2D;
import machinemuse.general.geometry.Point2D;
import machinemuse.powersuits.augmentation.Augmentation;
import machinemuse.powersuits.augmentation.AugmentationList;
import machinemuse.powersuits.item.ItemUtils;
import machinemuse.powersuits.network.MusePacketUpgradeRequest;
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
	protected ClickableItem selectedItemStack;
	protected List<ClickableAugmentation> augButtons;
	protected ClickableAugmentation selectedAugClickable;
	protected AugmentationList augData;
	protected Augmentation workingAugmentation;
	protected List<ItemStack> workingUpgradeCost;
	protected List<ItemStack> workingDowngradeRefund;
	protected ClickableButton upgradeButton;
	protected ClickableButton downgradeButton;

	/**
	 * Constructor. Takes a player as an argument.
	 * 
	 * @param player
	 */
	public GuiTinkerTable(EntityClientPlayerMP player) {
		this.player = player;
		this.itemButtons = new ArrayList<ClickableItem>();

		this.augButtons = new ArrayList<ClickableAugmentation>();
		this.xSize = 256;
		this.ySize = 226;
	}

	/**
	 * Add the buttons (and other controls) to the screen.
	 */
	public void initGui()
	{
		super.initGui();
		loadItems();
	}

	/**
	 * Add the clickables for the modular items available
	 * 
	 */
	public void loadItems() {
		List<ItemStack> stacks = ItemUtils
				.getModularItemsInInventory(player.inventory);

		List<Point2D> points = this.pointsInLine(stacks.size(),
				new Point2D(-0.9F, -0.9F),
				new Point2D(-0.9F, 0.9F));

		Iterator<ItemStack> stackiterator = stacks.iterator();
		Iterator<Point2D> pointiterator = points.iterator();

		for (int i = 0; i < stacks.size(); i++) {
			ClickableItem clickie = new ClickableItem(
					stackiterator.next(),
					// Fly from middle over 200 ms
					new FlyFromMiddlePoint2D(pointiterator.next(), 200));
			itemButtons.add(clickie);
		}

	}

	protected void loadAugList(ClickableItem itemClicked) {
		augButtons = new ArrayList<ClickableAugmentation>();
		augData = new AugmentationList(itemClicked.getItem());
		List<Augmentation> validAugs = ItemUtils.getAsModular(itemClicked
				.getItem().getItem()).getValidAugs();
		List<Point2D> points = this.pointsInLine(validAugs.size(),
				new Point2D(-0.7F, -0.9F),
				new Point2D(-0.7F, 0.9F));
		Iterator<Point2D> pointiter = points.iterator();
		for (Augmentation vaug : validAugs) {
			augButtons.add(new ClickableAugmentation(vaug, pointiter.next()));
		}
	}

	// public void drawNthItem(ItemStack stack, int n) {
	// // TBI
	// // draw item
	// // draw a button if it's moddable
	// }
	//

	public void drawSelection() {
		if (selectedItemStack != null) {
			Doodler.drawCircleAround(
					absX(selectedItemStack.getPosition().x()),
					absY(selectedItemStack.getPosition().y()),
					10);
		}

		if (selectedAugClickable != null) {
			Doodler.drawCircleAround(
					absX(selectedAugClickable.getPosition().x()),
					absY(selectedAugClickable.getPosition().y()),
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
		drawBackground();
		drawClickables(this.itemButtons);
		drawSelection();
		drawClickables(this.augButtons);
		drawUpgradeDowngrade();
	}

	/**
	 * Draws the upgrade/downgrade cost, buttons, and labels.
	 */
	public void drawUpgradeDowngrade() {
		if (workingUpgradeCost != null) {
			this.drawString(fontRenderer, "Cost:", absX(0.4F),
					absY(-0.7F),
					new Colour(0.5F, 1.0F, 0.5F, 1.0F).getInt());
			List<Point2D> points = this.pointsInLine(workingUpgradeCost.size(),
					new Point2D(0.4F, -0.5F),
					new Point2D(0.9F, -0.5F));
			Iterator<Point2D> pointiter = points.iterator();
			for (ItemStack item : workingUpgradeCost) {
				Point2D next = pointiter.next();
				Doodler.drawItemAt(absX(next.x()), absY(next.y()), this, item);
			}
			upgradeButton.draw(this.getRenderEngine(), this);
		}
		if (workingDowngradeRefund != null) {
			Doodler.on2D();
			this.drawString(fontRenderer, "Refund:", absX(0.4F),
					absY(0.3F),
					new Colour(1.0F, 0.6F, 0.2F, 1.0F).getInt());
			Doodler.off2D();
			List<Point2D> points = this.pointsInLine(
					workingDowngradeRefund.size(),
					new Point2D(0.4F, 0.5F),
					new Point2D(0.9F, 0.5F));
			Iterator<Point2D> pointiter = points.iterator();
			for (ItemStack item : workingDowngradeRefund) {
				Point2D next = pointiter.next();
				Doodler.drawItemAt(absX(next.x()), absY(next.y()), this, item);
			}
			downgradeButton.draw(this.getRenderEngine(), this);
		}

	}

	/**
	 * Clear all the UI stuff that's there.
	 */
	protected void clearSelections() {
		this.selectedAugClickable = null;
		this.workingAugmentation = null;
		this.workingUpgradeCost = null;
		this.workingDowngradeRefund = null;
		this.upgradeButton = null;
		this.downgradeButton = null;
	}

	/**
	 * Called when the mouse is clicked.
	 */
	protected void mouseClicked(int x, int y, int button)
	{
		if (button == 0) // Left Mouse Button
		{
			ClickableItem itemClicked =
					(ClickableItem) hitboxClickables(x, y, this.itemButtons);
			ClickableAugmentation augClicked =
					(ClickableAugmentation) hitboxClickables(x, y,
							this.augButtons);
			if (itemClicked != null) {
				clearSelections();
				this.selectedItemStack = itemClicked;
				loadAugList(itemClicked);
			} else if (augClicked != null) {
				this.selectedAugClickable = augClicked;
				this.workingAugmentation = augData.getAugData(augClicked.aug);
				refreshUpgrades();
			} else if (upgradeButton != null
					&& upgradeButton.enabled
					&& upgradeButton.hitBox(x, y, this)) {
				doUpgrade();
			} else if (downgradeButton != null
					&& downgradeButton.enabled
					&& downgradeButton.hitBox(x, y, this)) {
				doDowngrade();
			}
		}
	}

	/**
	 * 
	 */
	private void doDowngrade() {
		// TODO Auto-generated method stub

	}

	/**
	 * Performs all the functions associated with the upgrade button. This
	 * requires communicating with the server.
	 */
	private void doUpgrade() {
		if (ItemUtils.hasInInventory(workingUpgradeCost, player.inventory)) {
			// ItemUtils.deleteFromInventory(workingUpgradeCost,
			// player.inventory);
			// workingAugmentation.upgrade();
			player.sendQueue.addToSendQueue(new MusePacketUpgradeRequest(
					(Player) player, 0, 1).getPacket());
			// player.sendQueue.sendPacket();
		}
	}

	/**
	 * Updates the upgrade/downgrade buttons. May someday also include repairs.
	 */
	private void refreshUpgrades() {
		if (workingAugmentation != null) {
			this.workingUpgradeCost = workingAugmentation.getUpgradeCost();
			if (workingUpgradeCost != null) {
				this.upgradeButton = new ClickableButton("Upgrade",
						new Point2D(0.6F, -0.2F),
						new Point2D(0.25F, 0.05F), true);
				if (ItemUtils.hasInInventory(workingUpgradeCost,
						player.inventory)) {
					upgradeButton.enabled = true;
				} else {
					upgradeButton.enabled = false;
				}
			}
			this.workingDowngradeRefund = workingAugmentation
					.getDowngradeRefund();
			if (workingDowngradeRefund != null) {
				this.downgradeButton = new ClickableButton("Downgrade",
						new Point2D(0.6F, 0.8F),
						new Point2D(0.25F, 0.05F), true);
			}
		}
	}
}
