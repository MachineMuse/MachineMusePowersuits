package net.machinemuse.powersuits.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Ported to Java by lehjr on 11/3/16.
 */
public class MPSCreativeTab extends CreativeTabs {
    public MPSCreativeTab() {
        super("powersuits");
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ItemStack createIcon() {
        return new ItemStack(MPSItems.powerArmorHead);
    }
}