package net.machinemuse.powersuits.client.render.item;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.machinemuse.powersuits.item.ItemPowerGauntlet;
import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.IItemRenderer;

/**
 * Custom renderer for the power armor and tools. Note - this only renders the item as held in hand, in an inventory slot, or sitting on the ground.
 * To render the player's armor is a different interface (not sure yet).
 *
 * @author MachineMuse
 */
public class ToolRenderer extends Render implements IItemRenderer {
    public ToolModel model = new ToolModel();

    /**
     * Forge checks this to see if our custom renderer will handle a certain type of rendering.
     * <p/>
     * type can be:
     * <p/>
     * ENTITY - When the item is floating in the world, e.g. after being tossed or dropped by a mob.
     * <p/>
     * INVENTORY - Drawing it on an inventory slot.
     * <p/>
     * EQUIPPED - Rendering the item in an entity's hand e.g. endermen.
     * <p/>
     * FIRST_PERSON_MAP - Drawing it in the viewing player's hand
     */
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    /**
     * Called to actually render the item. type is as above, item is the item to render, and data is some extra data depending on the type.
     */
    @Override
    public void renderItem(ItemRenderType type, ItemStack itemStack, Object... data) {
        boolean drawIcon = false;
        ItemPowerGauntlet item = (ItemPowerGauntlet) itemStack.getItem();

        Colour colour = item.getColorFromItemStack(itemStack);
        Colour glow = item.getColorFromItemStack(itemStack);
        switch (type) {
            case ENTITY:
                RenderBlocks renderEntity = (RenderBlocks) data[0];
                EntityItem entityEntity = (EntityItem) data[1];
                model.setNeutralPose();

                model.render(null, 1, false, colour, glow);
                break;
            case INVENTORY:
                RenderBlocks renderInventory = (RenderBlocks) data[0];
                MuseRenderer.drawIconAt(0, 0, ModularPowersuits.powerTool.getIconIndex(itemStack), colour);
                break;
            case EQUIPPED:
                RenderBlocks renderEquipped = (RenderBlocks) data[0];
                EntityLiving entityEquipped = (EntityLiving) data[1];

                if (entityEquipped instanceof EntityPlayer) {
                    model.setPoseForPlayer((EntityPlayer) entityEquipped, itemStack);
                } else {
                    model.setNeutralPose();
                }

                model.render(entityEquipped, 1, false, colour, glow);
                break;
            case FIRST_PERSON_MAP:
                EntityPlayer playerFirstPerson = (EntityPlayer) data[0];
                RenderEngine engineFirstPerson = (RenderEngine) data[1];
                MapData mapDataFirstPerson = (MapData) data[2];
                model.setPoseForPlayer(playerFirstPerson, itemStack);

                model.render(playerFirstPerson, 1, true, colour, glow);
                break;
            case EQUIPPED_FIRST_PERSON:
                RenderBlocks renderEquFP = (RenderBlocks) data[0];
                EntityLiving entityEquFP = (EntityLiving) data[1];
                if (entityEquFP instanceof EntityPlayer) {
                    model.setPoseForPlayer((EntityPlayer) entityEquFP, itemStack);
                } else {
                    model.setNeutralPose();
                }

                model.render(entityEquFP, 1, true, colour, glow);
                break;
            default:
        }
    }

    /**
     * Whether or not to use the RenderHelper for this item. Helper can be:
     * <p/>
     * ENTITY_ROTATION - Isometric rotation, for block items
     * <p/>
     * ENTITY_BOBBING - Up-and-down bobbing effect for EntityItem
     * <p/>
     * EQUIPPED_BLOCK - Determines if the currently equipped item should be rendered as a 3D block or as a 2D texture.
     * <p/>
     * BLOCK_3D - Determines if the item should equate to a block that has RenderBlocks.renderItemIn3d return true
     * <p/>
     * INVENTORY_BLOCK - Determines if the item should be rendered in GUI inventory slots as a 3D block or as a 2D texture.
     */
    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return type == ItemRenderType.ENTITY;
    }

    @Override
    public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {

    }

}
