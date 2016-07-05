package net.machinemuse.powersuits.powermodule.energy;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.electricity.ElectricConversions;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by leon on 7/3/16.
 */
public class UltimateBatteryModule extends PowerModuleBase {
    public UltimateBatteryModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.evcapacitor, 1));
        addBaseProperty(ElectricItemUtils.MAXIMUM_ENERGY(), 750000, "J");
        addBaseProperty(MuseCommonStrings.WEIGHT, 2000, "g");
        addTradeoffProperty("Battery Size", ElectricItemUtils.MAXIMUM_ENERGY(), 4250000);
        addTradeoffProperty("Battery Size", MuseCommonStrings.WEIGHT, 8000);
        addBaseProperty(ElectricConversions.IC2_TIER(), 1);
        addTradeoffProperty("IC2 Tier", ElectricConversions.IC2_TIER(), 2);
    }

    @Override
    public String getCategory() {
        return null;
    }

    @Override
    public String getDataName() {
        return null;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return super.getIcon(item);
    }
}
/*
package net.machinemuse.powersuits.powermodule.energy;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.electricity.ElectricConversions;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;

import java.util.List;

public class EliteBatteryModule extends PowerModuleBase {
    public static final String MODULE_BATTERY_ELITE = "Elite Battery";

    public EliteBatteryModule(List<IModularItem> validItems) {

    }

    @Override
    public String getTextureFile() {
        return "crystalcapacitor";
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_ENERGY;
    }

    @Override
    public String getDataName() {
        return MODULE_BATTERY_ELITE;
    }

    @Override
    public String getUnlocalizedName() { return "eliteBattery";
    }

    @Override
    public String getDescription() {
        return "Integrate a the most advanced battery to store an extensive amount of energy.";
    }
}

 */