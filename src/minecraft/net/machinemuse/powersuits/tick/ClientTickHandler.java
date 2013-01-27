package net.machinemuse.powersuits.tick;

import java.util.EnumSet;

import net.machinemuse.general.gui.clickable.ClickableKeybinding;
import net.machinemuse.powersuits.client.KeybindManager;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

/**
 * This handler is called before/after the game processes input events and
 * updates the gui state mainly. *Before rendering*
 * 
 * @author MachineMuse
 */
public class ClientTickHandler implements ITickHandler {
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		for (ClickableKeybinding kb : KeybindManager.getKeybindings()) {
			kb.doToggleTick();
		}

	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public String getLabel() {
		return "MMMPS: Client Tick";
	}

}
