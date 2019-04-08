package net.machinemuse.powersuits.item.module.armor;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ItemModulePlatingLeather extends ItemAbstractPowerModule {
    protected final ItemStack leather = new ItemStack(Items.LEATHER);

    public ItemModulePlatingLeather(String regName) {
        super(regName, EnumModuleTarget.ARMORONLY, EnumModuleCategory.CATEGORY_ARMOR);
//        addBasePropertyDouble(MPSModuleConstants.ARMOR_VALUE_PHYSICAL, 3, MPSModuleConstants.MODULE_TRADEOFF_PREFIX + MPSModuleConstants.ARMOR_POINTS);
//        addBasePropertyDouble(MAXIMUM_HEAT, 75);
    }
}