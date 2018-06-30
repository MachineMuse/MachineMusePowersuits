package net.machinemuse.powersuits.common.proxy;

import api.player.model.ModelPlayerAPI;
import net.machinemuse.general.sound.SoundDictionary;
import net.machinemuse.numina.network.MusePacket;
import net.machinemuse.numina.network.MusePacketModeChangeRequest;
import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.block.BlockTinkerTable;
import net.machinemuse.powersuits.block.TileEntityTinkerTable;
import net.machinemuse.powersuits.client.model.obj.OBJPlusLoader;
import net.machinemuse.powersuits.client.render.block.TinkerTableRenderer;
import net.machinemuse.powersuits.client.render.entity.EntityRendererLuxCapacitorEntity;
import net.machinemuse.powersuits.client.render.entity.EntityRendererPlasmaBolt;
import net.machinemuse.powersuits.client.render.entity.EntityRendererSpinningBlade;
import net.machinemuse.powersuits.client.render.item.SMovingArmorModel;
import net.machinemuse.powersuits.client.render.model.ModelLuxCapacitor;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.MPSItems;
import net.machinemuse.powersuits.common.ModCompatibility;
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
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import static net.machinemuse.powersuits.common.ModularPowersuits.MODID;

//import net.machinemuse.numina.render.RenderGameOverlayEventHandler;

/**
 * Client side of the proxy.
 *
 * @author MachineMuse
 *
 * Ported to Java by lehjr on 11/14/16.
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        ModelLoaderRegistry.registerLoader(OBJPlusLoader.INSTANCE);
        OBJPlusLoader.INSTANCE.addDomain(MODID.toLowerCase());
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTinkerTable.class, new TinkerTableRenderer());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        KeybindManager.readInKeybinds();
    }

    @Override
    public void registerEvents() {
        super.registerEvents();
        MinecraftForge.EVENT_BUS.register(new KeybindKeyHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerUpdateHandler());
        MinecraftForge.EVENT_BUS.register(new ClientTickHandler());
        MinecraftForge.EVENT_BUS.register(new SoundDictionary());
        MinecraftForge.EVENT_BUS.register(new RenderEventHandler());
        MinecraftForge.EVENT_BUS.register(ModelBakeEventHandler.getInstance());
    }

    @Override
    public void registerRenderers() {
        super.registerRenderers();
//        regRenderer(MPSItems.INSTANCE.powerFist);
//        regRenderer(MPSItems.INSTANCE.powerArmorHead);
//        regRenderer(MPSItems.INSTANCE.powerArmorTorso);
//        regRenderer(MPSItems.INSTANCE.powerArmorLegs);
//        regRenderer(MPSItems.INSTANCE.powerArmorFeet);
//
//        Item components = MPSItems.components;
//        if (components != null) {
//            for (Integer  meta : ((ItemComponent)components).names.keySet()) {
//                String oredictName = ((ItemComponent)components).names.get(meta);
//                ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(Config.COMPONENTS_PREFIX + oredictName, "inventory");
//                ModelLoader.setCustomModelResourceLocation(components, meta, itemModelResourceLocation);
//                OreDictionary.registerOre(oredictName, new ItemStack(components, 1, meta));
//            }
//        }

        regRenderer(Item.getItemFromBlock(BlockTinkerTable.getInstance()));
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(BlockTinkerTable.getInstance()), 0, TileEntityTinkerTable.class);
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlockLuxCapacitor.getInstance()), 0, ModelLuxCapacitor.modelResourceLocation);

        RenderingRegistry.registerEntityRenderingHandler(EntitySpinningBlade.class, EntityRendererSpinningBlade::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPlasmaBolt.class, EntityRendererPlasmaBolt::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityLuxCapacitor.class, EntityRendererLuxCapacitorEntity::new);


        if (ModCompatibility.isRenderPlayerAPILoaded()) {
            ModelPlayerAPI.register(MODID, SMovingArmorModel.class);
        }
    }

    @Override
    public void sendModeChange(int dMode, String newMode) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        MusePacket modeChangePacket = new MusePacketModeChangeRequest(player, newMode, player.inventory.currentItem);
        PacketSender.sendToServer(modeChangePacket);
    }

    private void regRenderer(Item item) {
        ModelResourceLocation location =  new ModelResourceLocation(item.getRegistryName(), "inventory");
        ModelLoader.setCustomModelResourceLocation(item, 0,location);
    }
}