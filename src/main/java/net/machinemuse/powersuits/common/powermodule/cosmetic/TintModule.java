package net.machinemuse.powersuits.common.powermodule.cosmetic;

import net.machinemuse.api.IModularItem;
import net.machinemuse.powersuits.client.events.MuseIcon;
import net.machinemuse.powersuits.common.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

import java.util.List;

public class TintModule extends PowerModuleBase {
    public static final String MODULE_TINT = "Custom Colour Module";
    public static final String RED_TINT = "Red Tint";
    public static final String GREEN_TINT = "Green Tint";
    public static final String BLUE_TINT = "Blue Tint";

    public TintModule(List<IModularItem> validItems) {
        super(validItems);
        addTradeoffProperty("Red Intensity", RED_TINT, 1, "%");
        addTradeoffProperty("Green Intensity", GREEN_TINT, 1, "%");
        addTradeoffProperty("Blue Intensity", BLUE_TINT, 1, "%");
    }

//    @Override
//    public boolean isAllowed() {
//        return false;
//    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_COSMETIC;
    }

    @Override
    public String getDataName() {
        return MODULE_TINT;
    }

    @Override
    public String getUnlocalizedName() { return "tint";
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.tint;
    }
}