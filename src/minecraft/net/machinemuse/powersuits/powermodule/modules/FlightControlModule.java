package net.machinemuse.powersuits.powermodule.modules;

import java.util.Arrays;

import net.machinemuse.api.ElectricItemUtils;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPlayerTickModule;
import net.machinemuse.api.IToggleableModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.machinemuse.powersuits.common.PlayerInputMap;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.tick.PlayerTickHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class FlightControlModule extends PowerModuleBase implements IToggleableModule {

	public FlightControlModule() {
		super(Arrays.asList((IModularItem) ModularPowersuits.powerArmorHead));
		addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
	}

	@Override
	public MuseIcon getIcon(ItemStack item) {
		return MuseIcon.INDICATOR_1_GREEN;
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_SPECIAL;
	}

	@Override
	public String getName() {
		return MuseCommonStrings.MODULE_FLIGHT_CONTROL;
	}

	@Override
	public String getDescription() {
		return "An integrated control circuit to help you fly better. Press Z to go down.";
	}

}
