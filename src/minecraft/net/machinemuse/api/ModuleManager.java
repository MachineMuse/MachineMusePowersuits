package net.machinemuse.api;

import net.machinemuse.api.moduletrigger.IBlockBreakingModule;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleManager {

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
        NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
        for (IPowerModule module : moduleList) {
            if (MuseItemUtils.itemHasActiveModule(stack, module.getDataName())) {
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
}
