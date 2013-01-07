package net.machinemuse.powersuits.common;

import net.machinemuse.powersuits.network.MusePacketHandler;
import net.machinemuse.powersuits.tick.PlayerTickHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

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
	
	public static ITickHandler tickHandler = new PlayerTickHandler();
	public static MusePacketHandler packetHandler = new MusePacketHandler();
	
	/**
	 * Only the client needs to register renderers.
	 */
	public void registerRenderers() {}
	
	/**
	 * Register the server-side tickhandler and packethandler.
	 */
	public void registerHandlers() {
		TickRegistry.registerTickHandler(tickHandler, Side.SERVER);
		
		packetHandler.register();
	}
	
	public void postInit() {
		// TODO Auto-generated method stub
		
	}
}
