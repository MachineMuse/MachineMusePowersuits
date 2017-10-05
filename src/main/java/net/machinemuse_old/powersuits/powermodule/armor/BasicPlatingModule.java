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

public class BasicPlatingModule extends PowerModuleBase {
    public static final String MODULE_BASIC_PLATING = "Iron Plating";

    public BasicPlatingModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.basicPlating, 1));
        addTradeoffProperty("Plating Thickness", MuseCommonStrings.ARMOR_VALUE_PHYSICAL, 5, " Points");
        addTradeoffProperty("Plating Thickness", MuseCommonStrings.WEIGHT, 10000, "g");
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_ARMOR;
    }

    @Override
    public String getDataName() {
        return MODULE_BASIC_PLATING;
    }

    @Override
    public String getUnlocalizedName() {
        return "basicPlating";
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.basicPlating;
    }
}