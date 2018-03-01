package net.machinemuse.powersuits.proxy;

import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.common.MPSGuiHandler;
import net.machinemuse.powersuits.common.ModCompatibility;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.common.config.MPSSettings;
import net.machinemuse.powersuits.entity.EntityLuxCapacitor;
import net.machinemuse.powersuits.entity.EntityPlasmaBolt;
import net.machinemuse.powersuits.entity.EntitySpinningBlade;
import net.machinemuse.powersuits.event.HarvestEventHandler;
import net.machinemuse.powersuits.event.MovementManager;
import net.machinemuse.powersuits.network.packets.MPSPacketList;
import net.machinemuse.powersuits.item.module.tool.TerminalHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import static net.machinemuse.powersuits.api.constants.MPSModConstants.MODID;

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
//        File newConfig = new File(event.getModConfigurationDirectory() + "/machinemuse/powersuits.cfg");
//        Config.init(new Configuration(newConfig));

        MPSConfig.getInstance().setConfigFolderBase(event);

//        Config.setConfigFolderBase(event.getModConfigurationDirectory());
//        Config.extractRecipes();

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
        MPSConfig.getInstance().addCustomInstallCosts();
    }

    public void registerEvents(){
        MinecraftForge.EVENT_BUS.register(new HarvestEventHandler());
        MinecraftForge.EVENT_BUS.register(new MovementManager());
    }

    public void registerRenderers() {}

    public void sendModeChange(int dMode, String newMode) {}
}