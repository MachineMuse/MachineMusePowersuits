/**
 * 
 */
package net.machinemuse.powersuits.network;

import java.io.DataInputStream;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.Player;

/**
 * @author Claire
 *
 */
public class MusePacketModeChangeRequest extends MusePacket {

	/**
	 * @param player
	 */
	public MusePacketModeChangeRequest(Player player) {
		super(player);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param player
	 * @param data
	 */
	public MusePacketModeChangeRequest(Player player, DataInputStream data) {
		super(player, data);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see net.machinemuse.powersuits.network.MusePacket#handleClient(net.minecraft.client.entity.EntityClientPlayerMP)
	 */
	@Override
	public void handleClient(EntityClientPlayerMP player) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see net.machinemuse.powersuits.network.MusePacket#handleServer(net.minecraft.entity.player.EntityPlayerMP)
	 */
	@Override
	public void handleServer(EntityPlayerMP player) {
		// TODO Auto-generated method stub

	}

}
