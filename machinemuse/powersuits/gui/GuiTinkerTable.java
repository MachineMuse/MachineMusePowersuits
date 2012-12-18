package machinemuse.powersuits.gui;

import java.util.ArrayList;

import machinemuse.general.geometry.Colour;
import machinemuse.powersuits.common.item.IModularItem;
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
	protected ArrayList<ItemStack> modularItems;

	/**
	 * Constructor. Takes a player as an argument.
	 * 
	 * @param player
	 */
	public GuiTinkerTable(EntityPlayer player) {
		this.player = player;
		this.modularItems = new ArrayList<ItemStack>();
		this.xSize = 256;
		this.ySize = 226;
	}

	/**
	 * Add the buttons (and other controls) to the screen.
	 */
	public void initGui()
	{
		super.initGui();
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (stack != null && stack.getItem() instanceof IModularItem) {
				modularItems.add(stack);
			}
		}
	}

	// public void drawNthItem(ItemStack stack, int n) {
	// // TBI
	// // draw item
	// // draw a button if it's moddable
	// }
	//
	// public void drawSelection() {
	// // if(selectedSlot != null) {
	// // drawCircleAround(selectedSlot.position, renderEngine, selectedSlot);
	// // }
	// // for (Augmentation a : Augmentation.getAllAugs()) {
	// // if (a.canGoInSlot(selectedSlot.getType())) {
	// //
	// // }
	// // }
	//
	// }

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
		drawItemList();
		Colour colour = Colour.getGreyscale(1.0F, 1.0F);
		// if (editingItem != null && editingLayout != null) {
		// drawLayout(editingLayout);
		// if (selectedSlot != null) {
		// drawSelection();
		// }
		// }
	}

	/**
	 * todo: Replace this with clickables...
	 */
	public void drawItemList() {
		this.drawItemsOnVerticalLine(modularItems, -0.9F, 0.0F,
				0.9f,
				this.mc.renderEngine);
	}

}
