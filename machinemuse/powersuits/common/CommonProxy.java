package machinemuse.powersuits.common;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.TickRegistry;

public class CommonProxy {
	public static String ITEMS_PNG = "/tutorial/generic/items.png";
	public static String BLOCK_PNG = "/tutorial/generic/block.png";

	public static ITickHandler tickHandler;
	public static IPacketHandler packetHandler;

	// Client stuff
	public void registerRenderers() {
		// Nothing here as this is the server side proxy
	}

	public void registerHandlers() {
		tickHandler = new TickHandler();
		TickRegistry.registerTickHandler(tickHandler, Side.SERVER);

		packetHandler = new ServerPacketHandler();
		NetworkRegistry.instance().registerChannel(packetHandler,
				Config.getNetworkChannelName());
	}
}
