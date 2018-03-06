package net.machinemuse.numina.api.module;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import java.util.List;

public interface IModuleManager {
    List<ItemStack> getAllModules();

    List<ItemStack> getPlayerTickModules();

    @Nonnull
    ItemStack getModule(String key);

    void addModule(@Nonnull ItemStack module);

    double computeModularPropertyDouble(@Nonnull ItemStack stack, String propertyName);

    int computeModularPropertyInteger(@Nonnull ItemStack stack, String propertyName);

    List<ItemStack> getRightClickModules();

    List<ItemStack> getToggleableModules();

    List<ItemStack> getBlockBreakingModules();

    List<ItemStack> getValidModulesForItem(@Nonnull ItemStack stack);

    boolean itemHasModule(@Nonnull ItemStack stack, String moduleName);

    ItemStack itemAddModule(@Nonnull ItemStack stack, @Nonnull ItemStack moduleType);

    NonNullList<ItemStack> removeModule(@Nonnull ItemStack stack, String moduleName);

    boolean itemHasActiveModule(@Nonnull ItemStack itemStack, String moduleName);

    void toggleModuleForPlayer(EntityPlayer player, String name, boolean toggleval);

    NonNullList<ItemStack> getPlayerInstalledModules(EntityPlayer player);

    boolean hasCustomInstallCost(String dataName);

    List<ItemStack> getCustomInstallCost(String dataName);

    void addCustomInstallCost(String moduleName, @Nonnull ItemStack stack);
}
