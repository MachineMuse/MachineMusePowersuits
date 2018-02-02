package net.machinemuse.powersuits.common.items.old;

import net.machinemuse.powersuits.api.electricity.MuseElectricItem;
import net.machinemuse.numina.api.item.IModularItemBase;
import net.machinemuse.numina.geometry.EnumColour;
import net.machinemuse.numina.utils.energy.ElectricItemUtils;
import net.machinemuse.numina.utils.string.MuseCommonStrings;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.numina.utils.string.MuseStringUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:49 PM, 4/23/13
 *
 * Ported to Java by lehjr on 11/4/16.
 */
public class ModularItemBase extends Item implements IModularItemBase {
    private static ModularItemBase INSTANCE;

    public static ModularItemBase getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ModularItemBase();
        }
        return INSTANCE;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getColorFromItemStack(ItemStack stack, int par2) {
        return getColorFromItemStack(stack).getColour().getInt();
    }

    @Override
    public EnumColour getColorFromItemStack(ItemStack stack) {
        return MuseItemUtils.getStackSingleColour(stack);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List currentTipList, boolean advancedToolTips) {
        MuseCommonStrings.addInformation(stack, player, currentTipList, advancedToolTips);
    }

    @Override
    public String formatInfo(String string, double value) {
        return string + '\t' + MuseStringUtils.formatNumberShort(value);
    }

    /* IModularItem ------------------------------------------------------------------------------- */
    @Override
    public List<String> getLongInfo(EntityPlayer player, ItemStack stack) {
        List<String> info = new ArrayList<>();

        info.add("Detailed Summary");
        info.add(formatInfo("Armor", getArmorDouble(player, stack)));
        info.add(formatInfo("Energy Storage", getCurrentMPSEnergy(stack)) + 'J');
        info.add(formatInfo("Weight", MuseCommonStrings.getTotalWeight(stack)) + 'g');
        return info;
    }

    @Override
    public double getArmorDouble(EntityPlayer player, ItemStack stack) {
        return 0;
    }

    @Override
    public double getPlayerEnergy(EntityPlayer player) {
        return ElectricItemUtils.getPlayerEnergy(player);
    }

    @Override
    public void drainPlayerEnergy(EntityPlayer player, double drainEnergy) {
        ElectricItemUtils.drainPlayerEnergy(player, drainEnergy);
    }

    @Override
    public void givePlayerEnergy(EntityPlayer player, double joulesToGive) {
        ElectricItemUtils.givePlayerEnergy(player, joulesToGive);
    }


    /* MuseElectricItem --------------------------------------------------------------------------- */
    @Override
    public double getCurrentMPSEnergy(ItemStack stack) {
        return MuseElectricItem.getInstance().getCurrentMPSEnergy(stack);
    }

    @Override
    public double getMaxMPSEnergy(ItemStack stack) {
        return MuseElectricItem.getInstance().getCurrentMPSEnergy(stack);
    }

    @Override
    public void setCurrentMPSEnergy(ItemStack stack, double energy) {
        MuseElectricItem.getInstance().setCurrentMPSEnergy(stack, energy);
    }

    @Override
    public double drainMPSEnergyFrom(ItemStack stack, double requested) {
        return MuseElectricItem.getInstance().drainMPSEnergyFrom(stack, requested);
    }

    @Override
    public double giveMPSEnergyTo(ItemStack stack, double provided) {
        return MuseElectricItem.getInstance().giveMPSEnergyTo(stack, provided);
    }


    /* Thermal Expansion -------------------------------------------------------------------------- */
    @Override
    public int receiveEnergy(ItemStack stack, int energy, boolean simulate) {
        return MuseElectricItem.getInstance().receiveEnergy(stack, energy, simulate);
    }

    @Override
    public int extractEnergy(ItemStack stack, int energy, boolean simulate) {
        return MuseElectricItem.getInstance().extractEnergy(stack, energy, simulate);
    }

    @Override
    public int getEnergyStored(ItemStack theItem) {
        return MuseElectricItem.getInstance().getEnergyStored(theItem);
    }

    @Override
    public int getMaxEnergyStored(ItemStack theItem) {
        return MuseElectricItem.getInstance().getMaxEnergyStored(theItem);
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