package net.machinemuse.powersuits.item.module.cosmetic;

import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.module.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

public class TintModule extends PowerModuleBase {
    public static final String RED_TINT = "Red Tint";
    public static final String GREEN_TINT = "Green Tint";
    public static final String BLUE_TINT = "Blue Tint";

    public TintModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.ARMORONLY, resourceDommain,UnlocalizedName);
        addTradeoffPropertyDouble("Red Intensity", RED_TINT, 1, "%");
        addTradeoffPropertyDouble("Green Intensity", GREEN_TINT, 1, "%");
        addTradeoffPropertyDouble("Blue Intensity", BLUE_TINT, 1, "%");
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_COSMETIC;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.tint;
    }
}