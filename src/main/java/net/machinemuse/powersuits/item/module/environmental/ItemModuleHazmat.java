package net.machinemuse.powersuits.item.module.environmental;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;

public class ItemModuleHazmat extends ItemAbstractPowerModule {
    public ItemModuleHazmat(String regName) {
        super(regName, EnumModuleTarget.ARMORONLY, EnumModuleCategory.CATEGORY_ENVIRONMENTAL);
    }
}