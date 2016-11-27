package net.machinemuse.powersuits.powermodule.tool;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

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



}
