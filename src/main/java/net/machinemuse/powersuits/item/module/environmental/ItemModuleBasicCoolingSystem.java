package net.machinemuse.powersuits.item.module.environmental;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemModuleBasicCoolingSystem extends ItemAbstractCoolingSystemModule {
    public ItemModuleBasicCoolingSystem(String regName) {
        super(regName);
//        addTradeoffPropertyDouble(MPSModuleConstants.BASIC_COOLING_POWER, MPSModuleConstants.COOLING_BONUS, 4, "%");
//        addTradeoffPropertyDouble(MPSModuleConstants.BASIC_COOLING_POWER, MPSModuleConstants.BASIC_COOLING_SYSTEM_ENERGY_CONSUMPTION, 100, "RF/t");
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack itemStack) {
//        if (player.world.isRemote)
//            return;
//
//        super.onPlayerTickActive(player, itemStack);
//        FluidUtils fluidUtils = new FluidUtils(player, itemStack, this.getDataName());
//        fluidUtils.fillWaterFromEnvironment();
    }

    @Override
    public double getCoolingFactor() {
        return 1;
    }

    @Override
    public double getCoolingBonus(@Nonnull ItemStack itemStack) {
        return 0;

//        return ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.COOLING_BONUS);
    }

    @Override
    public double getEnergyConsumption(@Nonnull ItemStack itemStack) {
        return 0;
//        return ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.BASIC_COOLING_SYSTEM_ENERGY_CONSUMPTION);
    }
}