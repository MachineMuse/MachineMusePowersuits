package net.machinemuse.powersuits.item.module.armor;

import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

public class DiamondPlatingModule extends LeatherPlatingModule {
    public DiamondPlatingModule(String resourceDommain, String UnlocalizedName) {
        super(resourceDommain, UnlocalizedName);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.advancedPlating, 1));
        addTradeoffPropertyDouble("Plating Thickness", MPSModuleConstants.ARMOR_VALUE_PHYSICAL, 6, " Points");
        addTradeoffPropertyDouble("Plating Thickness", MPSModuleConstants.WEIGHT, 6000, "g");
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_ARMOR;
    }
}