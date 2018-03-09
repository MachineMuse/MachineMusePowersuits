package net.machinemuse.numina.api.module;

import net.machinemuse.numina.api.capability_ports.inventory.IModeChangingItemCapability;
import net.machinemuse.numina.api.capability_ports.inventory.IModularItemCapability;
import net.machinemuse.numina.api.item.IMuseItem;
import net.machinemuse.numina.utils.nbt.NuminaNBTUtils;
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

    protected static final Map<String, NonNullList<ItemStack>> customInstallCosts = new HashMap<>();
    protected static final Map<String, ItemStack> moduleMap = new HashMap<>();
    protected static final NonNullList<ItemStack> moduleList = NonNullList.create();
    protected static final NonNullList<ItemStack> playerTickModules = NonNullList.create();
    protected static final NonNullList<ItemStack> rightClickModules = NonNullList.create();
    protected static final NonNullList<ItemStack> toggleableModules = NonNullList.create();
    protected static final NonNullList<ItemStack> blockBreakingModules = NonNullList.create();

    @Override
    public NonNullList<ItemStack> getAllModules() {
        return moduleList;
    }

    @Override
    public NonNullList<ItemStack> getPlayerTickModules() {
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
        for (ItemStack module : moduleList) {
            if (itemHasActiveModule(stack, module.getUnlocalizedName())) {
                NBTTagCompound moduleTag = NuminaNBTUtils.getMuseItemTag(module);
                propertyValue = ((IModule)module.getItem()).applyPropertyModifiersDouble(moduleTag, propertyName, propertyValue);
            }
        }
        return propertyValue;
    }

    @Override
    public int computeModularPropertyInteger(ItemStack stack, String propertyName) {
        int propertyValue = 0;
        for (ItemStack module : moduleList) {
            if (itemHasActiveModule(stack, module.getUnlocalizedName())) {
                NBTTagCompound moduleTag = NuminaNBTUtils.getMuseItemTag(module);
                propertyValue = ((IModule)module.getItem()).applyPropertyModifiersInt(moduleTag, propertyName, propertyValue);
            }
        }
        return propertyValue;
    }

    @Override
    public NonNullList<ItemStack> getRightClickModules() {
        return rightClickModules;
    }

    @Override
    public NonNullList<ItemStack> getToggleableModules() {
        return toggleableModules;
    }

    @Override
    public NonNullList<ItemStack> getBlockBreakingModules() {
        return blockBreakingModules;
    }

    @Override
    public NonNullList<ItemStack> getValidModulesForItem(ItemStack stack) {
        NonNullList<ItemStack> validModules = NonNullList.create();
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
        NonNullList<ItemStack> installedModules = NonNullList.create();

        for (ItemStack stack : MuseItemUtils.modularItemCapsEquipped(player)) {
            IItemHandler modularItemCap = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            if (modularItemCap instanceof IModularItemCapability) {
                for (ItemStack module : ((IModularItemCapability) modularItemCap).getInstallledModules()) {
                    if (!module.isEmpty())
                        installedModules.add(module);
                }
            }
        }
        return installedModules;
    }

    @Override
    public boolean hasCustomInstallCost(String unLocalizedName) {
        return customInstallCosts.containsKey(unLocalizedName);
    }

    @Override
    public NonNullList<ItemStack> getCustomInstallCost(String unLocalizedName) {
        return customInstallCosts.get(unLocalizedName);
    }

    @Override
    public void addCustomInstallCost(String moduleName, ItemStack stack) {
        if(customInstallCosts.containsKey(moduleName)) {
            customInstallCosts.get(moduleName).add(stack);
        } else {
            customInstallCosts.put(moduleName, NonNullList.create());
            customInstallCosts.get(moduleName).add(stack);
        }
    }
}