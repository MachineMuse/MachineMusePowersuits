package net.machinemuse.powersuits.powermodule.misc;

import net.machinemuse.api.IModularItem;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;

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
    public String getUnlocalizedName() { return "citizenJoe";
    }

    @Override
    public String getDescription() {
        return "An alternative armor texture, c/o CitizenJoe of IC2 forums.";
    }
}
