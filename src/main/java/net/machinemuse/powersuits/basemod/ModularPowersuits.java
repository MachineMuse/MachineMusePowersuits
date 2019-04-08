package net.machinemuse.powersuits.basemod;

import net.machinemuse.powersuits.proxy.ClientProxy;
import net.machinemuse.powersuits.proxy.CommonProxy;
import net.machinemuse.powersuits.proxy.ServerProxy;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ModularPowersuits.MODID)
public class ModularPowersuits {
    public static final String MODID = "powersuits";

    public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    public ModularPowersuits() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, MPSConfig.INSTANCE.SERVER_SPEC, MPSConfig.INSTANCE.serverFile.getAbsolutePath());
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, MPSConfig.INSTANCE.CLIENT_SPEC, MPSConfig.INSTANCE.clientFile.getAbsolutePath());

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the setupClient method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);

        // Register ourselves for server, registry and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, MPSItems.INSTANCE::registerBlocks);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, MPSItems.INSTANCE::registerItems);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(TileEntityType.class, MPSItems.INSTANCE::registerTileEntities);
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> {
            return (openContainer) -> {
                ResourceLocation location = openContainer.getId();
//                if (location.equals(some resource location here)) {
//                    EntityPlayerSP player = Minecraft.getInstance().player;
//                        return new Gui with params;
//                }
                return null;
            };
        });
    }

    // preInit
    private void setup(final FMLCommonSetupEvent event) {
        proxy.setup(event);
    }

    // client preInit
    private void setupClient(final FMLClientSetupEvent event) {
        proxy.setupClient(event);
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class
    @Mod.EventBusSubscriber
    public static class ServerEvents {
        @SubscribeEvent
        public static void onServerStarting(FMLServerStartingEvent event) {
            // do something when the server starts
//            MuseLogger.logInfo("HELLO from server starting");
        }
    }
}