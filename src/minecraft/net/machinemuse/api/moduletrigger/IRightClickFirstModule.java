package net.machinemuse.api.moduletrigger;

import net.machinemuse.api.IPowerModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IRightClickFirstModule extends IPowerModule {
	public boolean onItemUseFirst(
			ItemStack itemStack, EntityPlayer player, World worldObj,
			int x, int y, int z,
			int side, float hitX, float hitY, float hitZ);
}
