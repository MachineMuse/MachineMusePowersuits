/**
 * 
 */
package net.machinemuse.powersuits.network.packets;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.network.MusePacket;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
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
public class MusePacketSalvageModuleRequest extends MusePacket {
	protected ItemStack stack;
	protected int itemSlot;
	protected String moduleName;

	/**
	 * Constructor for sending this packet.
	 * 
	 * @param player
	 *            Player making the request
	 * @param itemSlot
	 *            Slot containing the item which is requested to be removed
	 * @param moduleName
	 */
	public MusePacketSalvageModuleRequest(Player player, int itemSlot,
			String moduleName) {
		super(player);
		writeInt(itemSlot);
		writeString(moduleName);
	}

	/**
	 * Constructor for receiving this packet.
	 * 
	 * @param player
	 * @param data
	 * @throws IOException
	 * 
	 */
	public MusePacketSalvageModuleRequest(DataInputStream data, Player player) {
		super(data, player);
		itemSlot = readInt();
		moduleName = readString(64);
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (side == Side.SERVER) {
			EntityPlayerMP srvplayer = (EntityPlayerMP) player;
			stack = srvplayer.inventory.getStackInSlot(itemSlot);
		}
	}

	@Override
	public void handleServer(EntityPlayerMP playerEntity) {
		if (moduleName != null) {
			InventoryPlayer inventory = playerEntity.inventory;
			int entityId = playerEntity.entityId;
			IPowerModule moduleType = ModuleManager.getModule(moduleName);
			List<ItemStack> refund = moduleType.getInstallCost();

			if (MuseItemUtils.itemHasModule(stack, moduleName)) {
				Set<Integer> slots = new HashSet<Integer>();
				MuseItemUtils.removeModule(stack, moduleName);
				for (ItemStack refundItem : refund) {
					slots.addAll(MuseItemUtils.giveOrDropItemWithChance(refundItem.copy(), playerEntity, Config.getSalvageChance()));
				}
				slots.add(this.itemSlot);
				for (Integer slotiter : slots) {
					MusePacket reply = new MusePacketInventoryRefresh(player, slotiter, inventory.getStackInSlot(slotiter));
					PacketDispatcher.sendPacketToPlayer(reply.getPacket250(), player);

				}
			}
		}
	}

	@Override
	public void handleClient(EntityClientPlayerMP player) {
		// TODO Auto-generated method stub

	}

}
