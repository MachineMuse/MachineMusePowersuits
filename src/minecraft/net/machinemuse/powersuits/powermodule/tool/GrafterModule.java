package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.IModularItem;
import net.machinemuse.powersuits.common.ModCompatability;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;

import java.util.List;

/**
 * Created by User: Andrew
 * Date: 4/21/13
 * Time: 2:02 PM
 */
public class GrafterModule extends PowerModuleBase {
    public static final String MODULE_GRAFTER = "Grafter";
    public static final String GRAFTER_ENERGY_CONSUMPTION = "Grafter Energy Consumption";
    public static final String GRAFTER_HEAT_GENERATION = "Grafter Heat Generation";

    public GrafterModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(ModCompatability.getForestryItem("grafter", 1));
        addBaseProperty(GRAFTER_ENERGY_CONSUMPTION, 1000, "J");
        addBaseProperty(GRAFTER_HEAT_GENERATION, 20);
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getName() {
        return MODULE_GRAFTER;
    }

    @Override
    public String getDescription() {
        return "A Forestry grafter integrated into your power tool.";
    }

    @Override
    public String getTextureFile() {
        return "grafter";
    }
}
