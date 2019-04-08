package net.machinemuse.powersuits.item.module.armor;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;

public class ItemModulePlatingDiamond extends ItemAbstractPowerModule {
    public ItemModulePlatingDiamond(String regName) {
        super(regName, EnumModuleTarget.ARMORONLY, EnumModuleCategory.CATEGORY_ARMOR);
//        addBasePropertyDouble(MPSModuleConstants.ARMOR_VALUE_PHYSICAL, 5, MPSModuleConstants.MODULE_TRADEOFF_PREFIX + MPSModuleConstants.ARMOR_POINTS);
//        addBasePropertyDouble(MAXIMUM_HEAT, 400);
    }
}