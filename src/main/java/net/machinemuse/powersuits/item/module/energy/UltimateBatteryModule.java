package net.machinemuse.powersuits.item.module.energy;

import net.machinemuse.numina.api.energy.ElectricConversions;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.item.powersuits.module.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

/**
 * Created by leon on 7/3/16.
 */
public class UltimateBatteryModule extends PowerModuleBase {
    public UltimateBatteryModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.ALLITEMS, resourceDommain, UnlocalizedName);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.evcapacitor, 1));
        addBasePropertyDouble(ElectricItemUtils.MAXIMUM_ENERGY, 1250000, "J");
        addBasePropertyDouble(MPSModuleConstants.WEIGHT, 1500, "g");
        addTradeoffPropertyDouble("Battery Size", ElectricItemUtils.MAXIMUM_ENERGY, 4250000);
        addTradeoffPropertyDouble("Battery Size", MPSModuleConstants.WEIGHT, 6000);
//        addBasePropertyDouble(ElectricConversions.IC2_TIER, 1);
//        addTradeoffPropertyDouble("IC2 Tier", ElectricConversions.IC2_TIER, 3);

        addBasePropertyInt(ElectricConversions.IC2_TIER, 1);
        addTradeoffPropertyInt("IC2 Tier", ElectricConversions.IC2_TIER, 3);
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_ENERGY;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.ultimateBattery;
    }
}