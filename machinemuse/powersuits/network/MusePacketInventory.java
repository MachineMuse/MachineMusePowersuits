/**
 * 
 */
package machinemuse.powersuits.network;

import java.io.DataInputStream;

import machinemuse.powersuits.common.MuseLogger;
import machinemuse.powersuits.gui.MuseGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

/**
 * @author MachineMuse
 * 
 */
public class MusePacketInventory extends MusePacket {
	protected ItemStack stack;
	protected int slot;

	/**
	 * @param player
	 */
	public MusePacketInventory(Player player, int slot, ItemStack stack) {
		super(player);
		writeInt(slot);
		writeItemStack(stack);
	}

	/**
	 * @param player
	 * @param data
	 */
	public MusePacketInventory(DataInputStream datain, Player player) {
		super(player, datain);
		slot = readInt();
		stack = readItemStack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see machinemuse.powersuits.network.MusePacket#handleSelf()
	 */
	@Override
	public void handleSelf() {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (side == Side.CLIENT) {
			EntityClientPlayerMP mpplayer = (EntityClientPlayerMP) player;
			IInventory inventory = mpplayer.inventory;
			inventory.setInventorySlotContents(slot, stack);
			MuseLogger.logDebug("Received slot " + slot);
			GuiScreen playerscreen = Minecraft.getMinecraft().currentScreen;
			if (playerscreen != null && playerscreen instanceof MuseGui) {
				((MuseGui) playerscreen).refresh();
			}

		}

	}
}
