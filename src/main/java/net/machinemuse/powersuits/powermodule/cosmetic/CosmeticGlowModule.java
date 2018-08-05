//package net.machinemuse.powersuits.powermodule.cosmetic;
//
//import net.machinemuse.numina.api.module.EnumModuleCategory;
//import net.machinemuse.numina.api.module.EnumModuleTarget;
//import net.machinemuse.powersuits.client.event.MuseIcon;
//import net.machinemuse.powersuits.powermodule.PowerModuleBase;
//import net.machinemuse.powersuits.utils.MuseCommonStrings;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.item.ItemStack;
//
//public class CosmeticGlowModule extends PowerModuleBase {
//    public static final String MODULE_GLOW = "Glow Module";
//    public static final String RED_GLOW = "Red Glow";
//    public static final String GREEN_GLOW = "Green Glow";
//    public static final String BLUE_GLOW = "Blue Glow";
//
//    public CosmeticGlowModule(EnumModuleTarget moduleTarget) {
//        super(moduleTarget);
//        addTradeoffProperty("Red Glow", RED_GLOW, 1, "%");
//        addTradeoffProperty("Green Glow", GREEN_GLOW, 1, "%");
//        addTradeoffProperty("Blue Glow", BLUE_GLOW, 1, "%");
//    }
//
//    @Override
//    public boolean isAllowed() {
//        return false;
//    }
//
//    @Override
//    public EnumModuleCategory getCategory() {
//        return EnumModuleCategory.CATEGORY_COSMETIC;
//    }
//
//    @Override
//    public String getDataName() {
//        return MODULE_GLOW;
//    }
//
//    @Override
//    public String getUnlocalizedName() { return "cosmeticGlow";
//    }
//
//    @Override
//    public TextureAtlasSprite getIcon(ItemStack item) {
//        return MuseIcon.cosmeticGlow;
//    }
//}