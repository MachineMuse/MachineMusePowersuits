package net.machinemuse.general.gui;

import net.machinemuse.general.geometry.Point2D;
import net.machinemuse.general.gui.frame.KeybindConfigFrame;
import net.minecraft.entity.player.EntityPlayer;

public class KeyConfigGui extends MuseGui {
	private EntityPlayer player;

	public KeyConfigGui(EntityPlayer player) {
		super();
		this.player = player;
		this.xSize = 256;
		this.ySize = 226;
	}
	/**
	 * Add the buttons (and other controls) to the screen.
	 */
	@Override public void initGui() {
		super.initGui();
		frames.add(new KeybindConfigFrame(
				new Point2D(absX(-0.95F), absY(-0.95F)),
				new Point2D(absX(0.95F), absY(0.95F)), player));
	}
}
