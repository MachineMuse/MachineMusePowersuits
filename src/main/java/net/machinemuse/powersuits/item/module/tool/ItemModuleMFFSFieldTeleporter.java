package net.machinemuse.powersuits.item.module.tool;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;

/**
 * Created by User: Andrew2448
 * 7:21 PM 4/25/13
 */
public class ItemModuleMFFSFieldTeleporter extends ItemAbstractPowerModule {
    public ItemModuleMFFSFieldTeleporter(String regName) {//} throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        super(regName, EnumModuleTarget.TOOLONLY, EnumModuleCategory.CATEGORY_TOOL);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
//
//        addBasePropertyDouble(MPSModuleConstants.FIELD_TELEPORTER_ENERGY_CONSUMPTION, 200000, "RF");
    }
}