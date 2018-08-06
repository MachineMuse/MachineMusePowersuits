package net.machinemuse.powersuits.utils.modulehelpers;

import net.machinemuse.numina.api.item.IModularItem;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.numina.utils.nbt.MuseNBTUtils;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorChestplate;
import net.machinemuse.powersuits.powermodule.environmental.WaterTankModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

@Deprecated // TODO: switch to fluid capabilities
public class FluidUtils {
    public static final String TAG_WATER = "Water";
    public static final String TAG_LIQUID = "Liquid";

    public static double getPlayerWater(EntityPlayer player) {
        double water = 0;
        for (ItemStack stack : MuseItemUtils.getModularItemsInInventory(player)) {
            if (stack.getItem() instanceof ItemPowerArmorChestplate && ModuleManager.INSTANCE.itemHasActiveModule(stack, WaterTankModule.MODULE_WATER_TANK)) {
                water = getWaterLevel(stack);
            }
        }
        return water;
    }

    public static double getMaxWater(EntityPlayer player) {
        double water = 0;
        for (ItemStack stack : MuseItemUtils.getModularItemsInInventory(player)) {
            if (stack.getItem() instanceof ItemPowerArmorChestplate && ModuleManager.INSTANCE.itemHasActiveModule(stack, WaterTankModule.MODULE_WATER_TANK)) {
                water = ModuleManager.INSTANCE.computeModularProperty(stack, WaterTankModule.WATER_TANK_SIZE);
            }
        }
        return water;
    }



    public static double getWaterLevel(@Nonnull ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);
            Double waterLevel = itemTag.getDouble(TAG_WATER);
            if (waterLevel != null) {
                return waterLevel;
            }
        }
        return 0;
    }

    public static void setWaterLevel(@Nonnull ItemStack stack, double d) {
        if (!stack.isEmpty() && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);
            itemTag.setDouble(TAG_WATER, d);
        }
    }

    public static void setLiquid(@Nonnull ItemStack stack, String name) {
        if (!stack.isEmpty() && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);
            itemTag.setString(TAG_LIQUID, name);
        }
    }

    public static String getLiquid(@Nonnull ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);
            String s = itemTag.getString(TAG_LIQUID);
            if (s != null) {
                return s;
            }
        }
        return "";
    }
}
