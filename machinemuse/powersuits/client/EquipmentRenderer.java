package machinemuse.powersuits.client;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class EquipmentRenderer implements IItemRenderer {
	private static final boolean useRenderHelper = true;

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return useRenderHelper;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		MuseGui muse = new MuseGui();
		muse.drawCircleAround(8, 8, 8);
	}

}
