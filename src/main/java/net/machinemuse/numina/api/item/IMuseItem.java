package net.machinemuse.numina.api.item;

import net.machinemuse.numina.math.geometry.Colour;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Interface for ItemPowerArmor and ItemPowerTool to share.
 *
 * @author MachineMuse
 *
 * Ported to Java by lehjr on 11/3/16.
 */
public interface IMuseItem {
    /**
     * Gets the item's extended summary for displaying in the gui.
     *
     * @param player
     * @param stack
     * @return
     */
    List<String> getLongInfo(EntityPlayer player, ItemStack stack);

    @SideOnly(Side.CLIENT)
    int getColorFromItemStack(ItemStack stack, int p1);

// TODO: is this even a thing?
    Colour getGlowFromItemStack(ItemStack stack);

    Colour getColorFromItemStack(ItemStack stack);

    @SideOnly(Side.CLIENT)
    void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> currentTipList, boolean advancedToolTips);

    String formatInfo(String string, double value);

    double getArmorDouble(EntityPlayer player, ItemStack stack);
}