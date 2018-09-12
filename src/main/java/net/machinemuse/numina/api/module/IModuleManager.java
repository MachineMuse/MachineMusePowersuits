package net.machinemuse.numina.api.module;

import net.machinemuse.numina.api.item.IModeChangingItem;
import net.machinemuse.numina.api.item.IModularItem;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.numina.utils.nbt.MuseNBTUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.machinemuse.numina.api.constants.NuminaNBTConstants.TAG_ONLINE;

public interface IModuleManager {
    void addModule(IPowerModule module);

    NonNullList<IPowerModule> getAllModules();

    Map<String, IPowerModule> getModuleMap();

    @Nullable
    IPowerModule getModule(String key);

    default NonNullList<IPowerModule> getModulesOfType(Class<? extends IPowerModule> type) {
        NonNullList<IPowerModule> retList = NonNullList.create();
        for (IPowerModule module : getModuleMap().values()) {
            if (type.isAssignableFrom(module.getClass())){
                retList.add(module);
            }
        }
        return retList;
    }

    /**
     * Call this whenever the getValue changes, such as changing a setting or installing a module
     */
    default double computeModularPropertyDouble (@Nonnull ItemStack stack, String propertyName) {
        return (double) computeModularProperty(stack, propertyName);
    }

    // fixme: this requires sync between logical sides.
    default double getOrSetModularPropertyDouble(@Nonnull ItemStack stack, String propertyName) {
        double propertyValue = 0;
        NBTTagCompound valuesTag = MuseNBTUtils.getMuseValuesTag(stack);
        if (!valuesTag.hasKey(propertyName, Constants.NBT.TAG_DOUBLE)) {
            propertyValue = computeModularPropertyDouble(stack, propertyName);
            if (propertyValue > 0)
            valuesTag.setDouble(propertyName, propertyValue);
        } else {
            propertyValue = valuesTag.getDouble(propertyName);
            if (propertyValue == 0)
                valuesTag.removeTag(propertyName);
        }
        return propertyValue;
    }

    default Object computeModularProperty(@Nonnull ItemStack stack, String propertyName) {
        double propertyValue = 0;
        NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);
        for (IPowerModule module : getAllModules()) {
            if (itemHasActiveModule(stack, module.getDataName())) {
                propertyValue = module.applyPropertyModifiers(itemTag, propertyName, propertyValue);
            }
        }
        return propertyValue;
    }

