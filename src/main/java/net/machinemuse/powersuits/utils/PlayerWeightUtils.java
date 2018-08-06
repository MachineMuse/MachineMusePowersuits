package net.machinemuse.powersuits.utils;

import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class PlayerWeightUtils {

    public static double getPlayerWeight(EntityPlayer player) {
        double weight = 0;
        for (ItemStack stack : MuseItemUtils.modularItemsEquipped(player)) {
            weight += ModuleManager.INSTANCE.computeModularProperty(stack, MPSModuleConstants.WEIGHT);
        }
        return weight;
    }
}
