package net.machinemuse.powersuits.common.capabilities.heat;

import net.machinemuse.api.ModuleManager;
import net.machinemuse.powersuits.common.config.MPSSettings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

import java.util.List;
import java.util.Objects;

public interface IMuseHeatHandler {

    String MAXIMUM_HEAT = "Maximum Heat";
    String CURRENT_HEAT = "Current Heat";

    static double getPlayerHeat(EntityPlayer player) {
        double avail = 0;
        for (ItemStack stack : MuseItemUtils.getModularItemsInInventory(player)) {
            avail += getItemHeat(stack);
        }
        return avail;
    }

    static double getMaxHeat(EntityPlayer player) {
        double avail = 0;
        for (ItemStack stack : MuseItemUtils.getModularItemsInInventory(player)) {
            avail += getMaxHeat(stack);
        }
        return avail + MPSSettings.general.baseMaxHeat;
    }

    static double getMaxHeat(ItemStack stack) {
        return ModuleManager.computeModularProperty(stack, MAXIMUM_HEAT);
    }

    static void coolPlayer(EntityPlayer player, double coolDegrees) {
        List<ItemStack> items = MuseItemUtils.getModularItemsInInventory(player);
        if (player.isHandActive()) {
            items.remove(player.inventory.getCurrentItem());
        }
        for (ItemStack stack : items) {
            double currHeat = getItemHeat(stack);
            if (coolDegrees > 0) {
                if (currHeat > coolDegrees) {
                    setItemHeat(stack, currHeat - coolDegrees);
                    return;
                } else {
                    coolDegrees -= currHeat;
                    setItemHeat(stack, 0);
                }
            } else {
                return;
            }
        }
    }

    static void heatPlayer(EntityPlayer player, double heatDegrees) {
        List<ItemStack> items = MuseItemUtils.getModularItemsInInventory(player);
        if (player.isHandActive()) {
            items.remove(player.inventory.getCurrentItem());
        }
        for (ItemStack stack : items) {
            double currHeat = getItemHeat(stack);
            double maxHeat = getMaxHeat(stack);
            if (currHeat + heatDegrees < maxHeat) {
                setItemHeat(stack, currHeat + heatDegrees);
                return;
            } else {
                heatDegrees -= (maxHeat - currHeat);
                setItemHeat(stack, maxHeat);
            }
        }
        double heatPerStack = heatDegrees / items.size();
        for (ItemStack stack : items) {
            heatItem(stack, heatPerStack);
        }
    }

    static double getItemHeat(ItemStack stack) {
        return MuseItemUtils.getDoubleOrZero(stack, CURRENT_HEAT);
    }

    static void setItemHeat(ItemStack stack, double heat) {
        MuseItemUtils.setDoubleOrRemove(stack, CURRENT_HEAT, heat);
    }

    static void heatItem(ItemStack stack, double value) {
        setItemHeat(stack, getItemHeat(stack) + value);
    }

    static void coolItem(ItemStack stack, double value) {
        setItemHeat(stack, getItemHeat(stack) - value);
    }

    DamageSource overheatDamage = new OverheatDamage();

    final class OverheatDamage extends DamageSource {
        public OverheatDamage() {
            super("Overheat");
            this.setFireDamage();
            this.setDamageBypassesArmor();
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof DamageSource ? Objects.equals(((DamageSource) other).damageType, this.damageType) : false;
        }
    }
}
