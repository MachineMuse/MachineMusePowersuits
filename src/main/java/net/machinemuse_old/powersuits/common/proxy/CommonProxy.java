package net.machinemuse_old.powersuits.common.proxy;

import net.machinemuse_old.powersuits.common.*;
import net.machinemuse_old.powersuits.entity.EntityLuxCapacitor;
import net.machinemuse_old.powersuits.entity.EntityPlasmaBolt;
import net.machinemuse_old.powersuits.entity.EntitySpinningBlade;
import net.machinemuse_old.powersuits.event.HarvestEventHandler;
import net.machinemuse_old.powersuits.event.MovementManager;
import net.machinemuse_old.powersuits.network.packets.MPSPacketList;
import net.machinemuse_old.powersuits.powermodule.tool.TerminalHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
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
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        File newConfig = new File(event.getModConfigurationDirectory() + "/machinemuse_old/powersuits.cfg");
        Config.init(new Configuration(newConfig));
        Config.setConfigFolderBase(event.getModConfigurationDirectory());
        Config.extractRecipes();
        MPSItems.populateItems();
        MPSItems.populateComponents();
    }

    public void init(FMLInitializationEvent event) {
        Config.loadPowerModules();
        Config.getMaximumArmorPerPiece();
        Config.getMaximumFlyingSpeedmps();
        Config.useMouseWheel();
        Config.useGraphicalMeters();
        Config.getSalvageChance();
        Config.baseMaxHeat();
        Config.allowConflictingKeybinds();
        Config.fontAntiAliasing();
        Config.useCustomFonts();
        Config.glowMultiplier();
        Config.useShaders();
        Config.getWeightCapacity();
        Config.keybindHUDon();
        Config.keybindHUDx();
        Config.toggleModuleSpam();
        EntityRegistry.registerModEntity(EntityPlasmaBolt.class, "entityPlasmaBolt", 2477, ModularPowersuits.getInstance(), 64, 20, true);
        EntityRegistry.registerModEntity(EntitySpinningBlade.class, "entitySpinningBlade", 2478, ModularPowersuits.getInstance(), 64, 20, true);
        EntityRegistry.registerModEntity(EntityLuxCapacitor.class, "entityLuxCapacitor", 2479, ModularPowersuits.getInstance(), 64, 20, true);
        MPSPacketList.registerPackets();
        NetworkRegistry.INSTANCE.registerGuiHandler(ModularPowersuits.getInstance(), MPSGuiHandler.getInstance());
        TerminalHandler.registerHandler();
    }

    public void postInit(FMLPostInitializationEvent event) {
        ModCompatibility.registerModSpecificModules();
        Config.addCustomInstallCosts();
        Config.getConfig().save();
    }

    public void registerEvents(){
        MinecraftForge.EVENT_BUS.register(new HarvestEventHandler());
        MinecraftForge.EVENT_BUS.register(new MovementManager());
    }

    public void registerRenderers() {}

    public void sendModeChange(int dMode, String newMode) {}
}