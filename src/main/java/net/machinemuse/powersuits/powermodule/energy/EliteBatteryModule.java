package net.machinemuse.powersuits.powermodule.energy;

import net.machinemuse.numina.api.constants.NuminaNBTConstants;
import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

public class EliteBatteryModule extends PowerModuleBase {
    public EliteBatteryModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.hvcapacitor, 1));
        addBasePropertyDouble(NuminaNBTConstants.MAXIMUM_ENERGY, 7500000, "RF");
        addTradeoffPropertyDouble(MPSModuleConstants.BATTERY_SIZE, NuminaNBTConstants.MAXIMUM_ENERGY, 42500000);
        addBasePropertyDouble(MPSModuleConstants.SLOT_POINTS, 1);
        addIntTradeoffProperty(MPSModuleConstants.BATTERY_SIZE, MPSModuleConstants.SLOT_POINTS, 4, "pts", 1, 0);
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_ENERGY;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_BATTERY_ELITE__DATANAME;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.eliteBattery;
    }
}