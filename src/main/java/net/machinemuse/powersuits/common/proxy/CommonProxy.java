package net.machinemuse.powersuits.common.proxy;

import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.MPSItems;
import net.machinemuse.powersuits.common.ModCompatibility;
import net.machinemuse.powersuits.entity.EntityLuxCapacitor;
import net.machinemuse.powersuits.entity.EntityPlasmaBolt;
import net.machinemuse.powersuits.entity.EntitySpinningBlade;
import net.machinemuse.powersuits.event.HarvestEventHandler;
import net.machinemuse.powersuits.event.MovementManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.io.File;

/**
 * Common side of the proxy. Provides functions which
 * the ClientProxy and ServerProxy will override if the behaviour is different for client and
 * server, and keeps some common behaviour.
 *
 * @author MachineMuse
 *
 * Ported to Java by lehjr on 11/14/16.
 */
public abstract class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        File newConfig = new File(event.getModConfigurationDirectory() + "/machinemuse/powersuits.cfg");
        Config.init(new Configuration(newConfig));
        Config.setConfigFolderBase(event.getModConfigurationDirectory());
        MinecraftForge.EVENT_BUS.register(new HarvestEventHandler());
        MinecraftForge.EVENT_BUS.register(new MovementManager());
        MPSItems.populateItems();
        MPSItems.populateComponents();

    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {

    }

    public void registerHandlers(){

    }

    public void registerEvents(){

    }

    public void registerRenderers() {

    }

    public abstract void sendModeChange(int dMode, String newMode);
}