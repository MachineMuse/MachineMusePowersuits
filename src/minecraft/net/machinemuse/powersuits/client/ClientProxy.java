package net.machinemuse.powersuits.client;

import net.machinemuse.powersuits.block.TileEntityTinkerTable;
import net.machinemuse.powersuits.client.render.RenderPlasmaBolt;
import net.machinemuse.powersuits.client.render.TinkerTableRenderer;
import net.machinemuse.powersuits.client.render.ToolRenderer;
import net.machinemuse.powersuits.common.CommonProxy;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.ModCompatability;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.machinemuse.powersuits.entity.EntityPlasmaBolt;
import net.machinemuse.powersuits.event.SoundEventHandler;
import net.machinemuse.powersuits.event.ThaumRenderEventHandler;
import net.machinemuse.powersuits.network.MusePacketHandler;
import net.machinemuse.powersuits.tick.ClientTickHandler;
import net.machinemuse.powersuits.tick.PlayerTickHandler;
import net.machinemuse.powersuits.tick.RenderTickHandler;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * The Client Proxy does all the things that should only be done client-side,
 * like registering client-side handlers and renderers.
 * 
 * @author MachineMuse
 * 
 */
public class ClientProxy extends CommonProxy {
	private static ToolRenderer toolRenderer = new ToolRenderer();
	private static ClientTickHandler clientTickHandler;
	private static RenderTickHandler renderTickHandler;
	private static PlayerTickHandler playerTickHandler;
	public static KeybindKeyHandler keybindHandler;

	/**
	 * Register all the custom renderers for this mod.
	 */
	@Override
	public void registerRenderers() {
		MinecraftForgeClient.registerItemRenderer(ModularPowersuits.powerTool.itemID, toolRenderer);
		int tinkTableRenderID = RenderingRegistry.getNextAvailableRenderId();
		TinkerTableRenderer tinkTableRenderer = new TinkerTableRenderer(tinkTableRenderID);
		ModularPowersuits.tinkerTable.setRenderType(tinkTableRenderID);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTinkerTable.class, tinkTableRenderer);
		RenderingRegistry.registerBlockHandler(tinkTableRenderer);
		RenderingRegistry.registerEntityRenderingHandler(EntityPlasmaBolt.class, new RenderPlasmaBolt());

		MinecraftForgeClient.preloadTexture(Config.SEBK_ICON_PATH);
		MinecraftForgeClient.preloadTexture(Config.WC_ICON_PATH);
		MinecraftForgeClient.preloadTexture(Config.TINKERTABLE_TEXTURE_PATH);
		MinecraftForgeClient.preloadTexture(Config.BLANK_ARMOR_MODEL_PATH);
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
		KeybindManager.readInKeybinds();

		playerTickHandler = new PlayerTickHandler();
		TickRegistry.registerTickHandler(playerTickHandler, Side.CLIENT);
		// TickRegistry.registerTickHandler(playerTickHandler, Side.SERVER);

		renderTickHandler = new RenderTickHandler();
		TickRegistry.registerTickHandler(renderTickHandler, Side.CLIENT);

		clientTickHandler = new ClientTickHandler();
		TickRegistry.registerTickHandler(clientTickHandler, Side.CLIENT);

		MinecraftForge.EVENT_BUS.register(new SoundEventHandler());

		if (ModCompatability.isThaumCraftLoaded() && ModCompatability.enableThaumGogglesModule()) {
			MinecraftForge.EVENT_BUS.register(new ThaumRenderEventHandler());
		}

		packetHandler = new MusePacketHandler().register();
	}

	@Override
	public void postInit() {

	}
}