package net.machinemuse.powersuits.item.module.environmental;

import net.machinemuse.numina.energy.ElectricItemUtils;
import net.machinemuse.numina.module.*;
import net.machinemuse.numina.nbt.MuseNBTUtils;
import net.machinemuse.powersuits.basemod.MPSConfig;
import net.machinemuse.powersuits.basemod.ModuleManager;
import net.machinemuse.powersuits.constants.MPSModuleConstants;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.FoodStats;

import javax.annotation.Nonnull;

public class ItemModuleAutoFeeder extends ItemAbstractPowerModule implements IToggleableModule, IPlayerTickModule {
    public static final String TAG_FOOD = "Food";
    public static final String TAG_SATURATION = "Saturation";
    
    public ItemModuleAutoFeeder(String regName) {
        super(regName, EnumModuleTarget.HEADONLY, EnumModuleCategory.CATEGORY_ENVIRONMENTAL);
//        addBasePropertyDouble(MPSModuleConstants.EATING_ENERGY_CONSUMPTION, 100);
//        addBasePropertyDouble(MPSModuleConstants.EATING_EFFICIENCY, 50);
//        addTradeoffPropertyDouble(MPSModuleConstants.EFFICIENCY, MPSModuleConstants.EATING_ENERGY_CONSUMPTION, 1000, "RF");
//        addTradeoffPropertyDouble(MPSModuleConstants.EFFICIENCY, MPSModuleConstants.EATING_EFFICIENCY, 50);
    }

    public static double getFoodLevel(@Nonnull ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof ItemModuleAutoFeeder) {
            NBTTagCompound itemTag = MuseNBTUtils.getMuseModuleTag(stack);
            return itemTag.getDouble(TAG_FOOD);
        }
        return 0.0;
    }

    public static void setFoodLevel(@Nonnull ItemStack stack, double d) {
        if (!stack.isEmpty() && stack.getItem() instanceof ItemModuleAutoFeeder) {
            NBTTagCompound itemTag = MuseNBTUtils.getMuseModuleTag(stack);
            itemTag.putDouble(TAG_FOOD, d);
        }
    }

    public static double getSaturationLevel(@Nonnull ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof ItemModuleAutoFeeder) {
            NBTTagCompound itemTag = MuseNBTUtils.getMuseModuleTag(stack);
            Double saturationLevel = itemTag.getDouble(TAG_SATURATION);
            if (saturationLevel != null) {
                return saturationLevel;
            }
        }
        return 0.0F;
    }

    public static void setSaturationLevel(@Nonnull ItemStack stack, double d) {
        if (!stack.isEmpty() && stack.getItem() instanceof ItemModuleAutoFeeder) {
            NBTTagCompound itemTag = MuseNBTUtils.getMuseModuleTag(stack);
            itemTag.putDouble(TAG_SATURATION, d);
        }
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        double foodLevel = getFoodLevel(item);
        double saturationLevel = getSaturationLevel(item);
        IInventory inv = player.inventory;
        double eatingEnergyConsumption = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.EATING_ENERGY_CONSUMPTION);
        double efficiency = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.EATING_EFFICIENCY);

        FoodStats foodStats = player.getFoodStats();
        int foodNeeded = 20 - foodStats.getFoodLevel();
        double saturationNeeded = 20 - foodStats.getSaturationLevel();

        // this consumes all food in the player's inventory and stores the stats in a buffer
