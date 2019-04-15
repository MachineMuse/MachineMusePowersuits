package net.machinemuse.powersuits.powermodule.environmental;

import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.modulehelpers.FluidUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class BasicCoolingSystemModule extends CoolingSystemBase {
    public BasicCoolingSystemModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), new ItemStack(Items.ENDER_EYE, 4));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));

        addTradeoffPropertyDouble(MPSModuleConstants.BASIC_COOLING_POWER, MPSModuleConstants.COOLING_BONUS, 4, "%");
        addTradeoffPropertyDouble(MPSModuleConstants.BASIC_COOLING_POWER, MPSModuleConstants.BASIC_COOLING_SYSTEM_ENERGY_CONSUMPTION, 100, "RF/t");
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_BASIC_COOLING_SYSTEM__DATANAME;
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack itemStack) {
        if (player.world.isRemote)
            return;

        super.onPlayerTickActive(player, itemStack);
        FluidUtils fluidUtils = new FluidUtils(player, itemStack, this.getDataName());
        fluidUtils.fillWaterFromEnvironment();
    }

    @Override
    public double getCoolingFactor() {
        return 1;
    }

    @Override
    public double getCoolingBonus(@Nonnull ItemStack itemStack) {
        return ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.COOLING_BONUS);
    }

    @Override
    public double getEnergyConsumption(@Nonnull ItemStack itemStack) {
        return ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.BASIC_COOLING_SYSTEM_ENERGY_CONSUMPTION);
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.basicCoolingSystem;
    }
}