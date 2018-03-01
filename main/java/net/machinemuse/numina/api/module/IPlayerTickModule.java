package net.machinemuse.numina.api.module;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IPlayerTickModule extends IModule {
    void onPlayerTickActive(EntityPlayer player, ItemStack item);

    void onPlayerTickInactive(EntityPlayer player, ItemStack item);
}
