package machinemuse.powersuits.client;

import machinemuse.powersuits.common.CommonProxy;
import machinemuse.powersuits.common.PowersuitsMod;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

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

}