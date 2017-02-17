package net.machinemuse.powersuits.common.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
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
import net.machinemuse.powersuits.client.render.entity.RenderLuxCapacitorEntity;
import net.machinemuse.powersuits.client.render.entity.RenderPlasmaBolt;
import net.machinemuse.powersuits.client.render.entity.RenderSpinningBlade;
import net.machinemuse.powersuits.client.render.item.ToolRenderer;
import net.machinemuse.powersuits.client.render.modelspec.ModelSpecXMLReader;
import net.machinemuse.powersuits.common.MPSItems;
import net.machinemuse.powersuits.control.KeybindKeyHandler;
import net.machinemuse.powersuits.control.KeybindManager;
import net.machinemuse.powersuits.entity.EntityLuxCapacitor;
import net.machinemuse.powersuits.entity.EntityPlasmaBolt;
import net.machinemuse.powersuits.entity.EntitySpinningBlade;
import net.machinemuse.powersuits.event.ClientTickHandler;
import net.machinemuse.powersuits.event.PlayerUpdateHandler;
import net.machinemuse.powersuits.event.RenderEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import java.net.URL;

/**
 * Client side of the proxy.
 *
 * @author MachineMuse
 *
 * Ported to Java by lehjr on 11/14/16.
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void registerEvents() {
        super.registerEvents();
        MinecraftForge.EVENT_BUS.register(new SoundDictionary());
    }

    /**
     * Register all the custom renderers for this mod.
     */
    @Override
    public void registerRenderers() {
        super.registerRenderers();
        MinecraftForgeClient.registerItemRenderer(MPSItems.getInstance().powerTool, new ToolRenderer());
        int tinkTableRenderID = RenderingRegistry.getNextAvailableRenderId();
        TinkerTableRenderer tinkTableRenderer = new TinkerTableRenderer(tinkTableRenderID);
        BlockTinkerTable.setRenderType(tinkTableRenderID);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTinkerTable.class, tinkTableRenderer);
        RenderingRegistry.registerBlockHandler(tinkTableRenderer);
        int luxCapacitorRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderLuxCapacitorTESR luxCapacitorRenderer = new RenderLuxCapacitorTESR(luxCapacitorRenderID);
        MPSItems.getInstance().luxCapacitor.setRenderType(luxCapacitorRenderID);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLuxCapacitor.class, luxCapacitorRenderer);
        RenderingRegistry.registerBlockHandler(luxCapacitorRenderer);
        RenderingRegistry.registerEntityRenderingHandler(EntityPlasmaBolt.class, new RenderPlasmaBolt());
        RenderingRegistry.registerEntityRenderingHandler(EntitySpinningBlade.class, new RenderSpinningBlade());
        RenderingRegistry.registerEntityRenderingHandler(EntityLuxCapacitor.class, new RenderLuxCapacitorEntity());
        MinecraftForge.EVENT_BUS.register(new RenderEventHandler());
        URL resource = ClientProxy.class.getResource("/assets/powersuits/models/modelspec.xml");
        ModelSpecXMLReader.getINSTANCE().parseFile(resource);
        URL otherResource = ClientProxy.class.getResource("/assets/powersuits/models/armor2.xml");
        ModelSpecXMLReader.getINSTANCE().parseFile(otherResource);
    }

    /**
     * Register the tick handler (for on-tick behaviour) and packet handler (for
     * network synchronization and permission stuff).
     */
    @Override
    public void registerHandlers() {
        super.registerHandlers();
        MinecraftForge.EVENT_BUS.register(new PlayerUpdateHandler());
        FMLCommonHandler.instance().bus().register(new KeybindKeyHandler());
        FMLCommonHandler.instance().bus().register(new ClientTickHandler());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        KeybindManager.readInKeybinds();
    }

    @Override
    public void sendModeChange(int dMode, String newMode) {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        RenderGameOverlayEventHandler.updateSwap((int) Math.signum(dMode));
        MusePacket modeChangePacket = new MusePacketModeChangeRequest(player, newMode, player.inventory.currentItem);
        PacketSender.sendToServer(modeChangePacket);
    }
}