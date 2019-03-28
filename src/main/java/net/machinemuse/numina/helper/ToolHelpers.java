package net.machinemuse.numina.helper;

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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Helper methods for the tool classes. Gets rid of multiple copies of similar code.
 * by lehjr on 11/27/16.
 */
public class ToolHelpers {
    public static boolean isToolEffective(IBlockAccess world, BlockPos pos, @Nonnull ItemStack emulatedTool) {
        IBlockState state = world.getBlockState(pos);
        state = state.getBlock().getActualState(state, world, pos);

        if (state.getBlock().getBlockHardness(state, (World) world, pos) == -1.0F) // unbreakable
            return false;

        if (emulatedTool.getItem().canHarvestBlock(state))
            return true;

        String harvestTool = state.getBlock().getHarvestTool(state);
        if (harvestTool != null) {
            // this should be enough but nooooo, stairs still don't work here;
            for (String type : emulatedTool.getItem().getToolClasses(emulatedTool)) {
                if (state.getBlock().isToolEffective(type, state) || (Objects.equals(harvestTool, type) &&
                        emulatedTool.getItem().getHarvestLevel(emulatedTool, harvestTool, null, null) >= state.getBlock().getHarvestLevel(state)))
                    return true;
            }
        } else {
            Item.ToolMaterial material;
            if (emulatedTool.getItem() instanceof ItemTool)
                material = Item.ToolMaterial.valueOf(((ItemTool) emulatedTool.getItem()).getToolMaterialName());
            else
                material = Item.ToolMaterial.IRON;
            if (emulatedTool.getDestroySpeed(state) >= material.getEfficiency())
                return true;
        }
        return false;
    }

    public static boolean blockCheckAndHarvest(EntityPlayer player, World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block == null || block == Blocks.AIR || block == Blocks.BEDROCK)
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