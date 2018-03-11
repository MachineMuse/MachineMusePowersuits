package net.machinemuse.powersuits.item.module.environmental;

import net.machinemuse.powersuits.item.module.PowerModuleBase;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.MuseItemUtils;

public class HazmatModule extends PowerModuleBase {
    public HazmatModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.ARMORONLY, resourceDommain, UnlocalizedName);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.ironPlating, 3));
        addBasePropertyDouble(MPSModuleConstants.WEIGHT, 0.5);
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_ENVIRONMENTAL;
    }
}