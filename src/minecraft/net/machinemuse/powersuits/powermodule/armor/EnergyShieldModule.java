package net.machinemuse.powersuits.powermodule.armor;

import net.machinemuse.api.IModularItem;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.util.StatCollector;

import java.util.List;

public class EnergyShieldModule extends PowerModuleBase {
    public static String MODULE_ENERGY_SHIELD;

    public EnergyShieldModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.fieldEmitter, 2));
        addTradeoffProperty("Field Strength", MuseCommonStrings.ARMOR_VALUE_ENERGY, 6, " Points");
        addTradeoffProperty("Field Strength", MuseCommonStrings.ARMOR_ENERGY_CONSUMPTION, 500, "J");
    }

    @Override
    public String getTextureFile() {
        return "energyshield";
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_ARMOR;
    }

    @Override
    public String getName() {
        MODULE_ENERGY_SHIELD = StatCollector.translateToLocal("module.energyShield.name");
        return MODULE_ENERGY_SHIELD;
    }

    @Override
    public String getDescription() {
        return "Energy shields are much lighter than plating, but consume energy.";
    }
}
