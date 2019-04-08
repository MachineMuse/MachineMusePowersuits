package net.machinemuse.powersuits.common;

import net.machinemuse.powersuits.common.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nonnull;

import static net.machinemuse.powersuits.common.ModularPowersuits.MODID;
import static net.machinemuse.powersuits.common.ModularPowersuits.VERSION;

/**
 * Modular Powersuits
 *
 * @author MachineMuse
 * <p>
 * Ported to Java by lehjr on 11/14/16.
 */
@Mod(modid = MODID, version = VERSION, dependencies = "required-after:numina@[@numina_version@,)")
public enum ModularPowersuits {
    INSTANCE;

    public static final String MODID = "powersuits";
    public static final String VERSION = "@VERSION@";
    @SidedProxy(clientSide = "net.machinemuse.powersuits.common.proxy.ClientProxy", serverSide = "net.machinemuse.powersuits.common.proxy.CommonProxy")
    public static CommonProxy proxy;
    public static Configuration config = null;

    static {
        FluidRegistry.enableUniversalBucket();
    }

    @Nonnull
    @Mod.InstanceFactory
    public static ModularPowersuits getInstance() {
        return INSTANCE;
    }

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
