package net.machinemuse.powersuits.proxy;

import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.MPSGuiHandler;
import net.machinemuse.powersuits.common.ModCompatibility;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.machinemuse.powersuits.common.blocks.BlockLuxCapacitor;
import net.machinemuse.powersuits.common.blocks.BlockTinkerTable;
import net.machinemuse.powersuits.common.events.HarvestEventHandler;
import net.machinemuse.powersuits.common.events.MovementManager;
import net.machinemuse.powersuits.common.powermodule.tool.TerminalHandler;
import net.machinemuse.powersuits.common.tileentities.TileEntityLuxCapacitor;
import net.machinemuse.powersuits.common.tileentities.TileEntityTinkerTable;
import net.machinemuse.powersuits.entity.EntityLuxCapacitor;
import net.machinemuse.powersuits.network.packets.MPSPacketList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.File;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        Config.init(new Configuration(new File(event.getModConfigurationDirectory() + "/machinemuse/powersuits.cfg")));
        Config.setConfigFolderBase(event.getModConfigurationDirectory());
        Config.extractRecipes();

        GameRegistry.registerTileEntity(TileEntityTinkerTable.class, BlockTinkerTable.name);
        GameRegistry.registerTileEntity(TileEntityLuxCapacitor.class, BlockLuxCapacitor.name);
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

//        EntityRegistry.registerModEntity(EntityPlasmaBolt.class, "entityPlasmaBolt", 2477, ModularPowersuits.getInstance(), 64, 20, true);
//        EntityRegistry.registerModEntity(EntitySpinningBlade.class, "entitySpinningBlade", 2478, ModularPowersuits.getInstance(), 64, 20, true);
        EntityRegistry.registerModEntity(BlockLuxCapacitor.getInstance().getRegistryName(), EntityLuxCapacitor.class, "entityLuxCapacitor", 2479, ModularPowersuits.getInstance(), 64, 20, true);

        MPSPacketList.registerPackets();
        NetworkRegistry.INSTANCE.registerGuiHandler(ModularPowersuits.getInstance(), MPSGuiHandler.getInstance());
        TerminalHandler.registerHandler();
    }

    public void postInit(FMLPostInitializationEvent event) {
        ModCompatibility.registerModSpecificModules();
        Config.addCustomInstallCosts();
        Config.getConfig().save();
    }

    public void registerEvents() {
        MinecraftForge.EVENT_BUS.register(new HarvestEventHandler());
        MinecraftForge.EVENT_BUS.register(new MovementManager());
    }
}
