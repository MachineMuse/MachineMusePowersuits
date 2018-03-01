package net.machinemuse.powersuits.utils;

import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.utils.module.helpers.FluidUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorChestplate;
import net.machinemuse.powersuits.item.module.environmental.WaterTankModule;
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
            if (stack.getItem() instanceof ItemPowerArmorChestplate && ModuleManager.getInstance().itemHasActiveModule(stack, MPSModuleConstants.MODULE_WATER_TANK)) {
                water = FluidUtils.getWaterLevel(stack);
            }
        }
        return water;
    }

    public static double getMaxWater(EntityPlayer player) {
        double water = 0;
        for (ItemStack stack : MuseItemUtils.getModularItemsInInventory(player)) {
            if (stack.getItem() instanceof ItemPowerArmorChestplate && ModuleManager.getInstance().itemHasActiveModule(stack, MPSModuleConstants.MODULE_WATER_TANK)) {
                water = ModuleManager.getInstance().computeModularPropertyDouble(stack, WaterTankModule.WATER_TANK_SIZE);
            }
        }
        return water;
    }
}