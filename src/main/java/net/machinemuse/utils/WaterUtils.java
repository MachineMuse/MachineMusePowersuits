package net.machinemuse.utils;

import net.machinemuse.api.ModuleManager;
import net.machinemuse.powersuits.common.MPSConstants;
import net.machinemuse.powersuits.common.items.modules.armor.WaterTankModule;
import net.machinemuse.powersuits.common.items.old.ItemPowerArmorChestplate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by User: Andrew2448
 * 4:52 PM 6/21/13
 */
public class WaterUtils {

    public static double getPlayerWater(EntityPlayer player) {
        double water = 0;
        for (ItemStack stack : MuseItemUtils.getModularItemsInInventory(player)) {
            if (stack.getItem() instanceof ItemPowerArmorChestplate && ModuleManager.itemHasActiveModule(stack, MPSConstants.MODULE_WATER_TANK)) {
                water = MuseItemUtils.getWaterLevel(stack);
            }
        }
        return water;
    }

    public static double getMaxWater(EntityPlayer player) {
        double water = 0;
        for (ItemStack stack : MuseItemUtils.getModularItemsInInventory(player)) {
            if (stack.getItem() instanceof ItemPowerArmorChestplate && ModuleManager.itemHasActiveModule(stack, MPSConstants.MODULE_WATER_TANK)) {
                water = ModuleManager.computeModularProperty(stack, WaterTankModule.WATER_TANK_SIZE);
            }
        }
        return water;
    }
}