package net.machinemuse.powersuits.item.module.environmental;

import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.item.powersuits.module.PowerModuleBase;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

public class HazmatModule extends PowerModuleBase {
    public HazmatModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.ARMORONLY, resourceDommain, UnlocalizedName);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.basicPlating, 3));
        addBasePropertyDouble(MPSModuleConstants.WEIGHT, 0.5);
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_ENVIRONMENTAL;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.hazmat;
    }
}