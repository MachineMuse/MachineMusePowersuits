package net.machinemuse_old.powersuits.common.proxy;

import net.machinemuse_old.general.sound.SoundDictionary;
import net.machinemuse_old.numina.network.MusePacket;
import net.machinemuse_old.numina.network.MusePacketModeChangeRequest;
import net.machinemuse_old.numina.network.PacketSender;
import net.machinemuse_old.powersuits.block.TileEntityTinkerTable;
import net.machinemuse_old.powersuits.client.render.block.TinkerTableRenderer;
import net.machinemuse_old.powersuits.client.render.entity.EntityRendererLuxCapacitorEntity;
import net.machinemuse_old.powersuits.client.render.entity.EntityRendererPlasmaBolt;
import net.machinemuse_old.powersuits.client.render.entity.EntityRendererSpinningBlade;
import net.machinemuse_old.powersuits.client.render.model.LuxCapModelHelper;
import net.machinemuse_old.powersuits.client.render.model.MPSOBJLoader;
import net.machinemuse_old.powersuits.common.Config;
import net.machinemuse_old.powersuits.common.MPSItems;
import net.machinemuse_old.powersuits.common.ModularPowersuits;
import net.machinemuse_old.powersuits.control.KeybindKeyHandler;
import net.machinemuse_old.powersuits.control.KeybindManager;
import net.machinemuse_old.powersuits.entity.EntityLuxCapacitor;
import net.machinemuse_old.powersuits.entity.EntityPlasmaBolt;
import net.machinemuse_old.powersuits.entity.EntitySpinningBlade;
import net.machinemuse_old.powersuits.event.ClientTickHandler;
import net.machinemuse_old.powersuits.event.ModelBakeEventHandler;
import net.machinemuse_old.powersuits.event.PlayerUpdateHandler;
import net.machinemuse_old.powersuits.event.RenderEventHandler;
import net.machinemuse_old.powersuits.item.ItemComponent;
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

//import net.machinemuse_old.numina.render.RenderGameOverlayEventHandler;

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
        ModelLoaderRegistry.registerLoader(MPSOBJLoader.INSTANCE);
        MPSOBJLoader.INSTANCE.addDomain(ModularPowersuits.MODID.toLowerCase());
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
        regRenderer(MPSItems.powerTool);
        regRenderer(MPSItems.powerArmorHead);
        regRenderer(MPSItems.powerArmorTorso);
        regRenderer(MPSItems.powerArmorLegs);
        regRenderer(MPSItems.powerArmorFeet);

        Item components = MPSItems.components;
        if (components != null) {
            for (Integer  meta : ((ItemComponent)components).names.keySet()) {
                String oredictName = ((ItemComponent)components).names.get(meta);
                ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(Config.COMPONENTS_PREFIX + oredictName, "inventory");
                ModelLoader.setCustomModelResourceLocation(components, meta, itemModelResourceLocation);
                OreDictionary.registerOre(oredictName, new ItemStack(components, 1, meta));
            }
        }

        // TODO, eliminate as much TESR dependency as possible.
        regRenderer(Item.getItemFromBlock(MPSItems.tinkerTable));
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(MPSItems.tinkerTable), 0, TileEntityTinkerTable.class);
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(MPSItems.luxCapacitor), 0, LuxCapModelHelper.getInstance().luxCapItemLocation);

//        // TODO: model testing block. Not a permanent addition
//        regRenderer(Item.getItemFromBlock(MPSItems.testBlock));
        RenderingRegistry.registerEntityRenderingHandler(EntitySpinningBlade.class, EntityRendererSpinningBlade::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPlasmaBolt.class, EntityRendererPlasmaBolt::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityLuxCapacitor.class, EntityRendererLuxCapacitorEntity::new);
    }

    @Override
    public void sendModeChange(int dMode, String newMode) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
//        RenderGameOverlayEventHandler.updateSwap((int) Math.signum(dMode));
        MusePacket modeChangePacket = new MusePacketModeChangeRequest(player, newMode, player.inventory.currentItem);
        PacketSender.sendToServer(modeChangePacket);
    }

    private void regRenderer(Item item) {
        ModelResourceLocation location =  new ModelResourceLocation(item.getRegistryName(), "inventory");
        ModelLoader.setCustomModelResourceLocation(item, 0,location);
    }
}
