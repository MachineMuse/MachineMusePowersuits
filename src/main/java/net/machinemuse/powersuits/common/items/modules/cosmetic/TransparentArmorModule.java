package net.machinemuse.powersuits.common.items.modules.cosmetic;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.powersuits.client.events.MuseIcon;
import net.machinemuse.powersuits.common.items.ItemComponent;
import net.machinemuse.powersuits.common.items.modules.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

import java.util.List;

import static net.machinemuse.powersuits.common.MPSConstants.MODULE_TRANSPARENT_ARMOR;

public class TransparentArmorModule extends PowerModuleBase implements IToggleableModule {


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