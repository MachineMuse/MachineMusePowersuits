package net.machinemuse.powersuits.item.module.cosmetic;

import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.item.powersuits.module.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

public class CitizenJoeStyle extends PowerModuleBase {
    public CitizenJoeStyle(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.ARMORONLY, resourceDommain,UnlocalizedName);
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_COSMETIC;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.citizenJoe;
    }
}