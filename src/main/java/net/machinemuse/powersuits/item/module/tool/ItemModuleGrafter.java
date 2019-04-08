package net.machinemuse.powersuits.item.module.tool;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;

/**
 * Created by User: Andrew
 * Date: 4/21/13
 * Time: 2:02 PM
 */
public class ItemModuleGrafter extends ItemAbstractPowerModule {
//    private static ItemStack grafter = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("forestry", "grafter")), 1);

    public ItemModuleGrafter(String regName) {
        super(regName, EnumModuleTarget.TOOLONLY, EnumModuleCategory.CATEGORY_TOOL);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), grafter);
//        addBasePropertyDouble(MPSModuleConstants.GRAFTER_ENERGY_CONSUMPTION, 10000, "RF");
//        addBasePropertyDouble(MPSModuleConstants.GRAFTER_HEAT_GENERATION, 20);
    }
}