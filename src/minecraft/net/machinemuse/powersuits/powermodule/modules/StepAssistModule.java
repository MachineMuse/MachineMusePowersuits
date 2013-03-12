package net.machinemuse.powersuits.powermodule.modules;

import java.util.List;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPlayerTickModule;
import net.machinemuse.api.IToggleableModule;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class StepAssistModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
	public StepAssistModule(List<IModularItem> validItems) {
		super(validItems);
		addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
	}

	@Override
	public MuseIcon getIcon(ItemStack item) {
		return MuseIcon.STEP_ASSIST;
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_MOVEMENT;
	}

	@Override
	public String getName() {
		return MuseCommonStrings.MODULE_CLIMB_ASSIST;
	}

	@Override
	public String getDescription() {
		return "A pair of dedicated servos allow you to effortlessly step up 1m-high ledges.";
	}

	@Override
	public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
		player.stepHeight = 1.001F;
	}

	@Override
	public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
		if (player.stepHeight == 1.001F) {
			player.stepHeight = 0.5001F;
		}
	}

}
