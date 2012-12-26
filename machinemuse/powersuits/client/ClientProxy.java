package machinemuse.powersuits.client;

import machinemuse.powersuits.block.TileEntityTinkerTable;
import machinemuse.powersuits.common.CommonProxy;
import machinemuse.powersuits.common.PlayerTickHandler;
import machinemuse.powersuits.common.PowersuitsMod;
import machinemuse.powersuits.network.MusePacketHandler;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * The Client Proxy does all the things that should only be done client-side,
 * like registering client-side handlers and renderers.
 * 
 * @author Claire
 * 
 */
public class ClientProxy extends CommonProxy {
	private static EquipmentRenderer eRenderer = new EquipmentRenderer();

	/**
	 * Register all the custom renderers for this mod.
	 */
	@Override
	public void registerRenderers() {
		for (Item i : PowersuitsMod.allItems) {
			MinecraftForgeClient.registerItemRenderer(
					i.shiftedIndex, eRenderer);
		}
		// for (Item i : PowersuitsMod.allBlocks) {
		// MinecraftForgeClient.registerItemRenderer(
		// i.shiftedIndex, eRenderer);
		// }
		ClientRegistry.bindTileEntitySpecialRenderer(
				TileEntityTinkerTable.class, new TinkerTableRenderer());
		MinecraftForgeClient.preloadTexture("/tinkertable.png");
		MinecraftForgeClient.preloadTexture("/moduleicons.png");
	}

	/**
	 * Register the tick handler (for on-tick behaviour) and packet handler (for
	 * network synchronization and permission stuff).
	 */
	@Override
	public void registerHandlers() {
		tickHandler = new PlayerTickHandler();
		TickRegistry.registerTickHandler(tickHandler, Side.CLIENT);

		packetHandler = new MusePacketHandler().register();
	}
}