package net.machinemuse.numina.api.module;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public interface IMiningEnhancementModule extends IPowerModule {
    boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player);

    @Override
    default EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_MINING_ENHANCEMENT;
    }

    int getEnergyUsage(@Nonnull ItemStack itemStack);
}
