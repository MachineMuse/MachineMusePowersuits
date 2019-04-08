package net.machinemuse.powersuits.item.module.vision;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 1:08 AM, 4/24/13
 * <p>
 * Ported to Java by lehjr on 10/11/16.
 */
public class ItemModuleBinoculars extends ItemAbstractPowerModule implements IToggleableModule {
    public ItemModuleBinoculars(String regName) {
        super(regName, EnumModuleTarget.HEADONLY, EnumModuleCategory.CATEGORY_VISION);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.laserHologram, 1));
//        addBasePropertyDouble(MPSModuleConstants.FOV, 0.5);
//        addTradeoffPropertyDouble(MPSModuleConstants.FIELD_OF_VIEW, MPSModuleConstants.FOV, 9.5, "%");
    }
}