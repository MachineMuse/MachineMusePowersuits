package net.machinemuse.powersuits.powermodule.modules;

import java.util.Arrays;
import java.util.List;

import net.machinemuse.api.ElectricItemUtils;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPlayerTickModule;
import net.machinemuse.api.IToggleableModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.machinemuse.powersuits.common.PlayerInputMap;
import net.machinemuse.powersuits.item.ItemComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class SwimAssistModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
	public static final String MODULE_SWIM_BOOST = "Swim Boost";
	public static final String SWIM_BOOST_AMOUNT = "Underwater Movement Boost";
	public static final String SWIM_BOOST_ENERGY_CONSUMPTION = "Swim Boost Energy Consumption";
	public SwimAssistModule(List<IModularItem> validItems) {
		super(validItems);
		addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.ionThruster, 1));
		addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 2));
		addTradeoffProperty("Thrust", SWIM_BOOST_ENERGY_CONSUMPTION, 100, "J");
		addTradeoffProperty("Thrust", SWIM_BOOST_AMOUNT, 1, "m/s");
	}

	@Override
	public MuseIcon getIcon(ItemStack item) {
		return MuseIcon.SWIM_BOOST;
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_MOVEMENT;
	}

	@Override
	public String getName() {
		return MODULE_SWIM_BOOST;
	}

	@Override
	public String getDescription() {
		return "By refitting an ion thruster for underwater use, you may be able to add extra forward (or backward) thrust when underwater.";
	}

	@Override
	public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
		if (player.isInWater()) {
			ItemStack pants = player.getCurrentArmor(1);
			PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.username);
			boolean jumpkey = movementInput.jumpKey;
			boolean sneakkey = movementInput.sneakKey;
			float forwardkey = movementInput.forwardKey;
			float strafekey = movementInput.strafeKey;
			if (forwardkey != 0 || strafekey != 0 || jumpkey || sneakkey) {
				double moveRatio = 0;
				if (forwardkey != 0) {
					moveRatio += forwardkey * forwardkey;
				}
				if (strafekey != 0) {
					moveRatio += strafekey * strafekey;
				}
				if (jumpkey || sneakkey) {
					moveRatio += 0.2 * 0.2;
				}
				double swimAssistRate = ModuleManager.computeModularProperty(pants, SWIM_BOOST_AMOUNT) * 0.05;
				double swimEnergyConsumption = ModuleManager.computeModularProperty(pants, SWIM_BOOST_ENERGY_CONSUMPTION);
				if (swimEnergyConsumption < ElectricItemUtils.getPlayerEnergy(player)) {

					// if (swimTicker == 0) {
					// world.playSoundAtEntity(player,
					// MuseCommonStrings.SOUND_SWIM_ASSIST, 2.0F, 1.0F);
					// swimTicker++;
					// }
					// else {
					// swimTicker++;
					// if (swimTicker >= 60) {
					// swimTicker = 0;
					// }
					// }
					// Forward/backward movement
					player.motionX += player.getLookVec().xCoord * swimAssistRate * forwardkey / moveRatio;
					player.motionY += player.getLookVec().yCoord * swimAssistRate * forwardkey / moveRatio;
					player.motionZ += player.getLookVec().zCoord * swimAssistRate * forwardkey / moveRatio;

					if (jumpkey) {
						player.motionY += swimAssistRate * 0.2 / moveRatio;
					}

					if (sneakkey) {
						player.motionY -= swimAssistRate * 0.2 / moveRatio;
					}
					ElectricItemUtils.drainPlayerEnergy(player, swimEnergyConsumption);
				}
			}
		}
	}

	@Override
	public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {}

}
