package net.machinemuse.powersuits.common;

import net.machinemuse.numina.item.IModularItem;
import net.machinemuse.numina.module.IModuleManager;
import net.machinemuse.numina.module.IPowerModule;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.item.armor.*;
import net.machinemuse.powersuits.item.tool.ItemPowerFist;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public enum ModuleManager implements IModuleManager {
    INSTANCE;

    protected static final Map<String, NonNullList<ItemStack>> installCosts = new HashMap<>();
    protected static Map<String, NonNullList<ItemStack>> customInstallCosts = new HashMap<>();
    protected static final Map<String, IPowerModule> moduleMap = new LinkedHashMap<>();

    @Override
    public void addModule(IPowerModule module) {
        moduleMap.put(module.getDataName(), module);
    }

    @Override
    public NonNullList<IPowerModule> getAllModules() {
        NonNullList<IPowerModule> retList = NonNullList.create();
        for (IPowerModule module : moduleMap.values()) {
            retList.add(module);
        }
        return retList;
    }

    @Override
    public boolean isValidForItem(@Nonnull ItemStack stack, IPowerModule module) {
        if (stack.isEmpty() || !(stack.getItem() instanceof IModularItem))
            return false;
        if (module == null || !MPSConfig.INSTANCE.getModuleAllowedorDefault(module.getDataName(), true))
            return false;

        Item item = stack.getItem();
        switch (module.getTarget()) {
            case ALLITEMS:
                return true;
            case TOOLONLY:
                return item instanceof ItemPowerFist;
            case ARMORONLY:
                return item instanceof ItemPowerArmor;
            case HEADONLY:
                return item instanceof ItemPowerArmorHelmet;
            case TORSOONLY:
                return item instanceof ItemPowerArmorChestplate;
            case LEGSONLY:
                return item instanceof ItemPowerArmorLeggings;
            case FEETONLY:
                return item instanceof ItemPowerArmorBoots;
        }
        return false;
    }

    @Override
    public boolean isValidForItem(@Nonnull ItemStack stack, String moduleDataName) {
        IPowerModule module = getModule(moduleDataName);

        return isValidForItem(stack, module);
    }

    @Override
    public Map<String, IPowerModule> getModuleMap() {
        return moduleMap;
    }

    @Nullable
    @Override
    public IPowerModule getModule(String key) {
        return moduleMap.get(key);
    }

    @Override
    public NonNullList<ItemStack> getInstallCost(String dataName) {
        return installCosts.getOrDefault(dataName, NonNullList.create());
    }

    @Override
    public void addInstallCost(String dataName, @Nonnull ItemStack installCost) {
        NonNullList<ItemStack> costForModule = getInstallCost(dataName);
        costForModule.add(installCost);
        installCosts.put(dataName, costForModule);
    }

    @Override
    public void addInstallCost(String dataName, NonNullList<ItemStack> installCost) {
        NonNullList<ItemStack> costForModule = getInstallCost(dataName);
        costForModule.addAll(installCost);
        installCosts.put(dataName, costForModule);
    }

    public Map<String, NonNullList<ItemStack>> getCustomInstallCosts() {
        return customInstallCosts;
    }

    public Map<String, ItemStack[]> getCustomInstallCostsForServerToClientConfig() {
        Map<String, ItemStack[]> retMap = new HashMap<>();
        for (Map.Entry<String, NonNullList<ItemStack>> entry : customInstallCosts.entrySet()) {
            NonNullList<ItemStack> nonNullStacks = entry.getValue();
            ItemStack[] stacks = new ItemStack[nonNullStacks.size()];

            for (int i = 0; i < nonNullStacks.size(); i++) {
                stacks[i] = nonNullStacks.get(i);
            }
            retMap.put(entry.getKey(), stacks);
        }

        return retMap;
    }

    public void setCustomInstallCosts(Map<String, NonNullList<ItemStack>> customInstallCosts) {
        this.customInstallCosts = customInstallCosts;
    }

    @Override
    public boolean hasCustomInstallCost(String dataName) {
        return customInstallCosts.containsKey(dataName);
    }

    @Override
    public NonNullList<ItemStack> getCustomInstallCost(String dataName) {
        return customInstallCosts.getOrDefault(dataName, NonNullList.create());
    }

    @Override
    public void addCustomInstallCost(String dataName, @Nonnull ItemStack installCost) {
        NonNullList<ItemStack> costForModule = getCustomInstallCost(dataName);
        costForModule.add(installCost);
        customInstallCosts.put(dataName, costForModule);
    }
}