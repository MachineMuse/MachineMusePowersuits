/**
 * 
 */
package net.machinemuse.powersuits.network.packets;

import java.io.DataInputStream;
import java.io.IOException;

import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.powersuits.network.MusePacket;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

/**
 * Packet for requesting to purchase an upgrade. Player-to-server. Server
 * decides whether it is a valid upgrade or not and replies with an associated
 * inventoryrefresh packet.
 * 
 * @author MachineMuse
 * 
 */
public class MusePacketTweakRequest extends MusePacket {
	protected ItemStack stack;
	protected int itemSlot;
	protected String moduleName;
	protected String tweakName;
	protected double tweakValue;
	
	/**
	 * Constructor for sending this packet.
	 * 
	 * @param player
	 *            Player making the request
	 * @param itemSlot
	 *            Slot containing the item for which the upgrade is requested
	 * @param tinkerName
	 */
	public MusePacketTweakRequest(Player player, int itemSlot,
			String moduleName, String tweakName, double tweakValue) {
		super(player);
		writeInt(itemSlot);
		writeString(moduleName);
		writeString(tweakName);
		writeDouble(tweakValue);
	}
	
	/**
	 * Constructor for receiving this packet.
	 * 
	 * @param player
	 * @param data
	 * @throws IOException
	 * 
	 */
	public MusePacketTweakRequest(DataInputStream data, Player player) {
		super(data, player);
		itemSlot = readInt();
		moduleName = readString(64);
		tweakName = readString(64);
		tweakValue = readDouble();
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (side == Side.SERVER) {
			EntityPlayerMP srvplayer = (EntityPlayerMP) player;
			stack = srvplayer.inventory.getStackInSlot(itemSlot);
		}
	}
	
	@Override public void handleServer(EntityPlayerMP playerEntity) {
		if (moduleName != null && tweakName != null) {
			InventoryPlayer inventory = playerEntity.inventory;
			int entityId = playerEntity.entityId;
			NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
			if (MuseItemUtils.tagHasModule(itemTag, moduleName)) {
				NBTTagCompound moduleTag = itemTag.getCompoundTag(moduleName);
				tweakValue = Math.min(1, Math.max(0, tweakValue));
				moduleTag.setDouble(tweakName, tweakValue);
			}
		}
	}
	
	@Override public void handleClient(EntityClientPlayerMP player) {
		// TODO Auto-generated method stub
		
	}
	
}
