package net.machinemuse.powersuits.common.items.modules.cosmetic;

import net.machinemuse.api.IModularItem;
import net.machinemuse.powersuits.client.events.MuseIcon;
import net.machinemuse.powersuits.common.items.modules.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

import java.util.List;

import static net.machinemuse.powersuits.common.MPSConstants.HIGH_POLY_ARMOR;

/**
 * Modular Powersuits by MachineMuse
 * Created by lehjr on 3/7/17.
 */
public class HighPolyArmor extends PowerModuleBase {
    public HighPolyArmor(List<IModularItem> validItems) {
        super(validItems);
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_COSMETIC;
    }

    @Override
    public String getDataName() {
        return HIGH_POLY_ARMOR;
    }

    @Override
    public String getUnlocalizedName() { return "3dArmor";
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.highPoly;
    }
}