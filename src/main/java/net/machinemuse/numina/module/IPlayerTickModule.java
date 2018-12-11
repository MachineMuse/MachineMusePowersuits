package net.machinemuse.numina.module;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IPlayerTickModule extends IPowerModule {
    void onPlayerTickActive(EntityPlayer player, ItemStack item);

    void onPlayerTickInactive(EntityPlayer player, ItemStack item);
}
