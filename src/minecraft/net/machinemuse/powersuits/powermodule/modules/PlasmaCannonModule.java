package net.machinemuse.powersuits.powermodule.modules;

import java.util.List;

import net.machinemuse.api.ElectricItemUtils;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IRightClickModule;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PlasmaCannonModule extends PowerModuleBase implements IRightClickModule {
	public static final String MODULE_PLASMA_CANNON = "Plasma Cannon";
	public static final String PLASMA_CANNON_ENERGY_PER_TICK = "Plasma Energy Per Tick";
	public static final String PLASMA_CANNON_DAMAGE_AT_FULL_CHARGE = "Plasma Damage At Full Charge";
	public static final String PLASMA_CANNON_EXPLOSIVENESS = "Plasma Explosiveness";

	public PlasmaCannonModule(List<IModularItem> validItems) {
		super(validItems);
		addBaseProperty(PLASMA_CANNON_ENERGY_PER_TICK, 10, "J");
		addBaseProperty(PLASMA_CANNON_DAMAGE_AT_FULL_CHARGE, 2, "pt");
		addTradeoffProperty("Amperage", PLASMA_CANNON_ENERGY_PER_TICK, 150, "J");
		addTradeoffProperty("Amperage", PLASMA_CANNON_DAMAGE_AT_FULL_CHARGE, 38, "pt");
		addTradeoffProperty("Voltage", PLASMA_CANNON_ENERGY_PER_TICK, 50, "J");
		addTradeoffProperty("Voltage", PLASMA_CANNON_EXPLOSIVENESS, 0.5, "Creeper");
		addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.fieldEmitter, 2));
		addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.hvcapacitor, 2));
	}

	@Override
	public MuseIcon getIcon(ItemStack item) {
		return MuseIcon.WEAPON_ELECTRIC;
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_WEAPON;
	}

	@Override
	public String getName() {
		return MODULE_PLASMA_CANNON;
	}

	@Override
	public String getDescription() {
		return "Use electrical arcs in a containment field to superheat air to a plasma and launch it at enemies.";
	}

	@Override
	public void onRightClick(EntityPlayer player, World world, ItemStack item) {
		if (ElectricItemUtils.getPlayerEnergy(player) > 500) {
			player.setItemInUse(item, 72000);
		}
	}

}
