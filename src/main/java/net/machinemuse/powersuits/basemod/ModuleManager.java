package net.machinemuse.powersuits.basemod;

import net.machinemuse.numina.capabilities.inventory.IModeChangingItem;
import net.machinemuse.numina.capabilities.inventory.IModularItem;
import net.machinemuse.numina.module.IMuseModuleManager;
import net.machinemuse.numina.module.IPowerModule;
import net.machinemuse.powersuits.item.armor.*;
import net.machinemuse.powersuits.item.tool.ItemPowerFist;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public enum ModuleManager implements IMuseModuleManager {
    INSTANCE;

    NonNullList<ResourceLocation> moduleNames = NonNullList.create();

    @Override
    public void addModule(ResourceLocation resourceLocation) {
        moduleNames.add(resourceLocation);
    }

    @Override
    public NonNullList<ResourceLocation> getAllModules() {
        return moduleNames;
    }

    @Nullable
    @Override
    public ItemStack getModule(String s) {
        return null;
    }

    @Override
    public boolean isModuleAllowed(String resourceLocation) {
        return true; // FIXME: use config for this
    }

    @Override
    public boolean isValidForItem(@Nonnull ItemStack itemStack, ResourceLocation moduleRegName) {
        if (itemStack.isEmpty())
            return false;

        Item module = ForgeRegistries.ITEMS.getValue(moduleRegName);
        if (module == null || !(module instanceof IPowerModule))
            return false;

        if (module == null || !MPSConfig.INSTANCE.getModuleAllowedorDefault(moduleRegName, true))
            return false;

        Item item = itemStack.getItem();
        switch (((IPowerModule)module).getTarget()) {
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

    public static LazyOptional<IModularItem> getModularItemCapability(@Nonnull ItemStack itemStack) {
        return IMuseModuleManager.getModularItemCapability(itemStack);
    }

    public static LazyOptional<IModeChangingItem> getModeChangingModularItemCapability(@Nonnull ItemStack itemStack) {
        return IMuseModuleManager.getModeChangingModularItemCapability(itemStack);
    }
}