package machinemuse.powersuits.common;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.TickRegistry;

/**
 * Server side of the CommonProxy/ClientProxy paradigm. Provides functions which
 * the ClientProxy will override if the behaviour is different for client and
 * server.
 * 
 * @author MachineMuse
 * 
 */
public class CommonProxy {
	public static String ITEMS_PNG = "/tutorial/generic/items.png";
	public static String BLOCK_PNG = "/tutorial/generic/block.png";

	public static ITickHandler tickHandler;
	public static IPacketHandler packetHandler;

	/**
	 * Only the client needs to register renderers.
	 */
	public void registerRenderers() {
	}

	/**
	 * Register the server-side tickhandler and packethandler.
	 */
	public void registerHandlers() {
		tickHandler = new TickHandler();
		TickRegistry.registerTickHandler(tickHandler, Side.SERVER);

		packetHandler = new ServerPacketHandler();
		NetworkRegistry.instance().registerChannel(packetHandler,
				Config.getNetworkChannelName());
	}
}
