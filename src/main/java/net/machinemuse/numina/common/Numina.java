package net.machinemuse.numina.common;

import net.machinemuse.numina.capabilities.heat.CapabilityHeat;
import net.machinemuse.numina.common.constants.NuminaConstants;
import net.machinemuse.numina.common.proxy.ClientProxy;
import net.machinemuse.numina.event.NuminaPlayerTracker;
import net.machinemuse.numina.network.NuminaPackets;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;

import javax.annotation.Nonnull;
import java.io.File;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 6:06 AM, 6/18/13
 * <p>
 * Ported to Java by lehjr on 11/15/16.
 */
@Mod(modid = NuminaConstants.MODID, version = NuminaConstants.VERSION, name = NuminaConstants.NAME)
public enum Numina {
    INSTANCE;

    public static File configDir = null;
    @SidedProxy(clientSide = "net.machinemuse.numina.common.proxy.ClientProxy")
    static ClientProxy proxy;

    @Nonnull
    @Mod.InstanceFactory
    public static Numina getInstance() {
        return INSTANCE;
    }

    @Mod.EventHandler
    private void preInit(FMLPreInitializationEvent event) {
        NuminaPackets.registerNuminaPackets();
        CapabilityHeat.register();
        Numina.INSTANCE.configDir = new File(event.getModConfigurationDirectory(), NuminaConstants.CONFIG_FOLDER);
    }

    @Mod.EventHandler
    private void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new NuminaPlayerTracker());
        proxy.init(event);
    }

    @Mod.EventHandler
    private void postInit(FMLPostInitializationEvent event) {
    }

    @Mod.EventHandler
    private void serverstart(FMLServerStartedEvent event) {

    }
}