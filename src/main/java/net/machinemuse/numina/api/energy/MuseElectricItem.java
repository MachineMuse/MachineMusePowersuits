//package net.machinemuse.numina.api.energy;
//
////import appeng.api.config.AccessRestriction;
//
//import ic2.api.item.ElectricItem;
//import mekanism.api.energy.IEnergizedItem;
//import net.machinemuse.numina.api.module.ModuleManager;
//import net.machinemuse.numina.utils.energy.ElectricItemUtils;
//import net.machinemuse.powersuits.utils.MuseItemUtils;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//
///**
// * Author: MachineMuse (Claire Semple)
// * Created: 10:12 PM, 4/20/13
// *
// * Ported to Java by lehjr on 11/3/16.
// */
//public class MuseElectricItem extends Item implements IMuseElectricItem {
//    private static MuseElectricItem INSTANCE;
//    private MuseElectricItem() {}
//    public static MuseElectricItem getInstance() {
//        if (INSTANCE == null) {
//            synchronized (MuseElectricItem.class) {
//                if (INSTANCE == null) INSTANCE = new MuseElectricItem();
//            }
//        }
//        return INSTANCE;
//    }
//
//    /**
//     * Call to get the energy of an item
//     *
//     * @param stack ItemStack to set
//     * @return Current energy level
//     */
//    public double getEnergyStored(ItemStack stack) {
//        return MuseItemUtils.getDoubleOrZero(stack, ElectricItemUtils.CURRENT_ENERGY);
//    }
//
//    /**
//     * Call to set the energy of an item
//     *
//     * @param stack ItemStack to set
//     * @return Maximum energy level
//     */
//    public double getMaxEnergyStored(ItemStack stack) {
//        return ModuleManager.getInstance().computeModularPropertyDouble(stack, ElectricItemUtils.MAXIMUM_ENERGY);
//    }
//
//    /**
//     * Call to set the energy of an item
//     *
//     * @param stack ItemStack to set
//     * @param energy Level to set it to
//     */
//    public void setCurrentEnergy(ItemStack stack, double energy) {
//        MuseItemUtils.setDoubleOrRemove(stack, ElectricItemUtils.CURRENT_ENERGY, Math.min(energy, getMaxEnergyStored(stack)));
//    }
//
//    /**
//     * Call to drain energy from an item
//     *
//     * @param stack ItemStack being requested for energy
//     * @param requested Amount of energy to drain
//     * @return Amount of energy successfully drained
//     */
//    public double extractEnergy(ItemStack stack, double requested) {
//        double available = getEnergyStored(stack);
//        if (available > requested) {
//            setCurrentEnergy(stack, available - requested);
//            return requested;
//        } else {
//            setCurrentEnergy(stack, 0);
//            return available;
//        }
//    }
//
//    /**
//     * Call to give energy to an item
//     *
//     * @param stack ItemStack being provided with energy
//     * @param provided Amount of energy to add
//     * @return Amount of energy added
//     */
//    public double receiveEnergy(ItemStack stack, double provided) {
//        double available = getEnergyStored(stack);
//        double max = getMaxEnergyStored(stack);
//
//        if (available + provided < max) {
//            setCurrentEnergy(stack, available + provided);
//            return provided;
//        } else {
//            setCurrentEnergy(stack, max);
//            return max - available;
//        }
//    }
//
//    /* Industrialcraft 2 -------------------------------------------------------------------------- */
//    public IMuseElectricItem getManager(ItemStack stack) {
//        return this;
//    }
//
//    @Override
//    public void chargeFromArmor(ItemStack itemStack, EntityLivingBase entity) {
//        ElectricItem.rawManager.chargeFromArmor(itemStack, entity);
//    }
//
//    @Override
//    public boolean use(ItemStack itemStack, double amount, EntityLivingBase entity) {
//        return ElectricItem.rawManager.use(itemStack, ElectricConversions.museEnergyToEU(amount), entity);
//    }
//
//    public boolean canProvideEnergy(ItemStack itemStack) {
//        if (itemStack != null) {
//            Item item = itemStack.getItem();
//            if (itemStack.getItem() instanceof IEnergizedItem)
//                return ((IEnergizedItem)item).canSend(itemStack);
//        }
//        return true;
//    }
//
//    @Override
//    public double getCharge(ItemStack itemStack) {
//        return ElectricConversions.museEnergyToEU(getEnergyStored(itemStack));
//    }
//
//    public double getMaxCharge(ItemStack itemStack) {
//        return ElectricConversions.museEnergyToEU(getMaxEnergyStored(itemStack));
//    }
//
//    public int getTier(ItemStack itemStack) {
//        return ElectricConversions.getTier(itemStack);
//    }
//
//    public double getTransferLimit(ItemStack itemStack) {
//        return ElectricConversions.museEnergyToEU(Math.sqrt(getMaxEnergyStored(itemStack)));
//    }
//
//    @Override
//    public double charge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate){
//        double current = getEnergyStored(itemStack);
//        double transfer = (ignoreTransferLimit || amount < getTransferLimit(itemStack)) ? ElectricConversions.museEnergyFromEU(amount) : getTransferLimit(itemStack);
//        double given = receiveEnergy(itemStack, transfer);
//        if (simulate) {
//            setCurrentEnergy(itemStack, current);
//        }
//        return ElectricConversions.museEnergyToEU(given);
//    }
//
//    @Override
//    public double discharge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate) {
//        double current = getEnergyStored(itemStack);
//        double transfer = (ignoreTransferLimit || amount < getTransferLimit(itemStack)) ? ElectricConversions.museEnergyFromEU(amount) : getTransferLimit(itemStack);
//        double taken = extractEnergy(itemStack, transfer);
//        if (simulate) {
//            setCurrentEnergy(itemStack, current);
//        }
//        return ElectricConversions.museEnergyToEU(taken);
//    }
//
//    @Override
//    public boolean canUse(ItemStack itemStack, double amount) {
//        return ElectricConversions.museEnergyFromEU(amount) < getEnergyStored(itemStack);
//    }
//
//    @Override
//    public String getToolTip(ItemStack itemStack) {
//        return "";
//
////        return itemStack.getTooltip(Minecraft.getMinecraft().player, false).toString();
//    }
//
//    public Item getChargedItem(ItemStack itemStack) {
//        return this;
//    }
//
//    public Item getEmptyItem(ItemStack itemStack) {
//        return this;
//    }
//
//    /* Thermal Expansion -------------------------------------------------------------------------- */
//    public int receiveEnergy(ItemStack stack, int energy, boolean simulate) {
//        double current = getEnergyStored(stack);
//        double receivedME = ElectricConversions.museEnergyFromRF(energy);
//        double eatenME = receiveEnergy(stack, receivedME);
//        if (simulate) {
//            setCurrentEnergy(stack, current);
//        }
//        return ElectricConversions.museEnergyToRF(eatenME);
//    }
//
//    public int extractEnergy(ItemStack stack, int energy, boolean simulate) {
//        double current = getEnergyStored(stack);
//        double requesteddME = ElectricConversions.museEnergyFromRF(energy);
//        double takenME = extractEnergy(stack, requesteddME);
//        if (simulate) {
//            setCurrentEnergy(stack, current);
//        }
//        return ElectricConversions.museEnergyToRF(takenME);
//    }
//
//    public int getEnergyStored(ItemStack theItem) {
//        return ElectricConversions.museEnergyToRF(getEnergyStored(theItem));
//    }
//
//    public int getMaxEnergyStored(ItemStack theItem) {
//        return ElectricConversions.museEnergyToRF(getMaxEnergyStored(theItem));
//    }
//
//    public int getMaxDamage(ItemStack itemStack) {
//        return 0;
//    }
//
//    /* Mekanism ----------------------------------------------------------------------------------- */
//    @Override
//    public double getEnergy(ItemStack itemStack) {
//        return ElectricConversions.museEnergyToMek(getEnergyStored(itemStack));
//    }
//
//    @Override
//    public void setEnergy(ItemStack itemStack, double v) {
//        setCurrentEnergy(itemStack, ElectricConversions.museEnergyFromMek(v));
//    }
//
//    @Override
//    public double getMaxEnergy(ItemStack itemStack) {
//        return ElectricConversions.museEnergyToMek(getMaxEnergyStored(itemStack));
//    }
//
//    @Override
//    public double getMaxTransfer(ItemStack itemStack) {
//        return ElectricConversions.museEnergyToMek(getMaxEnergyStored(itemStack));
//    }
//
//    @Override
//    public boolean canReceive(ItemStack itemStack) {
//        return true;
//    }
//
//    @Override
//    public boolean canSend(ItemStack itemStack) {
//        return true;
//    }
//
//
//
//
////
////    @Override
////    public boolean canExtract()
////    {
////        return this.maxExtract > 0;
////    }
////
////    @Override
////    public boolean canReceive()
////    {
////        return this.maxReceive > 0;
////    }
//}