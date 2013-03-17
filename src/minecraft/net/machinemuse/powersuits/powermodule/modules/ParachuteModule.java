package net.machinemuse.powersuits.powermodule.modules;

import java.util.List;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPlayerTickModule;
import net.machinemuse.api.IToggleableModule;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.PlayerInputMap;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.tick.PlayerTickHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ParachuteModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
	public static final String MODULE_PARACHUTE = "Parachute";

	public ParachuteModule(List<IModularItem> validItems) {
		super(validItems);
		addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.parachute, 2));
	}

	@Override
	public MuseIcon getIcon(ItemStack item) {
		return MuseIcon.PARACHUTE_MODULE;
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_MOVEMENT;
	}

	@Override
	public String getName() {
		return MODULE_PARACHUTE;
	}

	@Override
	public String getDescription() {
		return "Add a parachute to slow your descent. Activate by pressing sneak (defaults to Shift) in midair.";
	}

	@Override
	public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
		PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.username);
		float forwardkey = movementInput.forwardKey;
		boolean sneakkey = movementInput.sneakKey;
		ItemStack torso = player.getCurrentArmor(2);
		boolean hasGlider = false;
		if (torso != null && torso.getItem() instanceof IModularItem) {
			hasGlider = MuseItemUtils.itemHasActiveModule(torso, GliderModule.MODULE_GLIDER);
		}
		if (sneakkey && player.motionY < -0.1 && (!hasGlider || forwardkey <= 0)) {
			double totalVelocity = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ + player.motionY * player.motionY)
					* PlayerTickHandler.getWeightPenaltyRatio(MuseItemUtils.getPlayerWeight(player), 25000);
			if (totalVelocity > 0) {
				player.motionX = player.motionX * 0.1 / totalVelocity;
				player.motionY = player.motionY * 0.1 / totalVelocity;
				player.motionZ = player.motionZ * 0.1 / totalVelocity;
			}
		}
	}

	@Override
	public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
	}

}
