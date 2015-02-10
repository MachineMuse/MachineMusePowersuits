package net.machinemuse.powersuits.powermodule.misc;

import net.machinemuse.api.IModularItem;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.minecraft.util.StatCollector;

import java.util.List;

public class TintModule extends PowerModuleBase {
    public static String MODULE_TINT = "Custom Colour Module";
    public static final String RED_TINT = "Red Tint";
    public static final String GREEN_TINT = "Green Tint";
    public static final String BLUE_TINT = "Blue Tint";

    public TintModule(List<IModularItem> validItems) {
        super(validItems);
        addTradeoffProperty("Red Intensity", RED_TINT, 1, "%");
        addTradeoffProperty("Green Intensity", GREEN_TINT, 1, "%");
        addTradeoffProperty("Blue Intensity", BLUE_TINT, 1, "%");
        addPropertyLocalString("Red Tint", StatCollector.translateToLocal("module.tint.red"));
        addPropertyLocalString("Green Tint", StatCollector.translateToLocal("module.tint.green"));
        addPropertyLocalString("Blue Tint", StatCollector.translateToLocal("module.tint.blue"));
        addPropertyLocalString("Red Intensity", StatCollector.translateToLocal("module.tint.intensity.red"));
        addPropertyLocalString("Green Intensity", StatCollector.translateToLocal("module.tint.intensity.green"));
        addPropertyLocalString("Blue Intensity", StatCollector.translateToLocal("module.tint.intensity.blue"));
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
    public String getDataName() {
        return MODULE_TINT;
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal("module.tint.name");
    }

    @Override
    public String getDescription() {
        return StatCollector.translateToLocal("module.tint.desc");
    }
}
