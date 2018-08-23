package net.machinemuse.numina.api.module;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;

public interface IBlockBreakingModule extends IPowerModule {
    /**
     * Return true if using the tool allows the block to drop as an item (e.g. diamond pickaxe on obsidian)
     *
     * @param state  IBlockState of block being checked for breakability
     * @param stack  IC2ItemTest being used as a tool
     * @param player Player doing the breaking
     * @return True if the player can harvest the block, false if not
     */
    boolean canHarvestBlock(ItemStack stack, IBlockState state, EntityPlayer player);

//      public float getStrVsBlock(ItemStack stack, IBlockState state); // TODO: use if needed


    boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving);

    void handleBreakSpeed(PlayerEvent.BreakSpeed event);

    ItemStack getEmulatedTool();
}
