package net.machinemuse.api;

import net.machinemuse.api.moduletrigger.IBlockBreakingModule;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleManager {

    public static final String ONLINE = "Active";

    protected static final Map<String, IPowerModule> moduleMap = new HashMap<String, IPowerModule>();
    protected static final List<IPowerModule> moduleList = new ArrayList<IPowerModule>();
    protected static final List<IPlayerTickModule> playerTickModules = new ArrayList<IPlayerTickModule>();
    protected static final List<IRightClickModule> rightClickModules = new ArrayList<IRightClickModule>();
    protected static final List<IToggleableModule> toggleableModules = new ArrayList<IToggleableModule>();
    protected static final List<IBlockBreakingModule> blockBreakingModules = new ArrayList<IBlockBreakingModule>();

    public static List<IPowerModule> getAllModules() {
        return moduleList;
    }

    public static List<IPlayerTickModule> getPlayerTickModules() {
        return playerTickModules;
    }

    public static IPowerModule getModule(String key) {
        return moduleMap.get(key);
    }

    public static void addModule(IPowerModule module) {

        moduleMap.put(module.getDataName(), module);
        moduleList.add(module);
        if (module instanceof IPlayerTickModule) {
            playerTickModules.add((IPlayerTickModule) module);
        }
        if (module instanceof IRightClickModule) {
            rightClickModules.add((IRightClickModule) module);
        }
        if (module instanceof IToggleableModule) {
            toggleableModules.add((IToggleableModule) module);
        }
        if (module instanceof IBlockBreakingModule) {
            blockBreakingModules.add((IBlockBreakingModule) module);
        }
    }

    public static double computeModularProperty(ItemStack stack, String propertyName) {
        double propertyValue = 0;
        NBTTagCompound itemTag = MuseItemTag.getMuseItemTag(stack);
        for (IPowerModule module : moduleList) {
            if (itemHasActiveModule(stack, module.getDataName())) {
                propertyValue = module.applyPropertyModifiers(itemTag, propertyName, propertyValue);
            }
        }
        return propertyValue;
    }

    public static List<IRightClickModule> getRightClickModules() {
        return rightClickModules;
    }

    public static List<IToggleableModule> getToggleableModules() {
        return toggleableModules;
    }

    public static List<IBlockBreakingModule> getBlockBreakingModules() {
        return blockBreakingModules;
    }

    public static List<IPowerModule> getValidModulesForItem(EntityPlayer player, ItemStack stack) {
        List<IPowerModule> validModules = new ArrayList();
        for (IPowerModule module : getAllModules()) {
            if (module.isValidForItem(stack, player)) {
                validModules.add(module);
            }
        }
        return validModules;
    }

    public static boolean tagHasModule(NBTTagCompound tag, String moduleName) {
        return tag.hasKey(moduleName);
    }

    public static boolean isModuleOnline(NBTTagCompound itemTag, String moduleName) {
        if (tagHasModule(itemTag, moduleName) && !itemTag.getCompoundTag(moduleName).hasKey(ONLINE)) {
            return true;
        } else if (tagHasModule(itemTag, moduleName) && itemTag.getCompoundTag(moduleName).getBoolean(ONLINE)) {
            return true;
        }
        return false;
    }

    public static void toggleModule(NBTTagCompound itemTag, String name, boolean toggleval) {
        if (tagHasModule(itemTag, name)) {
            NBTTagCompound moduleTag = itemTag.getCompoundTag(name);
            moduleTag.setBoolean(ONLINE, toggleval);
        }
    }

    public static boolean itemHasModule(ItemStack stack, String moduleName) {
        return tagHasModule(MuseItemTag.getMuseItemTag(stack), moduleName);
    }

    public static void tagAddModule(NBTTagCompound tag, IPowerModule module) {
        tag.setCompoundTag(module.getDataName(), module.getNewTag());
    }

    public static void itemAddModule(ItemStack stack, IPowerModule moduleType) {
        tagAddModule(MuseItemTag.getMuseItemTag(stack), moduleType);
    }

    public static boolean removeModule(NBTTagCompound tag, String moduleName) {
        if (tag.hasKey(moduleName)) {
            tag.removeTag(moduleName);
            return true;
        } else {
            return false;
        }
    }

    public static boolean removeModule(ItemStack stack, String moduleName) {
        return removeModule(MuseItemTag.getMuseItemTag(stack), moduleName);
    }

    public static boolean itemHasActiveModule(ItemStack itemStack, String moduleName) {
        IPowerModule module = getModule(moduleName);
        if (module == null || itemStack == null || !module.isAllowed() || !(itemStack.getItem() instanceof IModularItem)) {
            // playerEntity.sendChatToPlayer("Server has disallowed this module. Sorry!");
            return false;
        }
        if (module instanceof IRightClickModule) {
            // MuseLogger.logDebug("Module: " + moduleName + " vs Mode: " +
            // MuseItemUtils.getActiveMode(itemStack));

            return moduleName.equals(MuseItemTag.getActiveMode(itemStack));
        } else {
            return isModuleOnline(MuseItemTag.getMuseItemTag(itemStack), moduleName);
        }
    }
}
