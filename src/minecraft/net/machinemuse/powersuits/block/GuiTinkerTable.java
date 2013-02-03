package net.machinemuse.powersuits.block;

import net.machinemuse.general.MuseRenderer;
import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MusePoint2D;
import net.machinemuse.general.gui.MuseGui;
import net.machinemuse.general.gui.frame.InstallSalvageFrame;
import net.machinemuse.general.gui.frame.ItemInfoFrame;
import net.machinemuse.general.gui.frame.ItemSelectionFrame;
import net.machinemuse.general.gui.frame.ModuleSelectionFrame;
import net.machinemuse.general.gui.frame.ModuleTweakFrame;
import net.minecraft.client.entity.EntityClientPlayerMP;

/**
 * The gui class for the TinkerTable block.
 * 
 * @author MachineMuse
 * 
 */
public class GuiTinkerTable extends MuseGui {
	protected EntityClientPlayerMP player;

	protected ItemSelectionFrame itemSelectFrame;

	/**
	 * Constructor. Takes a player as an argument.
	 * 
	 * @param player
	 */
	public GuiTinkerTable(EntityClientPlayerMP player) {
		this.player = player;
		this.xSize = 256;
		this.ySize = 200;
	}

	/**
	 * Add the buttons (and other controls) to the screen.
	 */
	@Override
	public void initGui() {
		super.initGui();
		itemSelectFrame = new ItemSelectionFrame(new MusePoint2D(absX(-0.95F), absY(-0.95F)), new MusePoint2D(absX(-0.78F), absY(0.95F)),
				Colour.LIGHTBLUE.withAlpha(0.8F), Colour.DARKBLUE.withAlpha(0.8F), player);
		frames.add(itemSelectFrame);

		ItemInfoFrame statsFrame = new ItemInfoFrame(player, new MusePoint2D(absX(0f), absY(-0.9f)), new MusePoint2D(absX(0.9f), absY(-0.05f)),
				Colour.LIGHTBLUE.withAlpha(0.8), Colour.DARKBLUE.withAlpha(0.8), itemSelectFrame);
		frames.add(statsFrame);

		ModuleSelectionFrame moduleSelectFrame = new ModuleSelectionFrame(new MusePoint2D(absX(-0.75F), absY(-0.95f)), new MusePoint2D(absX(-0.05F),
				absY(0.55f)), Colour.LIGHTBLUE.withAlpha(0.8), Colour.DARKBLUE.withAlpha(0.8), itemSelectFrame);
		frames.add(moduleSelectFrame);

		InstallSalvageFrame installFrame = new InstallSalvageFrame(player, new MusePoint2D(absX(-0.75F), absY(0.6f)), new MusePoint2D(absX(-0.05F),
				absY(0.95f)), Colour.LIGHTBLUE.withAlpha(0.8), Colour.DARKBLUE.withAlpha(0.8), itemSelectFrame, moduleSelectFrame);
		frames.add(installFrame);

		ModuleTweakFrame tweakFrame = new ModuleTweakFrame(player, new MusePoint2D(absX(0f), absY(0f)), new MusePoint2D(absX(0.9f), absY(0.9f)),
				Colour.LIGHTBLUE.withAlpha(0.8), Colour.DARKBLUE.withAlpha(0.8), itemSelectFrame, moduleSelectFrame);
		frames.add(tweakFrame);
	}

	@Override
	public void drawScreen(int x, int y, float z) {
		super.drawScreen(x, y, z);
		if (itemSelectFrame.hasNoItems()) {
			double centerx = absX(0);
			double centery = absY(0);
			MuseRenderer.drawCenteredString("No modular powersuit items", centerx, centery - 15);
			MuseRenderer.drawCenteredString("found in inventory. Make some!", centerx, centery - 5);
			MuseRenderer.drawCenteredString("We recommend experimenting", centerx, centery + 5);
			MuseRenderer.drawCenteredString("with steel plates and basic circuits.", centerx, centery + 15);
		}
	}
}
