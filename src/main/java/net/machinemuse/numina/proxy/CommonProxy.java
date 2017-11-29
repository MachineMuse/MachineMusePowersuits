package net.machinemuse.numina.proxy;

import net.machinemuse.numina.common.Numina;
import net.machinemuse.numina.common.events.NuminaPlayerTracker;
import net.machinemuse.numina.network.NuminaPackets;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 11:57 AM, 9/3/13
 *
 * Ported to Java by lehjr on 10/26/16.
 */
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
//        NuminaConfig.init(event);
        Numina.getInstance().configDir = event.getModConfigurationDirectory();
        File recipesFolder = new File(Numina.getInstance().configDir, "machinemuse/recipes");
        recipesFolder.mkdirs();
        recipesFolder.mkdir();
        //MinecraftForge.EVENT_BUS.register(PlayerTickHandler)
        //    MinecraftForge.EVENT_BUS.register(DeathEventHandler)
        //    NetworkRegistry.instance.registerGuiHandler(Numina.getInstance(), NuminaGuiHandler);

    }

    public void init(FMLInitializationEvent event) {
        NuminaPackets.init();
        MinecraftForge.EVENT_BUS.register(new NuminaPlayerTracker());
    }

    public void postInit(FMLPostInitializationEvent event) {

    }
}
