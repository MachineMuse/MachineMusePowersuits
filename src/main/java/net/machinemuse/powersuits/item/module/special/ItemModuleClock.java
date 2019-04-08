package net.machinemuse.powersuits.item.module.special;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;

/**
 * Created by User: Andrew2448
 * 11:12 PM 6/11/13
 */
public class ItemModuleClock extends ItemAbstractPowerModule implements IToggleableModule {
   public ItemModuleClock(String regName) {
        super(regName, EnumModuleTarget.HEADONLY, EnumModuleCategory.CATEGORY_SPECIAL);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), clock);
    }
}