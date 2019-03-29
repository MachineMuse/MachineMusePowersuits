package net.machinemuse.numina.basemod;

import net.machinemuse.numina.constants.NuminaConstants;
import net.machinemuse.numina.proxy.CommonProxy;
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

    @SidedProxy(clientSide = "net.machinemuse.numina.proxy.ClientProxy", serverSide = "net.machinemuse.numina.proxy.CommonProxy")
    static CommonProxy proxy;

    @Nonnull
    @Mod.InstanceFactory
    public static Numina getInstance() {
        return INSTANCE;
    }

    @Mod.EventHandler
    private void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    private void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    private void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    private void serverstart(FMLServerStartedEvent event) {
    }
}