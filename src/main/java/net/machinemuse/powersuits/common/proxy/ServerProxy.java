package net.machinemuse.powersuits.common.proxy;

import net.machinemuse.powersuits.event.PlayerLoginHandlerThingy;
import net.machinemuse.powersuits.event.PlayerUpdateHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Server side of the proxy.
 *
 * @author MachineMuse
 *
 * Ported to Java by lehjr on 11/14/16.
 */
public class ServerProxy extends CommonProxy{
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Override
    public void registerHandlers() {
        super.registerHandlers();
        MinecraftForge.EVENT_BUS.register(new PlayerUpdateHandler());
    }

    @Override
    public void registerEvents() {
        super.registerEvents();
        FMLCommonHandler.instance().bus().register(new PlayerLoginHandlerThingy());
    }

    @Override
    public void registerRenderers() {
        super.registerRenderers();
    }

    @Override
    public void sendModeChange(int dMode, String newMode) {}
}