//        if (MPSConfig.INSTANCE.useOldAutoFeeder()) { // FIXME!!!!!
            if (true) {

            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (!stack.isEmpty() && stack.getItem() instanceof ItemFood) {
                    ItemFood food = (ItemFood) stack.getItem();
                    for (int a = 0; a < stack.getCount(); a++) {
                        foodLevel += food.getHealAmount(stack) * efficiency / 100.0;
                        //  copied this from FoodStats.addStats()
                        saturationLevel += Math.min(food.getHealAmount(stack) * food.getSaturationModifier(stack) * 2.0F, 20F) * efficiency / 100.0;
                    }
                    player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                }
            }
            setFoodLevel(item, foodLevel);
            setSaturationLevel(item, saturationLevel);
        } else {
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                if (foodNeeded < foodLevel)
                    break;
                ItemStack stack = inv.getStackInSlot(i);
                if (!stack.isEmpty() && stack.getItem() instanceof ItemFood) {
                    ItemFood food = (ItemFood) stack.getItem();
                    while (true) {
                        if (foodNeeded > foodLevel) {
                            foodLevel += food.getHealAmount(stack) * efficiency / 100.0;
                            //  copied this from FoodStats.addStats()
                            saturationLevel += Math.min(food.getHealAmount(stack) * (double) food.getSaturationModifier(stack) * 2.0D, 20D) * efficiency / 100.0;
                            stack.setCount(stack.getCount() - 1);
                            if (stack.getCount() == 0) {
                                player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                                break;
                            } else
                                player.inventory.setInventorySlotContents(i, stack);
                        } else
                            break;
                    }
                }
            }
            setFoodLevel(item, foodLevel);
            setSaturationLevel(item, saturationLevel);
        }

        NBTTagCompound foodStatNBT = new NBTTagCompound();

        // only consume saturation if food is consumed. This keeps the food buffer from overloading with food while the
        //   saturation buffer drains completely.
        if (foodNeeded > 0 && getFoodLevel(item) >= 1) {
            int foodUsed = 0;
            // if buffer has enough to fill player stat
            if (getFoodLevel(item) >= foodNeeded && foodNeeded * eatingEnergyConsumption * 0.5 < ElectricItemUtils.getPlayerEnergy(player)) {
                foodUsed = foodNeeded;
                // if buffer has some but not enough to fill the player stat
            } else if ((foodNeeded - getFoodLevel(item)) > 0 && getFoodLevel(item) * eatingEnergyConsumption * 0.5 < ElectricItemUtils.getPlayerEnergy(player)) {
                foodUsed = (int) getFoodLevel(item);
                // last resort where using just 1 unit from buffer
            } else if (eatingEnergyConsumption * 0.5 < ElectricItemUtils.getPlayerEnergy(player) && getFoodLevel(item) >= 1) {
                foodUsed = 1;
            }
            if (foodUsed > 0) {
                // populate the tag with the nbt data
                foodStats.write(foodStatNBT);
                foodStatNBT.putInt("foodLevel", foodStatNBT.getInt("foodLevel") + foodUsed);
                // put the values back into foodstats
                foodStats.read(foodStatNBT);
                // update getValue stored in buffer
                setFoodLevel(item, getFoodLevel(item) - foodUsed);
                // split the cost between using food and using saturation
                ElectricItemUtils.drainPlayerEnergy(player, (int) (eatingEnergyConsumption * 0.5 * foodUsed));

                if (saturationNeeded >= 1.0D) {
                    // using int for better precision
                    int saturationUsed = 0;
                    // if buffer has enough to fill player stat
                    if (getSaturationLevel(item) >= saturationNeeded && saturationNeeded * eatingEnergyConsumption * 0.5 < ElectricItemUtils.getPlayerEnergy(player)) {
                        saturationUsed = (int) saturationNeeded;
                        // if buffer has some but not enough to fill the player stat
                    } else if ((saturationNeeded - getSaturationLevel(item)) > 0 && getSaturationLevel(item) * eatingEnergyConsumption * 0.5 < ElectricItemUtils.getPlayerEnergy(player)) {
                        saturationUsed = (int) getSaturationLevel(item);
                        // last resort where using just 1 unit from buffer
                    } else if (eatingEnergyConsumption * 0.5 < ElectricItemUtils.getPlayerEnergy(player) && getSaturationLevel(item) >= 1) {
                        saturationUsed = 1;
                    }

                    if (saturationUsed > 0) {
                        // populate the tag with the nbt data
                        foodStats.write(foodStatNBT);
                        foodStatNBT.putFloat("foodSaturationLevel", foodStatNBT.getFloat("foodSaturationLevel") + saturationUsed);
                        // put the values back into foodstats
                        foodStats.read(foodStatNBT);
                        // update getValue stored in buffer
                        setSaturationLevel(item, getSaturationLevel(item) - saturationUsed);
                        // split the cost between using food and using saturation
                        ElectricItemUtils.drainPlayerEnergy(player, (int) (eatingEnergyConsumption * 0.5 * saturationUsed));
                    }
                }
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }
}