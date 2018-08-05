//package net.machinemuse.powersuits.powermodule.cosmetic;
//
//import net.machinemuse.numina.api.module.EnumModuleCategory;
//import net.machinemuse.numina.api.module.EnumModuleTarget;
//import net.machinemuse.powersuits.client.event.MuseIcon;
//import net.machinemuse.powersuits.powermodule.PowerModuleBase;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.item.ItemStack;
//
//public class TintModule extends PowerModuleBase {
//    public static final String MODULE_TINT = "Custom Colour Module";
//    public static final String RED_TINT = "Red Tint";
//    public static final String GREEN_TINT = "Green Tint";
//    public static final String BLUE_TINT = "Blue Tint";
//
//    public TintModule(EnumModuleTarget moduleTarget) {
//        super(moduleTarget);
//        addTradeoffProperty("Red Intensity", RED_TINT, 1, "%");
//        addTradeoffProperty("Green Intensity", GREEN_TINT, 1, "%");
//        addTradeoffProperty("Blue Intensity", BLUE_TINT, 1, "%");
//    }
//
//    @Override
//    public EnumModuleCategory getCategory() {
//        return EnumModuleCategory.CATEGORY_COSMETIC;
//    }
//
//    @Override
//    public String getDataName() {
//        return MODULE_TINT;
//    }
//
//    @Override
//    public String getUnlocalizedName() { return "tint";
//    }
//
//    @Override
//    public TextureAtlasSprite getIcon(ItemStack item) {
//        return MuseIcon.tint;
//    }
//}