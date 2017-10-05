package net.machinemuse_old.powersuits.common;

import net.machinemuse_old.powersuits.common.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nonnull;

import static net.machinemuse_old.powersuits.common.ModularPowersuits.MODID;
import static net.machinemuse_old.powersuits.common.ModularPowersuits.VERSION;

/**
 * Main mod class. This is what Forge loads to get the mod up and running, both
 * server- and client-side.
 *
 * @author MachineMuse
 *
 * Ported to Java by lehjr on 11/14/16.
 */
@Mod(modid = MODID, version = VERSION, dependencies = "required-after:numina@[@numina_version@,)")
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

    @SidedProxy(clientSide = "net.machinemuse_old.powersuits.common.proxy.ClientProxy", serverSide = "net.machinemuse_old.powersuits.common.proxy.ServerProxy")
    public static CommonProxy proxy;

    public static Configuration config = null;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
        proxy.registerEvents();
        proxy.registerRenderers();
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
