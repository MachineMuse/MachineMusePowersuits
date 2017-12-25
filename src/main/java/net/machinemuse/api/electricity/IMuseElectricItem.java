package net.machinemuse.api.electricity;

//import appeng.api.config.AccessRestriction;
//import appeng.api.implementations.item.IAEItemPowerStorage;
//import cofh.api.energy.IEnergyContainerItem;
//import ic2.api.item.IElectricItemManager;
//import ic2.api.item.ISpecialElectricItem;
import mekanism.api.energy.IEnergizedItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:12 PM, 4/20/13
 *
 * Ported to Java by lehjr on 11/3/16.
 */
@Optional.InterfaceList({
//        @Optional.Interface(iface = "cofh.api.energy.IEnergyContainerItem", modid = "CoFHAPI|energy", striprefs = true),
//        @Optional.Interface(iface = "ic2.api.item.IElectricItemManager", modid = "IC2", striprefs = true),
//        @Optional.Interface(iface = "ic2.api.item.ISpecialElectricItem", modid = "IC2", striprefs = true),
        @Optional.Interface(iface = "mekanism.api.energy.IEnergizedItem", modid = "Mekanism", striprefs = true)
})
public interface IMuseElectricItem extends
//        IEnergyContainerItem,
//        ISpecialElectricItem,
//        IElectricItemManager,
        IEnergizedItem {
    /**
     * Call to get the energy of an item
     *
     * @param stack ItemStack to set
     * @return Current energy level
     */
    double getCurrentMPSEnergy(ItemStack stack);

    /**
     * Call to set the energy of an item
     *
     * @param stack ItemStack to set
     * @return Maximum energy level
     */
    double getMaxMPSEnergy(ItemStack stack);

    /**
     * Call to set the energy of an item
     *
     * @param stack ItemStack to set
     * @param energy Level to set it to
     */
    void setCurrentMPSEnergy(ItemStack stack, double energy);

    /**
     * Call to drain energy from an item
     *
     * @param stack ItemStack being requested for energy
     * @param requested Amount of energy to drain
     * @return Amount of energy successfully drained
     */
    double drainMPSEnergyFrom(ItemStack stack, double requested);

    /**
     * Call to give energy to an item
     *
     * @param stack ItemStack being provided with energy
     * @param provided Amount of energy to add
     * @return Amount of energy added
     */
    double giveMPSEnergyTo(ItemStack stack, double provided);

    /* Industrialcraft 2 -------------------------------------------------------------------------- */
//    IMuseElectricItem getManager(ItemStack stack);
//
//    @Override
//    void chargeFromArmor(ItemStack itemStack, EntityLivingBase entities);
//
//    @Override
//    boolean use(ItemStack itemStack, double amount, EntityLivingBase entities);
//
//    boolean canProvideEnergy(ItemStack itemStack);
//
//    @Override
//    double getCharge(ItemStack itemStack);
//
//    double getMaxCharge(ItemStack itemStack);
//
//    int getTier(ItemStack itemStack);
//
//    double getTransferLimit(ItemStack itemStack);
//
//    @Override
//    double charge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate);
//
//    @Override
//    double discharge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate);
//
//    @Override
//    boolean canUse(ItemStack itemStack, double amount);
//
//    @Override
//    String getToolTip(ItemStack itemStack);
//
//    Item getChargedItem(ItemStack itemStack);
//
//    Item getEmptyItem(ItemStack itemStack);

    /* Thermal Expansion -------------------------------------------------------------------------- */
    int receiveEnergy(ItemStack stack, int energy, boolean simulate);

    int extractEnergy(ItemStack stack, int energy, boolean simulate);

    int getEnergyStored(ItemStack theItem);

    int getMaxEnergyStored(ItemStack theItem);

    int getMaxDamage(ItemStack itemStack);

    /* Mekanism ----------------------------------------------------------------------------------- */
    @Override
    double getEnergy(ItemStack itemStack);

    @Override
    void setEnergy(ItemStack itemStack, double v);

    @Override
    double getMaxEnergy(ItemStack itemStack);

    @Override
    double getMaxTransfer(ItemStack itemStack);

    @Override
    boolean canReceive(ItemStack itemStack);

    @Override
    boolean canSend(ItemStack itemStack);
}
