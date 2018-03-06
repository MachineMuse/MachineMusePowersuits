package net.machinemuse.powersuits.item.module.cosmetic;

import net.machinemuse.powersuits.item.module.PowerModuleBase;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

public class CosmeticGlowModule extends PowerModuleBase {
    public static final String RED_GLOW = "Red Glow";
    public static final String GREEN_GLOW = "Green Glow";
    public static final String BLUE_GLOW = "Blue Glow";

    public CosmeticGlowModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.ARMORONLY, resourceDommain,UnlocalizedName);
        addTradeoffPropertyDouble("Red Glow", RED_GLOW, 1, "%");
        addTradeoffPropertyDouble("Green Glow", GREEN_GLOW, 1, "%");
        addTradeoffPropertyDouble("Blue Glow", BLUE_GLOW, 1, "%");
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_COSMETIC;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.cosmeticGlow;
    }
}