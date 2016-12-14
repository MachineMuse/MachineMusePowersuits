package net.machinemuse.powersuits.powermodule.weapon;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.entity.EntitySpinningBlade;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class BladeLauncherModule extends PowerModuleBase implements IRightClickModule {
    public static final String MODULE_BLADE_LAUNCHER = "Blade Launcher";
    public static final String BLADE_ENERGY = "Spinning Blade Energy Consumption";
    public static final String BLADE_DAMAGE = "Spinning Blade Damage";

    public BladeLauncherModule(List<IModularItem> validItems) {
        super(validItems);
        addBaseProperty(BLADE_ENERGY, 500, "J");
        addBaseProperty(BLADE_DAMAGE, 6, "pt");
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 1));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.mvcapacitor, 1));
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_WEAPON;
    }

    @Override
    public String getDataName() {
        return MODULE_BLADE_LAUNCHER;
    }

    @Override
    public String getUnlocalizedName() { return "bladeLauncher";
    }

    @Override
    public String getDescription() {
        return "Launches a spinning blade of death (or shearing).";
    }

    @Override
    public String getTextureFile() {
        return "spinningblade";
    }

    @Override
    public void onRightClick(EntityPlayer player, World world, ItemStack stack) {
        if (ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(stack, BLADE_ENERGY)) {
            player.setItemInUse(stack, 72000);
        }
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {
        // int chargeTicks = Math.max(itemStack.getMaxItemUseDuration() - par4, 10);
        if (!world.isRemote) {
            double energyConsumption = ModuleManager.computeModularProperty(itemStack, BLADE_ENERGY);
            if (ElectricItemUtils.getPlayerEnergy(player) > energyConsumption) {
                ElectricItemUtils.drainPlayerEnergy(player, energyConsumption);
                EntitySpinningBlade blade = new EntitySpinningBlade(world, player);
                world.spawnEntityInWorld(blade);
            }
        }
    }
}
