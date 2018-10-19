package net.machinemuse.api.electricity;

import appeng.api.config.AccessRestriction;
import appeng.api.implementations.items.IAEItemPowerStorage;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.Optional;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:12 PM, 4/20/13
 *
 * Ported to Java by lehjr on 11/3/16.
 */
@Optional.InterfaceList({
        @Optional.Interface(iface = "cofh.api.energy.IEnergyContainerItem", modid = "CoFHAPI|energy", striprefs = true),
        @Optional.Interface(iface = "ic2.api.item.IElectricItemManager", modid = "IC2", striprefs = true),
        @Optional.Interface(iface = "ic2.api.item.ISpecialElectricItem", modid = "IC2", striprefs = true),
        @Optional.Interface(iface = "appeng.api.implementations.items.IAEItemPowerStorage", modid = "appliedenergistics2", striprefs = true)
})
public interface IMuseElectricItem extends
        IEnergyContainerItem,
        ISpecialElectricItem,
        IElectricItemManager,
        IElectricItem,
        IAEItemPowerStorage {
    /**
     * Call to get the energy of an item
     *
     * @param stack ItemStack to set
     * @return Current energy level
     */
    double getCurrentEnergy(ItemStack stack);

    /**
     * Call to set the energy of an item
     *
     * @param stack ItemStack to set
     * @return Maximum energy level
     */
    double getMaxEnergy(ItemStack stack);

    /**
     * Call to set the energy of an item
     *
     * @param stack ItemStack to set
     * @param energy Level to set it to
     */
    void setCurrentEnergy(ItemStack stack, double energy);

    /**
     * Call to drain energy from an item
     *
     * @param stack ItemStack being requested for energy
     * @param requested Amount of energy to drain
     * @return Amount of energy successfully drained
     */
    double drainEnergyFrom(ItemStack stack, double requested);

    /**
     * Call to give energy to an item
     *
     * @param stack ItemStack being provided with energy
     * @param provided Amount of energy to add
     * @return Amount of energy added
     */
    double giveEnergyTo(ItemStack stack, double provided);

    /**
     * Override max damage of an item. Nothing to do with power, at all :P
     */
    int getMaxDamage(ItemStack itemStack);


    /* Industrialcraft 2 -------------------------------------------------------------------------- */
    @Override
    boolean canProvideEnergy(ItemStack itemStack);

    @Override
    Item getChargedItem(ItemStack itemStack);

    @Override
    Item getEmptyItem(ItemStack itemStack);

    @Override
    double getMaxCharge(ItemStack itemStack);

    @Override
    int getTier(ItemStack itemStack);

    @Override
    double getTransferLimit(ItemStack itemStack);

    @Override
    double charge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate);

    @Override
    double discharge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate);

    @Override
    double getCharge(ItemStack itemStack);

    @Override
    boolean canUse(ItemStack itemStack, double amount);

    @Override
    boolean use(ItemStack itemStack, double amount, EntityLivingBase entity);

    @Override
    void chargeFromArmor(ItemStack itemStack, EntityLivingBase entity);

    @Override
    String getToolTip(ItemStack itemStack);

    @Override
    IElectricItemManager getManager(ItemStack itemStack);

    /* Thermal Expansion -------------------------------------------------------------------------- */
    @Override
    int receiveEnergy(ItemStack itemStack, int energy, boolean simulate);

    @Override
    int extractEnergy(ItemStack itemStack, int energy, boolean simulate);

    @Override
    int getEnergyStored(ItemStack itemStack);

    @Override
    int getMaxEnergyStored(ItemStack itemStack);


    /* Applied Energistics 2 ---------------------------------------------------------------------- */
    @Override
    double injectAEPower(ItemStack itemStack, double ae);

    @Override
    double extractAEPower(ItemStack itemStack, double ae);

    @Override
    double getAEMaxPower(ItemStack itemStack);

    @Override
    double getAECurrentPower(ItemStack itemStack);

    @Override
    AccessRestriction getPowerFlow(ItemStack itemStack);
}
