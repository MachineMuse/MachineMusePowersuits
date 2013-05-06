package net.machinemuse.powersuits.powermodule.misc;

import net.machinemuse.api.IModularItem;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;

import java.util.List;

public class TransparentArmorModule extends PowerModuleBase {
    public static final String MODULE_TRANSPARENT_ARMOR = "Transparent Armor";

    public TransparentArmorModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.laserHologram, 1));
    }

    @Override
    public boolean isAllowed() {
        return false;
    }
    @Override
    public String getTextureFile() {
        return "transparentarmor";
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_COSMETIC;
    }

    @Override
    public String getName() {
        return MODULE_TRANSPARENT_ARMOR;
    }

    @Override
    public String getDescription() {
        return "Make the item transparent, so you can show off your skin without losing armor.";
    }
}
