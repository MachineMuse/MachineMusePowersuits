package net.machinemuse.powersuits.api.electricity.adapter;

//import appeng.api.config.AccessRestriction;
//import appeng.api.implementations.items.IAEItemPowerStorage;

import cofh.redstoneflux.api.IEnergyContainerItem;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import mekanism.api.energy.IEnergizedItem;
import net.machinemuse.numina.api.constants.NuminaNBTConstants;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.electricity.ElectricConversions;
import net.machinemuse.powersuits.utils.ElectricItemUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.Nonnull;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:12 PM, 4/20/13
 *
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

    /**
     * Call to get the energy of an item
     *
     * @param stack ItemStack to set
     * @return Current energy level
     */
    default double getCurrentMPSEnergy(@Nonnull ItemStack stack) {
        return MuseItemUtils.getDoubleOrZero(stack, NuminaNBTConstants.CURRENT_ENERGY);
    }

    /**
     * Call to set the energy of an item
     *
     * @param stack ItemStack to set
     * @return Maximum energy level
     */
    double getMaxMPSEnergy(@Nonnull ItemStack stack);
//    {
//        return ModuleManager.computeModularProperty(stack, ElectricItemUtils.MAXIMUM_ENERGY);
//    }

    /**
     * Call to set the energy of an item
     *
     * @param stack ItemStack to set
     * @param energy Level to set it to
     */
    default void setCurrentMPSEnergy(@Nonnull ItemStack stack, double energy) {
        MuseItemUtils.setDoubleOrRemove(stack, NuminaNBTConstants.CURRENT_ENERGY, Math.min(energy, getMaxMPSEnergy(stack)));
    }

    /**
     * Call to drain energy from an item
     *
     * @param stack ItemStack being requested for energy
     * @param requested Amount of energy to drain
     * @return Amount of energy successfully drained
     */
    default double drainMPSEnergyFrom(@Nonnull ItemStack stack, double requested) {
        double available = getCurrentMPSEnergy(stack);
        if (available > requested) {
            setCurrentMPSEnergy(stack, available - requested);
            return requested;
        } else {
            setCurrentMPSEnergy(stack, 0);
            return available;
        }
    }

    /**
     * Call to give energy to an item
     *
     * @param stack ItemStack being provided with energy
     * @param provided Amount of energy to add
     * @return Amount of energy added
     */
    default double giveMPSEnergyTo(@Nonnull ItemStack stack, double provided) {
        double available = getCurrentMPSEnergy(stack);
        double max = getMaxMPSEnergy(stack);

        if (available + provided < max) {
            setCurrentMPSEnergy(stack, available + provided);
            return provided;
        } else {
            setCurrentMPSEnergy(stack, max);
            return max - available;
        }
    }

    default boolean canProvideEnergy(@Nonnull ItemStack itemStack) {
        if (!itemStack.isEmpty()) {
            Item item = itemStack.getItem();
            if (itemStack.getItem() instanceof IEnergizedItem)
                return ((IEnergizedItem)item).canSend(itemStack);
        }
        return true;
    }



    /* Industrialcraft 2 -------------------------------------------------------------------------- */
    @Override
    default IMuseElectricItem getManager(ItemStack stack) {
        return this;
    }

    @Override
    default void chargeFromArmor(ItemStack itemStack, EntityLivingBase entity) {
        ElectricItem.rawManager.chargeFromArmor(itemStack, entity);
    }

    @Override
    default boolean use(ItemStack itemStack, double amount, EntityLivingBase entity) {
        return ElectricItem.rawManager.use(itemStack, ElectricConversions.museEnergyToEU(amount), entity);
    }

    @Override
    default double getCharge(ItemStack itemStack) {
        return ElectricConversions.museEnergyToEU(getCurrentMPSEnergy(itemStack));
    }

    @Override
    default double getMaxCharge(ItemStack itemStack) {
        return ElectricConversions.museEnergyToEU(getMaxMPSEnergy(itemStack));
    }

    @Override
    default int getTier(ItemStack itemStack) {
        return ElectricConversions.getTier(itemStack);
    }

    @Override
    default double charge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate){
        double current = getCurrentMPSEnergy(itemStack);
        double transfer = (ignoreTransferLimit || amount < getTransferLimit(itemStack)) ? ElectricConversions.museEnergyFromEU(amount) : getTransferLimit(itemStack);
        double given = giveMPSEnergyTo(itemStack, transfer);
        if (simulate) {
            setCurrentMPSEnergy(itemStack, current);
        }
        return ElectricConversions.museEnergyToEU(given);
    }

    @Override
    default boolean canUse(ItemStack itemStack, double amount) {
        return ElectricConversions.museEnergyFromEU(amount) < getCurrentMPSEnergy(itemStack);
    }

    @Override
    default double discharge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate) {
        double current = getCurrentMPSEnergy(itemStack);
        double transfer = (ignoreTransferLimit || amount < getTransferLimit(itemStack)) ? ElectricConversions.museEnergyFromEU(amount) : getTransferLimit(itemStack);
        double taken = drainMPSEnergyFrom(itemStack, transfer);
        if (simulate) {
            setCurrentMPSEnergy(itemStack, current);
        }
        return ElectricConversions.museEnergyToEU(taken);
    }

    default double getTransferLimit(ItemStack itemStack) {
        return ElectricConversions.museEnergyToEU(Math.sqrt(getMaxMPSEnergy(itemStack)));
    }

    @Override
    String getToolTip(ItemStack itemStack);


    /* Thermal Expansion -------------------------------------------------------------------------- */
    default int receiveEnergy(ItemStack stack, int energy, boolean simulate) {
        double current = getCurrentMPSEnergy(stack);
        double receivedME = ElectricConversions.museEnergyFromRF(energy);
        double eatenME = giveMPSEnergyTo(stack, receivedME);
        if (simulate) {
            setCurrentMPSEnergy(stack, current);
        }
        return ElectricConversions.museEnergyToRF(eatenME);
    }

    default int extractEnergy(ItemStack stack, int energy, boolean simulate) {
        double current = getCurrentMPSEnergy(stack);
        double requesteddME = ElectricConversions.museEnergyFromRF(energy);
        double takenME = drainMPSEnergyFrom(stack, requesteddME);
        if (simulate) {
            setCurrentMPSEnergy(stack, current);
        }
        return ElectricConversions.museEnergyToRF(takenME);
    }

    default int getEnergyStored(ItemStack theItem) {
        return ElectricConversions.museEnergyToRF(getCurrentMPSEnergy(theItem));
    }

    default int getMaxEnergyStored(ItemStack theItem) {
        return ElectricConversions.museEnergyToRF(getMaxMPSEnergy(theItem));
    }
}