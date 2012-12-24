/**
 * 
 */
package machinemuse.powersuits.network;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

import machinemuse.powersuits.augmentation.AugManager;
import machinemuse.powersuits.common.MuseLogger;
import machinemuse.powersuits.item.ItemUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet5PlayerInventory;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

/**
 * Packet for requesting to purchase an upgrade. Player-to-server. Server
 * decides whether it is a valid upgrade or not and replies with an associated
 * inventoryrefresh packet.
 * 
 * @author MachineMuse
 * 
 */
public class MusePacketUpgradeRequest extends MusePacket {
	protected ItemStack stack;
	protected int slot;
	protected String aug;

	/**
	 * Constructor for sending this packet.
	 * 
	 * @param player
	 *            Player making the request
	 * @param slotToUpgrade
	 *            Slot containing the item for which the upgrade is requested
	 * @param augToUpgrade
	 */
	public MusePacketUpgradeRequest(Player player, int slotToUpgrade,
			String augToUpgrade) {
		super(player);
		writeInt(slotToUpgrade, data);
		writeString(augToUpgrade, data);
	}

	/**
	 * Constructor for receiving this packet.
	 * 
	 * @param player
	 * @param data
	 * 
	 */
	public MusePacketUpgradeRequest(DataInputStream data, Player player) {
		super(player, data);
		try {
			slot = data.readInt();
			aug = readString(data, 64);
			Side side = FMLCommonHandler.instance().getEffectiveSide();
			if (side == Side.SERVER) {
				EntityPlayerMP srvplayer = (EntityPlayerMP) player;
				stack = srvplayer.inventory
						.getStackInSlot(slot);

			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			MuseLogger.logError("Problem creating new Augmentation D:");
			e.printStackTrace();
		}
	}

	@Override
	public void handleSelf() {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (side == Side.SERVER) {
			if (aug != null) {
				InventoryPlayer inventory = ((EntityPlayerMP) player).inventory;
				int entityId = ((EntityPlayerMP) player).entityId;
				List<ItemStack> cost = AugManager.getInstallCost(aug);
				if (ItemUtils.hasInInventory(cost, inventory)) {
					List<Integer> slots = ItemUtils.deleteFromInventory(
							AugManager.getInstallCost(aug), inventory);
					slots.add(this.slot);
					for (Integer slotiter : slots) {
						PacketDispatcher.sendPacketToPlayer(
								new Packet5PlayerInventory(entityId, slotiter,
										stack), player);
					}
				}
			}
		}
	}
}
