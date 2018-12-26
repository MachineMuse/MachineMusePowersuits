package net.machinemuse.numina.module;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This is only an IRightClickModule so that it can be selected and activated and deselected and deactivated like one.
 * No right click functionality is actually being used.
 */
public interface IMiningEnhancementModule extends IRightClickModule {
    boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player);

    @Override
    default EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_MINING_ENHANCEMENT;
    }

    @Override
    default ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    default EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.PASS;
    }

    @Override
    default EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return EnumActionResult.PASS;
    }

    @Override
    default void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
    }
}