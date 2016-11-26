package net.machinemuse.powersuits.item;

import appeng.api.config.AccessRestriction;
import net.machinemuse.api.electricity.IMuseElectricItem;
import net.machinemuse.api.electricity.MuseElectricItem;
import net.machinemuse.numina.geometry.Colour;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Ported to Java by lehjr on 10/26/16.
 */
public abstract class ItemElectricArmor extends ItemArmor implements IModularItemBase
{
    public ItemElectricArmor(ItemArmor.ArmorMaterial material, int renderIndexIn, EntityEquipmentSlot slot) {
        super(material, renderIndexIn, slot);
//        MuseElectricItem$class.$init$(this);
//        ModularItemBase$class.$init$(this);
    }

    @Override
    public String getToolTip(ItemStack itemStack) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int par2) {
        return ModularItemBase.getInstance().getColorFromItemStack(stack, par2);
    }

    public Colour getGlowFromItemStack(ItemStack stack) {
        return ModularItemBase.getInstance().getGlowFromItemStack(stack);
    }

    public Colour getColorFromItemStack(ItemStack stack) {
        return ModularItemBase.getInstance().getColorFromItemStack(stack);
    }

//    @SideOnly(Side.CLIENT)
//    public boolean requiresMultipleRenderPasses() {
//        return ModularItemBase.getInstance().requiresMultipleRenderPasses();
//    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> currentTipList, boolean advancedToolTips) {
        ModularItemBase.getInstance().addInformation(stack, playerIn, currentTipList, advancedToolTips);
    }

    public String formatInfo(String string, double value) {
        return ModularItemBase.getInstance().formatInfo(string, value);
    }

    public List<String> getLongInfo(EntityPlayer player, ItemStack stack) {
        return ModularItemBase.getInstance().getLongInfo(player, stack);
    }

    public double getArmorDouble(EntityPlayer player, ItemStack stack) {
        return ModularItemBase.getInstance().getArmorDouble(player, stack);
    }

    public double getPlayerEnergy(EntityPlayer player) {
        return ModularItemBase.getInstance().getPlayerEnergy(player);
    }

    public void drainPlayerEnergy(EntityPlayer player, double drainEnergy) {
        ModularItemBase.getInstance().drainPlayerEnergy(player, drainEnergy);
    }

    public void givePlayerEnergy(EntityPlayer player, double joulesToGive) {
        ModularItemBase.getInstance().givePlayerEnergy(player, joulesToGive);
    }

    public double getCurrentEnergy(ItemStack stack) {
        return MuseElectricItem.getInstance().getCurrentEnergy(stack);
    }

    public double getMaxEnergy(ItemStack stack) {
        return MuseElectricItem.getInstance().getMaxEnergy(stack);
    }

    public void setCurrentEnergy(ItemStack stack, double energy) {
        MuseElectricItem.getInstance().setCurrentEnergy(stack, energy);
    }

    public double drainEnergyFrom(ItemStack stack, double requested) {
        return MuseElectricItem.getInstance().drainEnergyFrom(stack, requested);
    }

    public double giveEnergyTo(ItemStack stack, double provided) {
        return MuseElectricItem.getInstance().giveEnergyTo(stack, provided);
    }

    public IMuseElectricItem getManager(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getManager(itemStack);
    }

    public void chargeFromArmor(ItemStack itemStack, EntityLivingBase entity) {
        MuseElectricItem.getInstance().chargeFromArmor(itemStack, entity);
    }

    public boolean use(ItemStack itemStack, double amount, EntityLivingBase entity) {
        return MuseElectricItem.getInstance().use(itemStack, amount, entity);
    }

    public boolean canProvideEnergy(ItemStack itemStack) {
        return MuseElectricItem.getInstance().canProvideEnergy(itemStack);
    }

    public double getCharge(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getCharge(itemStack);
    }

    public double getMaxCharge(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getMaxCharge(itemStack);
    }

    public int getTier(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getTier(itemStack);
    }

    public double getTransferLimit(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getTransferLimit(itemStack);
    }

    public double charge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
        return MuseElectricItem.getInstance().charge(itemStack, amount, tier, ignoreTransferLimit, simulate);
    }

    public double discharge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate) {
        return MuseElectricItem.getInstance().discharge(itemStack, amount, tier, ignoreTransferLimit, externally, simulate);
    }

    public boolean canUse(ItemStack itemStack, double amount) {
        return MuseElectricItem.getInstance().canUse(itemStack, amount);
    }

    public Item getChargedItem(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getChargedItem(itemStack);
    }

    public Item getEmptyItem(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getEmptyItem(itemStack);
    }

    public int receiveEnergy(ItemStack stack, int energy, boolean simulate) {
        return MuseElectricItem.getInstance().receiveEnergy(stack, energy, simulate);
    }

    public int extractEnergy(ItemStack stack, int energy, boolean simulate) {
        return MuseElectricItem.getInstance().extractEnergy(stack, energy, simulate);
    }

    public int getEnergyStored(ItemStack theItem) {
        return MuseElectricItem.getInstance().getEnergyStored(theItem);
    }

    public int getMaxEnergyStored(ItemStack theItem) {
        return MuseElectricItem.getInstance().getMaxEnergyStored(theItem);
    }

    public int getMaxDamage(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getMaxDamage(itemStack);
    }

    /* Applied Energistics 2 ---------------------------------------------------------------------- */
    public double injectAEPower(ItemStack stack, double ae) {
        return MuseElectricItem.getInstance().injectAEPower(stack, ae);
    }

    public double extractAEPower(ItemStack stack, double ae) {
        return MuseElectricItem.getInstance().extractAEPower(stack, ae);
    }

    public double getAEMaxPower(ItemStack stack) {
        return MuseElectricItem.getInstance().getAEMaxPower(stack);
    }

    public double getAECurrentPower(ItemStack stack) {
        return MuseElectricItem.getInstance().getAECurrentPower(stack);
    }

    public AccessRestriction getPowerFlow(ItemStack stack) {
        return MuseElectricItem.getInstance().getPowerFlow(stack);
    }
}