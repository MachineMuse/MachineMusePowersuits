package net.machinemuse.powersuits.powermodule.misc;

import net.machinemuse.api.IModularItem;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.minecraft.util.StatCollector;

import java.util.List;

public class CitizenJoeStyle extends PowerModuleBase {
    public static final String CITIZEN_JOE_STYLE = "Citizen Joe Style";

    public CitizenJoeStyle(List<IModularItem> validItems) {
        super(validItems);
    }

    @Override
    public String getTextureFile() {
        return "greendrone";
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_COSMETIC;
    }

    @Override
    public String getDataName() {
        return CITIZEN_JOE_STYLE;
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal("module.citizenJoe.name");
    }

    @Override
    public String getDescription() {
        return StatCollector.translateToLocal("module.citizenJoe.desc");
    }
}
