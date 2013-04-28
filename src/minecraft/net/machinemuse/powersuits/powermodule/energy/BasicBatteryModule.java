package net.machinemuse.powersuits.powermodule.energy;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.electricity.ElectricConversions;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;

import java.util.List;

public class BasicBatteryModule extends PowerModuleBase {
    public static final String MODULE_BATTERY_BASIC = "Basic Battery";

    public BasicBatteryModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.lvcapacitor, 1));
        addBaseProperty(ElectricItemUtils.MAXIMUM_ENERGY(), 20000, "J");
        addBaseProperty(MuseCommonStrings.WEIGHT, 2000, "g");
        addTradeoffProperty("Battery Size", ElectricItemUtils.MAXIMUM_ENERGY(), 80000);
        addTradeoffProperty("Battery Size", MuseCommonStrings.WEIGHT, 8000);
        addBaseProperty(ElectricConversions.IC2_TIER(), 1);
        addTradeoffProperty("IC2 Tier", ElectricConversions.IC2_TIER(), 2);
    }

    @Override
    public String getTextureFile() {
        return "lvbattery";
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_ENERGY;
    }

    @Override
    public String getName() {
        return MODULE_BATTERY_BASIC;
    }

    @Override
    public String getDescription() {
        return "Integrate a battery to allow the item to store energy.";
    }
}
