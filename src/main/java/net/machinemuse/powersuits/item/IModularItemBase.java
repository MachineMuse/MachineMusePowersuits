package net.machinemuse.powersuits.item;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.electricity.IMuseElectricItem;
import net.machinemuse.numina.geometry.Colour;
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
    int getColorFromItemStack(final ItemStack stack, final int p1);

    Colour getGlowFromItemStack(final ItemStack stack);

    Colour getColorFromItemStack(final ItemStack stack);

    @SideOnly(Side.CLIENT)
    void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> currentTipList, boolean advancedToolTips);


    String formatInfo(final String string, final double value);

    List<String> getLongInfo(final EntityPlayer player, final ItemStack stack);

    double getArmorDouble(final EntityPlayer player, final ItemStack stack);

    double getPlayerEnergy(final EntityPlayer player);

    void drainPlayerEnergy(final EntityPlayer player, final double drainEnergy);

    void givePlayerEnergy(final EntityPlayer player, final double joulesToGive);
}