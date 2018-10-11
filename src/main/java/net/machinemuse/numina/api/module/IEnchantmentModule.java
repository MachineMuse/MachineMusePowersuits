package net.machinemuse.numina.api.module;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Map;

public interface IEnchantmentModule extends IPowerModule {
    /**
     * enable enchantment
     */
    @Nonnull
    default ItemStack addEnchantment(@Nonnull ItemStack itemStack) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
        enchantments.put(getEnchantment(), getLevel());
        EnchantmentHelper.setEnchantments(enchantments, itemStack);
        return itemStack;
    }

    /**
     * disable enchantment
     */
    @Nonnull
    default ItemStack removeEnchantment(@Nonnull ItemStack itemStack) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
        enchantments.remove(getEnchantment());
        EnchantmentHelper.setEnchantments(enchantments, itemStack);
        return itemStack;
    }

    Enchantment getEnchantment();

    int getLevel();
}
