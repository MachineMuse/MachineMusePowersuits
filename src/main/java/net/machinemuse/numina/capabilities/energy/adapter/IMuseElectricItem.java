package net.machinemuse.numina.capabilities.energy.adapter;

//import appeng.api.config.AccessRestriction;
//import appeng.api.implementations.items.IAEItemPowerStorage;

import cofh.redstoneflux.api.IEnergyContainerItem;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import net.machinemuse.numina.capabilities.energy.ElectricConversions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:12 PM, 4/20/13
 * <p>
 * Ported to Java by lehjr on 11/3/16.
 */
@Optional.InterfaceList({
        @Optional.Interface(iface = "cofh.redstoneflux.api.IEnergyContainerItem", modid = "redstoneflux", striprefs = true),
        @Optional.Interface(iface = "ic2.api.item.ISpecialElectricItem", modid = "ic2", striprefs = true),
        @Optional.Interface(iface = "ic2.api.item.IElectricItemManager", modid = "ic2", striprefs = true)
})
public interface IMuseElectricItem
        extends
        IEnergyContainerItem,
        ISpecialElectricItem,
        IElectricItemManager {

    /* Industrialcraft 2 -------------------------------------------------------------------------- */
    @Override
    default IElectricItemManager getManager(ItemStack stack) {
        return this;
    }

    default double getTransferLimit(ItemStack itemStack) {
        if (itemStack.isEmpty() || !(itemStack.getItem() instanceof IMuseElectricItem))
            return 0;
        IMuseElectricItem iMuseElectricItem = (IMuseElectricItem) itemStack.getItem();
        return ElectricConversions.museEnergyToEU(iMuseElectricItem.getMaxEnergyStored(itemStack) * 0.75);
    }

    @Override
    default double charge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
        // some machines use "Inifinity" and it converts to "-1" This negates their weird effect.
        if (amount == Double.POSITIVE_INFINITY || amount == Double.NEGATIVE_INFINITY)
            amount = Integer.MAX_VALUE * 0.25D;

        int transfer = (ignoreTransferLimit || amount < getTransferLimit(itemStack)) ? ElectricConversions.museEnergyFromEU(amount) : ElectricConversions.museEnergyFromEU(getTransferLimit(itemStack));
        transfer = Math.abs(transfer);

        return ElectricConversions.museEnergyToEU(receiveEnergy(itemStack, transfer, simulate));
    }

    @Override
    default double discharge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate) {
        // some machines use "Inifinity" and it converts to "-1" This negates their weird effect.
        if (amount == Double.POSITIVE_INFINITY || amount == Double.NEGATIVE_INFINITY)
            amount = Integer.MAX_VALUE * 0.25D;

        int transfer = (ignoreTransferLimit || amount < getTransferLimit(itemStack)) ? ElectricConversions.museEnergyFromEU(amount) : ElectricConversions.museEnergyFromEU(getTransferLimit(itemStack));
        transfer = Math.abs(transfer);

        return ElectricConversions.museEnergyToEU(extractEnergy(itemStack, transfer, simulate));
    }

    @Override
    default double getMaxCharge(ItemStack itemStack) {
        return ElectricConversions.museEnergyToEU(getMaxEnergyStored(itemStack));
    }

    @Override
    default double getCharge(ItemStack itemStack) {
        return ElectricConversions.museEnergyToEU(getEnergyStored(itemStack));
    }


    @Override
    default boolean canUse(ItemStack itemStack, double amount) {
        return ElectricConversions.museEnergyFromEU(amount) < getEnergyStored(itemStack);
    }

    @Override
    default boolean use(ItemStack itemStack, double amount, EntityLivingBase entityLivingBase) {
        return ElectricItem.rawManager.use(itemStack, ElectricConversions.museEnergyToEU(amount), entityLivingBase);
    }

    @Override
    default void chargeFromArmor(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        ElectricItem.rawManager.chargeFromArmor(itemStack, entityLivingBase);
    }

    @Override
    default String getToolTip(ItemStack itemStack) {
        return null;
    }

    @Override
    default int getTier(ItemStack itemStack) {
        return ElectricConversions.getTier(itemStack);
    }

    /* Thermal Expansion -------------------------------------------------------------------------- */
    @Override
    default int receiveEnergy(ItemStack container, int maxExtract, boolean simulate) {
        IEnergyStorage energyStorage = container.getCapability(CapabilityEnergy.ENERGY, null);
        return energyStorage != null ? energyStorage.receiveEnergy(maxExtract, simulate) : 0;
    }

    @Override
    default int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        IEnergyStorage energyStorage = container.getCapability(CapabilityEnergy.ENERGY, null);
        return energyStorage != null ? energyStorage.extractEnergy(maxExtract, simulate) : 0;
    }

    @Override
    default int getEnergyStored(ItemStack container) {
        IEnergyStorage energyStorage = container.getCapability(CapabilityEnergy.ENERGY, null);
        return energyStorage != null ? energyStorage.getEnergyStored() : 0;
    }

    @Override
    default int getMaxEnergyStored(ItemStack container) {
        IEnergyStorage energyStorage = container.getCapability(CapabilityEnergy.ENERGY, null);
        return energyStorage != null ? energyStorage.getMaxEnergyStored() : 0;
    }
}