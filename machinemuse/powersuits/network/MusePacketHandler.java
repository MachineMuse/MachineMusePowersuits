/**
 * 
 */
package machinemuse.powersuits.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import machinemuse.powersuits.common.MuseLogger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

/**
 * @author MachineMuse
 * 
 */
public class MusePacketHandler implements IPacketHandler {
	public enum PacketTypes {
		UpgradeRequest,
		DowngradeRequest,
		AugData,
		InventoryRefresh;
	}

	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload payload, Player player) {
		MusePacket repackaged = repackage(payload, player);
		if (repackaged != null) {
			repackaged.handleSelf();
		}
	}

	public static MusePacket repackage(Packet250CustomPayload payload,
			Player player) {
		MusePacket repackaged;
		DataInputStream data = new DataInputStream(new ByteArrayInputStream(
				payload.data));
		EntityPlayer target = (EntityPlayer) player;
		PacketTypes packetType;
		try {
			packetType = PacketTypes.values()[data.readInt()];
		} catch (IOException e) {
			MuseLogger.logDebug("PROBLEM READING PACKET TYPE D:");
			e.printStackTrace();
			return null;
		}
		switch (packetType) {
		case UpgradeRequest:
			repackaged = new MusePacketUpgradeRequest(data,
					player);
			break;
		case AugData:
			repackaged = null;
			break;
		case DowngradeRequest:
			repackaged = null;
			break;
		case InventoryRefresh:
			repackaged = null;
			break;
		default:
			repackaged = null;
			break;
		}

		return repackaged;
	}
}
