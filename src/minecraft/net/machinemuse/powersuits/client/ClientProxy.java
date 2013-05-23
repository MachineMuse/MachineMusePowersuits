package net.machinemuse.powersuits.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import net.machinemuse.general.MuseLogger;
import net.machinemuse.general.sound.SoundLoader;
import net.machinemuse.powersuits.block.TileEntityLuxCapacitor;
import net.machinemuse.powersuits.block.TileEntityTinkerTable;
import net.machinemuse.powersuits.client.render.block.RenderLuxCapacitorTESR;
import net.machinemuse.powersuits.client.render.block.TinkerTableRenderer;
import net.machinemuse.powersuits.client.render.entity.RenderLuxCapacitorEntity;
import net.machinemuse.powersuits.client.render.entity.RenderPlasmaBolt;
import net.machinemuse.powersuits.client.render.entity.RenderSpinningBlade;
import net.machinemuse.powersuits.client.render.item.ToolRenderer;
import net.machinemuse.powersuits.client.render.modelspec.ModelSpecXMLReader;
import net.machinemuse.powersuits.common.CommonProxy;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.ModCompatability;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.machinemuse.powersuits.control.KeybindKeyHandler;
import net.machinemuse.powersuits.control.KeybindManager;
import net.machinemuse.powersuits.entity.EntityLuxCapacitor;
import net.machinemuse.powersuits.entity.EntityPlasmaBolt;
import net.machinemuse.powersuits.entity.EntitySpinningBlade;
import net.machinemuse.powersuits.event.RenderEventHandler;
import net.machinemuse.powersuits.event.ThaumRenderEventHandler;
import net.machinemuse.powersuits.network.MusePacketHandler;
import net.machinemuse.powersuits.tick.ClientTickHandler;
import net.machinemuse.powersuits.tick.PlayerTickHandler;
import net.machinemuse.powersuits.tick.RenderTickHandler;
import net.machinemuse.utils.render.MuseShaders;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import java.net.URL;

/**
 * The Client Proxy does all the things that should only be done client-side,
 * like registering client-side handlers and renderers.
 *
 * @author MachineMuse
 */
public class ClientProxy extends CommonProxy {
    private static ToolRenderer toolRenderer;
    private static ClientTickHandler clientTickHandler;
    private static RenderTickHandler renderTickHandler;
    private static PlayerTickHandler playerTickHandler;
    public static KeybindKeyHandler keybindHandler;

    @Override
    public void registerEvents() {
        MinecraftForge.EVENT_BUS.register(new SoundLoader());
        if (ModCompatability.isThaumCraftLoaded() && ModCompatability.enableThaumGogglesModule()) {
            MinecraftForge.EVENT_BUS.register(new ThaumRenderEventHandler());
        }
    }

    /**
     * Register all the custom renderers for this mod.
     */
    @Override
    public void registerRenderers() {
        toolRenderer = new ToolRenderer();
        MinecraftForgeClient.registerItemRenderer(ModularPowersuits.powerTool.itemID, toolRenderer);

        int tinkTableRenderID = RenderingRegistry.getNextAvailableRenderId();
        TinkerTableRenderer tinkTableRenderer = new TinkerTableRenderer(tinkTableRenderID);
        ModularPowersuits.tinkerTable.setRenderType(tinkTableRenderID);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTinkerTable.class, tinkTableRenderer);
        RenderingRegistry.registerBlockHandler(tinkTableRenderer);

        int luxCapacitorRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderLuxCapacitorTESR luxCapacitorRenderer = new RenderLuxCapacitorTESR(luxCapacitorRenderID);
        ModularPowersuits.luxCapacitor.setRenderType(luxCapacitorRenderID);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLuxCapacitor.class, luxCapacitorRenderer);
        RenderingRegistry.registerBlockHandler(luxCapacitorRenderer);

        RenderingRegistry.registerEntityRenderingHandler(EntityPlasmaBolt.class, new RenderPlasmaBolt());
        RenderingRegistry.registerEntityRenderingHandler(EntitySpinningBlade.class, new RenderSpinningBlade());
        RenderingRegistry.registerEntityRenderingHandler(EntityLuxCapacitor.class, new RenderLuxCapacitorEntity());

        MinecraftForge.EVENT_BUS.register(new RenderEventHandler());

        URL resource = ClientProxy.class.getResource(Config.RESOURCE_PREFIX + "models/modelspec.xml");
        ModelSpecXMLReader.parseFile(resource);
        URL otherResource = ClientProxy.class.getResource(Config.RESOURCE_PREFIX + "models/armor2.xml");
        ModelSpecXMLReader.parseFile(otherResource);

        try {
            MuseShaders.hBlurProgram().program();
            Config.canUseShaders = true;
        } catch (Throwable e) {
            MuseLogger.logException("Loading shaders failed!", e);
        }
//        DefaultModelSpec.loadDefaultModel();
//        ModelSpecXMLWriter.writeRegistry("modelspec.xml");

    }

    /**
     * Register the tick handler (for on-tick behaviour) and packet handler (for
     * network synchronization and permission stuff).
     */
    @Override
    public void registerHandlers() {
        super.registerHandlers();
        keybindHandler = new KeybindKeyHandler();
        KeyBindingRegistry.registerKeyBinding(keybindHandler);

        playerTickHandler = new PlayerTickHandler();
        TickRegistry.registerTickHandler(playerTickHandler, Side.CLIENT);
        // TickRegistry.registerTickHandler(playerTickHandler, Side.SERVER);

        renderTickHandler = new RenderTickHandler();
        TickRegistry.registerTickHandler(renderTickHandler, Side.CLIENT);

        clientTickHandler = new ClientTickHandler();
        TickRegistry.registerTickHandler(clientTickHandler, Side.CLIENT);


        packetHandler = new MusePacketHandler();
    }

    @Override
    public void postInit() {
        KeybindManager.readInKeybinds();
    }
}