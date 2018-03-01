package net.machinemuse.numina.utils.module.helpers;

import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class WeightHelper {
    public static double getTotalWeight(ItemStack stack) {
        return ModuleManager.getInstance().computeModularPropertyDouble(stack, MPSModuleConstants.WEIGHT);
    }

    public static double getPlayerWeight(EntityPlayer player) {
        double weight = 0;
        for (ItemStack stack : MuseItemUtils.modularItemsEquipped(player)) {
            weight += ModuleManager.getInstance().computeModularPropertyDouble(stack, MPSModuleConstants.WEIGHT);
        }
        return weight;
    }

    public static double getWeightPenaltyRatio(double currentWeight, double capacity) {
        if (currentWeight < capacity) {
            return 1;
        } else {
            return capacity / currentWeight;
        }
    }
}
