package net.machinemuse.general.gui;

import net.machinemuse.general.geometry.MusePoint2D;
import net.machinemuse.general.gui.frame.KeybindConfigFrame;
import net.machinemuse.powersuits.client.KeybindManager;
import net.minecraft.entity.player.EntityPlayer;

public class KeyConfigGui extends MuseGui {
	private EntityPlayer player;
	protected KeybindConfigFrame frame;

	public KeyConfigGui(EntityPlayer player) {
		super();
		KeybindManager.readInKeybinds();
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
		frame = new KeybindConfigFrame(this,
				new MusePoint2D(absX(-0.95), absY(-0.95)),
				new MusePoint2D(absX(0.95), absY(0.95)), player);
		frames.add(frame);
	}

	@Override
	public void handleKeyboardInput() {
		super.handleKeyboardInput();
		frame.handleKeyboard();
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		KeybindManager.writeOutKeybinds();
	}
}
