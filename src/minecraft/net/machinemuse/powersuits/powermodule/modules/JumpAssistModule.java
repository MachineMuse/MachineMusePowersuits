package net.machinemuse.powersuits.powermodule.modules;

import java.util.Arrays;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPlayerTickModule;
import net.machinemuse.api.IToggleableModule;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.machinemuse.powersuits.common.PlayerInputMap;
import net.machinemuse.powersuits.event.MovementManager;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.tick.PlayerTickHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class JumpAssistModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {

	public JumpAssistModule() {
		super(Arrays.asList((IModularItem) ModularPowersuits.powerArmorLegs));
		addSimpleTradeoff(this, "Power", MuseCommonStrings.JUMP_ENERGY_CONSUMPTION, "J", 0, 25, MuseCommonStrings.JUMP_MULTIPLIER, "%", 1, 4);
		addSimpleTradeoff(this, "Compensation", MuseCommonStrings.JUMP_ENERGY_CONSUMPTION, "J", 0, 5, MuseCommonStrings.JUMP_FOOD_COMPENSATION, "%", 0, 1);
		addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 4));
	}

	@Override
	public MuseIcon getIcon(ItemStack item) {
		return MuseIcon.JUMP_ASSIST;
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_MOVEMENT;
	}

	@Override
	public String getName() {
		return MuseCommonStrings.MODULE_JUMP_ASSIST;
	}

	@Override
	public String getDescription() {
		return "Another set of servo motors to help you jump higher.";
	}

	@Override
	public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
		PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.username);
		boolean jumpkey = movementInput.jumpKey;
		if (jumpkey) {
			double multiplier = MovementManager.getPlayerJumpMultiplier(player);
			if (multiplier > 0) {
				player.motionY += 0.15 * Math.min(multiplier, 1) * PlayerTickHandler.getWeightPenaltyRatio(MuseItemUtils.getPlayerWeight(player), 25000);
				MovementManager.setPlayerJumpTicks(player, multiplier - 1);
			}
			player.jumpMovementFactor = player.landMovementFactor * .7f;
		} else {
			MovementManager.setPlayerJumpTicks(player, 0);
		}
	}

	@Override
	public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {}

}
