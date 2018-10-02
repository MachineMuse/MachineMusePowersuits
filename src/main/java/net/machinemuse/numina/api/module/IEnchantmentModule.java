package net.machinemuse.numina.api.module;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import javax.annotation.Nonnull;

public interface IEnchantmentModule extends IPowerModule {
    /**
     silk touch:
    ench: [{lvl: 1 as short, id: 33 as short}]

     Fortune (level 3; comes in 3 lvls)
     <minecraft:enchanted_book>.withTag({StoredEnchantments: [{lvl: 3 as short, id: 35 as short}]})

     */


    /**
     * enable enchantment
     */
    @Nonnull
    default ItemStack addEnchantment(@Nonnull ItemStack itemStack) {
        itemStack.addEnchantment(getEnchantment(), getLevel());
        return itemStack;
    }

    /**
     * disable enchantment
     */
    @Nonnull
    default ItemStack removeEnchantment(@Nonnull ItemStack itemStack) {
        NBTTagList enchantments = itemStack.getEnchantmentTagList();
        if (enchantments == null)
            return itemStack;

        int id = Enchantment.getEnchantmentID(getEnchantment());

        for(int i = 0 ; i < enchantments.tagCount(); i++) {
            NBTTagCompound compound = enchantments.getCompoundTagAt(i);
            if (compound.getShort("id") == Enchantment.getEnchantmentID(getEnchantment()))
                enchantments.removeTag(i);
        }

        if (enchantments.hasNoTags())
           itemStack.getTagCompound().removeTag("ench");
        return itemStack;
    }

    Enchantment getEnchantment();

    short getLevel();
}
