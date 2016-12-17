package net.machinemuse.powersuits.common.proxy;

import net.machinemuse.powersuits.event.PlayerLoginHandlerThingy;
import net.machinemuse.powersuits.event.PlayerUpdateHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * Server side of the proxy.
 *
 * @author MachineMuse
 *
 * Ported to Java by lehjr on 11/14/16.
 */
public class ServerProxy implements CommonProxy{

    @Override
    public void preInit() {
        registerEvents();
    }

    @Override
    public void init() {
        registerHandlers();
    }


    public void registerEvents() {
        FMLCommonHandler.instance().bus().register(new PlayerLoginHandlerThingy());
    }

    public void registerHandlers() {
        MinecraftForge.EVENT_BUS.register(new PlayerUpdateHandler());
    }

    @Override
    public void postInit() {

    }

    @Override
    public void sendModeChange(int dMode, String newMode) {

    }
}