package net.machinemuse.api.electricity;

import appeng.api.config.AccessRestriction;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItemManager;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.EntityLivingBase;
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
    @Override
    public double getCurrentEnergy(ItemStack stack) {
        return MuseItemUtils.getDoubleOrZero(stack, ElectricItemUtils.CURRENT_ENERGY);
    }

    /**
     * Call to set the energy of an item
     *
     * @param stack ItemStack to set
     * @return Maximum energy level
     */
    @Override
    public double getMaxEnergy(ItemStack stack) {
        return ModuleManager.computeModularProperty(stack, ElectricItemUtils.MAXIMUM_ENERGY);
    }

    /**
     * Call to set the energy of an item
     *
     * @param stack ItemStack to set
     * @param energy Level to set it to
     */
    @Override
    public void setCurrentEnergy(ItemStack stack, double energy) {
        MuseItemUtils.setDoubleOrRemove(stack, ElectricItemUtils.CURRENT_ENERGY, Math.min(energy, getMaxEnergy(stack)));
    }

    /**
     * Call to drain energy from an item
     *
     * @param stack ItemStack being requested for energy
     * @param requested Amount of energy to drain
     * @return Amount of energy successfully drained
     */
    @Override
    public double drainEnergyFrom(ItemStack stack, double requested) {
        double available = getCurrentEnergy(stack);
        if (available > requested) {
            setCurrentEnergy(stack, available - requested);
            return requested;
        } else {
            setCurrentEnergy(stack, 0);
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
    @Override
    public double giveEnergyTo(ItemStack stack, double provided) {
        double available = getCurrentEnergy(stack);
        double max = getMaxEnergy(stack);
        if (available + provided < max) {
            setCurrentEnergy(stack, available + provided);
            return provided;
        } else {
            setCurrentEnergy(stack, max);
            return max - available;
        }
    }

    @Override
    public int getMaxDamage(ItemStack itemStack) {
        return 0;
    }

    /* Industrialcraft 2 -------------------------------------------------------------------------- */
    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return true;
    }

    @Override
    public Item getChargedItem(ItemStack itemStack) {
        return this;
    }

    @Override
    public Item getEmptyItem(ItemStack itemStack) {
        return this;
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return ElectricConversions.museEnergyToEU(getMaxEnergy(itemStack));
    }

    @Override
    public int getTier(ItemStack itemStack) {
        return ElectricConversions.getTier(itemStack);
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return ElectricConversions.museEnergyToEU(Math.sqrt(getMaxEnergy(itemStack)));
    }

    @Override
    public double charge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate){
        double current = getCurrentEnergy(itemStack);
        double transfer = (ignoreTransferLimit || amount < getTransferLimit(itemStack)) ? ElectricConversions.museEnergyFromEU(amount) : getTransferLimit(itemStack);
        double given = giveEnergyTo(itemStack, transfer);
        if (simulate) {
            setCurrentEnergy(itemStack, current);
        }
        return ElectricConversions.museEnergyToEU(given);
    }

    @Override
    public double discharge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate) {
        double current = getCurrentEnergy(itemStack);
        double transfer = (ignoreTransferLimit || amount < getTransferLimit(itemStack)) ? ElectricConversions.museEnergyFromEU(amount) : getTransferLimit(itemStack);
        double taken = drainEnergyFrom(itemStack, transfer);
        if (simulate) {
            setCurrentEnergy(itemStack, current);
        }
        return ElectricConversions.museEnergyToEU(taken);
    }

    @Override
    public double getCharge(ItemStack itemStack) {
        return ElectricConversions.museEnergyToEU(getCurrentEnergy(itemStack));
    }

    @Override
    public boolean canUse(ItemStack itemStack, double amount) {
        return ElectricConversions.museEnergyFromEU(amount) < getCurrentEnergy(itemStack);
    }

    @Override
    public boolean use(ItemStack itemStack, double amount, EntityLivingBase entity) {
        return ElectricItem.rawManager.use(itemStack, ElectricConversions.museEnergyToEU(amount), entity);
    }

    @Override
    public void chargeFromArmor(ItemStack itemStack, EntityLivingBase entity) {
        ElectricItem.rawManager.chargeFromArmor(itemStack, entity);
    }

    @Override
    public String getToolTip(ItemStack itemStack) {
//        return itemStack.getTooltip(Minecraft.getMinecraft().thePlayer, false).toString(); // enabling this doubles up the tooltips
        return "";
    }

    @Override
    public IElectricItemManager getManager(ItemStack itemStack) {
        return this;
    }


    /* Thermal Expansion -------------------------------------------------------------------------- */
    @Override
    public int receiveEnergy(ItemStack itemStack, int energy, boolean simulate) {
        double current = getCurrentEnergy(itemStack);
        double receivedME = ElectricConversions.museEnergyFromRF(energy);
        double eatenME = giveEnergyTo(itemStack, receivedME);
        if (simulate) setCurrentEnergy(itemStack, current);
        return ElectricConversions.museEnergyToRF(eatenME);
    }

    @Override
    public int extractEnergy(ItemStack itemStack, int energy, boolean simulate) {
        double current = getCurrentEnergy(itemStack);
        double requesteddME = ElectricConversions.museEnergyFromRF(energy);
        double takenME = drainEnergyFrom(itemStack, requesteddME);
        if (simulate) {
            setCurrentEnergy(itemStack, current);
        }
        return ElectricConversions.museEnergyToRF(takenME);
    }

    @Override
    public int getEnergyStored(ItemStack itemStack) {
        return ElectricConversions.museEnergyToRF(getCurrentEnergy(itemStack));
    }

    @Override
    public int getMaxEnergyStored(ItemStack itemStack) {
        return ElectricConversions.museEnergyToRF(getMaxEnergy(itemStack));
    }


    /* Applied Energistics 2 ---------------------------------------------------------------------- */
    @Override
    public double injectAEPower(ItemStack itemStack, double ae) {
        double current = getCurrentEnergy(itemStack);
        double recieved = ElectricConversions.museEnergyFromAE(ae);
        setCurrentEnergy(itemStack, current);
        return ElectricConversions.museEnergyToAE(recieved);
    }

    @Override
    public double extractAEPower(ItemStack itemStack, double ae) {
        double current = getCurrentEnergy(itemStack);
        double taken = ElectricConversions.museEnergyFromAE(ae);
        setCurrentEnergy(itemStack, current);
        return ElectricConversions.museEnergyToAE(taken);
    }

    @Override
    public double getAEMaxPower(ItemStack itemStack) {
        return ElectricConversions.museEnergyToAE(getCurrentEnergy(itemStack));
    }

    @Override
    public double getAECurrentPower(ItemStack itemStack) {
        return ElectricConversions.museEnergyToAE(getCurrentEnergy(itemStack));
    }

    @Override
    public AccessRestriction getPowerFlow(ItemStack itemStack) {
        return AccessRestriction.READ_WRITE;
    }
}
