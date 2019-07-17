package net.machinemuse.powersuits.powermodule.environmental;

import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.MPSItems;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.item.ItemComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;

/**
 * Created by Eximius88 on 1/17/14.
 */
public class AdvancedCoolingSystem extends CoolingSystemBase {
    public AdvancedCoolingSystem(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        Fluid liquid_nitrogen = FluidRegistry.getFluid("liquidnitrogen");
        if (liquid_nitrogen == null)
            liquid_nitrogen = FluidRegistry.getFluid("liquid_nitrogen");

        if (liquid_nitrogen != null) {
            ItemStack nitrogenBucket = FluidUtil.getFilledBucket(new FluidStack(liquid_nitrogen, 1000));
            ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(
                    nitrogenBucket, 1));
        }

        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.rubberHose, 2));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.computerChip, 2));

        addTradeoffPropertyDouble(MPSModuleConstants.ADVANCED_COOLING_POWER, MPSModuleConstants.COOLING_BONUS, 7, "%");
        addTradeoffPropertyDouble(MPSModuleConstants.ADVANCED_COOLING_POWER, MPSModuleConstants.ADVANCED_COOLING_SYSTEM_ENERGY_CONSUMPTION, 160, "RF/t");
    }

    @Override
    public double getCoolingFactor() {
        return 2.1;
    }

    @Override
    public double getCoolingBonus(@Nonnull ItemStack itemStack) {
        return ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.COOLING_BONUS);
    }

    @Override
    public double getEnergyConsumption(@Nonnull ItemStack itemStack) {
        return ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.ADVANCED_COOLING_SYSTEM_ENERGY_CONSUMPTION);
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_ADVANCED_COOLING_SYSTEM__DATANAME;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.advancedCoolingSystem;
    }
}