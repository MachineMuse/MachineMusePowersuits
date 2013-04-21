package net.machinemuse.powersuits.powermodule.movement;

import java.util.List;

import net.machinemuse.api.*;
import net.machinemuse.api.electricity.ElectricItemUtils;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.powersuits.control.PlayerInputMap;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.tick.PlayerTickHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class JetBootsModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
	public static final String MODULE_JETBOOTS = "Jet Boots";
	public static final String JET_ENERGY_CONSUMPTION = "Jet Energy Consumption";
	public static final String JET_THRUST = "Jet Thrust";

	public JetBootsModule(List<IModularItem> validItems) {
		super(validItems);
		addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.ionThruster, 2));
		addBaseProperty(JET_ENERGY_CONSUMPTION, 0);
		addBaseProperty(JET_THRUST, 0);
		addTradeoffProperty("Thrust", JET_ENERGY_CONSUMPTION, 75);
		addTradeoffProperty("Thrust", JET_THRUST, 0.08);
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_MOVEMENT;
	}

	@Override
	public String getName() {
		return MODULE_JETBOOTS;
	}

	@Override
	public String getDescription() {
		return "Jet boots are not as strong as a jetpack, but they should at least be strong enough to counteract gravity.";
	}

	@Override
	public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        ItemStack chest = player.getCurrentArmor(1);
        if(MuseItemUtils.itemHasActiveModule(chest,JetPackModule.MODULE_JETPACK) || player.isInWater()) {
            return;
        }
		PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.username);
		boolean jumpkey = movementInput.jumpKey;
		ItemStack helmet = player.getCurrentArmor(3);
		boolean hasFlightControl = MuseItemUtils.itemHasActiveModule(helmet, FlightControlModule.MODULE_FLIGHT_CONTROL);
		double jetEnergy = ModuleManager.computeModularProperty(item, JET_ENERGY_CONSUMPTION);
		double thrust = ModuleManager.computeModularProperty(item, JET_THRUST);

		if (jetEnergy < ElectricItemUtils.getPlayerEnergy(player)) {
			thrust *= MusePlayerUtils.getWeightPenaltyRatio(MuseItemUtils.getPlayerWeight(player), 25000);
			if (hasFlightControl && thrust > 0) {
                MusePlayerUtils.thrust(player, thrust, jetEnergy, true);
			} else if (jumpkey && player.motionY < 0.5) {
                MusePlayerUtils.thrust(player, thrust, jetEnergy, false);
			}
		}
	}

	@Override
	public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
	}

	@Override
	public String getTextureFile() {
		return "jetboots";
	}

}
