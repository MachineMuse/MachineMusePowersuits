/**
 * 
 */
package machinemuse.powersuits.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import machinemuse.powersuits.common.MuseLogger;
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
			int augToUpgrade) {
		super(player);
		DataOutputStream data = wrapNewPacket(MusePacketHandler.PacketTypes.UpgradeRequest);
		try {
			data.write(slotToUpgrade);
			data.write(augToUpgrade);
		} catch (IOException e) {
			MuseLogger.logError("PROBLEM WRITING PACKET TO SEND D:");
			e.printStackTrace();
		}
		endWrapPacket();
	}

	/**
	 * Constructor for receiving this packet.
	 * 
	 * @param player
	 * @param data
	 * 
	 */
	public MusePacketUpgradeRequest(DataInputStream data, Player player) {
		super(player);
		try {
			int slotToUpgrade = data.readInt();
			int augToUpgrade = data.readInt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void handleSelf() {
		// TODO Auto-generated method stub

	}

}
