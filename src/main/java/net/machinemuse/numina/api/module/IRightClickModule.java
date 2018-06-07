package net.machinemuse.api.moduletrigger;

import net.machinemuse.numina.api.module.IPowerModule;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IRightClickModule extends IPowerModule {
    ActionResult onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand);

    EnumActionResult onItemUse(
            EntityPlayer playerIn, World worldIn,
            BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ);

    EnumActionResult onItemUseFirst(EntityPlayer player, World world,
                                    BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand);

    void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft);
}
