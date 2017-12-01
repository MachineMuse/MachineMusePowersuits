package net.machinemuse.powersuits.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static net.machinemuse.powersuits.common.MPSConstants.NAME;


public class PowersuitsCreativeTab extends CreativeTabs {
    public PowersuitsCreativeTab() {
        super(CreativeTabs.getNextID(), NAME);
    }

    @SideOnly(Side.CLIENT)
    public ItemStack getTabIconItem() {
        return new ItemStack(Items.CARROT_ON_A_STICK);
    }
}