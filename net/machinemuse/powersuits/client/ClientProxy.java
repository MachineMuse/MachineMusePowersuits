package net.machinemuse.powersuits.client;

import net.machinemuse.powersuits.block.TileEntityTinkerTable;
import net.machinemuse.powersuits.block.TinkerTableRenderer;
import net.machinemuse.powersuits.common.CommonProxy;
import net.machinemuse.powersuits.network.MusePacketHandler;
import net.machinemuse.powersuits.tick.PlayerTickHandler;
import net.machinemuse.powersuits.tick.RenderTickHandler;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
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
	private static EquipmentRenderer eRenderer = new EquipmentRenderer();
	private static PlayerTickHandler playerTickHandler = new PlayerTickHandler();
	private static RenderTickHandler renderTickHandler = new RenderTickHandler();

	/**
	 * Register all the custom renderers for this mod.
	 */
	@Override
	public void registerRenderers() {
		// for (Item i : PowersuitsMod.allItems) {
		// MinecraftForgeClient.registerItemRenderer(
		// i.shiftedIndex, eRenderer);
		// }
		ClientRegistry.bindTileEntitySpecialRenderer(
				TileEntityTinkerTable.class, new TinkerTableRenderer());
		MinecraftForgeClient.preloadTexture("/tinkertable.png");
		MinecraftForgeClient.preloadTexture("/moduleicons.png");
		MinecraftForgeClient.preloadTexture("/icons.png");
	}

	/**
	 * Register the tick handler (for on-tick behaviour) and packet handler (for
	 * network synchronization and permission stuff).
	 */
	@Override
	public void registerHandlers() {

		TickRegistry.registerTickHandler(playerTickHandler, Side.CLIENT);
		TickRegistry.registerTickHandler(renderTickHandler, Side.CLIENT);

		packetHandler = new MusePacketHandler().register();
	}

	@Override
	public void postInit() {

	}
}