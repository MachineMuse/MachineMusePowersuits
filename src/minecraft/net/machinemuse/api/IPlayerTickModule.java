package net.machinemuse.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IPlayerTickModule extends IPowerModule {
	public void onPlayerTickActive(EntityPlayer player, ItemStack item);

	public void onPlayerTickInactive(EntityPlayer player, ItemStack item);
}
