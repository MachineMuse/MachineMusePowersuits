package net.machinemuse.powersuits.item.module.weapon;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;

public class ItemModuleMeleeAssist extends ItemAbstractPowerModule {
    public ItemModuleMeleeAssist(String regName) {
        super(regName, EnumModuleTarget.TOOLONLY, EnumModuleCategory.CATEGORY_WEAPON);

//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.lvcapacitor, 1));
//
//        addBasePropertyDouble(MPSModuleConstants.PUNCH_ENERGY, 10, "RF");
//        addBasePropertyDouble(MPSModuleConstants.PUNCH_DAMAGE, 2, "pt");
//        addTradeoffPropertyDouble(MPSModuleConstants.IMPACT, MPSModuleConstants.PUNCH_ENERGY, 1000, "RF");
//        addTradeoffPropertyDouble(MPSModuleConstants.IMPACT, MPSModuleConstants.PUNCH_DAMAGE, 8, "pt");
//        addTradeoffPropertyDouble(MPSModuleConstants.CARRY_THROUGH, MPSModuleConstants.PUNCH_ENERGY, 200, "RF");
//        addTradeoffPropertyDouble(MPSModuleConstants.CARRY_THROUGH, MPSModuleConstants.PUNCH_KNOCKBACK, 1, "P");
    }
}