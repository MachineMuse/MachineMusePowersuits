package net.machinemuse.powersuits.common.items.modules.armor;

import net.machinemuse.api.IModularItem;
import net.machinemuse.powersuits.client.events.MuseIcon;
import net.machinemuse.powersuits.common.items.ItemComponent;
import net.machinemuse.powersuits.common.items.modules.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

import java.util.List;

import static net.machinemuse.powersuits.common.MPSConstants.MODULE_DIAMOND_PLATING;

public class DiamondPlatingModule extends PowerModuleBase {


    public DiamondPlatingModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.advancedPlating, 1));
        addTradeoffProperty("Plating Thickness", MuseCommonStrings.ARMOR_VALUE_PHYSICAL, 6, " Points");
        addTradeoffProperty("Plating Thickness", MuseCommonStrings.WEIGHT, 6000, "g");
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_ARMOR;
    }

    @Override
    public String getDataName() {
        return MODULE_DIAMOND_PLATING;
    }

    @Override
    public String getUnlocalizedName() { return "diamondPlating";
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.diamondPlating;
    }
}