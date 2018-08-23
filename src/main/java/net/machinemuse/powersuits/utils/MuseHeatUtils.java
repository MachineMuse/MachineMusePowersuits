package net.machinemuse.powersuits.utils;

import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

import java.util.List;

public class MuseHeatUtils {

    public static final String MAXIMUM_HEAT = "Maximum Heat";
    public static final String CURRENT_HEAT = "Current Heat";

    public static double getPlayerHeat(EntityPlayer player) {
        double avail = 0;
        for (ItemStack stack : MuseItemUtils.getModularItemsInInventory(player)) {
            avail += getItemHeat(stack);
        }
        return avail;
    }

    public static double getMaxHeat(EntityPlayer player) {
        double avail = 0;
        for (ItemStack stack : MuseItemUtils.getModularItemsInInventory(player)) {
            avail += getMaxHeat(stack);
        }
        return avail + MPSConfig.INSTANCE.baseMaxHeat();
    }

    public static double getMaxHeat(ItemStack stack) {
        return ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MAXIMUM_HEAT);
    }

    public static void coolPlayer(EntityPlayer player, double coolDegrees) {
        coolDegrees = coolDegrees * 0.25;
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

    public static void heatPlayer(EntityPlayer player, double heatDegrees) {
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

    public static double getItemHeat(ItemStack stack) {
        return MuseItemUtils.getDoubleOrZero(stack, CURRENT_HEAT);
    }

    public static void setItemHeat(ItemStack stack, double heat) {
        MuseItemUtils.setDoubleOrRemove(stack, CURRENT_HEAT, heat);
    }

    public static void heatItem(ItemStack stack, double value) {
        setItemHeat(stack, getItemHeat(stack) + value);
    }

    public static void coolItem(ItemStack stack, double value) {
        setItemHeat(stack, getItemHeat(stack) - value);
    }

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
}
