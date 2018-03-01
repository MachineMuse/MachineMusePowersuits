package net.machinemuse.numina.utils.heat;

import net.machinemuse.numina.capabilities.CapabilityHeat;
import net.machinemuse.numina.utils.item.NuminaItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

import java.util.List;

/**
 * We'll call this heating/cooling in Joules for the sake of simplicity. Ideally, we'd use the heat capacity formula and
 *  calculate the temperature accordingly, but since this is a game where you can cut down a mountain with a pickaxe
 *  made of diamonds and sticks and then carry all of it in your pocket...
 *
 *  TODO: distribute heat evenly accross items rather than dumping all heat possible into each in succession
 *  TODO: also cool the same way
 *
 */
public class MuseHeatUtils {
    /**
     * Overheat damage for Armor
     */
    public static final DamageSource overheatDamage = new OverheatDamage();
    protected static final class OverheatDamage extends DamageSource {
        public OverheatDamage() {
            super("Overheat");
            this.setFireDamage();
            this.setDamageBypassesArmor();
        }

        public boolean equals(DamageSource other) {
            return other.damageType.equals(this.damageType);
        }
    }

    // Legacy wrappers ----------------------------------------------------------------------------

    public static double getPlayerHeatLegacy(EntityPlayer player) {
        return getPlayerHeat(player)/1000;
    }

    public static double getMaxHeatLegacy(EntityPlayer player) {
        return getMaxPlayerHeat(player)/1000;
    }

    public static double getMaxItemHeatLegacy(ItemStack stack) {
        return getMaxItemHeat(stack) / 1000;
    }

    public static double coolPlayerLegacy(EntityPlayer player, double coolDegrees) {
        int milliJoulesToCool = (int) Math.round(coolDegrees * 250);
        milliJoulesToCool = coolPlayer(player, milliJoulesToCool);
        return milliJoulesToCool;
    }

    public static double heatPlayerLegacy(EntityPlayer player, double heatDegrees) {
        int milliJoulesToHeat = (int) Math.round(heatDegrees * 1000);
        milliJoulesToHeat = heatPlayer(player, milliJoulesToHeat);
        return milliJoulesToHeat;
    }

    //---------------------------------------------------------------------------------------------
    public static final String MAXIMUM_HEAT = "Maximum Heat";
    public static final String CURRENT_HEAT = "Current Heat";


    /**
     * Gets all the equipped items that use the heat capability
     */
    public static List<ItemStack> getHeatableItemsEquipped(EntityPlayer player) {
        return NuminaItemUtils.getEquipedItemsInInventoryWithCapabilities(player, CapabilityHeat.HEAT);
    }

    /**
     * Gets all the items in the player's inventory that use the heat capability
     */
    public static List<ItemStack> getAllHeatableItems(EntityPlayer player) {
        return NuminaItemUtils.getAllItemsInInventoryWithCapabilities(player, CapabilityHeat.HEAT);
    }

    /**
     * Gets the heat from the ItemStack
     */
    public static int getItemHeat(ItemStack itemStack) {
        if (itemStack.hasCapability(CapabilityHeat.HEAT, null))
            return itemStack.getCapability(CapabilityHeat.HEAT, null).getHeatStored();
        return 0;
    }

    /**
     * Gets the heat from the ItemStack
     */
    public static int getMaxItemHeat(ItemStack itemStack) {
        if (itemStack.hasCapability(CapabilityHeat.HEAT, null))
            return itemStack.getCapability(CapabilityHeat.HEAT, null).getMaxHeatStored();
        return 0;
    }

    /**
     * Total heat from all items in player's inventory
     */
    public static int getPlayerHeat(EntityPlayer player) {
        int avail = 0;
        for (ItemStack stack : getAllHeatableItems(player)) {
            avail += getItemHeat(stack);
        }
        return avail;
    }

    /**
     * Total amount of heat the player's inventory can support. Used for the heat bar and other things.
     */
    public static int getMaxPlayerHeat(EntityPlayer player) {
        int max = 0;
        for (ItemStack itemStack :  getHeatableItemsEquipped(player)) {
            max += itemStack.getCapability(CapabilityHeat.HEAT, null).getMaxHeatStored();
        }
        return max;
    }

    /**
     * Adds heat to the ItemStack. Returns quantity of heat that was accepted.
     */
    public static int heatItem(ItemStack itemStack, int heatJoules) {
        return itemStack.getCapability(CapabilityHeat.HEAT, null).receiveHeat(heatJoules, false);
    }

    /**
     * Adds heat to the ItemStacks in the player's inventory. Returns quantity of heat that was accepted.
     */
    public static int heatPlayer(EntityPlayer player, int heatJoules) {
        int joulesLeft = heatJoules;
        List<ItemStack> items = getAllHeatableItems(player);

        // max out the heat for the current item first
        if (player.isHandActive() && items.contains(player.inventory.getCurrentItem())) {
            items.remove(player.inventory.getCurrentItem());
            joulesLeft = heatItem(player.inventory.getCurrentItem(), heatJoules);
        }

        for (ItemStack itemStack :  items) {
            if (joulesLeft > 0)
                joulesLeft = joulesLeft - itemStack.getCapability(CapabilityHeat.HEAT, null).receiveHeat(joulesLeft, false);
            else
                break;
        }
        return heatJoules - joulesLeft;
    }

    /**
     * Removes heat from the ItemStack. Returns quantity of heat that was removed.
     */
    public static int coolItem(ItemStack itemStack, int coolJoules) {
        return itemStack.getCapability(CapabilityHeat.HEAT, null).extractHeat(coolJoules, false);
    }

    /**
     * Removes heat from the capable items in the player's inventory. Returns quantity of heat that was removed.
     *
     * @param coolJoules
     *            Maximum amount of heat to be extracted.
     * @return Amount of heat that was extracted from the storage.
     */
    public static int coolPlayer(EntityPlayer player, int coolJoules) {
        int coolJoulesLeft = coolJoules;
        for (ItemStack itemStack : NuminaItemUtils.getAllItemsInInventoryWithCapabilities(player, CapabilityHeat.HEAT)) {
            if (coolJoulesLeft > 0)
                coolJoulesLeft = coolJoulesLeft - itemStack.getCapability(CapabilityHeat.HEAT, null).extractHeat(coolJoulesLeft, false);
            else
                break;
        }
        return coolJoules - coolJoulesLeft;
    }
}