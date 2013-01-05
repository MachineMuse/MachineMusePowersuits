package net.machinemuse.powersuits.client;

import net.machinemuse.general.geometry.MuseRenderer;
import net.machinemuse.powersuits.item.IModularItem;
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
		return true;
	}

	/**
	 * Called to actually render the item. type is as above, item is the item to
	 * render, and data is some extra data depending on the type.
	 * 
	 */
	@Override
	public void renderItem(ItemRenderType type, ItemStack itemStack,
			Object... data) {

		IModularItem item;
		if (itemStack.getItem() instanceof IModularItem) {
			item = (IModularItem) (itemStack.getItem());
		} else {
			return;
		}

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
		switch (item.getItemType()) {
		case PowerArmorHead:
			drawHead(itemStack);
			break;
		case PowerArmorTorso:
			drawTorso(itemStack);
			break;
		case PowerArmorLegs:
			drawLegs(itemStack);
			break;
		case PowerArmorFeet:
			drawFeet(itemStack);
			break;
		case PowerTool:
			drawTool(itemStack);
			break;
		default:
			break;
		}
	}

	public void drawHead(ItemStack itemStack) {
	}

	public void drawTorso(ItemStack itemStack) {
	}

	public void drawLegs(ItemStack itemStack) {
		float z = 1.0F;
		float a = 1F;

		float[] v = {
				2, 1, z,
				14, 1, z,
				15, 15, z,
				9, 15, z,
				8, 5, z,
				7, 15, z,
				1, 15, z
		};

		float[] c = {
				1.0F, 1.0F, 1.0F, a,
				0.1F, 0.1F, 0.1F, a,
				0.1F, 0.1F, 0.1F, a,
				0.7F, 0.7F, 0.7f, a,
				0.8F, 0.8F, 0.8F, a,
				0.9F, 0.9F, 0.9F, a,
				1.0F, 1.0F, 1.0F, a
		};
		int[] i = {
				0, 6, 5,
				0, 5, 4,
				0, 4, 1,
				1, 3, 2,
				1, 4, 3
		};
		MuseRenderer.drawTriangles2D(v, c, i);
	}

	public void drawFeet(ItemStack itemStack) {
		// MuseRenderer.drawRectPrism(0, 16, 0, 16, 0, 16);
	}

	public void drawTool(ItemStack itemStack) {
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
