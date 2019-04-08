package net.machinemuse.powersuits.item.module.environmental;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;

/**
 * Created by User: Andrew
 * Date: 4/21/13
 * Time: 2:03 PM
 */
public class ItemModuleApiaristArmor extends ItemAbstractPowerModule {
    public ItemModuleApiaristArmor(String regName) {
        super(regName, EnumModuleTarget.ARMORONLY, EnumModuleCategory.CATEGORY_ENVIRONMENTAL);
//        addBasePropertyDouble(MPSModuleConstants.APIARIST_ARMOR_ENERGY_CONSUMPTION, 100, "RF");
    }
}