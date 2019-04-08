package net.machinemuse.powersuits.basemod;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Ported to Java by lehjr on 11/3/16.
 */
public class MPSCreativeTab extends ItemGroup {
    public MPSCreativeTab() {
        super(ModularPowersuits.MODID);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ItemStack createIcon() {
        Item item = MPSItems.INSTANCE.powerArmorHead;
        if (item == null)
            item = Items.DEBUG_STICK;

        return new ItemStack(item);
    }
}