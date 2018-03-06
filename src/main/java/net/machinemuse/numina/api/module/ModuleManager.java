package net.machinemuse.numina.api.module;

import net.machinemuse.numina.api.capability_ports.inventory.IModeChangingItemCapability;
import net.machinemuse.numina.api.capability_ports.inventory.IModularItemCapability;
import net.machinemuse.numina.api.item.IMuseItem;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
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
    protected static final Map<String, ItemStack> moduleMap = new HashMap<>();
    protected static final List<ItemStack> moduleList = new ArrayList<>();
    protected static final List<ItemStack> playerTickModules = new ArrayList<>();
    protected static final List<ItemStack> rightClickModules = new ArrayList<>();
    protected static final List<ItemStack> toggleableModules = new ArrayList<>();
    protected static final List<ItemStack> blockBreakingModules = new ArrayList<>();

    @Override
    public List<ItemStack> getAllModules() {
        return moduleList;
    }

    @Override
    public List<ItemStack> getPlayerTickModules() {
        return playerTickModules;
    }

    @Override
    public @Nonnull ItemStack getModule(String key) {
        return moduleMap.getOrDefault(key, ItemStack.EMPTY);
    }

    @Override
    public void addModule(@Nonnull ItemStack module) {
        moduleMap.put(module.getUnlocalizedName(), module);
        moduleList.add(module);
        if (module.getItem() instanceof IPlayerTickModule) {
            playerTickModules.add(module);
        }
        if (module.getItem() instanceof IRightClickModule) {
            rightClickModules.add(module);
        }
        if (module.getItem() instanceof IToggleableModule) {
            toggleableModules.add(module);
        }
        if (module.getItem() instanceof IBlockBreakingModule) {
            blockBreakingModules.add(module);
        }
    }

    @Deprecated
    @Override
    public double computeModularPropertyDouble(ItemStack stack, String propertyName) {
        double propertyValue = 0;
        NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
        for (ItemStack module : moduleList) {
            if (itemHasActiveModule(stack, module.getUnlocalizedName())) {
                propertyValue = ((IModule)module.getItem()).applyPropertyModifiersDouble(itemTag, propertyName, propertyValue);
            }
        }
        return propertyValue;
    }

    @Override
    public int computeModularPropertyInteger(ItemStack stack, String propertyName) {
        int propertyValue = 0;
        NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
        for (ItemStack module : moduleList) {
            if (itemHasActiveModule(stack, module.getUnlocalizedName())) {
                propertyValue = ((IModule)module.getItem()).applyPropertyModifiersInt(itemTag, propertyName, propertyValue);
            }
        }
        return propertyValue;
    }

    @Override
    public List<ItemStack> getRightClickModules() {
        return rightClickModules;
    }

    @Override
    public List<ItemStack> getToggleableModules() {
        return toggleableModules;
    }

    @Override
    public List<ItemStack> getBlockBreakingModules() {
        return blockBreakingModules;
    }

    @Override
    public List<ItemStack> getValidModulesForItem(ItemStack stack) {
        List<ItemStack> validModules = new ArrayList();
        for (ItemStack module : getAllModules()) {
            if (((IModule)module.getItem()).isValidForItem(stack)) {
                validModules.add(module);
            }
        }
        return validModules;
    }

    @Override
    public boolean itemHasModule(@Nonnull ItemStack itemStack, String moduleName) {
        IItemHandler modularItemWrapper = itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (modularItemWrapper instanceof IModularItemCapability)
            return ((IModularItemCapability) modularItemWrapper).isModuleInstalled(moduleName);
        return false;
    }

    @Override
    public ItemStack itemAddModule(@Nonnull ItemStack stack, @Nonnull ItemStack moduleType) {
        IItemHandler modularItemCap = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (modularItemCap instanceof IModularItemCapability)
            return ((IModularItemCapability) modularItemCap).installModule(moduleType);
        return moduleType;
    }

    @Override
    public NonNullList<ItemStack> removeModule(@Nonnull ItemStack stack, String moduleName) {
        IItemHandler modularItemCap = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (modularItemCap instanceof IModularItemCapability)
            return ((IModularItemCapability) modularItemCap).removeModule(moduleName);
        return NonNullList.withSize(1, ItemStack.EMPTY);
    }

    @Override
    public boolean itemHasActiveModule(@Nonnull ItemStack itemStack, String moduleName) {
        ItemStack module = getModule(moduleName);
        if (module.isEmpty() || itemStack.isEmpty() || !((IModule)module.getItem()).isAllowed() || !(itemStack.getItem() instanceof IMuseItem)) {
            // playerEntity.sendChatToPlayer("Server has disallowed this module. Sorry!");
            return false;
        }

        IItemHandler modularItemWrapper = itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (module.getItem() instanceof IRightClickModule) {
            if (modularItemWrapper != null && modularItemWrapper instanceof IModeChangingItemCapability)
                return ((IModeChangingItemCapability) modularItemWrapper).getActiveModule().getUnlocalizedName().equals(moduleName);
            return false;
        } else {
            if (modularItemWrapper != null && modularItemWrapper instanceof IModularItemCapability)
                return ((IModularItemCapability) modularItemWrapper).isModuleOnline(moduleName);
            return false;
        }
    }

    @Override
    public void toggleModuleForPlayer(EntityPlayer player, String name, boolean toggleval) {
        for (ItemStack stack : MuseItemUtils.modularItemsEquipped(player)) {
            IItemHandler modularItemCap = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            if (modularItemCap instanceof IModularItemCapability) {
                ((IModularItemCapability) modularItemCap).toggleModule(name, toggleval);
            }
        }
    }

    @Override
    public NonNullList<ItemStack> getPlayerInstalledModules(EntityPlayer player) {
        List<ItemStack> installedModules = new ArrayList();
        for (ItemStack stack : MuseItemUtils.modularItemCapsEquipped(player)) {
            IItemHandler modularItemCap = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            if (modularItemCap instanceof IModularItemCapability)
                installedModules.addAll(((IModularItemCapability)installedModules).getInstallledModules());
        }

        if(installedModules.isEmpty())
            return NonNullList.withSize(1, ItemStack.EMPTY);

        NonNullList retList = NonNullList.withSize(installedModules.size(), ItemStack.EMPTY);
        for (int i=0; i< installedModules.size(); i++) {
            retList.set(i, installedModules.get(i));
        }
        return retList;
    }

    @Override
    public boolean hasCustomInstallCost(String unLocalizedName) {
        return customInstallCosts.containsKey(unLocalizedName);
    }

    @Override
    public List<ItemStack> getCustomInstallCost(String unLocalizedName) {
        return customInstallCosts.get(unLocalizedName);
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