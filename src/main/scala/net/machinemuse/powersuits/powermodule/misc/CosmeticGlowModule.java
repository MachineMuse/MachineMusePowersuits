package net.machinemuse.powersuits.powermodule.misc;

import net.machinemuse.api.IModularItem;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.minecraft.util.StatCollector;

import java.util.List;

public class CosmeticGlowModule extends PowerModuleBase {
    public static final String MODULE_GLOW = "Glow Module";
    public static final String RED_GLOW = "Red Glow";
    public static final String GREEN_GLOW = "Green Glow";
    public static final String BLUE_GLOW = "Blue Glow";

    public CosmeticGlowModule(List<IModularItem> validItems) {
        super(validItems);
        addTradeoffProperty(RED_GLOW, RED_GLOW, 1, "%");
        addTradeoffProperty(GREEN_GLOW, GREEN_GLOW, 1, "%");
        addTradeoffProperty(BLUE_GLOW, BLUE_GLOW, 1, "%");
        addPropertyLocalString(RED_GLOW, StatCollector.translateToLocal("module.cosmeticGlow.red"));
        addPropertyLocalString(GREEN_GLOW, StatCollector.translateToLocal("module.cosmeticGlow.green"));
        addPropertyLocalString(BLUE_GLOW, StatCollector.translateToLocal("module.cosmeticGlow.blue"));
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
        return MODULE_GLOW;
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal("module.cosmeticGlow.name");
    }

    @Override
    public String getDescription() {
        return StatCollector.translateToLocal("module.cosmeticGlow.desc");
    }

    @Override
    public String getTextureFile() {
        return "netherstar";
    }

}
