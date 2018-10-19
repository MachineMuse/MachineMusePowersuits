package net.machinemuse.powersuits.common;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.machinemuse.powersuits.common.proxy.CommonProxy;

import javax.annotation.Nonnull;

import static net.machinemuse.powersuits.common.ModularPowersuits.MODID;
import static net.machinemuse.powersuits.common.ModularPowersuits.VERSION;

/**
 * Main mod class. This is what Forge loads to get the mod up and running, both
 * server- and client-side.
 *
 * @author MachineMuse
 *
 * Ported to Java by lehjr on 11/14/16.
 */
@Mod(modid = MODID, version = VERSION, dependencies = "required-after:numina@[@numina_version@,), ae2wct")
public final class ModularPowersuits {
    public static final String MODID = "powersuits";
    public static final String VERSION = "@VERSION@";

    @Nonnull
    private static ModularPowersuits INSTANCE;

    @Nonnull
    @Mod.InstanceFactory
    public static ModularPowersuits getInstance() {
        if (INSTANCE == null)
            INSTANCE = new ModularPowersuits();
        return INSTANCE;
    }

    @SidedProxy(clientSide = "net.machinemuse.powersuits.common.proxy.ClientProxy", serverSide = "net.machinemuse.powersuits.common.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}