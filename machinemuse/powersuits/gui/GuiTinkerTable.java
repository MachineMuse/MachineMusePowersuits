package machinemuse.powersuits.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import machinemuse.general.geometry.Doodler;
import machinemuse.general.geometry.FlyFromMiddlePoint2D;
import machinemuse.general.geometry.Point2D;
import machinemuse.powersuits.augmentation.Augmentation;
import machinemuse.powersuits.augmentation.AugmentationList;
import machinemuse.powersuits.common.MuseLogger;
import machinemuse.powersuits.item.ItemUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * The gui class for the TinkerTable block.
 * 
 * @author MachineMuse
 * 
 */
public class GuiTinkerTable extends MuseGui {
	protected EntityPlayer player;
	protected List<ClickableItem> itemButtons;
	protected ClickableItem selectedItemStack;
	protected List<ClickableAugmentation> augButtons;
	protected ClickableAugmentation selectedAugmentation;
	protected AugmentationList augData;

	/**
	 * Constructor. Takes a player as an argument.
	 * 
	 * @param player
	 */
	public GuiTinkerTable(EntityPlayer player) {
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
		List<ItemStack> stacks = ItemUtil
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
		List<Augmentation> validAugs = ItemUtil.getAsModular(itemClicked
				.getItem().getItem()).getValidAugs();
		List<Point2D> points = this.pointsInLine(validAugs.size(),
				new Point2D(-0.7F, -0.9F),
				new Point2D(-0.7F, 0.9F));
		Iterator<Point2D> pointiter = points.iterator();
		for (Augmentation vaug : validAugs) {
			MuseLogger.logDebug("Found a valid augmentation");
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

		if (selectedAugmentation != null) {
			Doodler.drawCircleAround(
					absX(selectedAugmentation.getPosition().x()),
					absY(selectedAugmentation.getPosition().y()),
					10);
		}
		// for (Augmentation a : Augmentation.getAllAugs()) {
		// if (a.canGoInSlot(selectedSlot.getType())) {
		//
		// }
		// }

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
		// Colour colour = Colour.getGreyscale(1.0F, 1.0F);
		// if (editingItem != null && editingLayout != null) {
		// drawLayout(editingLayout);
		// if (selectedSlot != null) {
		// drawSelection();
		// }
		// }
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
				this.selectedItemStack = itemClicked;
				loadAugList(itemClicked);
			} else if (augClicked != null) {
				this.selectedAugmentation = augClicked;
				// TODO: add ui on the right for stuff
			}
		}
	}

}
