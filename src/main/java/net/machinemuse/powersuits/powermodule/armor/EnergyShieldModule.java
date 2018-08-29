package net.machinemuse.powersuits.powermodule.armor;

import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.utils.MuseHeatUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

public class EnergyShieldModule extends PowerModuleBase {
    public EnergyShieldModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(this.getDataName(), MuseItemUtils.copyAndResize(ItemComponent.fieldEmitter, 2));
        addTradeoffPropertyDouble(MPSModuleConstants.MODULE_FIELD_STRENGTH, MPSModuleConstants.ARMOR_VALUE_ENERGY, 6, " Points");
        addTradeoffPropertyDouble(MPSModuleConstants.MODULE_FIELD_STRENGTH, MPSModuleConstants.ARMOR_ENERGY_CONSUMPTION, 5000, "RF");
        addTradeoffPropertyDouble(MPSModuleConstants.MODULE_FIELD_STRENGTH, MuseHeatUtils.MAXIMUM_HEAT, 500, "");
        addBasePropertyDouble(MPSModuleConstants.SLOT_POINTS, 1);
        addIntTradeoffProperty(MPSModuleConstants.MODULE_FIELD_STRENGTH, MPSModuleConstants.SLOT_POINTS, 4, "pts", 1, 0);
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_ENERGY_SHIELD__DATANAME;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.energyShield;
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_ARMOR;
    }
}