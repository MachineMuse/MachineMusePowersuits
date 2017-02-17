package net.machinemuse.powersuits.common.proxy;

import cpw.mods.fml.common.FMLCommonHandler;
import net.machinemuse.powersuits.event.PlayerLoginHandlerThingy;
import net.machinemuse.powersuits.event.PlayerUpdateHandler;
import net.minecraftforge.common.MinecraftForge;

/**
 * Server side of the proxy.
 *
 * @author MachineMuse
 *
 * Ported to Java by lehjr on 11/14/16.
 */
public class ServerProxy extends CommonProxy{
    @Override
    public void registerEvents() {
        super.registerEvents();
        FMLCommonHandler.instance().bus().register(new PlayerLoginHandlerThingy());
    }

    @Override
    public void registerHandlers() {
        MinecraftForge.EVENT_BUS.register(new PlayerUpdateHandler());
    }
}