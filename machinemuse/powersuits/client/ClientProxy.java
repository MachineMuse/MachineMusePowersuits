package machinemuse.powersuits.client;

import machinemuse.powersuits.common.CommonProxy;
import machinemuse.powersuits.common.Config;
import machinemuse.powersuits.common.PowersuitsMod;
import machinemuse.powersuits.common.TickHandler;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.TickRegistry;

public class ClientProxy extends CommonProxy {
	private static EquipmentRenderer eRenderer = new EquipmentRenderer();

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

		MinecraftForgeClient.preloadTexture("/gui/tinktablegui.png");
	}

	public void registerHandlers() {
		tickHandler = new TickHandler();
		TickRegistry.registerTickHandler(tickHandler, Side.CLIENT);

		packetHandler = new ClientPacketHandler();
		NetworkRegistry.instance().registerChannel(packetHandler,
				Config.getNetworkChannelName());
	}
}