package net.machinemuse.powersuits.common;

import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import net.machinemuse.powersuits.network.MusePacketHandler;
import net.machinemuse.powersuits.tick.PlayerTickHandler;

import java.net.URL;

/**
 * Common side of the proxy. Provides functions which
 * the ClientProxy and ServerProxy will override if the behaviour is different for client and
 * server, and keeps some common behaviour.
 *
 * @author MachineMuse
 */
public class CommonProxy {
    public static String ITEMS_PNG = "/tutorial/generic/items.png";
    public static String BLOCK_PNG = "/tutorial/generic/block.png";

    public static MusePacketHandler packetHandler;
    public static PlayerTickHandler playerTickHandler;

    public void registerEvents() {
    }

    /**
     * Only the client needs to register renderers.
     */
    public void registerRenderers() {
    }

    /**
     * Register the server-side tickhandler and packethandler.
     */
    public void registerHandlers() {
        playerTickHandler = new PlayerTickHandler();
        TickRegistry.registerTickHandler(playerTickHandler, Side.SERVER);

        packetHandler = new MusePacketHandler();
    }

    public void postInit() {
    }

    public static URL getResource(String url) {
        return CommonProxy.class.getResource(url);
    }
}
