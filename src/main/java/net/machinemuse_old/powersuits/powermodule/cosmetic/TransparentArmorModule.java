package net.machinemuse_old.powersuits.powermodule.cosmetic;

import net.machinemuse_old.api.IModularItem;
import net.machinemuse_old.api.moduletrigger.IToggleableModule;
import net.machinemuse_old.general.gui.MuseIcon;
import net.machinemuse_old.powersuits.item.ItemComponent;
import net.machinemuse_old.powersuits.powermodule.PowerModuleBase;
import net.machinemuse_old.utils.MuseCommonStrings;
import net.machinemuse_old.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

import java.util.List;

public class TransparentArmorModule extends PowerModuleBase implements IToggleableModule {
    public static final String MODULE_TRANSPARENT_ARMOR = "Transparent Armor";

    public TransparentArmorModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.laserHologram, 1));
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
        return MODULE_TRANSPARENT_ARMOR;
    }

    @Override
    public String getUnlocalizedName() {
        return "transparentArmor";
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.transparentArmor;
    }
}