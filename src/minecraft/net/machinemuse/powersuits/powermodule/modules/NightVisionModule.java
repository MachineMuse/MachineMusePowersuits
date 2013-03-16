package net.machinemuse.powersuits.powermodule.modules;

import java.util.Arrays;
import java.util.Collection;

import net.machinemuse.api.ElectricItemUtils;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPlayerTickModule;
import net.machinemuse.api.IToggleableModule;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.machinemuse.powersuits.item.ItemComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class NightVisionModule extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {
	public NightVisionModule() {
		super(Arrays.asList((IModularItem) ModularPowersuits.powerArmorHead));
		addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.laserHologram, 1));
		addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
	}

	@Override
	public MuseIcon getIcon(ItemStack item) {
		return MuseIcon.SCANNER;
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_SPECIAL;
	}

	@Override
	public String getName() {
		return MuseCommonStrings.MODULE_NIGHT_VISION;
	}

	@Override
	public String getDescription() {
		return "A pair of augmented vision goggles to help you see at night and underwater.";
	}

	@Override
	public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
		double totalEnergy = ElectricItemUtils.getPlayerEnergy(player);
		PotionEffect nightVision = null;
		Collection<PotionEffect> effects = player.getActivePotionEffects();
		for (PotionEffect effect : effects) {
			if (effect.getAmplifier() == -337 && effect.getPotionID() == Potion.nightVision.id) {
				nightVision = effect;
				break;
			}
		}
		if (5 < totalEnergy) {
			if (nightVision == null || nightVision.getDuration() < 210) {
				player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 500, -337));
				ElectricItemUtils.drainPlayerEnergy(player, 5);
			}
		} else {
			if (nightVision != null) {
				player.removePotionEffect(Potion.nightVision.id);
			}
		}
	}

	@Override
	public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {}
}
