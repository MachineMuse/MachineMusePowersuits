package net.machinemuse.api.electricity;

//import appeng.api.config.AccessRestriction;
import ic2.api.item.ElectricItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.client.Minecraft;
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
    public double getCurrentEnergy(ItemStack stack) {
        return MuseItemUtils.getDoubleOrZero(stack, ElectricItemUtils.CURRENT_ENERGY);
    }

    /**
     * Call to set the energy of an item
     *
     * @param stack ItemStack to set
     * @return Maximum energy level
     */
    public double getMaxEnergy(ItemStack stack) {
        return ModuleManager.computeModularProperty(stack, ElectricItemUtils.MAXIMUM_ENERGY);
    }

    /**
     * Call to set the energy of an item
     *
     * @param stack ItemStack to set
     * @param energy Level to set it to
     */
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

    /* Industrialcraft 2 -------------------------------------------------------------------------- */
    public IMuseElectricItem getManager(ItemStack stack) {
        return this;
    }

    @Override
    public void chargeFromArmor(ItemStack itemStack, EntityLivingBase entity) {
        ElectricItem.rawManager.chargeFromArmor(itemStack, entity);
    }

    @Override
    public boolean use(ItemStack itemStack, double amount, EntityLivingBase entity) {
        return ElectricItem.rawManager.use(itemStack, ElectricConversions.museEnergyToEU(amount), entity);
    }

    public boolean canProvideEnergy(ItemStack itemStack) {
        return true;
    }

    @Override
    public double getCharge(ItemStack itemStack) {
        return ElectricConversions.museEnergyToEU(getCurrentEnergy(itemStack));
    }

    public double getMaxCharge(ItemStack itemStack) {
        return ElectricConversions.museEnergyToEU(getMaxEnergy(itemStack));
    }

    public int getTier(ItemStack itemStack) {
        return ElectricConversions.getTier(itemStack);
    }

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
    public boolean canUse(ItemStack itemStack, double amount) {
        return ElectricConversions.museEnergyFromEU(amount) < getCurrentEnergy(itemStack);
    }

    @Override
    public String getToolTip(ItemStack itemStack) {
        return "";

//        return itemStack.getTooltip(Minecraft.getMinecraft().thePlayer, false).toString();
    }

    public Item getChargedItem(ItemStack itemStack) {
        return this;
    }

    public Item getEmptyItem(ItemStack itemStack) {
        return this;
    }

    /* Thermal Expansion -------------------------------------------------------------------------- */
    public int receiveEnergy(ItemStack stack, int energy, boolean simulate) {
        double current = getCurrentEnergy(stack);
        double receivedME = ElectricConversions.museEnergyFromRF(energy);
        double eatenME = giveEnergyTo(stack, receivedME);
        if (simulate) {
            setCurrentEnergy(stack, current);
        }
        return ElectricConversions.museEnergyToRF(eatenME);
    }

    public int extractEnergy(ItemStack stack, int energy, boolean simulate) {
        double current = getCurrentEnergy(stack);
        double requesteddME = ElectricConversions.museEnergyFromRF(energy);
        double takenME = drainEnergyFrom(stack, requesteddME);
        if (simulate) {
            setCurrentEnergy(stack, current);
        }
        return ElectricConversions.museEnergyToRF(takenME);
    }

    public int getEnergyStored(ItemStack theItem) {
        return ElectricConversions.museEnergyToRF(getCurrentEnergy(theItem));
    }

    public int getMaxEnergyStored(ItemStack theItem) {
        return ElectricConversions.museEnergyToRF(getMaxEnergy(theItem));
    }

    public int getMaxDamage(ItemStack itemStack) {
        return 0;
    }

    /* Applied Energistics 2 ---------------------------------------------------------------------- */
//    public double injectAEPower(ItemStack stack, double ae) {
//        double current = getCurrentEnergy(stack);
//        double recieved = ElectricConversions.museEnergyFromAE(ae);
//        setCurrentEnergy(stack, current);
//        return ElectricConversions.museEnergyToAE(recieved);
//    }
//
//    public double extractAEPower(ItemStack stack, double ae) {
//        double current = getCurrentEnergy(stack);
//        double taken = ElectricConversions.museEnergyFromAE(ae);
//        setCurrentEnergy(stack, current);
//        return ElectricConversions.museEnergyToAE(taken);
//    }
//
//    public double getAEMaxPower(ItemStack stack) {
//        return ElectricConversions.museEnergyToAE(getCurrentEnergy(stack));
//    }
//
//    public double getAECurrentPower(ItemStack stack) {
//        return ElectricConversions.museEnergyToAE(getCurrentEnergy(stack));
//    }
//
//    public AccessRestriction getPowerFlow(ItemStack stack) {
//        return AccessRestriction.READ_WRITE;
//    }
}
