package net.machinemuse.powersuits.item.module.vision;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;

public class ItemModuleThaumicGoggles extends ItemAbstractPowerModule implements IToggleableModule {
    public ItemModuleThaumicGoggles(String regName) {
        super(regName, EnumModuleTarget.HEADONLY, EnumModuleCategory.CATEGORY_VISION);
//        if (ModCompatibility.isThaumCraftLoaded()) {
//            gogglesStack = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("thaumcraft", "goggles")));
//
//            ModuleManager.INSTANCE.addInstallCost(getDataName(), ItemComponent.laserHologram.copy());
//            ModuleManager.INSTANCE.addInstallCost(getDataName(), gogglesStack);
//        }
    }
}