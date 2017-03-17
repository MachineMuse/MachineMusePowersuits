package net.machinemuse.powersuits.powermodule.tool;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

import java.util.Set;

/**
 * Helper methods for the tool classes. Gets rid of multiple copies of similar code.
 *  by lehjr on 11/27/16.
 */
public class ToolHelpers {
    public static boolean canHarvestBlock(ItemStack stack, IBlockState state, EntityPlayer player) {
        String tool = state.getBlock().getHarvestTool(state);
        if (stack == null || tool == null) return false;
        return stack.getItem().getHarvestLevel(stack, tool, null, null) >= state.getBlock().getHarvestLevel(state);
    }

    public static boolean isEffectiveTool(IBlockState state, ItemStack emulatedTool) {
        String effectiveTool = state.getBlock().getHarvestTool(state);
        Set<String> emulatedToolClass = emulatedTool.getItem().getToolClasses(emulatedTool);

        // this should be enough but nooooo, stairs still don't work here;
        for (String tool : emulatedToolClass) {
            if (state.getBlock().isToolEffective(tool, state) || state.getBlock().getHarvestTool(state) == tool)
                return true;
        }

        if (emulatedTool.getItem().canHarvestBlock(state))
            return true;

        if (effectiveTool == null) {
            Item.ToolMaterial material;
            if (emulatedTool.getItem() instanceof ItemTool) {
                material = ((ItemTool) emulatedTool.getItem()).getToolMaterial();
            } else {
                material = Item.ToolMaterial.IRON;
            }
            if (emulatedTool.getStrVsBlock(state) >= material.getEfficiencyOnProperMaterial())
                return true;
        }
        return false;
    }
}
