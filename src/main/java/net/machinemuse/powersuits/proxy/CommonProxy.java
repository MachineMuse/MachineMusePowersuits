package net.machinemuse.powersuits.proxy;

import net.machinemuse.powersuits.common.MPSGuiHandler;
import net.machinemuse.powersuits.common.ModCompatibility;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.machinemuse.powersuits.common.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.common.block.BlockTinkerTable;
import net.machinemuse.powersuits.common.config.MPSSettings;
import net.machinemuse.powersuits.common.entity.EntityLuxCapacitor;
import net.machinemuse.powersuits.common.entity.EntityPlasmaBolt;
import net.machinemuse.powersuits.common.entity.EntitySpinningBlade;
import net.machinemuse.powersuits.common.events.HarvestEventHandler;
import net.machinemuse.powersuits.common.events.MovementManager;
import net.machinemuse.powersuits.common.items.modules.tool.TerminalHandler;
import net.machinemuse.powersuits.common.tileentities.TileEntityLuxCapacitor;
import net.machinemuse.powersuits.common.tileentities.TileEntityTinkerTable;
import net.machinemuse.powersuits.network.MPSPacketList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static net.machinemuse.powersuits.common.MPSConstants.MODID;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        MPSSettings.setConfigFolderBase(event);
        MPSSettings.extractRecipes();
        GameRegistry.registerTileEntity(TileEntityTinkerTable.class, BlockTinkerTable.name);
        GameRegistry.registerTileEntity(TileEntityLuxCapacitor.class, BlockLuxCapacitor.name);
    }

    public void init(FMLInitializationEvent event) {
        MPSSettings.loadPowerModules();
        EntityRegistry.registerModEntity(new ResourceLocation(MODID, "entityPlasmaBolt"), EntityPlasmaBolt.class, "entityPlasmaBolt", 2477, ModularPowersuits.getInstance(), 64, 20, true);
        EntityRegistry.registerModEntity(new ResourceLocation(MODID, "entitySpinningBlade"), EntitySpinningBlade.class, "entitySpinningBlade", 2478, ModularPowersuits.getInstance(), 64, 20, true);
        EntityRegistry.registerModEntity(BlockLuxCapacitor.getInstance().getRegistryName(), EntityLuxCapacitor.class, "entityLuxCapacitor", 2479, ModularPowersuits.getInstance(), 64, 20, true);

        MPSPacketList.registerPackets();
        NetworkRegistry.INSTANCE.registerGuiHandler(ModularPowersuits.getInstance(), MPSGuiHandler.getInstance());
        TerminalHandler.registerHandler();
    }

    public void postInit(FMLPostInitializationEvent event) {
        ModCompatibility.registerModSpecificModules();
        MPSSettings.addCustomInstallCosts();
    }

    public void registerEvents() {
        MinecraftForge.EVENT_BUS.register(new HarvestEventHandler());
        MinecraftForge.EVENT_BUS.register(new MovementManager());
    }
}