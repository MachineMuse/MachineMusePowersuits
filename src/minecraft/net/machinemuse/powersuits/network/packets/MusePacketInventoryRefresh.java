/**
 * 
 */
package net.machinemuse.powersuits.network.packets;

import java.io.DataInputStream;

import net.machinemuse.general.gui.MuseGui;
import net.machinemuse.powersuits.network.MusePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.network.Player;

/**
 * @author MachineMuse
 * 
 */
public class MusePacketInventoryRefresh extends MusePacket {
	protected ItemStack stack;
	protected int slot;

	/**
	 * @param player
	 */
	public MusePacketInventoryRefresh(Player player, int slot,
			ItemStack stack) {
		super(player);
		writeInt(slot);
		writeItemStack(stack);
	}

	/**
	 * @param player
	 * @param data
	 */
	public MusePacketInventoryRefresh(DataInputStream datain, Player player) {
		super(datain, player);
		slot = readInt();
		stack = readItemStack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see machinemuse.powersuits.network.MusePacket#handleSelf()
	 */
	@Override
	public void handleClient(EntityClientPlayerMP player) {
		IInventory inventory = player.inventory;
		inventory.setInventorySlotContents(slot, stack);
		GuiScreen playerscreen = Minecraft.getMinecraft().currentScreen;
		if (playerscreen != null && playerscreen instanceof MuseGui) {
			((MuseGui) playerscreen).refresh();
		}

	}

	@Override
	public void handleServer(EntityPlayerMP player) {
	}

}
