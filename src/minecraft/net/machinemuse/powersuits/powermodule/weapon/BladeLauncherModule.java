package net.machinemuse.powersuits.powermodule.weapon;

import java.util.List;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.electricity.ElectricItemUtils;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.entity.EntitySpinningBlade;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class BladeLauncherModule extends PowerModuleBase implements IRightClickModule {

	public BladeLauncherModule(List<IModularItem> validItems) {
		super(validItems);
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_WEAPON;
	}

	@Override
	public String getName() {
		return "Blade Launcher";
	}

	@Override
	public String getDescription() {
		return "Launches a spinning blade of death (or shearing; no damage to sheep).";
	}

	@Override
	public String getTextureFile() {
		return "clawclosed";
	}

	@Override
	public void onRightClick(EntityPlayer player, World world, ItemStack item) {
		if (ElectricItemUtils.getPlayerEnergy(player) > 500) {
			player.setItemInUse(item, 72000);
		}
	}

	@Override
	public void onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {

	}

	@Override
	public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ) {
		return false;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {
		int chargeTicks = Math.max(itemStack.getMaxItemUseDuration() - par4, 10);

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			double energyConsumption = ModuleManager.computeModularProperty(itemStack, PlasmaCannonModule.PLASMA_CANNON_ENERGY_PER_TICK)
					* chargeTicks;
			if (ElectricItemUtils.getPlayerEnergy(player) > energyConsumption) {
				ElectricItemUtils.drainPlayerEnergy(player, energyConsumption);
				double explosiveness = ModuleManager.computeModularProperty(itemStack, PlasmaCannonModule.PLASMA_CANNON_EXPLOSIVENESS);
				double damagingness = ModuleManager.computeModularProperty(itemStack, PlasmaCannonModule.PLASMA_CANNON_DAMAGE_AT_FULL_CHARGE);

				EntitySpinningBlade blade = new EntitySpinningBlade(world, player);
				world.spawnEntityInWorld(blade);
			}
		}
	}

}
