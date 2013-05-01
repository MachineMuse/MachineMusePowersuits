package net.machinemuse.powersuits.powermodule.movement;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

public class SprintAssistModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
    public static final String MODULE_SPRINT_ASSIST = "Sprint Assist";
    public static final String SPRINT_ENERGY_CONSUMPTION = "Sprint Energy Consumption";
    public static final String SPRINT_SPEED_MULTIPLIER = "Sprint Speed Multiplier";
    public static final String SPRINT_FOOD_COMPENSATION = "Sprint Exhaustion Compensation";
    public static final String WALKING_ENERGY_CONSUMPTION = "Walking Energy Consumption";
    public static final String WALKING_SPEED_MULTIPLIER = "Walking Speed Multiplier";

    public SprintAssistModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 4));
        addSimpleTradeoff(this, "Power", SPRINT_ENERGY_CONSUMPTION, "J", 0, 10, SPRINT_SPEED_MULTIPLIER, "%", 1, 2);
        addSimpleTradeoff(this, "Compensation", SPRINT_ENERGY_CONSUMPTION, "J", 0, 2, SPRINT_FOOD_COMPENSATION, "%", 0, 1);
        addSimpleTradeoff(this, "Walking Assist", WALKING_ENERGY_CONSUMPTION, "J", 0, 10, WALKING_SPEED_MULTIPLIER, "%", 1, 1);
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_MOVEMENT;
    }

    @Override
    public String getName() {
        return MODULE_SPRINT_ASSIST;
    }

    @Override
    public String getDescription() {
        return "A set of servo motors to help you sprint (double-tap forward) and walk faster.";
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        double horzMovement = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ);
        double totalEnergy = ElectricItemUtils.getPlayerEnergy(player);
        if (player.isSprinting()) {
            double exhaustion = Math.round(horzMovement * 100.0F) * 0.01;

            double sprintCost = ModuleManager.computeModularProperty(item, SPRINT_ENERGY_CONSUMPTION);
            if (sprintCost < totalEnergy) {
                double sprintMultiplier = ModuleManager.computeModularProperty(item, SPRINT_SPEED_MULTIPLIER);
                double exhaustionComp = ModuleManager.computeModularProperty(item, SPRINT_FOOD_COMPENSATION);
                ElectricItemUtils.drainPlayerEnergy(player, sprintCost * horzMovement * 5);
                player.landMovementFactor *= sprintMultiplier;

                player.getFoodStats().addExhaustion((float) (-0.01 * exhaustion * exhaustionComp));
                player.jumpMovementFactor = player.landMovementFactor * .5f;
            }
        } else {
            double cost = ModuleManager.computeModularProperty(item, WALKING_ENERGY_CONSUMPTION);
            if (cost < totalEnergy) {
                double walkMultiplier = ModuleManager.computeModularProperty(item, WALKING_SPEED_MULTIPLIER);
                ElectricItemUtils.drainPlayerEnergy(player, cost * horzMovement * 5);
                player.landMovementFactor *= walkMultiplier;
                player.jumpMovementFactor = player.landMovementFactor * .5f;
            }

        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public String getTextureFile() {
        return "sprintassist";
    }

}
