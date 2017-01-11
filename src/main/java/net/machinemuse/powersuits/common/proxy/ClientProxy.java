package net.machinemuse.powersuits.common.proxy;

import net.machinemuse.general.sound.SoundDictionary;
import net.machinemuse.numina.network.MusePacket;
import net.machinemuse.numina.network.MusePacketModeChangeRequest;
import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.numina.render.RenderGameOverlayEventHandler;
import net.machinemuse.powersuits.block.BlockTinkerTable;
import net.machinemuse.powersuits.block.TileEntityLuxCapacitor;
import net.machinemuse.powersuits.block.TileEntityTinkerTable;
import net.machinemuse.powersuits.client.render.block.RenderLuxCapacitorTESR;
import net.machinemuse.powersuits.client.render.block.TinkerTableRenderer;
import net.machinemuse.powersuits.client.render.entity.EntityRendererLuxCapacitorEntity;
import net.machinemuse.powersuits.client.render.entity.EntityRendererPlasmaBolt;
import net.machinemuse.powersuits.client.render.entity.EntityRendererSpinningBlade;
import net.machinemuse.powersuits.client.render.item.TileEntityItemRenderer;
import net.machinemuse.powersuits.client.render.modelspec.ModelSpecXMLReader;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.MPSItems;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.machinemuse.powersuits.control.KeybindKeyHandler;
import net.machinemuse.powersuits.control.KeybindManager;
import net.machinemuse.powersuits.entity.EntityLuxCapacitor;
import net.machinemuse.powersuits.entity.EntityPlasmaBolt;
import net.machinemuse.powersuits.entity.EntitySpinningBlade;
import net.machinemuse.powersuits.event.ClientTickHandler;
import net.machinemuse.powersuits.event.ModelBakeEventHandler;
import net.machinemuse.powersuits.event.PlayerUpdateHandler;
import net.machinemuse.powersuits.event.RenderEventHandler;
import net.machinemuse.powersuits.item.ItemComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.animation.AnimationTESR;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.animation.Event;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.io.IOException;
import java.net.URL;

//import net.machinemuse.powersuits.client.render.item.ToolRenderer;

