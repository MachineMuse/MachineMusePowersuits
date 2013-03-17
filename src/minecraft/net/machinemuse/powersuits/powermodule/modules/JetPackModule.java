package net.machinemuse.powersuits.powermodule.modules;

import java.util.List;

import net.machinemuse.api.ElectricItemUtils;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPlayerTickModule;
import net.machinemuse.api.IToggleableModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.PlayerInputMap;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.tick.PlayerTickHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class JetPackModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
	public static final String MODULE_JETPACK = "Jetpack";
	public static final String JET_ENERGY_CONSUMPTION = "Jet Energy Consumption";
	public static final String JET_THRUST = "Jet Thrust";

	public JetPackModule(List<IModularItem> validItems) {
		super(validItems);
		addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.ionThruster, 4));
		addBaseProperty(JET_ENERGY_CONSUMPTION, 0, "J/t");
		addBaseProperty(JET_THRUST, 0, "N");
		addTradeoffProperty("Thrust", JET_ENERGY_CONSUMPTION, 150);
		addTradeoffProperty("Thrust", JET_THRUST, 0.16);
	}

	@Override
	public MuseIcon getIcon(ItemStack item) {
		return MuseIcon.JETPACK;
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_MOVEMENT;
	}

	@Override
	public String getName() {
		return MODULE_JETPACK;
	}

	@Override
	public String getDescription() {
		return "A jetpack should allow you to jump indefinitely, or at least until you run out of power.";
	}

	@Override
	public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
		PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.username);
		boolean jumpkey = movementInput.jumpKey;
		ItemStack helmet = player.getCurrentArmor(3);
		ItemStack torso = player.getCurrentArmor(2);
		boolean hasFlightControl = false;
		if (helmet != null && helmet.getItem() instanceof IModularItem) {
			hasFlightControl = MuseItemUtils.itemHasActiveModule(helmet, FlightControlModule.MODULE_FLIGHT_CONTROL);
		}
		double jetEnergy = 0;
		double thrust = 0;
		jetEnergy += ModuleManager.computeModularProperty(torso, JET_ENERGY_CONSUMPTION);
		thrust += ModuleManager.computeModularProperty(torso, JET_THRUST);

		if (jetEnergy < ElectricItemUtils.getPlayerEnergy(player)) {
			thrust *= PlayerTickHandler.getWeightPenaltyRatio(MuseItemUtils.getPlayerWeight(player), 25000);
			if (hasFlightControl && thrust > 0) {
				PlayerTickHandler.thrust(player, thrust, jetEnergy, true);
			} else if (jumpkey && player.motionY < 0.5) {
				PlayerTickHandler.thrust(player, thrust, jetEnergy, false);
			}
		}
	}

	@Override
	public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
	}

}
