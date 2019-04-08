package net.machinemuse.powersuits.item.module.movement;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;

public class ItemModuleFlightControl extends ItemAbstractPowerModule implements IToggleableModule {
    public ItemModuleFlightControl(String regName) {
        super(regName, EnumModuleTarget.HEADONLY, EnumModuleCategory.CATEGORY_MOVEMENT);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
//        addTradeoffPropertyDouble(MPSModuleConstants.VERTICALITY, MPSModuleConstants.FLIGHT_VERTICALITY, 1.0, "%");
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_SPECIAL;
    }
}