/**
 * Client side of the proxy.
 *
 * @author MachineMuse
 *
 * Ported to Java by lehjr on 11/14/16.
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        OBJLoader.INSTANCE.addDomain(ModularPowersuits.MODID.toLowerCase());
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTinkerTable.class, new TinkerTableRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLuxCapacitor.class, new RenderLuxCapacitorTESR());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        KeybindManager.readInKeybinds();
    }

    @Override
    public void registerHandlers() {
        super.registerHandlers();
        MinecraftForge.EVENT_BUS.register(new KeybindKeyHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerUpdateHandler());
        MinecraftForge.EVENT_BUS.register(new ClientTickHandler());
    }

    @Override
    public void registerEvents() {
        super.registerEvents();
        MinecraftForge.EVENT_BUS.register(new SoundDictionary());
        MinecraftForge.EVENT_BUS.register(new RenderEventHandler());
        MinecraftForge.EVENT_BUS.register(ModelBakeEventHandler.getInstance());
    }

    @Override
    public void registerRenderers() {
        super.registerRenderers();

        regRenderer(MPSItems.powerTool);
        regRenderer(MPSItems.powerArmorHead);
        regRenderer(MPSItems.powerArmorTorso);
        regRenderer(MPSItems.powerArmorLegs);
        regRenderer(MPSItems.powerArmorFeet);

        Item components = MPSItems.components;
        if (components != null) {
            for (Integer  meta : ((ItemComponent)components).names.keySet()) {
                String oredictName = ((ItemComponent)components).names.get(meta);
                ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(Config.RESOURCE_PREFIX + oredictName, "inventory");
                ModelLoader.setCustomModelResourceLocation(components, meta, itemModelResourceLocation);

                OreDictionary.registerOre(oredictName, new ItemStack(components, 1, meta));
            }
        }

        // TODO, eliminate as much TESR dependency as possible.
        regRenderer(Item.getItemFromBlock(MPSItems.tinkerTable));
        GameRegistry.registerTileEntity(TileEntityTinkerTable.class, "TinkerTable");
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(MPSItems.tinkerTable), 0, TileEntityTinkerTable.class);

        // TODO, eliminate as much TESR dependency as possible.
        regRenderer(Item.getItemFromBlock(MPSItems.luxCapacitor));
        GameRegistry.registerTileEntity(TileEntityLuxCapacitor.class, "LuxCapacitor");
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(MPSItems.luxCapacitor), 0, TileEntityLuxCapacitor.class);

        RenderingRegistry.registerEntityRenderingHandler(EntitySpinningBlade.class, EntityRendererSpinningBlade::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPlasmaBolt.class, EntityRendererPlasmaBolt::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityLuxCapacitor.class, EntityRendererLuxCapacitorEntity::new);
        // TODO Lux Capacitor as an Entity

        URL resource = ClientProxy.class.getResource("/assets/powersuits/models/modelspec.xml");
        ModelSpecXMLReader.getINSTANCE().parseFile(resource);
        URL otherResource = ClientProxy.class.getResource("/assets/powersuits/models/armor2.xml");
        ModelSpecXMLReader.getINSTANCE().parseFile(otherResource);

    }

    @Override
    public void sendModeChange(int dMode, String newMode) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        RenderGameOverlayEventHandler.updateSwap((int) Math.signum(dMode));
        MusePacket modeChangePacket = new MusePacketModeChangeRequest(player, newMode, player.inventory.currentItem);
        PacketSender.sendToServer(modeChangePacket);
    }

    private void regRenderer(Item item) {
        ModelResourceLocation location =  new ModelResourceLocation(item.getRegistryName(), "inventory");

        System.out.println("location is: " + location.toString());
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

//    public void registerRenderers() {

////        ModelLoader.setCustomModelResourceLocation(mpsItems.powerTool, 0, ModelLocations.powerFistGUI);
//
//
//
////
////        ClientRegistry.registerTileEntity(TileEntityTinkerTable.class,
////                ModularPowersuits.MODID + ":tinkerTable", new TinkerTableRenderer());
//

//
//        // TODO: fix all rendering stuff
//
////        MinecraftForgeClient.registerItemRenderer(MPSItems.getInstance().powerTool, new ToolRenderer());
////        int tinkTableRenderID = RenderingRegistry.getNextAvailableRenderId();
////        TinkerTableRenderer tinkTableRenderer = new TinkerTableRenderer(tinkTableRenderID);
////        BlockTinkerTable.setRenderType(tinkTableRenderID);
////        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTinkerTable.class, tinkTableRenderer);
////        RenderingRegistry.registerBlockHandler(tinkTableRenderer);
////        int luxCapacitorRenderID = RenderingRegistry.getNextAvailableRenderId();
////        RenderLuxCapacitorTESR luxCapacitorRenderer = new RenderLuxCapacitorTESR(luxCapacitorRenderID);
////        MPSItems.getInstance().INSTANCE.luxCapacitor.setRenderType(luxCapacitorRenderID);
////        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLuxCapacitor.class, luxCapacitorRenderer);
////        RenderingRegistry.registerBlockHandler(luxCapacitorRenderer);
////        RenderingRegistry.registerEntityRenderingHandler(EntityPlasmaBolt.class, new EntityRendererPlasmaBolt());
////        RenderingRegistry.registerEntityRenderingHandler(EntitySpinningBlade.class, new EntityRendererSpinningBlade());

//
//

//
//
//

//    }
//

//
//
//    /**
//     * Register the tick handler (for on-tick behaviour) and packet handler (for
//     * network synchronization and permission stuff).
//     */
//
//    public void registerHandlers() {

//    }
//
//
//

}