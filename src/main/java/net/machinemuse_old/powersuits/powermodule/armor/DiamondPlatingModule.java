package net.machinemuse_old.powersuits.powermodule.armor;

import net.machinemuse_old.api.IModularItem;
import net.machinemuse_old.general.gui.MuseIcon;
import net.machinemuse_old.powersuits.item.ItemComponent;
import net.machinemuse_old.powersuits.powermodule.PowerModuleBase;
import net.machinemuse_old.utils.MuseCommonStrings;
import net.machinemuse_old.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

import java.util.List;

public class DiamondPlatingModule extends PowerModuleBase {
    public static final String MODULE_DIAMOND_PLATING = "Diamond Plating";

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