package net.machinemuse.powersuits.item.module.environmental;

import net.machinemuse.powersuits.item.module.PowerModuleBase;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.utils.heat.MuseHeatUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

public class HeatSinkModule extends PowerModuleBase {
    public static final String THICKNESS = "Thickness";

    public HeatSinkModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.ARMORONLY, resourceDommain, UnlocalizedName);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.basicPlating, 1));
        addTradeoffPropertyDouble(THICKNESS, MPSModuleConstants.WEIGHT, 5000, "g");
        addTradeoffPropertyDouble(THICKNESS, MuseHeatUtils.MAXIMUM_HEAT, 150, "");
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_ENVIRONMENTAL;
    }
}