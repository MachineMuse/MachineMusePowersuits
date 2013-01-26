package net.machinemuse.general.gui;

import net.machinemuse.general.geometry.MusePoint2D;
import net.machinemuse.general.gui.frame.KeybindConfigFrame;
import net.minecraft.entity.player.EntityPlayer;

public class KeyConfigGui extends MuseGui {
	private EntityPlayer player;
	protected static KeybindConfigFrame frame;

	public KeybindConfigFrame getFrame() {
		if (frame == null) {
			frame = new KeybindConfigFrame(this,
					new MusePoint2D(absX(-0.95F), absY(-0.95F)),
					new MusePoint2D(absX(0.95F), absY(0.95F)), player);
		}
		return frame;
	}

	public KeyConfigGui(EntityPlayer player) {
		super();
		this.player = player;
		this.xSize = 256;
		this.ySize = 226;
	}

	/**
	 * Add the buttons (and other controls) to the screen.
	 */
	@Override
	public void initGui() {
		super.initGui();
		frames.add(getFrame());
	}

	@Override
	public void handleKeyboardInput() {
		super.handleKeyboardInput();
		frame.handleKeyboard();
	}
}
