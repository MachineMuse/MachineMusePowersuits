package net.machinemuse.numina.proxy;

import net.machinemuse.numina.basemod.Numina;
import net.machinemuse.numina.capabilities.heat.CapabilityHeat;
import net.machinemuse.numina.capabilities.player.CapabilityPlayerValues;
import net.machinemuse.numina.constants.NuminaConstants;
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
 * <p>
 * Ported to Java by lehjr on 10/26/16.
 */
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        NuminaPackets.registerNuminaPackets();
        CapabilityHeat.register();
        CapabilityPlayerValues.register();
        Numina.INSTANCE.configDir = new File(event.getModConfigurationDirectory(), NuminaConstants.CONFIG_FOLDER);
    }

    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new NuminaPlayerTracker());
    }

    public void postInit(FMLPostInitializationEvent event) {
    }
}