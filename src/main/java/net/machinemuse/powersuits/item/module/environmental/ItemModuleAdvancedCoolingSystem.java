package net.machinemuse.powersuits.item.module.environmental;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Created by Eximius88 on 1/17/14.
 */
public class ItemModuleAdvancedCoolingSystem extends ItemAbstractCoolingSystemModule {
    public ItemModuleAdvancedCoolingSystem(String regName) {
        super(regName);

//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(
//                FluidUtil.getFilledBucket(new FluidStack(MPSItems.INSTANCE.liquidNitrogen, 1000)), 1));
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.rubberHose, 2));
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.computerChip, 2));
//
//        addTradeoffPropertyDouble(MPSModuleConstants.ADVANCED_COOLING_POWER, MPSModuleConstants.COOLING_BONUS, 7, "%");
//        addTradeoffPropertyDouble(MPSModuleConstants.ADVANCED_COOLING_POWER, MPSModuleConstants.ADVANCED_COOLING_SYSTEM_ENERGY_CONSUMPTION, 160, "RF/t");
    }

    @Override
    public double getCoolingFactor() {
        return 2.1;
    }

    @Override
    public double getCoolingBonus(@Nonnull ItemStack itemStack) {
        return 0;
//        return ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.COOLING_BONUS);
    }

    @Override
    public double getEnergyConsumption(@Nonnull ItemStack itemStack) {
        return 0;
//        return ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.ADVANCED_COOLING_SYSTEM_ENERGY_CONSUMPTION);
    }
}