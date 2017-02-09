package net.machinemuse.powersuits.common.proxy;

import com.google.common.collect.Maps;
import net.machinemuse.general.sound.SoundDictionary;
import net.machinemuse.numina.network.MusePacket;
import net.machinemuse.numina.network.MusePacketModeChangeRequest;
import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.numina.render.RenderGameOverlayEventHandler;
import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.block.TileEntityTinkerTable;
import net.machinemuse.powersuits.client.render.block.TinkerTableRenderer;
import net.machinemuse.powersuits.client.render.entity.EntityRendererLuxCapacitorEntity;
import net.machinemuse.powersuits.client.render.entity.EntityRendererPlasmaBolt;
import net.machinemuse.powersuits.client.render.entity.EntityRendererSpinningBlade;
import net.machinemuse.powersuits.client.render.model.LuxCapModelHelper;
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
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.net.URL;
import java.util.LinkedHashMap;

//import net.machinemuse.powersuits.client.render.block.RenderLuxCapacitorTESR;

//import net.machinemuse.powersuits.client.render.item.ToolRenderer;

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
        System.out.println("running here");
        super.preInit(event);
        System.out.println("running here");
        OBJLoader.INSTANCE.addDomain(ModularPowersuits.MODID.toLowerCase());
    }

    @Override
    public void init(FMLInitializationEvent event) {
        System.out.println("running here");
        super.init(event);
        System.out.println("running here");
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTinkerTable.class, new TinkerTableRenderer());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        System.out.println("running here");
        super.postInit(event);
        System.out.println("running here");
        KeybindManager.readInKeybinds();
    }

    @Override
    public void registerEvents() {
        System.out.println("running here");
        super.registerEvents();
        System.out.println("running here");
        MinecraftForge.EVENT_BUS.register(new KeybindKeyHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerUpdateHandler());
        MinecraftForge.EVENT_BUS.register(new ClientTickHandler());
        MinecraftForge.EVENT_BUS.register(new SoundDictionary());
        MinecraftForge.EVENT_BUS.register(new RenderEventHandler());
        MinecraftForge.EVENT_BUS.register(ModelBakeEventHandler.getInstance());
    }

    @Override
    public void registerRenderers() {
        System.out.println("running here");
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
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(MPSItems.tinkerTable), 0, TileEntityTinkerTable.class);

        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(MPSItems.luxCapacitor), 0, LuxCapModelHelper.getInstance().luxCapItemLocation);
        LuxCapModelHelper.getInstance().putLuxCapFameModels(LuxCapModelHelper.getInstance().luxCapItemLocation, null);

//        // This probably isn't needed but ...
//        ModelLoader.setCustomStateMapper(MPSItems.luxCapacitor, new StateMapperBase() {
//            @Override
//            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
//                LinkedHashMap<IProperty<?>, Comparable<?>> mappings = Maps.newLinkedHashMap(state.getProperties());
//                ModelResourceLocation modresloc = new ModelResourceLocation(MPSItems.luxCapacitor.getRegistryName(), this.getPropertyString(mappings));
//                return modresloc;
//            }
//        });

        RenderingRegistry.registerEntityRenderingHandler(EntitySpinningBlade.class, EntityRendererSpinningBlade::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPlasmaBolt.class, EntityRendererPlasmaBolt::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityLuxCapacitor.class, EntityRendererLuxCapacitorEntity::new);

        URL resource = ClientProxy.class.getResource("/assets/powersuits/models/modelspec.xml");
        ModelSpecXMLReader.getINSTANCE().parseFile(resource);
        URL otherResource = ClientProxy.class.getResource("/assets/powersuits/models/armor2.xml");
        ModelSpecXMLReader.getINSTANCE().parseFile(otherResource);
    }

    @Override
    public void sendModeChange(int dMode, String newMode) {
        System.out.println("running here");
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        RenderGameOverlayEventHandler.updateSwap((int) Math.signum(dMode));
        MusePacket modeChangePacket = new MusePacketModeChangeRequest(player, newMode, player.inventory.currentItem);
        PacketSender.sendToServer(modeChangePacket);
    }

    private void regRenderer(Item item) {
        ModelResourceLocation location =  new ModelResourceLocation(item.getRegistryName(), "inventory");

        System.out.println("location is: " + location.toString());
        ModelLoader.setCustomModelResourceLocation(item, 0,location);
    }
}