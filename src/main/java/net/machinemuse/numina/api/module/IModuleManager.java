package net.machinemuse.numina.api.module;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import java.util.List;

public interface IModuleManager {
    NonNullList<ItemStack> getAllModules();

    NonNullList<ItemStack> getPlayerTickModules();

    @Nonnull
    ItemStack getModule(String key);

    void addModule(@Nonnull ItemStack module);

    double computeModularPropertyDouble(@Nonnull ItemStack stack, String propertyName);

    int computeModularPropertyInteger(@Nonnull ItemStack stack, String propertyName);

    NonNullList<ItemStack> getRightClickModules();

    NonNullList<ItemStack> getToggleableModules();

    NonNullList<ItemStack> getBlockBreakingModules();

    NonNullList<ItemStack> getValidModulesForItem(@Nonnull ItemStack stack);

    boolean itemHasModule(@Nonnull ItemStack stack, String moduleName);

    ItemStack itemAddModule(@Nonnull ItemStack stack, @Nonnull ItemStack moduleType);

    NonNullList<ItemStack> removeModule(@Nonnull ItemStack stack, String moduleName);

    boolean itemHasActiveModule(@Nonnull ItemStack itemStack, String moduleName);

    void toggleModuleForPlayer(EntityPlayer player, String name, boolean toggleval);

    NonNullList<ItemStack> getPlayerInstalledModules(EntityPlayer player);

    boolean hasCustomInstallCost(String dataName);

    NonNullList<ItemStack> getCustomInstallCost(String dataName);

    void addCustomInstallCost(String moduleName, @Nonnull ItemStack stack);
}
