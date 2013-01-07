package net.machinemuse.powersuits.block;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.Point2D;
import net.machinemuse.general.gui.MuseGui;
import net.machinemuse.general.gui.frame.*;
import net.minecraft.client.entity.EntityClientPlayerMP;

/**
 * The gui class for the TinkerTable block.
 * 
 * @author MachineMuse
 * 
 */
public class GuiTinkerTable extends MuseGui {
	protected EntityClientPlayerMP player;
	
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
	@Override public void initGui()
	{
		super.initGui();
		ItemSelectionFrame itemSelectFrame = new ItemSelectionFrame(
				new Point2D(absX(-0.95F), absY(-0.95F)),
				new Point2D(absX(-0.78F), absY(0.95F)),
				Colour.LIGHTBLUE.withAlpha(0.8F),
				Colour.DARKBLUE.withAlpha(0.8F),
				player);
		frames.add(itemSelectFrame);
		
		ItemInfoFrame statsFrame = new ItemInfoFrame(player,
				new Point2D(absX(0f), absY(-0.9f)),
				new Point2D(absX(0.9f), absY(-0.05f)),
				Colour.LIGHTBLUE.withAlpha(0.8),
				Colour.DARKBLUE.withAlpha(0.8),
				itemSelectFrame);
		frames.add(statsFrame);
		
		ModuleSelectionFrame moduleSelectFrame = new ModuleSelectionFrame(
				new Point2D(absX(-0.75F), absY(-0.95f)),
				new Point2D(absX(-0.05F), absY(0.55f)),
				Colour.LIGHTBLUE.withAlpha(0.8),
				Colour.DARKBLUE.withAlpha(0.8),
				itemSelectFrame);
		frames.add(moduleSelectFrame);
		
		InstallSalvageFrame installFrame = new InstallSalvageFrame(
				player,
				new Point2D(absX(-0.75F), absY(0.6f)),
				new Point2D(absX(-0.05F), absY(0.95f)),
				Colour.LIGHTBLUE.withAlpha(0.8),
				Colour.DARKBLUE.withAlpha(0.8),
				itemSelectFrame, moduleSelectFrame);
		frames.add(installFrame);
		
		ModuleTweakFrame tweakFrame = new ModuleTweakFrame(
				player,
				new Point2D(absX(0f), absY(0f)),
				new Point2D(absX(0.9f), absY(0.9f)),
				Colour.LIGHTBLUE.withAlpha(0.8),
				Colour.DARKBLUE.withAlpha(0.8),
				itemSelectFrame, moduleSelectFrame);
		frames.add(tweakFrame);
	}
	
}
