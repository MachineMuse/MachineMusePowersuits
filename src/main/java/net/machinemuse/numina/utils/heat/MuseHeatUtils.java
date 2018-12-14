package net.machinemuse.numina.utils.heat;

import net.machinemuse.numina.capabilities.heat.CapabilityHeat;
import net.machinemuse.numina.capabilities.heat.IHeatStorage;
import net.machinemuse.numina.common.constants.NuminaConstants;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

import javax.annotation.Nonnull;
import java.util.List;

public class MuseHeatUtils {
    public static final DamageSource overheatDamage = new OverheatDamage();

    public static double getPlayerHeat(EntityPlayer player) {
        double heat = 0;
        for (ItemStack stack : MuseItemUtils.getModularItemsEquipped(player)) {
            heat += getItemHeat(stack);
        }
        return heat;
    }

    /**
     * Should only be called server side
     */
    public static double getPlayerMaxHeat(EntityPlayer player) {
        double maxHeat = 0;
        for (ItemStack stack : MuseItemUtils.getModularItemsEquipped(player)) {
            maxHeat += getItemMaxHeat(stack);
        }
        return maxHeat;
    }

    public static double coolPlayer(EntityPlayer player, double coolJoules) {
        List<ItemStack> items = MuseItemUtils.getModularItemsInInventory(player);
        if (player.isHandActive()) {
            items.remove(player.inventory.getCurrentItem());
        }

        double coolingLeft = coolJoules;
        for (ItemStack stack : items) {
            double currHeat = getItemHeat(stack);
            if (coolingLeft > 0) {
                if (currHeat > coolingLeft) {
                    coolingLeft -= coolItem(stack, coolingLeft);
                    return coolJoules - coolingLeft;
                } else {
                    coolingLeft -= coolItem(stack, coolingLeft);
                }
            } else {
                break;
            }
        }
        return coolJoules - coolingLeft;
    }

    /**
     * Should only be called server side
     */
    public static double heatPlayer(EntityPlayer player, double heatJoules) {
        List<ItemStack> items = MuseItemUtils.getModularItemsEquipped(player);
        if (player.isHandActive()) {
            items.remove(player.inventory.getCurrentItem());
        }

        double heatLeftToGive = heatJoules;

        // heat player equipped items up to max heat
        for (ItemStack stack : items) {
            double currHeat = getItemHeat(stack);
            double maxHeat = getItemMaxHeat(stack);
            if (currHeat + heatLeftToGive < maxHeat) {
                heatItem(stack, heatLeftToGive);
                return heatJoules;
            } else {
                heatLeftToGive = heatLeftToGive - heatItem(stack, maxHeat - currHeat);
            }
        }

        // apply remaining heat evenly accross the items
        double heatPerStack = heatLeftToGive / items.size();
        for (ItemStack stack : items) {
            heatLeftToGive -= heatItem(stack, heatPerStack);
        }
        return heatJoules - heatLeftToGive;
    }

    public static double getItemMaxHeat(@Nonnull ItemStack stack) {
        IHeatStorage heatStorage = stack.getCapability(CapabilityHeat.HEAT, null);
        if (heatStorage != null)
            return heatStorage.getMaxHeatStored();
        return 0;
    }

    public static double getItemHeat(@Nonnull ItemStack stack) {
        IHeatStorage heatStorage = stack.getCapability(CapabilityHeat.HEAT, null);
        if (heatStorage != null)
            return heatStorage.getHeatStored();
        return 0;
    }

    public static double heatItem(@Nonnull ItemStack stack, double value) {
        IHeatStorage heatStorage = stack.getCapability(CapabilityHeat.HEAT, null);
        if (heatStorage != null)
            return heatStorage.receiveHeat(value, false);
        return 0;
    }

    public static double coolItem(@Nonnull ItemStack stack, double value) {
        IHeatStorage heatStorage = stack.getCapability(CapabilityHeat.HEAT, null);
        if (heatStorage != null)
            return heatStorage.extractHeat(value, false);
        return 0;
    }

    protected static final class OverheatDamage extends DamageSource {
        public OverheatDamage() {
            super(NuminaConstants.OVERHEAT_DAMAGE);
            this.setFireDamage();
            this.setDamageBypassesArmor();
        }

        public boolean equals(DamageSource other) {
            return other.damageType.equals(this.damageType);
        }
    }
}
