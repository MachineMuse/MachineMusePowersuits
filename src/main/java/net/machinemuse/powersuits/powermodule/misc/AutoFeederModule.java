package net.machinemuse.powersuits.powermodule.misc;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.FoodStats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class AutoFeederModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
    public static final String MODULE_AUTO_FEEDER = "Auto-Feeder";
    public static final String EATING_ENERGY_CONSUMPTION = "Eating Energy Consumption";
    public static final String EATING_EFFICIENCY = "Auto-Feeder Efficiency";

    public AutoFeederModule(List<IModularItem> validItems) {
        super(validItems);
        addBaseProperty(EATING_ENERGY_CONSUMPTION, 100);
        addBaseProperty(EATING_EFFICIENCY, 50);
        addTradeoffProperty("Efficiency", EATING_ENERGY_CONSUMPTION, 100);
        addTradeoffProperty("Efficiency", EATING_EFFICIENCY, 50);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_ENVIRONMENTAL;
    }

    @Override
    public String getDataName() {
        return MODULE_AUTO_FEEDER;
    }

    @Override
    public String getUnlocalizedName() {
        return "autoFeeder";
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        double foodLevel = MuseItemUtils.getFoodLevel(item);
        double saturationLevel = MuseItemUtils.getSaturationLevel(item);
        IInventory inv = player.inventory;
        double eatingEnergyConsumption = ModuleManager.computeModularProperty(item, EATING_ENERGY_CONSUMPTION);
        double efficiency = ModuleManager.computeModularProperty(item, EATING_EFFICIENCY);
        FoodStats foodStats = player.getFoodStats();
        int foodNeeded = 20 - foodStats.getFoodLevel();
        double saturationNeeded = 20 - foodStats.getSaturationLevel();


        // this consumes all food in the player's inventory and stores the stats in a buffer
        if (Config.useOldAutoFeeder()) {
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (stack != null && stack.getItem() instanceof ItemFood) {
                    ItemFood food = (ItemFood) stack.getItem();
                    for (int a = 0; a < stack.stackSize; a++) {
                        foodLevel += food.getHealAmount(stack) * efficiency / 100.0;
                        //  copied this from FoodStats.addStats()
                        saturationLevel += Math.min(food.getHealAmount(stack) * food.getSaturationModifier(stack) * 2.0F, 20F) * efficiency / 100.0;
                    }
                    player.inventory.setInventorySlotContents(i, null);
                }
            }
            MuseItemUtils.setFoodLevel(item, foodLevel);
            MuseItemUtils.setSaturationLevel(item, saturationLevel);
        } else {
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                if (foodNeeded < foodLevel && saturationNeeded < saturationLevel)
                    break;
                ItemStack stack = inv.getStackInSlot(i);
                if (stack != null && stack.getItem() instanceof ItemFood) {
                    ItemFood food = (ItemFood) stack.getItem();
                    while (true) {
                        if (foodNeeded > foodLevel || saturationNeeded > saturationLevel) {
                            foodLevel += food.getHealAmount(stack) * efficiency / 100.0;
                            //  copied this from FoodStats.addStats()
                            saturationLevel += Math.min(food.getHealAmount(stack) * (double)food.getSaturationModifier(stack) * 2.0D, 20D) * efficiency / 100.0;
                            stack.stackSize--;
                            if (stack.stackSize == 0) {
                                player.inventory.setInventorySlotContents(i, null);
                                break;
                            } else
                                player.inventory.setInventorySlotContents(i, stack);
                        } else
                            break;
                    }
                }
            }
            MuseItemUtils.setFoodLevel(item, foodLevel);
            MuseItemUtils.setSaturationLevel(item, saturationLevel);
        }

        NBTTagCompound foodStatNBT = new NBTTagCompound();
        // if player has enough power to even use this once

        if (foodNeeded > 0 && MuseItemUtils.getFoodLevel(item) >= 1) {
            int foodUsed = 0;
            if (MuseItemUtils.getFoodLevel(item) >= foodNeeded && foodNeeded * eatingEnergyConsumption * 0.5 < ElectricItemUtils.getPlayerEnergy(player)) {
                foodUsed = foodNeeded;
            } else if (foodNeeded - MuseItemUtils.getFoodLevel(item) * eatingEnergyConsumption * 0.5 < ElectricItemUtils.getPlayerEnergy(player)) {
                foodUsed = foodNeeded - (int)MuseItemUtils.getFoodLevel(item);
            } else if (eatingEnergyConsumption * 0.5 < ElectricItemUtils.getPlayerEnergy(player) && MuseItemUtils.getFoodLevel(item) >= 1) {
                foodUsed = 1;
            }
            if (foodUsed > 0) {
                // populate the tag with the nbt data
                foodStats.writeNBT(foodStatNBT);
                foodStatNBT.setInteger("foodLevel",
                        foodStatNBT.getInteger("foodLevel") + foodUsed);
                // put the values back into foodstats
                foodStats.readNBT(foodStatNBT);
                // update value stored in buffer
                MuseItemUtils.setFoodLevel(item, MuseItemUtils.getFoodLevel(item) - foodUsed);
                // split the cost between using food and using saturation
                ElectricItemUtils.drainPlayerEnergy(player, eatingEnergyConsumption * 0.5 * foodUsed);
            }
        }

        if (saturationNeeded >= 1.0D) {
            // using int for better precision
            int saturationUsed = 0;
            if (MuseItemUtils.getSaturationLevel(item) >= saturationNeeded && saturationNeeded * eatingEnergyConsumption * 0.5 < ElectricItemUtils.getPlayerEnergy(player)) {
                saturationUsed = (int) saturationNeeded;
            } else if (saturationNeeded - MuseItemUtils.getSaturationLevel(item) * eatingEnergyConsumption * 0.5 < ElectricItemUtils.getPlayerEnergy(player)) {
                saturationUsed = (int)saturationNeeded - (int) MuseItemUtils.getSaturationLevel(item);
            } else if (eatingEnergyConsumption * 0.5 < ElectricItemUtils.getPlayerEnergy(player) && MuseItemUtils.getSaturationLevel(item) >= 1) {
                saturationUsed = 1;
            }

            if (saturationUsed > 0) {
                // populate the tag with the nbt data
                foodStats.writeNBT(foodStatNBT);
                foodStatNBT.setFloat("foodSaturationLevel", foodStatNBT.getFloat("foodSaturationLevel") + saturationUsed);
                // put the values back into foodstats
                foodStats.readNBT(foodStatNBT);
                // update value stored in buffer
                MuseItemUtils.setSaturationLevel(item, MuseItemUtils.getSaturationLevel(item) - saturationUsed);
                // split the cost between using food and using saturation
                ElectricItemUtils.drainPlayerEnergy(player, eatingEnergyConsumption * 0.5 * saturationUsed);
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.autoFeeder;
    }
}