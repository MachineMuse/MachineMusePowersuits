package net.machinemuse.numina.capabilities.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import java.util.List;

/**
 *
 *
 */
public interface IModularItemCapability {
    void toggleModule(String unLocalizedName, boolean online);

    void setModuleTweakInteger(String unLocalizedName, String tweakName, int teakVal);

    void setModuleTweakDouble(String unLocalizedName, String tweakName, double teakVal);

    List<String> getInstalledModuleNames();

    NonNullList<ItemStack> getInstallledModules();

    boolean isModuleInstalled(String unLocalizedName);

    boolean isModuleInstalled(@Nonnull ItemStack module);

    @Nonnull
    ItemStack installModule(@Nonnull ItemStack module);

    @Nonnull
    NonNullList<ItemStack> removeModule(String moduleName);

    boolean isModuleOnline(String unLocalizedName);
}