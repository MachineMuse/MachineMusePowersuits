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
///**
// * Modular Powersuits by MachineMuse
// * Created by lehjr on 3/7/17.
// */
//public class HighPolyArmor extends PowerModuleBase {
//    public static final String HighPolyArmor = "3D Armor";
//
//    public HighPolyArmor(EnumModuleTarget moduleTarget) {
//        super(moduleTarget);
//    }
//
//    @Override
//    public EnumModuleCategory getCategory() {
//        return EnumModuleCategory.CATEGORY_COSMETIC;
//    }
//
//    @Override
//    public String getDataName() {
//        return HighPolyArmor;
//    }
//
//    @Override
//    public String getUnlocalizedName() { return "3dArmor";
//    }
//
//    @Override
//    public TextureAtlasSprite getIcon(ItemStack item) {
//        return MuseIcon.highPoly;
//    }
//}