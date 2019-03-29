package net.machinemuse.powersuits.powermodule.environmental;

import net.machinemuse.numina.energy.ElectricItemUtils;
import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.utils.modulehelpers.AutoFeederHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.FoodStats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AutoFeederModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
    public AutoFeederModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));

        addBasePropertyDouble(MPSModuleConstants.EATING_ENERGY_CONSUMPTION, 100);
        addBasePropertyDouble(MPSModuleConstants.EATING_EFFICIENCY, 50);
        addTradeoffPropertyDouble(MPSModuleConstants.EFFICIENCY, MPSModuleConstants.EATING_ENERGY_CONSUMPTION, 1000, "RF");
        addTradeoffPropertyDouble(MPSModuleConstants.EFFICIENCY, MPSModuleConstants.EATING_EFFICIENCY, 50);
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_ENVIRONMENTAL;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_AUTO_FEEDER__DATANAME;
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        double foodLevel = AutoFeederHelper.getFoodLevel(item);
        double saturationLevel = AutoFeederHelper.getSaturationLevel(item);
        IInventory inv = player.inventory;
        double eatingEnergyConsumption = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.EATING_ENERGY_CONSUMPTION);
        double efficiency = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.EATING_EFFICIENCY);

        FoodStats foodStats = player.getFoodStats();
        int foodNeeded = 20 - foodStats.getFoodLevel();
        double saturationNeeded = 20 - foodStats.getSaturationLevel();

        // this consumes all food in the player's inventory and stores the stats in a buffer
        if (MPSConfig.INSTANCE.useOldAutoFeeder()) {
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
            AutoFeederHelper.setFoodLevel(item, foodLevel);
            AutoFeederHelper.setSaturationLevel(item, saturationLevel);
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
            AutoFeederHelper.setFoodLevel(item, foodLevel);
            AutoFeederHelper.setSaturationLevel(item, saturationLevel);
        }

        NBTTagCompound foodStatNBT = new NBTTagCompound();

        // only consume saturation if food is consumed. This keeps the food buffer from overloading with food while the
        //   saturation buffer drains completely.
        if (foodNeeded > 0 && AutoFeederHelper.getFoodLevel(item) >= 1) {
            int foodUsed = 0;
            // if buffer has enough to fill player stat
            if (AutoFeederHelper.getFoodLevel(item) >= foodNeeded && foodNeeded * eatingEnergyConsumption * 0.5 < ElectricItemUtils.getPlayerEnergy(player)) {
                foodUsed = foodNeeded;
                // if buffer has some but not enough to fill the player stat
            } else if ((foodNeeded - AutoFeederHelper.getFoodLevel(item)) > 0 && AutoFeederHelper.getFoodLevel(item) * eatingEnergyConsumption * 0.5 < ElectricItemUtils.getPlayerEnergy(player)) {
                foodUsed = (int) AutoFeederHelper.getFoodLevel(item);
                // last resort where using just 1 unit from buffer
            } else if (eatingEnergyConsumption * 0.5 < ElectricItemUtils.getPlayerEnergy(player) && AutoFeederHelper.getFoodLevel(item) >= 1) {
                foodUsed = 1;
            }
            if (foodUsed > 0) {
                // populate the tag with the nbt data
                foodStats.writeNBT(foodStatNBT);
                foodStatNBT.setInteger("foodLevel",
                        foodStatNBT.getInteger("foodLevel") + foodUsed);
                // put the values back into foodstats
                foodStats.readNBT(foodStatNBT);
                // update getValue stored in buffer
                AutoFeederHelper.setFoodLevel(item, AutoFeederHelper.getFoodLevel(item) - foodUsed);
                // split the cost between using food and using saturation
                ElectricItemUtils.drainPlayerEnergy(player, (int) (eatingEnergyConsumption * 0.5 * foodUsed));

                if (saturationNeeded >= 1.0D) {
                    // using int for better precision
                    int saturationUsed = 0;
                    // if buffer has enough to fill player stat
                    if (AutoFeederHelper.getSaturationLevel(item) >= saturationNeeded && saturationNeeded * eatingEnergyConsumption * 0.5 < ElectricItemUtils.getPlayerEnergy(player)) {
                        saturationUsed = (int) saturationNeeded;
                        // if buffer has some but not enough to fill the player stat
                    } else if ((saturationNeeded - AutoFeederHelper.getSaturationLevel(item)) > 0 && AutoFeederHelper.getSaturationLevel(item) * eatingEnergyConsumption * 0.5 < ElectricItemUtils.getPlayerEnergy(player)) {
                        saturationUsed = (int) AutoFeederHelper.getSaturationLevel(item);
                        // last resort where using just 1 unit from buffer
                    } else if (eatingEnergyConsumption * 0.5 < ElectricItemUtils.getPlayerEnergy(player) && AutoFeederHelper.getSaturationLevel(item) >= 1) {
                        saturationUsed = 1;
                    }

                    if (saturationUsed > 0) {
                        // populate the tag with the nbt data
                        foodStats.writeNBT(foodStatNBT);
                        foodStatNBT.setFloat("foodSaturationLevel", foodStatNBT.getFloat("foodSaturationLevel") + saturationUsed);
                        // put the values back into foodstats
                        foodStats.readNBT(foodStatNBT);
                        // update getValue stored in buffer
                        AutoFeederHelper.setSaturationLevel(item, AutoFeederHelper.getSaturationLevel(item) - saturationUsed);
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

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.autoFeeder;
    }
}