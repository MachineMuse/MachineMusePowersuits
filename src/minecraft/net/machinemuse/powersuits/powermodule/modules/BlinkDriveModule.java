package net.machinemuse.powersuits.powermodule.modules;

import java.util.List;

import net.machinemuse.api.ElectricItemUtils;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IRightClickModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.MusePlayerUtils;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.MuseLogger;
import net.machinemuse.powersuits.item.ItemComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlinkDriveModule extends PowerModuleBase implements IRightClickModule {

	public BlinkDriveModule(List<IModularItem> validItems) {
		super(validItems);
		addBaseProperty(MuseCommonStrings.BLINK_DRIVE_ENERGY_CONSUMPTION, 1000, "J");
		addBaseProperty(MuseCommonStrings.BLINK_DRIVE_RANGE, 5, "m");
		addTradeoffProperty("Range", MuseCommonStrings.BLINK_DRIVE_ENERGY_CONSUMPTION, 3000);
		addTradeoffProperty("Range", MuseCommonStrings.BLINK_DRIVE_RANGE, 59);
		addInstallCost(Config.copyAndResize(ItemComponent.ionThruster, 1));
		addInstallCost(Config.copyAndResize(ItemComponent.fieldEmitter, 2));
	}

	@Override
	public MuseIcon getIcon(ItemStack item) {
		return MuseIcon.CRYSTAL_BUBBLE;
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_SPECIAL;
	}

	@Override
	public String getName() {
		return MuseCommonStrings.MODULE_BLINK_DRIVE;
	}

	@Override
	public String getDescription() {
		return "Get from point A to point C via point B, where point B is a fold in space & time.";
	}

	@Override
	public void onRightClick(EntityPlayer player, World world, ItemStack itemStack) {
		double range = ModuleManager.computeModularProperty(itemStack, MuseCommonStrings.BLINK_DRIVE_RANGE);
		double energyConsumption = ModuleManager.computeModularProperty(itemStack, MuseCommonStrings.BLINK_DRIVE_ENERGY_CONSUMPTION);
		if (ElectricItemUtils.getPlayerEnergy(player) > energyConsumption) {
			ElectricItemUtils.drainPlayerEnergy(player, energyConsumption);
			world.playSoundAtEntity(player, "mob.endermen.portal", 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
			MuseLogger.logDebug("Range: " + range);
			MovingObjectPosition hitMOP = MusePlayerUtils.doCustomRayTrace(player.worldObj, player, true, range);
			MuseLogger.logDebug("Hit:" + hitMOP);
			MusePlayerUtils.teleportEntity(player, hitMOP);
			world.playSoundAtEntity(player, "mob.endermen.portal", 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
		}

	}

}
