/**
 * 
 */
package net.machinemuse.powersuits.network.packets;

import java.io.DataInputStream;
import java.util.List;

import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.powersuits.network.MusePacket;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.Player;

/**
 * @author Claire
 * 
 */
public class MusePacketModeChangeRequest extends MusePacket {
	protected String mode;
	protected int slot;

	/**
	 */
	public MusePacketModeChangeRequest(Player player, String mode, int slot) {
		super(player);
		writeInt(slot);
		writeString(mode);
	}

	/**
	 * @param player
	 * @param data
	 */
	public MusePacketModeChangeRequest(DataInputStream data, Player player) {
		super(data, player);
		slot = readInt();
		mode = readString(64);
	}

	@Override
	public void handleClient(EntityClientPlayerMP player) {

	}

	@Override
	public void handleServer(EntityPlayerMP player) {
		ItemStack stack = null;
		if (slot > -1 && slot < 9) {
			// player.inventory.currentItem = slot;
			stack = player.inventory.mainInventory[slot];
		}

		if (stack == null) {
			return;
		}
		List<String> modes = MuseItemUtils.getModes(stack, player);
		if (modes.contains(mode)) {
			NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
			itemTag.setString("Mode", mode);
		}
	}

}
