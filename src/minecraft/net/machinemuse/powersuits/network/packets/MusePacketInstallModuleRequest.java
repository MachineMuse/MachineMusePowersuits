/**
 * 
 */
package net.machinemuse.powersuits.network.packets;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.item.ItemUtils;
import net.machinemuse.powersuits.network.MusePacket;
import net.machinemuse.powersuits.powermodule.ModuleManager;
import net.machinemuse.powersuits.powermodule.PowerModule;
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
public class MusePacketInstallModuleRequest extends MusePacket {
	protected ItemStack stack;
	protected int itemSlot;
	protected String moduleName;

	/**
	 * Constructor for sending this packet.
	 * 
	 * @param player
	 *            Player making the request
	 * @param itemSlot
	 *            Slot containing the item for which the upgrade is requested
	 * @param moduleName
	 */
	public MusePacketInstallModuleRequest(Player player, int itemSlot,
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
	public MusePacketInstallModuleRequest(DataInputStream data, Player player) {
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
			PowerModule moduleType = ModuleManager.getModule(moduleName);
			List<ItemStack> cost = moduleType.getInstallCost();

			if (ItemUtils.hasInInventory(cost, playerEntity.inventory) || playerEntity.capabilities.isCreativeMode) {
				ItemUtils.itemAddModule(stack, moduleType);

				List<Integer> slotsToUpdate = new ArrayList();
				if (!playerEntity.capabilities.isCreativeMode) {
					slotsToUpdate = ItemUtils.deleteFromInventory(cost, inventory);
					for (ItemStack itemCost : cost) {
						double joules = ItemUtils.getAsModular(stack.getItem()).getJoules(stack);
						if (ItemUtils.isSameItem(itemCost, ItemComponent.lvcapacitor)) {
							ItemUtils.getAsModular(stack.getItem()).setJoules(joules + 20000, stack);
						}
						if (ItemUtils.isSameItem(itemCost, ItemComponent.mvcapacitor)) {
							ItemUtils.getAsModular(stack.getItem()).setJoules(joules + 100000, stack);
						}
						if (ItemUtils.isSameItem(itemCost, ItemComponent.hvcapacitor)) {
							ItemUtils.getAsModular(stack.getItem()).setJoules(joules + 750000, stack);
						}
					}
				}
				slotsToUpdate.add(itemSlot);
				for (Integer slotiter : slotsToUpdate) {
					MusePacket reply = new MusePacketInventoryRefresh(
							player,
							slotiter, inventory.getStackInSlot(slotiter));
					PacketDispatcher.sendPacketToPlayer(reply.getPacket250(),
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
