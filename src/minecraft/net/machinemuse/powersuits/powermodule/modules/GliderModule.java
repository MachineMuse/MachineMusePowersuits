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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;

public class GliderModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
	public static final String MODULE_GLIDER = "Glider";

	public GliderModule(List<IModularItem> validItems) {
		super(validItems);
		addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.gliderWing, 2));
	}

	@Override
	public MuseIcon getIcon(ItemStack item) {
		return MuseIcon.GLIDER;
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_MOVEMENT;
	}

	@Override
	public String getName() {
		return MODULE_GLIDER;
	}

	@Override
	public String getDescription() {
		return "Tack on some wings to turn downward into forward momentum. Press sneak+forward while falling to activate.";
	}

	@Override
	public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
		Vec3 playerHorzFacing = player.getLookVec();
		playerHorzFacing.yCoord = 0;
		playerHorzFacing.normalize();
		PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.username);
		boolean sneakkey = movementInput.sneakKey;
		float forwardkey = movementInput.forwardKey;
		ItemStack torso = player.getCurrentArmor(2);
		boolean hasParachute = false;
		if (torso != null && torso.getItem() instanceof IModularItem) {
			hasParachute = MuseItemUtils.itemHasActiveModule(torso, ParachuteModule.MODULE_PARACHUTE);
		}
		if (sneakkey && player.motionY < -0.1 && (!hasParachute || forwardkey > 0)) {
			if (player.motionY < -0.1) {
				double motionYchange = Math.min(0.08, -0.1 - player.motionY);
				player.motionY += motionYchange;
				player.motionX += playerHorzFacing.xCoord * motionYchange;
				player.motionZ += playerHorzFacing.zCoord * motionYchange;

				// sprinting speed
				player.jumpMovementFactor += 0.03f;

				// if (gliderTicker == 0) {
				// world.playSoundAtEntity(player,
				// MuseCommonStrings.SOUND_GLIDER, 5.0F, 1.0F);
				// gliderTicker++;
				// }
				// else {
				// gliderTicker++;
				// if (gliderTicker >= 35) {
				// gliderTicker = 0;
				// }
				// }
			}
		}
	}

	@Override
	public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
	}

}
