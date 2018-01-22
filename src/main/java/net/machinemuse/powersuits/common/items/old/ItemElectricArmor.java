package net.machinemuse.powersuits.common.items.old;

//import appeng.api.config.AccessRestriction;

import net.machinemuse.api.electricity.MuseElectricItem;
import net.machinemuse.api.item.IModularItemBase;
import net.machinemuse.powersuits.client.helpers.EnumColour;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
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
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int par2) {
        return ModularItemBase.getInstance().getColorFromItemStack(stack, par2);
    }

    public EnumColour getColorFromItemStack(ItemStack stack) {
        return ModularItemBase.getInstance().getColorFromItemStack(stack);
    }
    //=================================================================================
    @Override
    public boolean hasColor(ItemStack stack) {
        return true ;
    }

    @Override
    public int getColor(ItemStack stack) {
        return this.getColorFromItemStack(stack).getColour().getInt();
    }

    @Override
    public boolean hasOverlay(ItemStack stack) {
        return true;
    }

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

    public double getCurrentMPSEnergy(ItemStack stack) {
        return MuseElectricItem.getInstance().getCurrentMPSEnergy(stack);
    }

    public double getMaxMPSEnergy(ItemStack stack) {
        return MuseElectricItem.getInstance().getMaxMPSEnergy(stack);
    }

    public void setCurrentMPSEnergy(ItemStack stack, double energy) {
        MuseElectricItem.getInstance().setCurrentMPSEnergy(stack, energy);
    }

    public double drainMPSEnergyFrom(ItemStack stack, double requested) {
        return MuseElectricItem.getInstance().drainMPSEnergyFrom(stack, requested);
    }

    public double giveMPSEnergyTo(ItemStack stack, double provided) {
        return MuseElectricItem.getInstance().giveMPSEnergyTo(stack, provided);
    }
//
//    public IMuseElectricItem getManager(ItemStack itemStack) {
//        return MuseElectricItem.getInstance().getManager(itemStack);
//    }
//
//    public void chargeFromArmor(ItemStack itemStack, EntityLivingBase entity) {
//        MuseElectricItem.getInstance().chargeFromArmor(itemStack, entity);
//    }
//
//    public boolean use(ItemStack itemStack, double amount, EntityLivingBase entity) {
//        return MuseElectricItem.getInstance().use(itemStack, amount, entity);
//    }
//
//    public boolean canProvideEnergy(ItemStack itemStack) {
//        return MuseElectricItem.getInstance().canProvideEnergy(itemStack);
//    }
//
//    public double getCharge(ItemStack itemStack) {
//        return MuseElectricItem.getInstance().getCharge(itemStack);
//    }
//
//    public double getMaxCharge(ItemStack itemStack) {
//        return MuseElectricItem.getInstance().getMaxCharge(itemStack);
//    }
//
//    public int getTier(ItemStack itemStack) {
//        return MuseElectricItem.getInstance().getTier(itemStack);
//    }
//
//    public double getTransferLimit(ItemStack itemStack) {
//        return MuseElectricItem.getInstance().getTransferLimit(itemStack);
//    }
//
//    public double charge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
//        return MuseElectricItem.getInstance().charge(itemStack, amount, tier, ignoreTransferLimit, simulate);
//    }
//
//    public double discharge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate) {
//        return MuseElectricItem.getInstance().discharge(itemStack, amount, tier, ignoreTransferLimit, externally, simulate);
//    }
//
//    public boolean canUse(ItemStack itemStack, double amount) {
//        return MuseElectricItem.getInstance().canUse(itemStack, amount);
//    }
//
//    public Item getChargedItem(ItemStack itemStack) {
//        return MuseElectricItem.getInstance().getChargedItem(itemStack);
//    }
//
//    public Item getEmptyItem(ItemStack itemStack) {
//        return MuseElectricItem.getInstance().getEmptyItem(itemStack);
//    }

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

    /* Mekanism ----------------------------------------------------------------------------------- */
    @Override
    public double getEnergy(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getEnergy(itemStack);
    }

    @Override
    public void setEnergy(ItemStack itemStack, double v) {
        MuseElectricItem.getInstance().setEnergy(itemStack, v);
    }

    @Override
    public double getMaxEnergy(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getMaxEnergy(itemStack);
    }

    @Override
    public double getMaxTransfer(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getMaxTransfer(itemStack);
    }

    @Override
    public boolean canReceive(ItemStack itemStack) {
        return MuseElectricItem.getInstance().canReceive(itemStack);
    }

    @Override
    public boolean canSend(ItemStack itemStack) {
        return MuseElectricItem.getInstance().canSend(itemStack);
    }
}