package net.machinemuse.numina.module;

import net.machinemuse.numina.helper.ToolHelpers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;

import javax.annotation.Nonnull;

public interface IBlockBreakingModule extends IPowerModule {
    default boolean canHarvestBlock(@Nonnull ItemStack stack, IBlockState state, EntityPlayer player, BlockPos pos, int playerEnergy) {
        if (playerEnergy >= this.getEnergyUsage(stack) && ToolHelpers.isToolEffective(player.getEntityWorld(), pos, getEmulatedTool()))
            return true;
        return false;
    }

    boolean onBlockDestroyed(@Nonnull ItemStack itemStack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving, int playerEnergy);

    void handleBreakSpeed(PlayerEvent.BreakSpeed event);

    int getEnergyUsage(@Nonnull ItemStack itemStack);

    @Nonnull
    ItemStack getEmulatedTool();
}