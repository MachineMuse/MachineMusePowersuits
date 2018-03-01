package net.machinemuse.numina.api.energy;

//import appeng.api.config.AccessRestriction;
//import appeng.api.implementations.items.IAEItemPowerStorage;
import cofh.redstoneflux.api.IEnergyContainerItem;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import mekanism.api.energy.IEnergizedItem;
import net.machinemuse.numina.api.item.IMuseItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

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
        @Optional.Interface(iface = "mekanism.api.energy.IEnergizedItem", modid = "Mekanism", striprefs = true)
})
public interface IMuseElectricItem
        extends IMuseItem,
        IEnergyContainerItem,
        ISpecialElectricItem,
        IElectricItemManager,
        IEnergizedItem {
//
//    // TODO get rid of player energy references and use electric item utils instead
//    /**
//     * Returns the amount of energy contained in the player's inventory.
//     *
//     * @param player
//     * @return
//     */
//    double getPlayerEnergy(EntityPlayer player);
//
//    /**
//     * Drains the amount of energy from the player's inventory.
//     *
//     * @param player
//     * @param drainAmount
//     * @return
//     */
//    void drainPlayerEnergy(EntityPlayer player, double drainAmount);
//
//    /**
//     * Adds the amount of energy to the player's inventory.
//     *
//     * @param player
//     * @param joulesToGive
//     * @return
//     */
//    void givePlayerEnergy(EntityPlayer player, double joulesToGive);


    /**
     * Call to get the energy of an item
     *
     * @param stack ItemStack to set
     * @return Current energy level
     */
    int getCurrentMPSEnergy(ItemStack stack);

    /**
     * Call to set the energy of an item
     *
     * @param stack ItemStack to set
     * @return Maximum energy level
     */
    int getMaxMPSEnergy(ItemStack stack);

    /**
     * Call to set the energy of an item
     *
     * @param stack ItemStack to set
     * @param energy Level to set it to
     */
    void setCurrentMPSEnergy(ItemStack stack, int energy);

    /**
     * Call to drain energy from an item
     *
     * @param stack ItemStack being requested for energy
     * @param requested Amount of energy to drain
     * @return Amount of energy successfully drained
     */
    int drainMPSEnergyFrom(ItemStack stack, int requested);

    /**
     * Call to give energy to an item
     *
     * @param stack ItemStack being provided with energy
     * @param provided Amount of energy to add
     * @return Amount of energy added
     */
    int giveMPSEnergyTo(ItemStack stack, int provided);

    /* Industrialcraft 2 -------------------------------------------------------------------------- */
    IMuseElectricItem getManager(ItemStack stack);

    @Override
    void chargeFromArmor(ItemStack itemStack, EntityLivingBase entity);

    @Override
    boolean use(ItemStack itemStack, double amount, EntityLivingBase entity);

    boolean canProvideEnergy(ItemStack itemStack);

    @Override
    double getCharge(ItemStack itemStack);

    double getMaxCharge(ItemStack itemStack);

    int getTier(ItemStack itemStack);

    double getTransferLimit(ItemStack itemStack);

    @Override
    double charge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate);

    @Override
    double discharge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate);

    @Override
    boolean canUse(ItemStack itemStack, double amount);

    @Override
    String getToolTip(ItemStack itemStack);

    Item getChargedItem(ItemStack itemStack);

    Item getEmptyItem(ItemStack itemStack);

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