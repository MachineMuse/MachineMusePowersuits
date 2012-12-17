package machinemuse.powersuits.client;

import machinemuse.powersuits.common.ContainerTinkerTable;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;

import org.lwjgl.opengl.GL11;

public class GuiTinkerTable extends GuiContainer {
	public IInventory[] armorInventory;

	public GuiTinkerTable(ContainerTinkerTable container) {
		super(container);
		this.xSize = 256;
		this.ySize = 226;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui()
	{
		super.initGui();
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
	// public void drawBackground() {
	// this.drawDefaultBackground();
	// this.drawRectangularBackground();
	// }
	//
	// public void drawScreen(int par1, int par2, float par3) {
	// super.drawScreen(par1, par2, par3);
	// drawBackground();
	// drawItemList();
	// if (editingItem != null && editingLayout != null) {
	// drawLayout(editingLayout);
	// if (selectedSlot != null) {
	// drawSelection();
	// }
	// }
	// }
	//
	// public void drawItemList() {
	// List<ItemStack> items = new ArrayList<ItemStack>();
	// for (int i = 0; i < 4; i++) {
	// ItemStack item = editingPlayer.inventory.armorItemInSlot(i);
	// this.drawItemAt(new Point2Df(0, 0), this.mc.renderEngine, item);
	// items.add(item);
	// }
	// for (int i = 0; i < 9; i++) {
	// items.add(editingPlayer.inventory.mainInventory[i]);
	// }
	// this.drawItemsOnVerticalLine(items, new Point2Df(-0.9f, 0), 0.9f,
	// this.mc.renderEngine);
	// }

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		int x = 0;
		int y = 0;
		int color = 0;
		double weight = 0.0;
		double capacity = 0.0;

		this.fontRenderer.drawString("Weight: " + weight, 8, 104,
				color);
		this.fontRenderer.drawString("Capacity: " + capacity, 8, 114,
				color);
		this.fontRenderer.drawString("Speed: " + capacity, 8, 124,
				color);
	}

	protected void drawAugmentationContainerBackground(int paddingX,
			int paddingY, int j) {
		int n = 0; // set N to the number of augs in that item, plus 1
		for (int i = 0; i < n; i++) {
			this.drawTexturedModalRect(
					paddingX + 35, // where on the screen
					paddingY + 8 + 18 * j,
					0, // where in the texture
					227,
					18 * i, // how much
					18);
		}
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the
	 * items)
	 */
	protected void drawGuiContainerBackgroundLayer(float par1, int par2,
			int par3)
	{
		int textureIndex = this.mc.renderEngine
				.getTexture("/img/tinktablegui.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(textureIndex);
		int paddingX = (this.width - this.xSize) / 2;
		int paddingY = (this.height - this.ySize) / 2;
		int numrows = 5;
		this.drawTexturedModalRect(paddingX, paddingY, 0, 0, this.xSize,
				this.ySize);
	}
}
