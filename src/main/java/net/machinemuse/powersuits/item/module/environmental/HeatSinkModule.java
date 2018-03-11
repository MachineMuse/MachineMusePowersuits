package net.machinemuse.powersuits.item.module.environmental;

import net.machinemuse.numina.api.constants.NuminaNBTConstants;
import net.machinemuse.powersuits.item.module.PowerModuleBase;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.MuseItemUtils;

public class HeatSinkModule extends PowerModuleBase {
    public static final String THICKNESS = "Thickness";

    public HeatSinkModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.ARMORONLY, resourceDommain, UnlocalizedName);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.ironPlating, 1));
        addTradeoffPropertyDouble(THICKNESS, MPSModuleConstants.WEIGHT, 5000, "g");
        addTradeoffPropertyDouble(THICKNESS, NuminaNBTConstants.MAXIMUM_HEAT, 150, "");
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_ENVIRONMENTAL;
    }
}