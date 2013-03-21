package net.machinemuse.powersuits.powermodule.modules;

import java.util.List;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IRightClickModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.MusePlayerUtils;
import net.machinemuse.api.electricity.ElectricItemUtils;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlinkDriveModule extends PowerModuleBase implements IRightClickModule {
	public static final String MODULE_BLINK_DRIVE = "Blink Drive";
	public static final String BLINK_DRIVE_ENERGY_CONSUMPTION = "Blink Drive Energy Consuption";
	public static final String BLINK_DRIVE_RANGE = "Blink Drive Range";

	public BlinkDriveModule(List<IModularItem> validItems) {
		super(validItems);
		addBaseProperty(BLINK_DRIVE_ENERGY_CONSUMPTION, 1000, "J");
		addBaseProperty(BLINK_DRIVE_RANGE, 5, "m");
		addTradeoffProperty("Range", BLINK_DRIVE_ENERGY_CONSUMPTION, 3000);
		addTradeoffProperty("Range", BLINK_DRIVE_RANGE, 59);
		addInstallCost(Config.copyAndResize(ItemComponent.ionThruster, 1));
		addInstallCost(Config.copyAndResize(ItemComponent.fieldEmitter, 2));
	}

	@Override
	public String getTextureFile() {
		return "alien";
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_SPECIAL;
	}

	@Override
	public String getName() {
		return MODULE_BLINK_DRIVE;
	}

	@Override
	public String getDescription() {
		return "Get from point A to point C via point B, where point B is a fold in space & time.";
	}

	@Override
	public void onRightClick(EntityPlayer player, World world, ItemStack itemStack) {
		double range = ModuleManager.computeModularProperty(itemStack, BLINK_DRIVE_RANGE);
		double energyConsumption = ModuleManager.computeModularProperty(itemStack, BLINK_DRIVE_ENERGY_CONSUMPTION);
		if (ElectricItemUtils.getPlayerEnergy(player) > energyConsumption) {
			ElectricItemUtils.drainPlayerEnergy(player, energyConsumption);
			world.playSoundAtEntity(player, "mob.endermen.portal", 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
			// MuseLogger.logDebug("Range: " + range);
			MovingObjectPosition hitMOP = MusePlayerUtils.doCustomRayTrace(player.worldObj, player, true, range);
			// MuseLogger.logDebug("Hit:" + hitMOP);
			MusePlayerUtils.teleportEntity(player, hitMOP);
			world.playSoundAtEntity(player, "mob.endermen.portal", 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
		}

	}
}
