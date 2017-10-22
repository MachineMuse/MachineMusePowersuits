package net.machinemuse.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Interface for ItemPowerArmor and ItemPowerTool to share.
 *
 * @author MachineMuse
 *
 * Ported to Java by lehjr on 11/3/16.
 */
public interface IModularItem {
    /**
     * Gets the item's extended summary for displaying in the gui.
     *
     * @param player
     * @param stack
     * @return
     */
    List<String> getLongInfo(EntityPlayer player, ItemStack stack);

    /**
     * Returns the amount of energy contained in the player's inventory.
     *
     * @param player
     * @return
     */
    double getPlayerEnergy(EntityPlayer player);

    /**
     * Drains the amount of energy from the player's inventory.
     *
     * @param player
     * @param drainAmount
     * @return
     */
    void drainPlayerEnergy(EntityPlayer player, double drainAmount);

    /**
     * Adds the amount of energy to the player's inventory.
     *
     * @param player
     * @param joulesToGive
     * @return
     */
    void givePlayerEnergy(EntityPlayer player, double joulesToGive);
}
