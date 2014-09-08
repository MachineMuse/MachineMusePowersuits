package net.machinemuse.api;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Interface for ItemPowerArmor and ItemPowerTool to share.
 * 
 * @author MachineMuse
 */
public interface IModularItem
{

    /**
     * Gets the item's extended summary for displaying in the gui.
     * 
     * @param stack
     * @return
     */
    public List<String> getLongInfo(EntityPlayer player, ItemStack stack);

    /**
     * Returns the amount of energy contained in the player's inventory.
     * 
     * @param player
     * @return
     */
    public double getPlayerEnergy(EntityPlayer player);

    /**
     * Drains the amount of energy from the player's inventory.
     * 
     * @param player
     * @return
     */
    public void drainPlayerEnergy(EntityPlayer player, double drainAmount);

    /**
     * Adds the amount of energy to the player's inventory.
     * 
     * @param player
     * @return
     */
    public void givePlayerEnergy(EntityPlayer player, double joulesToGive);

}
