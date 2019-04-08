package net.machinemuse.powersuits.item.module.armor;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;

public class ItemModulePlatingEnergyShield extends ItemAbstractPowerModule {
    public ItemModulePlatingEnergyShield(String regName) {
        super(regName, EnumModuleTarget.ARMORONLY, EnumModuleCategory.CATEGORY_ARMOR);
//        addTradeoffPropertyDouble(MPSModuleConstants.MODULE_FIELD_STRENGTH, MPSModuleConstants.ARMOR_VALUE_ENERGY, 6, MPSModuleConstants.MODULE_TRADEOFF_PREFIX + MPSModuleConstants.ARMOR_POINTS);
//        addTradeoffPropertyDouble(MPSModuleConstants.MODULE_FIELD_STRENGTH, MPSModuleConstants.ARMOR_ENERGY_CONSUMPTION, 5000, "RF");
//        addTradeoffPropertyDouble(MPSModuleConstants.MODULE_FIELD_STRENGTH, MAXIMUM_HEAT, 500, "");
    }
}