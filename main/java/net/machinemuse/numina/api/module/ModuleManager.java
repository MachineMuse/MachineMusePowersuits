package net.machinemuse.numina.api.module;

import net.machinemuse.numina.api.constants.NuminaModuleConstants;
import net.machinemuse.numina.api.item.IMuseItem;
import net.machinemuse.numina.item.IModeChangingItem;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleManager implements IModuleManager {
    private static ModuleManager INSTANCE;
    private ModuleManager() {}
    public static ModuleManager getInstance() {
        if (INSTANCE == null) {
            synchronized (ModuleManager.class) {
                if (INSTANCE == null) INSTANCE = new ModuleManager();
            }
        }
        return INSTANCE;
    }

    protected static final Map<String, List<ItemStack>> customInstallCosts = new HashMap<>();
    protected static final Map<String, IModule> moduleMap = new HashMap<>();
    protected static final List<IModule> moduleList = new ArrayList<>();
    protected static final List<IPlayerTickModule> playerTickModules = new ArrayList<>();
    protected static final List<IRightClickModule> rightClickModules = new ArrayList<>();
    protected static final List<IToggleableModule> toggleableModules = new ArrayList<>();
    protected static final List<IBlockBreakingModule> blockBreakingModules = new ArrayList<>();

    @Override
    public List<IModule> getAllModules() {
        return moduleList;
    }

    @Override
    public List<IPlayerTickModule> getPlayerTickModules() {
        return playerTickModules;
    }

    @Nullable
    @Override
    public IModule getModule(String key) {
        return moduleMap.get(key);
    }

    @Override
    public void addModule(IModule module) {
        moduleMap.put(module.getUnlocalizedName(), module);
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

    @Override
    public double computeModularPropertyDouble(ItemStack stack, String propertyName) {
        double propertyValue = 0;
        NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
        for (IModule module : moduleList) {
            if (itemHasActiveModule(stack, module.getUnlocalizedName())) {
                propertyValue = module.applyPropertyModifiersDouble(itemTag, propertyName, propertyValue);
            }
        }
        return propertyValue;
    }

    @Override
    public int computeModularPropertyInteger(ItemStack stack, String propertyName) {
        int propertyValue = 0;
        NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
        for (IModule module : moduleList) {
            if (itemHasActiveModule(stack, module.getUnlocalizedName())) {
                propertyValue = module.applyPropertyModifiersInt(itemTag, propertyName, propertyValue);
            }
        }
        return propertyValue;
    }

    @Override
    public List<IRightClickModule> getRightClickModules() {
        return rightClickModules;
    }

    @Override
    public List<IToggleableModule> getToggleableModules() {
        return toggleableModules;
    }

    @Override
    public List<IBlockBreakingModule> getBlockBreakingModules() {
        return blockBreakingModules;
    }

    @Override
    public List<IModule> getValidModulesForItem(ItemStack stack) {
        List<IModule> validModules = new ArrayList();
        for (IModule module : getAllModules()) {
            if (module.isValidForItem(stack)) {
                validModules.add(module);
            }
        }
        return validModules;
    }

    @Override
    public boolean tagHasModule(NBTTagCompound tag, String moduleName) {
        return tag.hasKey(moduleName);
    }

    @Override
    public boolean isModuleOnline(NBTTagCompound itemTag, String moduleName) {
        if (tagHasModule(itemTag, moduleName) && !itemTag.getCompoundTag(moduleName).hasKey(NuminaModuleConstants.ONLINE)) {
            return true;
        } else if (tagHasModule(itemTag, moduleName) && itemTag.getCompoundTag(moduleName).getBoolean(NuminaModuleConstants.ONLINE)) {
            return true;
        }
        return false;
    }

    @Override
    public void toggleModule(NBTTagCompound itemTag, String name, boolean toggleval) {
        if (tagHasModule(itemTag, name)) {
            NBTTagCompound moduleTag = itemTag.getCompoundTag(name);
            moduleTag.setBoolean(NuminaModuleConstants.ONLINE, toggleval);
        }
    }

    @Override
    public boolean itemHasModule(ItemStack stack, String moduleName) {
        return tagHasModule(MuseItemUtils.getMuseItemTag(stack), moduleName);
    }

    @Override
    public void tagAddModule(NBTTagCompound tag, IModule module) {
        tag.setTag(module.getUnlocalizedName(), module.getNewTag());
    }

    @Override
    public void itemAddModule(ItemStack stack, IModule moduleType) {
        tagAddModule(MuseItemUtils.getMuseItemTag(stack), moduleType);
    }

    @Override
    public boolean removeModule(NBTTagCompound tag, String moduleName) {
        if (tag.hasKey(moduleName)) {
            tag.removeTag(moduleName);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeModule(ItemStack stack, String moduleName) {
        return removeModule(MuseItemUtils.getMuseItemTag(stack), moduleName);
    }

    @Override
    public boolean itemHasActiveModule(ItemStack itemStack, String moduleName) {
        IModule module = getModule(moduleName);
        if (module == null || itemStack == null || !module.isAllowed() || !(itemStack.getItem() instanceof IMuseItem)) {
            // playerEntity.sendChatToPlayer("Server has disallowed this module. Sorry!");
            return false;
        }
        if (module instanceof IRightClickModule && itemStack.getItem() instanceof IModeChangingItem) {
            // MuseLogger.logDebug("Module: " + moduleName + " vs Mode: " +
            // MuseItemUtils.getActiveMode(itemStack));
            IModeChangingItem item = (IModeChangingItem) itemStack.getItem();

            return moduleName.equals(item.getActiveMode(itemStack));
        } else {
            return isModuleOnline(MuseItemUtils.getMuseItemTag(itemStack), moduleName);
        }
    }

    public static void toggleModuleForPlayer(EntityPlayer player, String name, boolean toggleval) {
        for (ItemStack stack : MuseItemUtils.modularItemsEquipped(player)) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            ModuleManager.getInstance().toggleModule(itemTag, name, toggleval);
        }
    }

    public static List<IModule> getPlayerInstalledModules(EntityPlayer player) {
        List<IModule> installedModules = new ArrayList();
        for (ItemStack stack : MuseItemUtils.modularItemsEquipped(player)) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            for (IModule module : ModuleManager.getInstance().getValidModulesForItem(stack)) {
                if (ModuleManager.getInstance().tagHasModule(itemTag, module.getUnlocalizedName())) {
                    installedModules.add(module);
                }
            }
        }
        return installedModules;
    }




    @Override
    public boolean hasCustomInstallCost(String dataName) {
        return customInstallCosts.containsKey(dataName);
    }

    @Override
    public List<ItemStack> getCustomInstallCost(String dataName) {
        return customInstallCosts.get(dataName);
    }

    @Override
    public void addCustomInstallCost(String moduleName, ItemStack stack) {
        if(customInstallCosts.containsKey(moduleName)) {
            customInstallCosts.get(moduleName).add(stack);
        } else {
            customInstallCosts.put(moduleName, new ArrayList<ItemStack>());
            customInstallCosts.get(moduleName).add(stack);
        }
    }
}