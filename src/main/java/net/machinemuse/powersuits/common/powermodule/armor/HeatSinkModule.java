package net.machinemuse.powersuits.common.powermodule.armor;

import net.machinemuse.api.IModularItem;
import net.machinemuse.powersuits.client.events.MuseIcon;
import net.machinemuse.powersuits.common.items.ItemComponent;
import net.machinemuse.powersuits.common.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseHeatUtils;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

import java.util.List;

import static net.machinemuse.powersuits.common.MPSConstants.MODULE_HEAT_SINK;

public class HeatSinkModule extends PowerModuleBase {

    public static final String THICKNESS = "Thickness";

    public HeatSinkModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.basicPlating, 1));
        addTradeoffProperty(THICKNESS, MuseCommonStrings.WEIGHT, 5000, "g");
        addTradeoffProperty(THICKNESS, MuseHeatUtils.MAXIMUM_HEAT, 150, "");
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_ARMOR;
    }

    @Override
    public String getDataName() {
        return MODULE_HEAT_SINK;
    }

    @Override
    public String getUnlocalizedName() { return "heatSink";
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.heatSink;
    }
}