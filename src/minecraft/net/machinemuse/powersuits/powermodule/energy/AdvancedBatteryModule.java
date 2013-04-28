package net.machinemuse.powersuits.powermodule.energy;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.electricity.ElectricConversions;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;

import java.util.List;

public class AdvancedBatteryModule extends PowerModuleBase {
    public static final String MODULE_BATTERY_ADVANCED = "Advanced Battery";

    public AdvancedBatteryModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.mvcapacitor, 1));
        addBaseProperty(ElectricItemUtils.MAXIMUM_ENERGY(), 100000, "J");
        addBaseProperty(MuseCommonStrings.WEIGHT, 2000, "g");
        addTradeoffProperty("Battery Size", ElectricItemUtils.MAXIMUM_ENERGY(), 400000);
        addTradeoffProperty("Battery Size", MuseCommonStrings.WEIGHT, 8000);
        addBaseProperty(ElectricConversions.IC2_TIER(), 1);
        addTradeoffProperty("IC2 Tier", ElectricConversions.IC2_TIER(), 2);
    }

    @Override
    public String getTextureFile() {
        return "mvbattery";
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_ENERGY;
    }

    @Override
    public String getName() {
        return MODULE_BATTERY_ADVANCED;
    }

    @Override
    public String getDescription() {
        return "Integrate a more advanced battery to store more energy.";
    }
}
