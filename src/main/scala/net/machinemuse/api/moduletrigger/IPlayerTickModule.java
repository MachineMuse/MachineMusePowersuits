package net.machinemuse.api.moduletrigger;

import net.machinemuse.api.IPowerModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IPlayerTickModule extends IPowerModule {
    public void onPlayerTickActive(EntityPlayer player, ItemStack item);

    public void onPlayerTickInactive(EntityPlayer player, ItemStack item);
}
