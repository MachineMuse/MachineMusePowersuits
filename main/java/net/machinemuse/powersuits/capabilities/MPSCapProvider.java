package net.machinemuse.powersuits.capabilities;

import forestry.api.apiculture.ApicultureCapabilities;
import net.machinemuse.numina.api.capability_ports.itemwrapper.ModeChangingItemWrapper;
import net.machinemuse.numina.api.capability_ports.itemwrapper.ModularItemWrapper;
import net.machinemuse.numina.api.capability_ports.itemwrapper.MuseHeatItemWrapper;
import net.machinemuse.numina.api.constants.NuminaNBTConstants;
import net.machinemuse.numina.api.energy.forge.ForgeEnergyItemWrapper;
import net.machinemuse.numina.api.item.IMuseItem;
import net.machinemuse.numina.capabilities.CapabilityHeat;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.item.armor.*;
import net.machinemuse.powersuits.item.tool.ItemPowerFist;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public final class MPSCapProvider implements ICapabilityProvider {
    ItemStack container;

    MuseHeatItemWrapper heatWrapper;
    ForgeEnergyItemWrapper energyWrapper;
    boolean apiarist_module_installed = false;
    ModularItemWrapper modularItemWrapper;
    ModeChangingItemWrapper modeChangingItemWrapper;

    public MPSCapProvider(final ItemStack container, NBTTagCompound nbt) {
        this.container = container;
        if (container != null && container.getItem() instanceof IMuseItem) {
            NBTTagCompound containerNBT = MuseItemUtils.getMuseItemTag(container);
            Item item = container.getItem();

            int currentEnergy = 0;
            int maxEnergy = 0;
            int maxEnergyTransfer = 0;

            int currentHeat = 0;
            int maxHeat = 0;
            int maxHeatTransfer = 0;
            int maxModuleSlots = 0;

        if (item instanceof ItemPowerFist) {
            modeChangingItemWrapper = null;
            modularItemWrapper = new ModularItemWrapper(container, 50); // TODO
        } else if (item instanceof ItemPowerArmor) {
            modeChangingItemWrapper = new ModeChangingItemWrapper(container, 30); // TODO
            modularItemWrapper = null;

        } else {
            modeChangingItemWrapper = null;
            modularItemWrapper = null;
        }






            if (containerNBT.hasKey(NuminaNBTConstants.CURRENT_ENERGY)) {
                currentEnergy = containerNBT.getInteger(NuminaNBTConstants.CURRENT_ENERGY);
                if (containerNBT.hasKey(NuminaNBTConstants.MAXIMUM_ENERGY))
                    maxEnergyTransfer = maxEnergy = containerNBT.getInteger(NuminaNBTConstants.MAXIMUM_ENERGY);
            }

            if (containerNBT.hasKey(NuminaNBTConstants.CURRENT_HEAT)) {
                currentHeat = containerNBT.getInteger(NuminaNBTConstants.CURRENT_HEAT);
                if (containerNBT.hasKey(NuminaNBTConstants.MAXIMUM_HEAT))
                    maxHeatTransfer = maxHeat = containerNBT.getInteger(NuminaNBTConstants.MAXIMUM_HEAT);
            } else {

                if (item instanceof ItemPowerArmorHelmet)
                    maxHeat = MPSConfig.getInstance().maxHeatPowerArmorHelmet();
                else if (item instanceof ItemPowerArmorChestplate)
                    maxHeat = MPSConfig.getInstance().maxHeatPowerArmorChestplate();
                else if (item instanceof ItemPowerArmorLeggings)
                    maxHeat = MPSConfig.getInstance().maxHeatPowerArmorLeggings();
                else if (item instanceof ItemPowerArmorBoots)
                    maxHeat = MPSConfig.getInstance().maxHeatPowerArmorBoots();
                else if (item instanceof ItemPowerFist)
                    maxHeat = MPSConfig.getInstance().maxHeatPowerFist();

                containerNBT.setInteger(NuminaNBTConstants.CURRENT_HEAT, 0);
                containerNBT.setInteger(NuminaNBTConstants.MAXIMUM_HEAT, maxHeat);
            }
            heatWrapper = new MuseHeatItemWrapper(container, maxHeat, maxHeatTransfer, currentHeat);
            energyWrapper = new ForgeEnergyItemWrapper(container, maxEnergy, maxEnergyTransfer, currentEnergy);
        } else {
            heatWrapper = null;
            energyWrapper = null;
        }
    }






    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityHeat.HEAT || capability == CapabilityEnergy.ENERGY)
            return true;
        if (capability == ApicultureCapabilities.ARMOR_APIARIST && apiarist_module_installed)
            return true;

        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && (modularItemWrapper != null || modeChangingItemWrapper != null))
            return true;


        return false;


//        return this.getCapability(capability, facing) != null;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityHeat.HEAT) {
            heatWrapper.updateFromNBT();
            return (T) heatWrapper;
        }

        if (capability == CapabilityEnergy.ENERGY) {
            energyWrapper.updateFromNBT();
            return (T) energyWrapper;
        }

        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (modularItemWrapper != null) {

                return (T) modularItemWrapper;
            } else {

                return (T) modeChangingItemWrapper;
            }
        }


        if (capability == ApicultureCapabilities.ARMOR_APIARIST && apiarist_module_installed)
            return capability.getDefaultInstance();



        return null;
    }
}
