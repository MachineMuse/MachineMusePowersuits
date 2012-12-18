package machinemuse.powersuits.client;

import machinemuse.powersuits.gui.MuseGui;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.IItemRenderer;

public class EquipmentRenderer implements IItemRenderer {
	private static final boolean useRenderHelper = true;

	/**
	 * Forge checks this to see if our custom renderer will handle a certain
	 * type of rendering.
	 * 
	 * type can be:
	 * 
	 * ENTITY - When the item is floating in the world, e.g. after being tossed
	 * or dropped by a mob.
	 * 
	 * INVENTORY - Drawing it on an inventory slot.
	 * 
	 * EQUIPPED - Rendering the item in an entity's hand e.g. endermen.
	 * 
	 * FIRST_PERSON_MAP - Drawing it in the viewing player's hand
	 */
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return useRenderHelper;
	}

	/**
	 * Called to actually render the item. type is as above, item is the item to
	 * render, and data is some extra data depending on the type.
	 * 
	 */
	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		switch (type) {
		case ENTITY:
			RenderBlocks renderEntity = (RenderBlocks) data[0];
			EntityItem entityEntity = (EntityItem) data[1];
			break;
		case INVENTORY:
			RenderBlocks renderInventory = (RenderBlocks) data[0];
			break;
		case EQUIPPED:
			RenderBlocks renderEquipped = (RenderBlocks) data[0];
			EntityLiving entityEquipped = (EntityLiving) data[1];
			break;
		case FIRST_PERSON_MAP:
			EntityPlayer playerFirstPerson = (EntityPlayer) data[0];
			RenderEngine engineFirstPerson = (RenderEngine) data[1];
			MapData mapDataFirstPerson = (MapData) data[2];
			break;
		default:
		}
		MuseGui muse = new MuseGui();
		muse.drawCircleAround(8, 8, 8);
	}

	/**
	 * Whether or not to use the RenderHelper for this item. Helper can be:
	 * 
	 * ENTITY_ROTATION - Isometric rotation, for block items
	 * 
	 * ENTITY_BOBBING - Up-and-down bobbing effect for EntityItem
	 * 
	 * EQUIPPED_BLOCK - Determines if the currently equipped item should be
	 * rendered as a 3D block or as a 2D texture.
	 * 
	 * BLOCK_3D - Determines if the item should equate to a block that has
	 * RenderBlocks.renderItemIn3d return true
	 * 
	 * INVENTORY_BLOCK - Determines if the item should be rendered in GUI
	 * inventory slots as a 3D block or as a 2D texture.
	 */
	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		return false;
	}

}
