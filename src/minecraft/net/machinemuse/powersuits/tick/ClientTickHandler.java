package net.machinemuse.powersuits.tick;

import java.util.EnumSet;
import java.util.List;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.general.gui.clickable.ClickableKeybinding;
import net.machinemuse.powersuits.client.KeybindManager;
import net.machinemuse.powersuits.common.PlayerInputMap;
import net.machinemuse.powersuits.network.MusePacket;
import net.machinemuse.powersuits.network.packets.MusePacketModeChangeRequest;
import net.machinemuse.powersuits.network.packets.MusePacketPlayerUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.Player;

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
		try {
			EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
			// if (dWheel != 0) {
			// MuseLogger.logDebug("dWheel: " + dWheel);
			// }
			if (player.getCurrentEquippedItem().getItem() instanceof IModularItem
					&& player.isSneaking()) {
				slotSelected = player.inventory.currentItem;
				dWheel = Mouse.getDWheel() / 120;
			} else {
				slotSelected = -1;
			}
		} catch (NullPointerException e) {

		}
	}

	public int clampMode(int selection, int modesSize) {
		if (selection > 0) {
			return selection % modesSize;
		} else {
			return (selection + modesSize * (-selection)) % modesSize;
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
				if (stack != null && stack.getItem() instanceof IModularItem) {
					NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
					String mode = itemTag.getString("Mode");
					List<String> modes = MuseItemUtils.getModes(stack, player);
					if (mode == "" && modes.size() > 0) {
						mode = modes.get(0);
					}
					if (modes.size() > 0 && dWheel != 0) {
						int modeIndex = modes.indexOf(mode);
						String newMode = modes.get(clampMode(modeIndex + dWheel,
								modes.size()));
						itemTag.setString("Mode", newMode);
						RenderTickHandler.lastSwapTime = System.currentTimeMillis();
						RenderTickHandler.lastSwapDirection = (int) Math.signum(dWheel);
						MusePacket modeChangePacket = new
								MusePacketModeChangeRequest((Player) player, newMode,
										player.inventory.currentItem);
						player.sendQueue.addToSendQueue(modeChangePacket.getPacket250());
						dWheel = 0;
					}
				}
				slotSelected = -1;
			}
			PlayerInputMap inputmap = PlayerInputMap.getInputMapFor(player.username);
			inputmap.downKey = Keyboard.isKeyDown(Keyboard.KEY_Z) && Minecraft.getMinecraft().inGameHasFocus;
			inputmap.forwardKey = player.movementInput.moveForward;
			inputmap.strafeKey = player.movementInput.moveStrafe;
			inputmap.jumpKey = player.movementInput.jump;
			inputmap.sneakKey = player.movementInput.sneak;
			inputmap.motionX = player.motionX;
			inputmap.motionY = player.motionY;
			inputmap.motionZ = player.motionZ;

			MusePacket inputPacket = new MusePacketPlayerUpdate(player, inputmap);
			player.sendQueue.addToSendQueue(inputPacket.getPacket250());
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
