package net.machinemuse.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IRightClickModule extends IPowerModule {
	public void onRightClick(EntityPlayer playerClicking, World world, ItemStack item);
}
