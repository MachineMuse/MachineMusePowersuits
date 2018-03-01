package net.machinemuse.numina.api.module;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;

public interface IModuleManager {
    List<IModule> getAllModules();

    List<IPlayerTickModule> getPlayerTickModules();

    @Nullable
    IModule getModule(String key);

    void addModule(IModule module);

    double computeModularPropertyDouble(ItemStack stack, String propertyName);

    int computeModularPropertyInteger(ItemStack stack, String propertyName);

    List<IRightClickModule> getRightClickModules();

    List<IToggleableModule> getToggleableModules();

    List<IBlockBreakingModule> getBlockBreakingModules();

    List<IModule> getValidModulesForItem(ItemStack stack);

    boolean tagHasModule(NBTTagCompound tag, String moduleName);

    boolean isModuleOnline(NBTTagCompound itemTag, String moduleName);

    void toggleModule(NBTTagCompound itemTag, String name, boolean toggleval);

    boolean itemHasModule(ItemStack stack, String moduleName);

    void tagAddModule(NBTTagCompound tag, IModule module);

    void itemAddModule(ItemStack stack, IModule moduleType);

    boolean removeModule(NBTTagCompound tag, String moduleName);

    boolean removeModule(ItemStack stack, String moduleName);

    boolean itemHasActiveModule(ItemStack itemStack, String moduleName);

    boolean hasCustomInstallCost(String dataName);

    List<ItemStack> getCustomInstallCost(String dataName);

    void addCustomInstallCost(String moduleName, ItemStack stack);
}
