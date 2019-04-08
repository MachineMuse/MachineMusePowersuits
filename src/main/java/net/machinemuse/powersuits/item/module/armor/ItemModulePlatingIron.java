package net.machinemuse.powersuits.item.module.armor;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;

public class ItemModulePlatingIron extends ItemAbstractPowerModule {
    public ItemModulePlatingIron(String regName) {
        super(regName, EnumModuleTarget.ARMORONLY, EnumModuleCategory.CATEGORY_ARMOR);
//        addBasePropertyDouble(MPSModuleConstants.ARMOR_VALUE_PHYSICAL, 4, MPSModuleConstants.ARMOR_POINTS);
//        addBasePropertyDouble(MAXIMUM_HEAT, 300);
    }
}