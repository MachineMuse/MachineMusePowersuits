package net.machinemuse.powersuits.gui;

import java.util.List;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.Point2D;
import net.machinemuse.powersuits.gui.frame.InstallSalvageFrame;
import net.machinemuse.powersuits.gui.frame.ItemInfoFrame;
import net.machinemuse.powersuits.gui.frame.ItemSelectionFrame;
import net.machinemuse.powersuits.gui.frame.ModuleSelectionFrame;
import net.minecraft.client.entity.EntityClientPlayerMP;

/**
 * The gui class for the TinkerTable block.
 * 
 * @author MachineMuse
 * 
 */
public class GuiTinkerTable extends MuseGui {
	protected EntityClientPlayerMP player;
	protected ItemInfoFrame statsFrame;
	protected ItemSelectionFrame itemSelectFrame;
	protected ModuleSelectionFrame moduleSelectFrame;
	protected InstallSalvageFrame installFrame;

	/**
	 * Constructor. Takes a player as an argument.
	 * 
	 * @param player
	 */
	public GuiTinkerTable(EntityClientPlayerMP player) {
		this.player = player;
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
		itemSelectFrame = new ItemSelectionFrame(
				new Point2D(absX(-0.95F), absY(-0.95F)),
				new Point2D(absX(-0.78F), absY(0.95F)),
				Colour.LIGHTBLUE.withAlpha(0.8F),
				Colour.DARKBLUE.withAlpha(0.8F),
				player);
		statsFrame = new ItemInfoFrame(player,
				new Point2D(absX(0f), absY(-0.9f)),
				new Point2D(absX(0.9f), absY(0.0f)),
				Colour.LIGHTBLUE.withAlpha(0.8),
				Colour.DARKBLUE.withAlpha(0.8),
				itemSelectFrame);
		moduleSelectFrame = new ModuleSelectionFrame(
				new Point2D(absX(-0.75F), absY(-0.95f)),
				new Point2D(absX(-0.05F), absY(0.55f)),
				Colour.LIGHTBLUE.withAlpha(0.8),
				Colour.DARKBLUE.withAlpha(0.8),
				itemSelectFrame);
		installFrame = new InstallSalvageFrame(
				player,
				new Point2D(absX(-0.75F), absY(0.6f)),
				new Point2D(absX(-0.05F), absY(0.95f)),
				Colour.LIGHTBLUE.withAlpha(0.8),
				Colour.DARKBLUE.withAlpha(0.8),
				itemSelectFrame, moduleSelectFrame);
	}

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
		// if (refresh) {
		// refresh = false;
		// loadItems();
		// if (selectedItemStack != -1
		// && selectedItemStack < itemButtons.size()) {
		// loadModulesList(itemButtons.get(selectedItemStack));
		// }
		// refreshUpgrades();
		// }
		drawBackground();
		if (statsFrame != null) {
			statsFrame.draw();
		}
		if (itemSelectFrame != null) {
			itemSelectFrame.draw();
		}
		if (moduleSelectFrame != null) {
			moduleSelectFrame.draw();
		}
		if (installFrame != null) {
			installFrame.draw();
		}
		drawToolTip();
	}

	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int x, int y, int button)
	{
		if (button == 0) // Left Mouse Button
		{
			itemSelectFrame.onMouseDown(x, y);
			moduleSelectFrame.onMouseDown(x, y);
			installFrame.onMouseDown(x, y);
		}
	}

	/**
	 * @return
	 */
	@Override
	protected List<String> getToolTip(int x, int y) {
		List<String> hitTip = null;
		hitTip = itemSelectFrame.getToolTip(x, y);
		if (hitTip != null) {
			return hitTip;
		}
		hitTip = moduleSelectFrame.getToolTip(x, y);
		if (hitTip != null) {
			return hitTip;
		}
		return hitTip;
	}
}
