package net.machinemuse.powersuits.item.module.cosmetic;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;

public class ItemModuleTransparentArmor extends ItemAbstractPowerModule implements IToggleableModule {
    public ItemModuleTransparentArmor(String regName) {
        super(regName, EnumModuleTarget.ARMORONLY, EnumModuleCategory.CATEGORY_COSMETIC);
    }
}