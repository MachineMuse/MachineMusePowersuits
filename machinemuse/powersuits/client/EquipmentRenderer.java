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

/**
 * Custom renderer for the power armor and tools. Note - this only renders the
 * item as held in hand, in an inventory slot, or sitting on the ground. To
 * render the player's armor is a different interface (not sure yet).
 * 
 * @author MachineMuse
 * 
 */
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
		// Placeholder graphics
		switch (item.getItem().getIconIndex(item)) {
		case 0: // Head
			muse.draw3x3item(
					true, true, true,
					true, false, true,
					false, false, false);
			break;
		case 1: // Torso
			muse.draw3x3item(
					true, false, true,
					true, true, true,
					true, true, true);
			break;
		case 2: // Legs
			muse.draw3x3item(
					true, true, true,
					true, false, true,
					true, false, true);
			break;
		case 3: // Feet
			muse.draw3x3item(
					false, false, false,
					true, false, true,
					true, false, true);
			break;
		case 4: // Tool
			muse.draw3x3item(
					true, true, true,
					false, true, false,
					false, true, false);
			break;
		}
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
