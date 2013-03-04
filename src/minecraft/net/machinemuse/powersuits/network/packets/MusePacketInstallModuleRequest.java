/**
 * 
 */
package net.machinemuse.powersuits.network.packets;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.network.MusePacket;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import universalelectricity.core.implement.IItemElectric;
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
			IPowerModule moduleType = ModuleManager.getModule(moduleName);
			if (moduleType == null || !moduleType.isAllowed()) {
				playerEntity.sendChatToPlayer("Server has disallowed this module. Sorry!");
				return;
			}
			List<ItemStack> cost = moduleType.getInstallCost();

			if ((!MuseItemUtils.itemHasModule(stack, moduleName) && MuseItemUtils.hasInInventory(cost, playerEntity.inventory))
					|| playerEntity.capabilities.isCreativeMode) {
				MuseItemUtils.itemAddModule(stack, moduleType);

				for (ItemStack itemCost : cost) {
					if (stack.getItem() instanceof IItemElectric) {
						IItemElectric elecItem = (IItemElectric) stack.getItem();
						double joules = elecItem.getJoules(stack);
						if (MuseItemUtils.isSameItem(itemCost, ItemComponent.lvcapacitor)) {
							elecItem.setJoules(joules + 20000, stack);
						}
						if (MuseItemUtils.isSameItem(itemCost, ItemComponent.mvcapacitor)) {
							elecItem.setJoules(joules + 100000, stack);
						}
						if (MuseItemUtils.isSameItem(itemCost, ItemComponent.hvcapacitor)) {
							elecItem.setJoules(joules + 750000, stack);
						}
					}
				}
				List<Integer> slotsToUpdate = new ArrayList();
				if (!playerEntity.capabilities.isCreativeMode) {
					slotsToUpdate = MuseItemUtils.deleteFromInventory(cost, inventory);
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
