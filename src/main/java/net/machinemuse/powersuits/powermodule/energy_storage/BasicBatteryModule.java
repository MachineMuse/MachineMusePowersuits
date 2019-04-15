package net.machinemuse.powersuits.powermodule.energy_storage;

import net.machinemuse.numina.constants.NuminaNBTConstants;
import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

public class BasicBatteryModule extends PowerModuleBase {
    public BasicBatteryModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.lvcapacitor, 1));

        addBasePropertyDouble(NuminaNBTConstants.MAXIMUM_ENERGY, 1000000, "RF");
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_ENERGY_STORAGE;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_BATTERY_BASIC__DATANAME;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.basicBattery;
    }
}