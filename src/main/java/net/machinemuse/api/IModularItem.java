package net.machinemuse.api;

import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
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
    default double getPlayerEnergy(EntityPlayer player) {
        return ElectricItemUtils.getPlayerEnergy(player);
    }

    /**
     * Drains the amount of energy from the player's inventory.
     *
     * @param player
     * @param drainAmount
     * @return
     */
    default void drainPlayerEnergy(EntityPlayer player, double drainAmount) {
        ElectricItemUtils.drainPlayerEnergy(player, drainAmount);
    }

    /**
     * Adds the amount of energy to the player's inventory.
     *
     * @param player
     * @param joulesToGive
     * @return
     */
    default void givePlayerEnergy(EntityPlayer player, double joulesToGive) {
        ElectricItemUtils.givePlayerEnergy(player, joulesToGive);
    }
}
