package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.IModularItem;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;

import java.util.List;

/**
 * Created by User: Andrew2448
 * 7:21 PM 4/25/13
 */
public class MFFSFieldTeleporterModule extends PowerModuleBase {
    public static final String MODULE_FIELD_TELEPORTER = "MFFS Field Teleporter";
    public static final String FIELD_TELEPORTER_ENERGY_CONSUMPTION = "Field Teleporter Energy Consumption";

    public MFFSFieldTeleporterModule(List<IModularItem> validItems) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        super(validItems);
        addBaseProperty(FIELD_TELEPORTER_ENERGY_CONSUMPTION, 20000, "J");
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_FIELD_TELEPORTER;
    }

    @Override
    public String getUnlocalizedName() { return "mffsFieldTeleporter";
    }

    @Override
    public String getDescription() {
        return "A tool which allows you to teleport through MFFS forcefields that you own if you have enough energy.";
    }

    @Override
    public String getTextureFile() {
        return "fieldteleporter";
    }
}
