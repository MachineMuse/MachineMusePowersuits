package net.machinemuse.numina.common.proxy;

import net.machinemuse.numina.api.constants.NuminaConstants;
import net.machinemuse.numina.common.Numina;
import net.machinemuse.numina.event.NuminaPlayerTracker;
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
        Numina.INSTANCE.configDir = new File(event.getModConfigurationDirectory(), NuminaConstants.CONFIG_FOLDER);
        File recipesFolder = new File(Numina.getInstance().configDir, "/recipes");

        System.out.println("Numina config dir: " + Numina.getInstance().configDir.getAbsolutePath());





        recipesFolder.mkdirs();
        recipesFolder.mkdir();
    }

    public void init(FMLInitializationEvent event) {
        NuminaPackets.init();
        MinecraftForge.EVENT_BUS.register(new NuminaPlayerTracker());
    }

    public void postInit(FMLPostInitializationEvent event) {

    }
}
