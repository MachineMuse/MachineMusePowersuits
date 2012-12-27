/**
 * 
 */
package net.machinemuse.powersuits.network;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

import net.machinemuse.powersuits.item.ItemUtils;
import net.machinemuse.powersuits.trash.ModuleUtils;
import net.machinemuse.powersuits.trash.PowerModule;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
public class MusePacketTinkerRequest extends MusePacket {
	protected ItemStack stack;
	protected int slot;
	protected String moduleName;

	/**
	 * Constructor for sending this packet.
	 * 
	 * @param player
	 *            Player making the request
	 * @param slotToUpgrade
	 *            Slot containing the item for which the upgrade is requested
	 * @param augToUpgrade
	 */
	public MusePacketTinkerRequest(Player player, int slotToUpgrade,
			String augToUpgrade) {
		super(player);
		writeInt(slotToUpgrade);
		writeString(augToUpgrade);
	}

	/**
	 * Constructor for receiving this packet.
	 * 
	 * @param player
	 * @param data
	 * @throws IOException
	 * 
	 */
	public MusePacketTinkerRequest(DataInputStream data, Player player) {
		super(player, data);
		slot = readInt();
		moduleName = readString(64);
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (side == Side.SERVER) {
			EntityPlayerMP srvplayer = (EntityPlayerMP) player;
			stack = srvplayer.inventory.getStackInSlot(slot);
		}
	}

	@Override
	public void handleServer(EntityPlayerMP playerEntity) {
		if (moduleName != null) {
			InventoryPlayer inventory = playerEntity.inventory;
			int entityId = playerEntity.entityId;
			PowerModule moduleType = ModuleUtils.getModuleByID(moduleName);
			NBTTagCompound moduleTag = ModuleUtils.getItemModules(stack)
					.getCompoundTag(
							moduleName);
			List<ItemStack> cost = moduleType.getCost(playerEntity,
					moduleTag);
			if (ItemUtils.hasInInventory(cost, inventory)) {
				List<Integer> slots = ItemUtils.deleteFromInventory(
						cost, inventory);
				moduleType.onUpgrade(playerEntity, moduleTag);
				slots.add(this.slot);
				for (Integer slotiter : slots) {
					MusePacket reply = new MusePacketInventoryRefresh(
							player,
							slotiter, inventory.getStackInSlot(slotiter));
					PacketDispatcher.sendPacketToPlayer(reply.getPacket(),
							player);
				}
			}
		}
	}

	@Override
	public void handleClient(EntityClientPlayerMP player) {
		// TODO Auto-generated method stub

	}

}
