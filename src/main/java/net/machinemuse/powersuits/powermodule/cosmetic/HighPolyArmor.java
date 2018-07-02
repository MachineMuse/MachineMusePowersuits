package net.machinemuse.powersuits.powermodule.cosmetic;

import net.machinemuse.api.IModularItem;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Modular Powersuits by MachineMuse
 * Created by lehjr on 3/7/17.
 */
public class HighPolyArmor extends PowerModuleBase {
    public static final String HighPolyArmor = "3D Armor";

    public HighPolyArmor(List<IModularItem> validItems) {
        super(validItems);
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_COSMETIC;
    }

    @Override
    public String getDataName() {
        return HighPolyArmor;
    }

    @Override
    public String getUnlocalizedName() { return "3dArmor";
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.highPoly;
    }
}