package net.machinemuse.powersuits.item.module.movement;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;

public class ItemModuleShockAbsorber extends ItemAbstractPowerModule implements IToggleableModule {
    public ItemModuleShockAbsorber(String regName) {
        super(regName, EnumModuleTarget.FEETONLY, EnumModuleCategory.CATEGORY_MOVEMENT);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), new ItemStack(Blocks.WOOL, 2));
//        addBasePropertyDouble(MPSModuleConstants.SHOCK_ABSORB_ENERGY_CONSUMPTION, 0, "RF/m");
//        addTradeoffPropertyDouble(MPSModuleConstants.POWER, MPSModuleConstants.SHOCK_ABSORB_ENERGY_CONSUMPTION, 100);
//        addBasePropertyDouble(MPSModuleConstants.SHOCK_ABSORB_MULTIPLIER, 0, "%");
//        addTradeoffPropertyDouble(MPSModuleConstants.POWER, MPSModuleConstants.SHOCK_ABSORB_MULTIPLIER, 10);
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_MOVEMENT;
    }
}