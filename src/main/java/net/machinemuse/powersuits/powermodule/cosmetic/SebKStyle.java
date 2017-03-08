package net.machinemuse.powersuits.powermodule.cosmetic;

import net.machinemuse.api.IModularItem;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Modular Powersuits by MachineMuse
 * Created by lehjr on 3/7/17.
 */
public class SebKStyle extends PowerModuleBase {
    public static final String SEBK_STYLE = "SebK Style";

    public SebKStyle(List<IModularItem> validItems) {
        super(validItems);
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_COSMETIC;
    }

    @Override
    public String getDataName() {
        return SEBK_STYLE;
    }

    @Override
    public String getUnlocalizedName() { return "SebK";
    }

    @Override
    public String getDescription() {
        return "SebK armor texture.";
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.sebk;
    }
}