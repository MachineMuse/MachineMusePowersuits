package machinemuse.powersuits.trash;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

/**
 * Packet handler for the client side.
 * 
 * @author MachineMuse
 * 
 */
public class ClientPacketHandler implements IPacketHandler {
	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload payload, Player player) {

	}

}
