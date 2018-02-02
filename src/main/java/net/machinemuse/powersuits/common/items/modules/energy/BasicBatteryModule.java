package net.machinemuse.powersuits.common.items.modules.energy;

import net.machinemuse.numina.api.item.IModularItem;
import net.machinemuse.powersuits.api.electricity.ElectricConversions;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.items.ItemComponent;
import net.machinemuse.powersuits.common.items.modules.PowerModuleBase;
import net.machinemuse.numina.utils.energy.ElectricItemUtils;
import net.machinemuse.numina.utils.string.MuseCommonStrings;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

import java.util.List;

import static net.machinemuse.powersuits.common.MPSConstants.MODULE_BATTERY_BASIC;

public class BasicBatteryModule extends PowerModuleBase {
    public BasicBatteryModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.lvcapacitor, 1));
        addBaseProperty(ElectricItemUtils.MAXIMUM_ENERGY, 20000, "J");
        addBaseProperty(MuseCommonStrings.WEIGHT, 2000, "g");
        addTradeoffProperty("Battery Size", ElectricItemUtils.MAXIMUM_ENERGY, 80000);
        addTradeoffProperty("Battery Size", MuseCommonStrings.WEIGHT, 8000);
        addBaseProperty(ElectricConversions.IC2_TIER, 1);
        addTradeoffProperty("IC2 Tier", ElectricConversions.IC2_TIER, 2);
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_ENERGY;
    }

    @Override
    public String getDataName() {
        return MODULE_BATTERY_BASIC;
    }

    @Override
    public String getUnlocalizedName() {
        return "basicBattery";
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.basicBattery;
    }
}