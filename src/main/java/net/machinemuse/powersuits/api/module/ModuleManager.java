package net.machinemuse.powersuits.api.module;

import net.machinemuse.numina.api.module.IModuleManager;
import net.machinemuse.numina.api.module.IPowerModule;
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
    protected static final Map<String, NonNullList<ItemStack>> customInstallCosts = new HashMap<>();
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