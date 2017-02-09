package net.machinemuse.powersuits.common.proxy;

import net.machinemuse.powersuits.event.PlayerLoginHandlerThingy;
import net.machinemuse.powersuits.event.PlayerUpdateHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Server side of the proxy.
 *
 * @author MachineMuse
 *
 * Ported to Java by lehjr on 11/14/16.
 */
@SideOnly(Side.SERVER)
public class ServerProxy extends CommonProxy{
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        System.out.println("running here");
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        System.out.println("running here");
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        System.out.println("running here");
        super.postInit(event);
    }

    @Override
    public void registerEvents() {
        System.out.println("running here");
        super.registerEvents();
        MinecraftForge.EVENT_BUS.register(new PlayerUpdateHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerLoginHandlerThingy());
    }

    @Override
    public void registerRenderers() {}

    @Override
    public void sendModeChange(int dMode, String newMode) {}
}