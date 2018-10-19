package net.machinemuse.powersuits.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.electricity.IMuseElectricItem;
import net.machinemuse.numina.geometry.Colour;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:49 PM, 4/23/13
 *
 * Ported to Java by lehjr on 11/4/16.
 */
public interface IModularItemBase extends IModularItem, IMuseElectricItem {
    @SideOnly(Side.CLIENT)
    int getColorFromItemStack(ItemStack stack, int par2);

    Colour getGlowFromItemStack(ItemStack stack);

    Colour getColorFromItemStack(ItemStack stack);

    @SideOnly(Side.CLIENT)
    boolean requiresMultipleRenderPasses();

    /**
     * Adds information to the item's tooltip when 'getting' it.
     *
     * @param stack            The itemstack to get the tooltip for
     * @param player           The player (client) viewing the tooltip
     * @param currentTipList   A list of strings containing the existing tooltip. When
     *                         passed, it will just contain the name of the item;
     *                         enchantments and lore are
     *                         appended afterwards.
     * @param advancedToolTips Whether or not the player has 'advanced tooltips' turned on in
     *                         their settings.
     */
    @SideOnly(Side.CLIENT)
    void addInformation(ItemStack stack, EntityPlayer player, List<String> currentTipList, boolean advancedToolTips);

    String formatInfo(String string, double value);

    List<String> getLongInfo(EntityPlayer player, ItemStack stack);

    double getArmorDouble(EntityPlayer player, ItemStack stack);
}
