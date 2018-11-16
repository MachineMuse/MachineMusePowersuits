package net.machinemuse.powersuits.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Ported to Java by lehjr on 11/3/16.
 */
public class MPSCreativeTab extends CreativeTabs {
    public MPSCreativeTab() {
        super("powersuits");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack createIcon() {
        return new ItemStack(MPSItems.powerArmorHead);
    }
}