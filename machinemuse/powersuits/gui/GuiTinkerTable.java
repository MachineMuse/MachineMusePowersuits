package machinemuse.powersuits.gui;

import java.util.ArrayList;

import machinemuse.general.geometry.Colour;
import machinemuse.powersuits.common.item.IModularItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GuiTinkerTable extends MuseGui {
	protected EntityPlayer player;
	protected ArrayList<ItemStack> modularItems;

	public GuiTinkerTable(EntityPlayer player) {
		this.player = player;
		this.modularItems = new ArrayList<ItemStack>();
		this.xSize = 256;
		this.ySize = 226;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
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

	public void refresh() {
		for (int i = 0; i < 4; i++) {

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
	public void drawBackground() {
		this.drawDefaultBackground();
		this.drawRectangularBackground();
	}

	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
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

	public void drawItemList() {
		this.drawItemsOnVerticalLine(modularItems, -0.9F, 0.0F,
				0.9f,
				this.mc.renderEngine);
	}

}
