package net.machinemuse.powersuits.powermodule.energy;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.electricity.ElectricConversions;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.util.StatCollector;

import java.util.List;

public class EliteBatteryModule extends PowerModuleBase {
    public static String MODULE_BATTERY_ELITE;

    public EliteBatteryModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.hvcapacitor, 1));
        addBaseProperty(ElectricItemUtils.MAXIMUM_ENERGY(), 750000, "J");
        addBaseProperty(MuseCommonStrings.WEIGHT, 2000, "g");
        addTradeoffProperty("Battery Size", ElectricItemUtils.MAXIMUM_ENERGY(), 4250000);
        addTradeoffProperty("Battery Size", MuseCommonStrings.WEIGHT, 8000);
        addBaseProperty(ElectricConversions.IC2_TIER(), 1);
        addTradeoffProperty("IC2 Tier", ElectricConversions.IC2_TIER(), 2);
    }

    @Override
    public String getTextureFile() {
        return "crystalcapacitor";
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_ENERGY;
    }

    @Override
    public String getName() {
        MODULE_BATTERY_ELITE = StatCollector.translateToLocal("module.eliteBattery.name");
        return MODULE_BATTERY_ELITE;
    }

    @Override
    public String getDescription() {
        return "Integrate a the most advanced battery to store an extensive amount of energy.";
    }
}
