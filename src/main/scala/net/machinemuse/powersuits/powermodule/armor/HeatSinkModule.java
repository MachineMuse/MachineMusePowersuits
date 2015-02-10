package net.machinemuse.powersuits.powermodule.armor;

import net.machinemuse.api.IModularItem;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseHeatUtils;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.util.StatCollector;

import java.util.List;

public class HeatSinkModule extends PowerModuleBase {
    public static final String MODULE_HEAT_SINK = "Heat Sink";
    public static final String THICKNESS = "Thickness";

    public HeatSinkModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.basicPlating, 1));
        addTradeoffProperty(THICKNESS, MuseCommonStrings.WEIGHT, 5000, "g");
        addTradeoffProperty(THICKNESS, MuseHeatUtils.MAXIMUM_HEAT, 150, "");
        addPropertyLocalString(THICKNESS, StatCollector.translateToLocal("module.heatSink.thickness"));
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_ARMOR;
    }

    @Override
    public String getDataName() {
        return MODULE_HEAT_SINK;
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal("module.heatSink.name");
    }

    @Override
    public String getDescription() {
        return StatCollector.translateToLocal("module.heatSink.desc");
    }

    @Override
    public String getTextureFile() {
        return "heatresistantplating2";
    }

}
