package net.machinemuse.api.moduletrigger;

import net.machinemuse.api.IPowerModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IRightClickModule extends IPowerModule {
    public void onRightClick(EntityPlayer playerClicking, World world, ItemStack item);

    public void onItemUse(
            ItemStack itemStack, EntityPlayer player, World world,
            BlockPos pos,
            EnumFacing side, float hitX, float hitY, float hitZ);

    public boolean onItemUseFirst(
            ItemStack itemStack, EntityPlayer player, World world,
            BlockPos pos,
            EnumFacing side, float hitX, float hitY, float hitZ);

    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4);
}
