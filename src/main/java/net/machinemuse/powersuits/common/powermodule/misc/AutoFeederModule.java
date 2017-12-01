package net.machinemuse.powersuits.common.powermodule.misc;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.powersuits.client.events.MuseIcon;
import net.machinemuse.powersuits.common.config.MPSSettings;
import net.machinemuse.powersuits.common.items.ItemComponent;
import net.machinemuse.powersuits.common.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static net.machinemuse.powersuits.common.MPSConstants.MODULE_AUTO_FEEDER;

public class AutoFeederModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
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
        if (MPSSettings.general.useOldAutoFeeder) {
            IInventory inv = player.inventory;
            double foodLevel = MuseItemUtils.getFoodLevel(item);
            double saturationLevel = MuseItemUtils.getSaturationLevel(item);
            double efficiency = ModuleManager.computeModularProperty(item, EATING_EFFICIENCY);
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (stack != null && stack.getItem() instanceof ItemFood) {
                    ItemFood food = (ItemFood) stack.getItem();
                    for (int a = 0; a < stack.getCount(); a++) {
                        foodLevel += food.getHealAmount(stack);
                        saturationLevel += food.getSaturationModifier(stack);
                    }
                    foodLevel = foodLevel * efficiency / 100.0;
                    saturationLevel = saturationLevel * efficiency / 100.0;
                    MuseItemUtils.setFoodLevel(item, foodLevel);
                    MuseItemUtils.setSaturationLevel(item, saturationLevel);
                    player.inventory.setInventorySlotContents(i, null);
                }
            }
            double eatingEnergyConsumption = ModuleManager.computeModularProperty(item, EATING_ENERGY_CONSUMPTION);
            FoodStats foodStats = player.getFoodStats();
            int foodNeeded = 20 - foodStats.getFoodLevel();
            if (foodNeeded > 0 && (eatingEnergyConsumption * foodNeeded < ElectricItemUtils.getPlayerEnergy(player)) && MuseItemUtils.getFoodLevel(item) > foodNeeded) {
                if (MuseItemUtils.getSaturationLevel(item) >= 1.0F) {
                    foodStats.addStats(foodNeeded, 1.0F);
                    MuseItemUtils.setSaturationLevel(item, MuseItemUtils.getSaturationLevel(item) - 1.0F);
                } else {
                    foodStats.addStats(foodNeeded, 0.0F);
                }
                MuseItemUtils.setFoodLevel(item, MuseItemUtils.getFoodLevel(item) - foodNeeded);
                ElectricItemUtils.drainPlayerEnergy(player, eatingEnergyConsumption * foodNeeded);
            }
        } else {
            double totalEnergy = ElectricItemUtils.getPlayerEnergy(player);
            IInventory inv = player.inventory;
            double foodLevel = MuseItemUtils.getFoodLevel(item);
            double saturationLevel = MuseItemUtils.getSaturationLevel(item);
            double efficiency = ModuleManager.computeModularProperty(item, EATING_EFFICIENCY);
            FoodStats foodStats = player.getFoodStats();
            int foodNeeded = 20 - foodStats.getFoodLevel();
            for (int i = 0; i < inv.getSizeInventory() && foodNeeded > foodLevel; i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (stack != null && stack.getItem() instanceof ItemFood) {
                    ItemFood food = (ItemFood) stack.getItem();
                    for (; stack.getCount() > 0 && foodNeeded > foodLevel; stack.setCount(stack.getCount()-1)) {
                        foodLevel += food.getHealAmount(stack) * efficiency / 100.0;
                        saturationLevel += food.getSaturationModifier(stack) * efficiency / 100.0;
                    }
                    if (stack.getCount() == 0) {
                        player.inventory.setInventorySlotContents(i, null);
                    }
                }
            }
            double eatingEnergyConsumption = foodNeeded * ModuleManager.computeModularProperty(item, EATING_ENERGY_CONSUMPTION);
            int foodConsumed = (int) Math.min(foodNeeded, Math.min(foodLevel, eatingEnergyConsumption * totalEnergy));
            if (foodConsumed > 0) {
                if (saturationLevel >= 1.0F) {
                    foodStats.addStats(foodConsumed, 1.0F);
                    saturationLevel -= 1.0F;
                } else {
                    foodStats.addStats(foodConsumed, 0.0F);
                }
                foodLevel -= foodConsumed;
                ElectricItemUtils.drainPlayerEnergy(player, eatingEnergyConsumption * foodConsumed);
            }
            MuseItemUtils.setFoodLevel(item, foodLevel);
            MuseItemUtils.setSaturationLevel(item, saturationLevel);
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