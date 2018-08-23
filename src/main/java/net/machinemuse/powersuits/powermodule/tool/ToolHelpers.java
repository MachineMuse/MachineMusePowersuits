package net.machinemuse.powersuits.powermodule.tool;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

import java.util.Objects;
import java.util.Set;

/**
 * Helper methods for the tool classes. Gets rid of multiple copies of similar code.
 *  by lehjr on 11/27/16.
 */
public class ToolHelpers {
    public static boolean canHarvestBlock(ItemStack stack, IBlockState state, EntityPlayer player) {
        String tool = state.getBlock().getHarvestTool(state);
        if (stack.isEmpty() || tool == null) return false;
        return stack.getItem().getHarvestLevel(stack, tool, null, null) >= state.getBlock().getHarvestLevel(state);
    }

    public static boolean isEffectiveTool(IBlockState state, ItemStack emulatedTool) {
        String effectiveTool = state.getBlock().getHarvestTool(state);
        Set<String> emulatedToolClass = emulatedTool.getItem().getToolClasses(emulatedTool);

        // this should be enough but nooooo, stairs still don't work here;
        for (String tool : emulatedToolClass) {
            if (state.getBlock().isToolEffective(tool, state) || Objects.equals(state.getBlock().getHarvestTool(state), tool))
                return true;
        }

        if (emulatedTool.getItem().canHarvestBlock(state))
            return true;

        if (effectiveTool == null) {
            Item.ToolMaterial material;
            if (emulatedTool.getItem() instanceof ItemTool) {
                material = Item.ToolMaterial.valueOf(((ItemTool) emulatedTool.getItem()).getToolMaterialName());
            } else {
                material = Item.ToolMaterial.IRON;
                }
            if (emulatedTool.getDestroySpeed(state) >= material.getEfficiency())
                return true;
        }
        return false;
    }

    public static boolean blockCheckAndHarvest(EntityPlayer player, World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block == null || block == Blocks.AIR)
            return false;
        if ((block instanceof IShearable || block instanceof BlockFlower || block instanceof BlockBush || block instanceof BlockLeaves)
                && block.canHarvestBlock(world, pos, player) || block == Blocks.SNOW || block == Blocks.SNOW_LAYER) {
            block.harvestBlock(world, player, pos, state, world.getTileEntity(pos), new ItemStack(Items.IRON_SHOVEL));
            world.setBlockToAir(pos);
            return true;
        }
        return false;
    }
}