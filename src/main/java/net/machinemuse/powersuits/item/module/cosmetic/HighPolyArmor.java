package net.machinemuse.powersuits.item.module.cosmetic;

import net.machinemuse.powersuits.item.module.PowerModuleBase;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

/**
 * Modular Powersuits by MachineMuse
 * Created by lehjr on 3/7/17.
 */
public class HighPolyArmor extends PowerModuleBase {
    public HighPolyArmor(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.ARMORONLY, resourceDommain,UnlocalizedName);
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_COSMETIC;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.highPoly;
    }
}