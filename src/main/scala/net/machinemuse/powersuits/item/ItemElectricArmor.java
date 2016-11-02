package net.machinemuse.powersuits.item;

import ic2.api.item.IElectricItemManager;
import appeng.api.config.AccessRestriction;
import net.minecraft.item.Item;
import net.minecraft.entity.EntityLivingBase;
import net.machinemuse.api.electricity.MuseElectricItem;
import net.machinemuse.api.electricity.MuseElectricItem$class;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.machinemuse.numina.geometry.Colour;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import scala.reflect.ScalaSignature;
import net.minecraft.item.ItemArmor;

/**
 * Ported to Java by lehjr on 10/26/16.
 */
public abstract class ItemElectricArmor extends ItemArmor implements ModularItemBase
{
    public ItemElectricArmor(final ItemArmor.ArmorMaterial material, final int index1, final int index2) {
        super(material, index1, index2);
//        MuseElectricItem$class.$init$(this);
//        ModularItemBase$class.$init$(this);
    }

    @Override
    public String getToolTip(ItemStack itemStack) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(final ItemStack stack, final int par2) {
        return ModularItemBase$class.getColorFromItemStack(this, stack, par2);
    }

    public Colour getGlowFromItemStack(final ItemStack stack) {
        return ModularItemBase$class.getGlowFromItemStack(this, stack);
    }

    public Colour getColorFromItemStack(final ItemStack stack) {
        return ModularItemBase$class.getColorFromItemStack(this, stack);
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return ModularItemBase$class.requiresMultipleRenderPasses(this);
    }

//    @SideOnly(Side.CLIENT)
//    public void addInformation(final ItemStack stack, final EntityPlayer player, final List<?> currentTipList, final boolean advancedToolTips) {
//        ModularItemBase$class.addInformation(this, stack, player, currentTipList, advancedToolTips);
//    }

    public String formatInfo(final String string, final double value) {
        return ModularItemBase$class.formatInfo(this, string, value);
    }

    public List<String> getLongInfo(final EntityPlayer player, final ItemStack stack) {
        return (List<String>)ModularItemBase$class.getLongInfo(this, player, stack);
    }

    public double getArmorDouble(final EntityPlayer player, final ItemStack stack) {
        return ModularItemBase$class.getArmorDouble(this, player, stack);
    }

    public double getPlayerEnergy(final EntityPlayer player) {
        return ModularItemBase$class.getPlayerEnergy(this, player);
    }

    public void drainPlayerEnergy(final EntityPlayer player, final double drainEnergy) {
        ModularItemBase$class.drainPlayerEnergy(this, player, drainEnergy);
    }

    public void givePlayerEnergy(final EntityPlayer player, final double joulesToGive) {
        ModularItemBase$class.givePlayerEnergy(this, player, joulesToGive);
    }

    public double getCurrentEnergy(final ItemStack stack) {
        return MuseElectricItem$class.getCurrentEnergy(this, stack);
    }

    public double getMaxEnergy(final ItemStack stack) {
        return MuseElectricItem$class.getMaxEnergy(this, stack);
    }

    public void setCurrentEnergy(final ItemStack stack, final double energy) {
        MuseElectricItem$class.setCurrentEnergy(this, stack, energy);
    }

    public double drainEnergyFrom(final ItemStack stack, final double requested) {
        return MuseElectricItem$class.drainEnergyFrom(this, stack, requested);
    }

    public double giveEnergyTo(final ItemStack stack, final double provided) {
        return MuseElectricItem$class.giveEnergyTo(this, stack, provided);
    }

    public MuseElectricItem getManager(final ItemStack itemStack) {
        return MuseElectricItem$class.getManager(this, itemStack);
    }

    public void chargeFromArmor(final ItemStack itemStack, final EntityLivingBase entity) {
        MuseElectricItem$class.chargeFromArmor(this, itemStack, entity);
    }

    public boolean use(final ItemStack itemStack, final double amount, final EntityLivingBase entity) {
        return MuseElectricItem$class.use(this, itemStack, amount, entity);
    }

    public boolean canProvideEnergy(final ItemStack itemStack) {
        return MuseElectricItem$class.canProvideEnergy(this, itemStack);
    }

    public double getCharge(final ItemStack itemStack) {
        return MuseElectricItem$class.getCharge(this, itemStack);
    }

    public double getMaxCharge(final ItemStack itemStack) {
        return MuseElectricItem$class.getMaxCharge(this, itemStack);
    }

    public int getTier(final ItemStack itemStack) {
        return MuseElectricItem$class.getTier(this, itemStack);
    }

    public double getTransferLimit(final ItemStack itemStack) {
        return MuseElectricItem$class.getTransferLimit(this, itemStack);
    }

    public double charge(final ItemStack itemStack, final double amount, final int tier, final boolean ignoreTransferLimit, final boolean simulate) {
        return MuseElectricItem$class.charge(this, itemStack, amount, tier, ignoreTransferLimit, simulate);
    }

    public double discharge(final ItemStack itemStack, final double amount, final int tier, final boolean ignoreTransferLimit, final boolean externally, final boolean simulate) {
        return MuseElectricItem$class.discharge(this, itemStack, amount, tier, ignoreTransferLimit, externally, simulate);
    }

    public boolean canUse(final ItemStack itemStack, final double amount) {
        return MuseElectricItem$class.canUse(this, itemStack, amount);
    }

    public Item getChargedItem(final ItemStack itemStack) {
        return MuseElectricItem$class.getChargedItem(this, itemStack);
    }

    public Item getEmptyItem(final ItemStack itemStack) {
        return MuseElectricItem$class.getEmptyItem(this, itemStack);
    }

    public int receiveEnergy(final ItemStack stack, final int energy, final boolean simulate) {
        return MuseElectricItem$class.receiveEnergy(this, stack, energy, simulate);
    }

    public int extractEnergy(final ItemStack stack, final int energy, final boolean simulate) {
        return MuseElectricItem$class.extractEnergy(this, stack, energy, simulate);
    }

    public int getEnergyStored(final ItemStack theItem) {
        return MuseElectricItem$class.getEnergyStored(this, theItem);
    }

    public int getMaxEnergyStored(final ItemStack theItem) {
        return MuseElectricItem$class.getMaxEnergyStored(this, theItem);
    }

    public int getMaxDamage(final ItemStack itemStack) {
        return MuseElectricItem$class.getMaxDamage(this, itemStack);
    }

    public double injectAEPower(final ItemStack stack, final double ae) {
        return MuseElectricItem$class.injectAEPower(this, stack, ae);
    }

    public double extractAEPower(final ItemStack stack, final double ae) {
        return MuseElectricItem$class.extractAEPower(this, stack, ae);
    }

    public double getAEMaxPower(final ItemStack stack) {
        return MuseElectricItem$class.getAEMaxPower(this, stack);
    }

    public double getAECurrentPower(final ItemStack stack) {
        return MuseElectricItem$class.getAECurrentPower(this, stack);
    }

    public AccessRestriction getPowerFlow(final ItemStack stack) {
        return MuseElectricItem$class.getPowerFlow(this, stack);
    }
}