package net.machinemuse.api.moduletrigger;

import net.machinemuse.api.IPowerModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IRightClickModule extends IPowerModule {
    public void onRightClick(EntityPlayer playerClicking, World world, ItemStack item);

    public void onItemUse(
            ItemStack itemStack, EntityPlayer player, World world,
            int x, int y, int z,
            int side, float hitX, float hitY, float hitZ);

    public boolean onItemUseFirst(
            ItemStack itemStack, EntityPlayer player, World world,
            int x, int y, int z,
            int side, float hitX, float hitY, float hitZ);

    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4);
}
