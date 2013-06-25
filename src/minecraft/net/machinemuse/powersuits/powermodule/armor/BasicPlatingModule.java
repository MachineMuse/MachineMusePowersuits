package net.machinemuse.powersuits.powermodule.armor;

import net.machinemuse.api.IModularItem;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.util.StatCollector;

import java.util.List;

public class BasicPlatingModule extends PowerModuleBase {
    public static String MODULE_BASIC_PLATING;

    public BasicPlatingModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.basicPlating, 1));
        addTradeoffProperty("Plating Thickness", MuseCommonStrings.ARMOR_VALUE_PHYSICAL, 5, " Points");
        addTradeoffProperty("Plating Thickness", MuseCommonStrings.WEIGHT, 10000, "g");
    }

    @Override
    public String getTextureFile() {
        return "basicplating2";
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_ARMOR;
    }

    @Override
    public String getName() {
        MODULE_BASIC_PLATING = StatCollector.translateToLocal("module.basicPlating.name");
        return MODULE_BASIC_PLATING;
    }

    @Override
    public String getDescription() {
        return "Basic plating is heavy but protective.";
    }
}
