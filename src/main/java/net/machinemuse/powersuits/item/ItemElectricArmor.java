package net.machinemuse.powersuits.item;

import appeng.api.config.AccessRestriction;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.machinemuse.api.electricity.IMuseElectricItem;
import net.machinemuse.api.electricity.MuseElectricItem;
import net.machinemuse.numina.geometry.Colour;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Ported to Java by lehjr on 10/26/16.
 */
public abstract class ItemElectricArmor extends ItemArmor implements IModularItemBase, IMuseElectricItem {
    public ItemElectricArmor(final ItemArmor.ArmorMaterial material, final int index1, final int index2) {
        super(material, index1, index2);
    }

    @Override
    public String getToolTip(ItemStack itemStack) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(final ItemStack stack, final int par2) {
        return ModularItemBase.getInstance().getColorFromItemStack(stack, par2);
    }

    public Colour getGlowFromItemStack(final ItemStack stack) {
        return ModularItemBase.getInstance().getGlowFromItemStack(stack);
    }

    public Colour getColorFromItemStack(final ItemStack stack) {
        return ModularItemBase.getInstance().getColorFromItemStack(stack);
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return ModularItemBase.getInstance().requiresMultipleRenderPasses();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List currentTipList, boolean advancedToolTips) {
        ModularItemBase.getInstance().addInformation(stack, player, currentTipList, advancedToolTips);
    }

    public String formatInfo(final String string, final double value) {
        return ModularItemBase.getInstance().formatInfo(string, value);
    }

    public List<String> getLongInfo(final EntityPlayer player, final ItemStack stack) {
        return ModularItemBase.getInstance().getLongInfo(player, stack);
    }

    public double getArmorDouble(final EntityPlayer player, final ItemStack stack) {
        return ModularItemBase.getInstance().getArmorDouble(player, stack);
    }

    public double getPlayerEnergy(final EntityPlayer player) {
        return ModularItemBase.getInstance().getPlayerEnergy(player);
    }

    public void drainPlayerEnergy(final EntityPlayer player, final double drainEnergy) {
        ModularItemBase.getInstance().drainPlayerEnergy(player, drainEnergy);
    }

    public void givePlayerEnergy(final EntityPlayer player, final double joulesToGive) {
        ModularItemBase.getInstance().givePlayerEnergy(player, joulesToGive);
    }

    public double getCurrentEnergy(final ItemStack stack) {
        return MuseElectricItem.getInstance().getCurrentEnergy(stack);
    }

    public double getMaxEnergy(final ItemStack stack) {
        return MuseElectricItem.getInstance().getMaxEnergy(stack);
    }

    public void setCurrentEnergy(final ItemStack stack, final double energy) {
        MuseElectricItem.getInstance().setCurrentEnergy(stack, energy);
    }

    public double drainEnergyFrom(final ItemStack stack, final double requested) {
        return MuseElectricItem.getInstance().drainEnergyFrom(stack, requested);
    }

    public double giveEnergyTo(final ItemStack stack, final double provided) {
        return MuseElectricItem.getInstance().giveEnergyTo(stack, provided);
    }

    public IMuseElectricItem getManager(final ItemStack itemStack) {
        return (IMuseElectricItem) MuseElectricItem.getInstance().getManager(itemStack);
    }

    public void chargeFromArmor(final ItemStack itemStack, final EntityLivingBase entity) {
        MuseElectricItem.getInstance().chargeFromArmor(itemStack, entity);
    }

    public boolean use(final ItemStack itemStack, final double amount, final EntityLivingBase entity) {
        return MuseElectricItem.getInstance().use(itemStack, amount, entity);
    }

    public boolean canProvideEnergy(final ItemStack itemStack) {
        return MuseElectricItem.getInstance().canProvideEnergy(itemStack);
    }

    public double getCharge(final ItemStack itemStack) {
        return MuseElectricItem.getInstance().getCharge(itemStack);
    }

    public double getMaxCharge(final ItemStack itemStack) {
        return MuseElectricItem.getInstance().getMaxCharge(itemStack);
    }

    public int getTier(final ItemStack itemStack) {
        return MuseElectricItem.getInstance().getTier(itemStack);
    }

    public double getTransferLimit(final ItemStack itemStack) {
        return MuseElectricItem.getInstance().getTransferLimit(itemStack);
    }

    public double charge(final ItemStack itemStack, final double amount, final int tier, final boolean ignoreTransferLimit, final boolean simulate) {
        return MuseElectricItem.getInstance().charge(itemStack, amount, tier, ignoreTransferLimit, simulate);
    }

    public double discharge(final ItemStack itemStack, final double amount, final int tier, final boolean ignoreTransferLimit, final boolean externally, final boolean simulate) {
        return MuseElectricItem.getInstance().discharge(itemStack, amount, tier, ignoreTransferLimit, externally, simulate);
    }

    public boolean canUse(final ItemStack itemStack, final double amount) {
        return MuseElectricItem.getInstance().canUse(itemStack, amount);
    }

    public Item getChargedItem(final ItemStack itemStack) {
        return MuseElectricItem.getInstance().getChargedItem(itemStack);
    }

    public Item getEmptyItem(final ItemStack itemStack) {
        return MuseElectricItem.getInstance().getEmptyItem(itemStack);
    }

    public int receiveEnergy(final ItemStack stack, final int energy, final boolean simulate) {
        return MuseElectricItem.getInstance().receiveEnergy(stack, energy, simulate);
    }

    public int extractEnergy(final ItemStack stack, final int energy, final boolean simulate) {
        return MuseElectricItem.getInstance().extractEnergy(stack, energy, simulate);
    }

    public int getEnergyStored(final ItemStack theItem) {
        return MuseElectricItem.getInstance().getEnergyStored(theItem);
    }

    public int getMaxEnergyStored(final ItemStack theItem) {
        return MuseElectricItem.getInstance().getMaxEnergyStored(theItem);
    }

    public int getMaxDamage(final ItemStack itemStack) {
        return MuseElectricItem.getInstance().getMaxDamage(itemStack);
    }

    /* Applied Energistics 2 ---------------------------------------------------------------------- */
    public double injectAEPower(final ItemStack stack, final double ae) {
        return MuseElectricItem.getInstance().injectAEPower(stack, ae);
    }

    public double extractAEPower(final ItemStack stack, final double ae) {
        return MuseElectricItem.getInstance().extractAEPower(stack, ae);
    }

    public double getAEMaxPower(final ItemStack stack) {
        return MuseElectricItem.getInstance().getAEMaxPower(stack);
    }

    public double getAECurrentPower(final ItemStack stack) {
        return MuseElectricItem.getInstance().getAECurrentPower(stack);
    }

    public AccessRestriction getPowerFlow(final ItemStack stack) {
        return MuseElectricItem.getInstance().getPowerFlow(stack);
    }
}
