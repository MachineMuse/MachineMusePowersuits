package net.machinemuse.powersuits.tick;

import java.util.EnumSet;

import net.machinemuse.general.gui.clickable.ClickableKeybinding;
import net.machinemuse.powersuits.client.KeybindManager;
import net.machinemuse.powersuits.item.IModularItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

/**
 * This handler is called before/after the game processes input events and
 * updates the gui state mainly. *Before rendering*
 * 
 * @author MachineMuse
 */
public class ClientTickHandler implements ITickHandler {
	protected int slotSelected = -1;

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		for (ClickableKeybinding kb : KeybindManager.getKeybindings()) {
			kb.doToggleTick();
		}
		try {
			// int dWheel = Mouse.getDWheel() / 120;
			EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
			// if (dWheel != 0) {
			// MuseLogger.logDebug("dWheel: " + dWheel);
			// }
			if (player.getCurrentEquippedItem().getItem() instanceof IModularItem && player.isSneaking()) {
				slotSelected = player.inventory.currentItem;
			} else {
				slotSelected = -1;
			}
		} catch (NullPointerException e) {

		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		try {
			if (slotSelected > -1) {
				EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
				player.inventory.currentItem = slotSelected;
				slotSelected = -1;
			}

		} catch (NullPointerException e) {

		}
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
