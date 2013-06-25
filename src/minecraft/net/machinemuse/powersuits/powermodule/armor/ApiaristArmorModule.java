package net.machinemuse.powersuits.powermodule.armor;

import net.machinemuse.api.IModularItem;
import net.machinemuse.powersuits.common.ModCompatability;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

/**
 * Created by User: Andrew
 * Date: 4/21/13
 * Time: 2:03 PM
 */
public class ApiaristArmorModule extends PowerModuleBase {
    public static String MODULE_APIARIST_ARMOR;
    public static final String APIARIST_ARMOR_ENERGY_CONSUMPTION = "Apiarist Armor Energy Consumption";

    public ApiaristArmorModule(List<IModularItem> validItems) {
        super(validItems);
        ItemStack stack = ModCompatability.getForestryItem("craftingMaterial", 6);
        stack.setItemDamage(3);
        addInstallCost(stack);
        addBaseProperty(APIARIST_ARMOR_ENERGY_CONSUMPTION, 10, "J");
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_ARMOR;
    }

    @Override
    public String getName() {
        MODULE_APIARIST_ARMOR = StatCollector.translateToLocal("module.apiaristArmor.name");
        return MODULE_APIARIST_ARMOR;
    }

    @Override
    public String getDescription() {
        return "A set of Forestry apiarist armor integrated into your armor.";
    }

    @Override
    public String getTextureFile() {
        return "silkWisp";
    }
}
