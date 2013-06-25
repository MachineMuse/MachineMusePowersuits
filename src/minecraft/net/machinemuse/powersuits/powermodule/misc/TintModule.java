package net.machinemuse.powersuits.powermodule.misc;

import net.machinemuse.api.IModularItem;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.minecraft.util.StatCollector;

import java.util.List;

public class TintModule extends PowerModuleBase {
    public static String MODULE_TINT;
    public static final String RED_TINT = "Red Tint";
    public static final String GREEN_TINT = "Green Tint";
    public static final String BLUE_TINT = "Blue Tint";

    public TintModule(List<IModularItem> validItems) {
        super(validItems);
        addTradeoffProperty("Red Intensity", RED_TINT, 1, "%");
        addTradeoffProperty("Green Intensity", GREEN_TINT, 1, "%");
        addTradeoffProperty("Blue Intensity", BLUE_TINT, 1, "%");
    }

    @Override
    public String getTextureFile() {
        return "netherstar";
    }

    @Override
    public boolean isAllowed() {
        return false;
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_COSMETIC;
    }

    @Override
    public String getName() {
        MODULE_TINT = StatCollector.translateToLocal("module.tint.name");
        return MODULE_TINT;
    }

    @Override
    public String getDescription() {
        return "Give your armor some coloured tinting to customize your armor's appearance.";
    }
}
