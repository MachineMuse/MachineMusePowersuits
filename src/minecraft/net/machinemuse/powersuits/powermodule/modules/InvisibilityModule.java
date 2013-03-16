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

public class InvisibilityModule extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {
	public static final String MODULE_ACTIVE_CAMOUFLAGE = "Active Camouflage";
	public InvisibilityModule() {
		super(Arrays.asList((IModularItem) ModularPowersuits.powerArmorTorso));
		addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.laserHologram, 4));
		addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.fieldEmitter, 2));
		addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 2));
	}

	@Override
	public MuseIcon getIcon(ItemStack item) {
		return MuseIcon.ORB_1_BLUE;
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_SPECIAL;
	}

	@Override
	public String getName() {
		return MODULE_ACTIVE_CAMOUFLAGE;
	}

	@Override
	public String getDescription() {
		return "Emit a hologram of your surroundings to make yourself almost imperceptible.";
	}

	@Override
	public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
		double totalEnergy = ElectricItemUtils.getPlayerEnergy(player);
		PotionEffect invis = null;
		Collection<PotionEffect> effects = player.getActivePotionEffects();
		for (PotionEffect effect : effects) {
			if (effect.getAmplifier() == 81 && effect.getPotionID() == Potion.invisibility.id) {
				invis = effect;
				break;
			}
		}
		if (50 < totalEnergy) {
			if (invis == null || invis.getDuration() < 210) {
				player.addPotionEffect(new PotionEffect(Potion.invisibility.id, 500, 81));
				ElectricItemUtils.drainPlayerEnergy(player, 50);
			}
		} else {
			if (invis != null) {
				player.removePotionEffect(Potion.invisibility.id);
			}
		}
	}

	@Override
	public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {}
}
