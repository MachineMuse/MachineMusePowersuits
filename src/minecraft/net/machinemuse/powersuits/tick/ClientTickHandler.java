package net.machinemuse.powersuits.tick;

import java.util.EnumSet;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.general.gui.clickable.ClickableKeybinding;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.control.KeybindManager;
import net.machinemuse.powersuits.control.PlayerInputMap;
import net.machinemuse.powersuits.network.MusePacket;
import net.machinemuse.powersuits.network.packets.MusePacketPlayerUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Mouse;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

/**
 * This handler is called before/after the game processes input events and
 * updates the gui state mainly. *independent of rendering, so sometimes there
 * might be visual artifacts* -is also the parent class of KeyBindingHandler
 * 
 * @author MachineMuse
 */
public class ClientTickHandler implements ITickHandler {
	protected int slotSelected = -1;
	protected int dWheel;

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		for (ClickableKeybinding kb : KeybindManager.getKeybindings()) {
			kb.doToggleTick();
		}
		if (Config.useMouseWheel()) {
			try {
				EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
				if (player.getCurrentEquippedItem().getItem() instanceof IModularItem && player.isSneaking()) {
					slotSelected = player.inventory.currentItem;
					dWheel = Mouse.getDWheel() / 120;
				} else {
					slotSelected = -1;
				}
			} catch (NullPointerException e) {

			}
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;

		if (player != null) {
			if (slotSelected > -1) {
				player.inventory.currentItem = slotSelected;
				Minecraft.getMinecraft().playerController.updateController();
				ItemStack stack = player.inventory.getStackInSlot(slotSelected);
				MuseItemUtils.cycleMode(stack, player, dWheel);
				dWheel = 0;
				slotSelected = -1;
			}
			PlayerInputMap inputmap = PlayerInputMap.getInputMapFor(player.username);
			inputmap.forwardKey = Math.signum(player.movementInput.moveForward);
			inputmap.strafeKey = Math.signum(player.movementInput.moveStrafe);
			inputmap.jumpKey = player.movementInput.jump;
			inputmap.sneakKey = player.movementInput.sneak;
			inputmap.motionX = player.motionX;
			inputmap.motionY = player.motionY;
			inputmap.motionZ = player.motionZ;

			if (inputmap.hasChanged()) {
				inputmap.refresh();
				MusePacket inputPacket = new MusePacketPlayerUpdate(player, inputmap);
				player.sendQueue.addToSendQueue(inputPacket.getPacket250());
			}
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
