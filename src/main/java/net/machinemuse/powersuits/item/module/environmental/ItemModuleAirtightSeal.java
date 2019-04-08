package net.machinemuse.powersuits.item.module.environmental;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;

public class ItemModuleAirtightSeal extends ItemAbstractPowerModule {
    public ItemModuleAirtightSeal(String regName) {
        super(regName, EnumModuleTarget.HEADONLY, EnumModuleCategory.CATEGORY_ENVIRONMENTAL);
    }
}