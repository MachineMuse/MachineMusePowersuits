package net.machinemuse.powersuits.powermodule.modules;

import java.util.Arrays;

import net.machinemuse.api.ElectricItemUtils;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPlayerTickModule;
import net.machinemuse.api.IToggleableModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.machinemuse.powersuits.item.ItemComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class SprintAssistModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {

	public SprintAssistModule() {
		super(Arrays.asList((IModularItem) ModularPowersuits.powerArmorLegs));
		addInstallCost(Config.copyAndResize(ItemComponent.servoMotor, 4));
		addSimpleTradeoff(
				this, "Power",
				MuseCommonStrings.SPRINT_ENERGY_CONSUMPTION, "J", 0, 10,
				MuseCommonStrings.SPRINT_SPEED_MULTIPLIER, "%", 1, 2);
		addSimpleTradeoff(
				this, "Compensation",
				MuseCommonStrings.SPRINT_ENERGY_CONSUMPTION, "J", 0, 2,
				MuseCommonStrings.SPRINT_FOOD_COMPENSATION, "%", 0, 1);
		addSimpleTradeoff(
				this, "Walking Assist",
				MuseCommonStrings.WALKING_ENERGY_CONSUMPTION, "J", 0, 10,
				MuseCommonStrings.WALKING_SPEED_MULTIPLIER, "%", 1, 1);
	}

	@Override
	public MuseIcon getIcon(ItemStack item) {
		return MuseIcon.SPRINT_ASSIST;
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_MOVEMENT;
	}

	@Override
	public String getName() {
		return MuseCommonStrings.MODULE_SPRINT_ASSIST;
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

			double sprintCost = ModuleManager.computeModularProperty(item, MuseCommonStrings.SPRINT_ENERGY_CONSUMPTION);
			if (sprintCost < totalEnergy) {
				double sprintMultiplier = ModuleManager.computeModularProperty(item, MuseCommonStrings.SPRINT_SPEED_MULTIPLIER);
				double exhaustionComp = ModuleManager.computeModularProperty(item, MuseCommonStrings.SPRINT_FOOD_COMPENSATION);
				ElectricItemUtils.drainPlayerEnergy(player, sprintCost * horzMovement * 5);
				player.landMovementFactor *= sprintMultiplier;

				player.getFoodStats().addExhaustion((float) (-0.01 * exhaustion * exhaustionComp));
				player.jumpMovementFactor = player.landMovementFactor * .5f;
			}
		} else {
			double cost = ModuleManager.computeModularProperty(item, MuseCommonStrings.WALKING_ENERGY_CONSUMPTION);
			if (cost < totalEnergy) {
				double walkMultiplier = ModuleManager.computeModularProperty(item, MuseCommonStrings.WALKING_SPEED_MULTIPLIER);
				ElectricItemUtils.drainPlayerEnergy(player, cost * horzMovement * 5);
				player.landMovementFactor *= walkMultiplier;
				player.jumpMovementFactor = player.landMovementFactor * .5f;
			}

		}
	}

	@Override
	public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
		// TODO Auto-generated method stub

	}

}
