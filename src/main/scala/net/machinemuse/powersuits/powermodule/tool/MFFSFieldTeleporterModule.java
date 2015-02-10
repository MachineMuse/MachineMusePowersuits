package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.IModularItem;
import net.machinemuse.powersuits.common.ModCompatability;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

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
        addPropertyLocalString(FIELD_TELEPORTER_ENERGY_CONSUMPTION, StatCollector.translateToLocal("module.mffsFieldTeleporter.energy"));
        ItemStack stack = ModCompatability.getMFFSItem("MFFSitemForcePowerCrystal", 1);
        if (stack == null) {
            throw new IllegalAccessException("Failed to get MFFS forcefield teleporter");
        }
        addInstallCost(stack);
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
    public String getLocalizedName() {
        return StatCollector.translateToLocal("module.mffsFieldTeleporter.name");
    }

    @Override
    public String getDescription() {
        return StatCollector.translateToLocal("module.mffsFieldTeleporter.desc");
    }

    @Override
    public String getTextureFile() {
        return "fieldteleporter";
    }
}
