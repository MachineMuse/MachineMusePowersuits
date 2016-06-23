package net.machinemuse.api.moduletrigger;

import net.machinemuse.api.IPowerModule;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;

public interface IBlockBreakingModule extends IPowerModule {
    /**
     * Return true if using the tool allows the block to drop as an item (e.g. diamond pickaxe on obsidian)
     *
     * @param stack  IC2ItemTest being used as a tool
     * @param pos   BlockPos of the block being checked (required for checking tool effectiveness)
     * @param state   IBlockState of the block being checked
     * @param player Player doing the breaking
     * @return True if the player can harvest the block, false if not
     */
    boolean canHarvestBlock(ItemStack stack, BlockPos pos, IBlockState state, EntityPlayer player);

    boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityPlayer player);

    void handleBreakSpeed(PlayerEvent.BreakSpeed event);
}
