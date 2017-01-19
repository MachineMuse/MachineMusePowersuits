package net.machinemuse.powersuits.common;

import net.machinemuse.powersuits.common.proxy.CommonProxy;
import net.machinemuse.powersuits.entity.EntityLuxCapacitor;
import net.machinemuse.powersuits.entity.EntityPlasmaBolt;
import net.machinemuse.powersuits.entity.EntitySpinningBlade;
import net.machinemuse.powersuits.event.HarvestEventHandler;
import net.machinemuse.powersuits.event.MovementManager;
import net.machinemuse.powersuits.network.packets.MPSPacketList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import javax.annotation.Nonnull;

import java.io.File;

import static net.machinemuse.powersuits.common.ModularPowersuits.MODID;
import static net.machinemuse.powersuits.common.ModularPowersuits.VERSION;

//import net.machinemuse.powersuits.powermodule.tool.TerminalHandler;

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

    @SidedProxy(clientSide = "net.machinemuse.powersuits.common.proxy.ClientProxy", serverSide = "net.machinemuse.powersuits.common.proxy.ServerProxy")
    public static CommonProxy proxy;

    public static Configuration config = null;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
        proxy.registerEvents();
        proxy.registerRenderers();
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        EntityRegistry.registerModEntity(EntityPlasmaBolt.class, "entityPlasmaBolt", 2477, this, 64, 20, true);
        EntityRegistry.registerModEntity(EntitySpinningBlade.class, "entitySpinningBlade", 2478, this, 64, 20, true);
        EntityRegistry.registerModEntity(EntityLuxCapacitor.class, "entityLuxCapacitor", 2479, this, 64, 20, true);
        proxy.init(event);




//        proxy.registerHandlers();
//        proxy.registerRenderers();
        MPSPacketList.registerPackets();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, MPSGuiHandler.getInstance());
//        TerminalHandler.registerHandler();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.registerHandlers();
        proxy.postInit(event);
    }
}