//    default int computeModularPropertyInteger (@Nonnull ItemStack stack, String propertyName) {
//        return (int) Math.round((double)computeModularProperty(stack, propertyName));
//    }
//
////    // fixme: this requires sync between logical sides.
//    default int getOrSetModularPropertyInteger(@Nonnull ItemStack stack, String propertyName) {
//        int propertyValue = 0;
//        NBTTagCompound valuesTag = MuseNBTUtils.getMuseValuesTag(stack);
//        if (!valuesTag.hasKey(propertyName, Constants.NBT.TAG_INT)) {
//            propertyValue = computeModularPropertyInteger(stack, propertyName);
//            if (propertyValue > 0) {
//                valuesTag.setInteger(propertyName, propertyValue);
//            }
//        } else {
//            propertyValue = valuesTag.getInteger(propertyName);
//            if (propertyValue == 0)
//                valuesTag.removeTag(propertyName);
//        }
//        return propertyValue;
//    }

    default List<IPowerModule> getValidModulesForItem(@Nonnull ItemStack stack) {
        List<IPowerModule> validModules = new ArrayList();
        for (IPowerModule module : getAllModules()) {
            if (module.isValidForItem(stack)) {
                validModules.add(module);
            }
        }
        return validModules;
    }

    default boolean itemHasModule(@Nonnull ItemStack stack, String moduleName) {
        return tagHasModule(MuseNBTUtils.getMuseItemTag(stack), moduleName);
    }

    default boolean tagHasModule(NBTTagCompound tag, String moduleName) {
        return tag.hasKey(moduleName);
    }

    default void tagAddModule(NBTTagCompound tag, IPowerModule module) {
        tag.setTag(module.getDataName(), module.getNewTag());
    }

    default void toggleModule(NBTTagCompound itemTag, String name, boolean toggleval) {
        if (tagHasModule(itemTag, name)) {
            NBTTagCompound moduleTag = itemTag.getCompoundTag(name);
            moduleTag.setBoolean(TAG_ONLINE, toggleval);
        }
    }

    default void toggleModuleForPlayer(EntityPlayer player, String dataName, boolean toggleval) {
        for (ItemStack stack : MuseItemUtils.getModularItemsEquipped(player)) {
            NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);
            toggleModule(itemTag, dataName, toggleval);
        }
    }

    default List<IPowerModule> getItemInstalledModules(@Nonnull ItemStack stack) {
        List<IPowerModule> installedModules = new ArrayList();
        NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);
        for (IPowerModule module : getValidModulesForItem(stack)) {
            if (tagHasModule(itemTag, module.getDataName())) {
                installedModules.add(module);
            }
        }
        return installedModules;
    }

    default List<IPowerModule> getPlayerInstalledModules(EntityPlayer player) {
        List<IPowerModule> installedModules = new ArrayList();
        for (ItemStack stack : MuseItemUtils.getModularItemsEquipped(player)) {
            NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);
            for (IPowerModule module : getValidModulesForItem(stack)) {
                if (tagHasModule(itemTag, module.getDataName())) {
                    installedModules.add(module);
                }
            }
        }
        return installedModules;
    }

    default boolean isModuleOnline(@Nonnull ItemStack itemStack, String moduleName) {
        return isModuleOnline(MuseNBTUtils.getMuseItemTag(itemStack), moduleName);
    }

    default boolean isModuleOnline(NBTTagCompound itemTag, String moduleName) {
        if (tagHasModule(itemTag, moduleName) && !itemTag.getCompoundTag(moduleName).hasKey(TAG_ONLINE)) {
            return true;
        } else if (tagHasModule(itemTag, moduleName) && itemTag.getCompoundTag(moduleName).getBoolean(TAG_ONLINE)) {
            return true;
        }
        return false;
    }

    default void itemAddModule(@Nonnull ItemStack stack, IPowerModule moduleType) {
        tagAddModule(MuseNBTUtils.getMuseItemTag(stack), moduleType);
    }

    default boolean removeModule(NBTTagCompound tag, String moduleName) {
        if (tag.hasKey(moduleName)) {
            tag.removeTag(moduleName);
            return true;
        } else {
            return false;
        }
    }

    default boolean removeModule(@Nonnull ItemStack stack, String moduleName) {
        return removeModule(MuseNBTUtils.getMuseItemTag(stack), moduleName);
    }

    default boolean itemHasActiveModule(@Nonnull ItemStack itemStack, String moduleName) {
        IPowerModule module = getModule(moduleName);
        if (module == null || itemStack.isEmpty() || !module.isAllowed() || !(itemStack.getItem() instanceof IModularItem)) {
            // playerEntity.sendChatToPlayer("Server has disallowed this module. Sorry!");
            return false;
        }
        if (module instanceof IRightClickModule && itemStack.getItem() instanceof IModeChangingItem) {
//             MuseLogger.logDebug("Module: " + moduleName + " vs Mode: " + ((IModeChangingItem) itemStack.getItem()).getActiveMode(itemStack));
            IModeChangingItem item = (IModeChangingItem) itemStack.getItem();

            return moduleName.equals(item.getActiveMode(itemStack));
        } else {
            return isModuleOnline(MuseNBTUtils.getMuseItemTag(itemStack), moduleName);
        }
    }

    NonNullList<ItemStack> getInstallCost(String dataName);

    void addInstallCost(String moduleName, @Nonnull ItemStack installCostList);

    void addInstallCost(String dataName, NonNullList<ItemStack> installCost);

    boolean hasCustomInstallCost(String dataName);

    NonNullList<ItemStack> getCustomInstallCost(String dataName);

    void addCustomInstallCost(String moduleName, @Nonnull ItemStack installCostList);
}