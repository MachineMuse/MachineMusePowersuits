package net.machinemuse.powersuits.api.electricity;

//import appeng.api.config.AccessRestriction;

import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.utils.energy.ElectricItemUtils;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:12 PM, 4/20/13
 *
 * Ported to Java by lehjr on 11/3/16.
 */
public class MuseElectricItem extends Item implements IMuseElectricItem {
    private static MuseElectricItem INSTANCE;

    public static MuseElectricItem getInstance() {
        if (INSTANCE == null)
            INSTANCE = new MuseElectricItem();
        return INSTANCE;
    }

    /**
     * Call to get the energy of an item
     *
     * @param stack ItemStack to set
     * @return Current energy level
     */
    public double getCurrentMPSEnergy(ItemStack stack) {
        return MuseItemUtils.getDoubleOrZero(stack, ElectricItemUtils.CURRENT_ENERGY);
    }

    /**
     * Call to set the energy of an item
     *
     * @param stack ItemStack to set
     * @return Maximum energy level
     */
    public double getMaxMPSEnergy(ItemStack stack) {
        return ModuleManager.computeModularProperty(stack, ElectricItemUtils.MAXIMUM_ENERGY);
    }

    /**
     * Call to set the energy of an item
     *
     * @param stack ItemStack to set
     * @param energy Level to set it to
     */
    public void setCurrentMPSEnergy(ItemStack stack, double energy) {
        MuseItemUtils.setDoubleOrRemove(stack, ElectricItemUtils.CURRENT_ENERGY, Math.min(energy, getMaxMPSEnergy(stack)));
    }

    /**
     * Call to drain energy from an item
     *
     * @param stack ItemStack being requested for energy
     * @param requested Amount of energy to drain
     * @return Amount of energy successfully drained
     */
    public double drainMPSEnergyFrom(ItemStack stack, double requested) {
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
    public double giveMPSEnergyTo(ItemStack stack, double provided) {
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

    /* Industrialcraft 2 -------------------------------------------------------------------------- */
//    public IMuseElectricItem getManager(ItemStack stack) {
//        return this;
//    }
//
//    @Override
//    public void chargeFromArmor(ItemStack itemStack, EntityLivingBase entities) {
//        ElectricItem.rawManager.chargeFromArmor(itemStack, entities);
//    }
//
//    @Override
//    public boolean use(ItemStack itemStack, double amount, EntityLivingBase entities) {
//        return ElectricItem.rawManager.use(itemStack, ElectricConversions.museEnergyToEU(amount), entities);
//    }
//
//    public boolean canProvideEnergy(ItemStack itemStack) {
//        return true;
//    }
//
//    @Override
//    public double getCharge(ItemStack itemStack) {
//        return ElectricConversions.museEnergyToEU(getCurrentMPSEnergy(itemStack));
//    }
//
//    public double getMaxCharge(ItemStack itemStack) {
//        return ElectricConversions.museEnergyToEU(getMaxMPSEnergy(itemStack));
//    }
//
//    public int getTier(ItemStack itemStack) {
//        return ElectricConversions.getTier(itemStack);
//    }
//
//    public double getTransferLimit(ItemStack itemStack) {
//        return ElectricConversions.museEnergyToEU(Math.sqrt(getMaxMPSEnergy(itemStack)));
//    }
//
//    @Override
//    public double charge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate){
//        double current = getCurrentMPSEnergy(itemStack);
//        double transfer = (ignoreTransferLimit || amount < getTransferLimit(itemStack)) ? ElectricConversions.museEnergyFromEU(amount) : getTransferLimit(itemStack);
//        double given = giveMPSEnergyTo(itemStack, transfer);
//        if (simulate) {
//            setCurrentMPSEnergy(itemStack, current);
//        }
//        return ElectricConversions.museEnergyToEU(given);
//    }
//
//    @Override
//    public double discharge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate) {
//        double current = getCurrentMPSEnergy(itemStack);
//        double transfer = (ignoreTransferLimit || amount < getTransferLimit(itemStack)) ? ElectricConversions.museEnergyFromEU(amount) : getTransferLimit(itemStack);
//        double taken = drainMPSEnergyFrom(itemStack, transfer);
//        if (simulate) {
//            setCurrentMPSEnergy(itemStack, current);
//        }
//        return ElectricConversions.museEnergyToEU(taken);
//    }
//
//    @Override
//    public boolean canUse(ItemStack itemStack, double amount) {
//        return ElectricConversions.museEnergyFromEU(amount) < getCurrentMPSEnergy(itemStack);
//    }
//
//    @Override
//    public String getToolTip(ItemStack itemStack) {
//        return "";
//
////        return itemStack.getTooltip(Minecraft.getMinecraft().thePlayer, false).toString();
//    }
//
//    public Item getChargedItem(ItemStack itemStack) {
//        return this;
//    }
//
//    public Item getEmptyItem(ItemStack itemStack) {
//        return this;
//    }

    /* Thermal Expansion -------------------------------------------------------------------------- */
    public int receiveEnergy(ItemStack stack, int energy, boolean simulate) {
        double current = getCurrentMPSEnergy(stack);
        double receivedME = ElectricConversions.museEnergyFromRF(energy);
        double eatenME = giveMPSEnergyTo(stack, receivedME);
        if (simulate) {
            setCurrentMPSEnergy(stack, current);
        }
        return ElectricConversions.museEnergyToRF(eatenME);
    }

    public int extractEnergy(ItemStack stack, int energy, boolean simulate) {
        double current = getCurrentMPSEnergy(stack);
        double requesteddME = ElectricConversions.museEnergyFromRF(energy);
        double takenME = drainMPSEnergyFrom(stack, requesteddME);
        if (simulate) {
            setCurrentMPSEnergy(stack, current);
        }
        return ElectricConversions.museEnergyToRF(takenME);
    }

    public int getEnergyStored(ItemStack theItem) {
        return ElectricConversions.museEnergyToRF(getCurrentMPSEnergy(theItem));
    }

    public int getMaxEnergyStored(ItemStack theItem) {
        return ElectricConversions.museEnergyToRF(getMaxMPSEnergy(theItem));
    }

    public int getMaxDamage(ItemStack itemStack) {
        return 0;
    }

    /* Mekanism ----------------------------------------------------------------------------------- */
    @Override
    public double getEnergy(ItemStack itemStack) {
        return ElectricConversions.museEnergyToMek(getCurrentMPSEnergy(itemStack));
    }

    @Override
    public void setEnergy(ItemStack itemStack, double v) {
        setCurrentMPSEnergy(itemStack, ElectricConversions.museEnergyFromMek(v));
    }

    @Override
    public double getMaxEnergy(ItemStack itemStack) {
        return ElectricConversions.museEnergyToMek(getMaxMPSEnergy(itemStack));
    }

    @Override
    public double getMaxTransfer(ItemStack itemStack) {
        return ElectricConversions.museEnergyToMek(getMaxMPSEnergy(itemStack));
    }

    @Override
    public boolean canReceive(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean canSend(ItemStack itemStack) {
        return true;
    }
}
