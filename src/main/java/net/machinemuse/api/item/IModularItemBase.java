package net.machinemuse.api.item;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.electricity.IMuseElectricItem;
import net.machinemuse.powersuits.client.helpers.EnumColour;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:49 PM, 4/23/13
 *
 * Ported to Java by lehjr on 11/4/16.
 */
public interface IModularItemBase extends IModularItem, IMuseElectricItem {
    @SideOnly(Side.CLIENT)
    int getColorFromItemStack(ItemStack stack, int p1);

    EnumColour getColorFromItemStack(ItemStack stack);

    @SideOnly(Side.CLIENT)
    void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> currentTipList, boolean advancedToolTips);

    String formatInfo(String string, double value);

    List<String> getLongInfo(EntityPlayer player, ItemStack stack);

    double getArmorDouble(EntityPlayer player, ItemStack stack);

    double getPlayerEnergy(EntityPlayer player);

    void drainPlayerEnergy(EntityPlayer player, double drainEnergy);

    void givePlayerEnergy(EntityPlayer player, double joulesToGive);
}