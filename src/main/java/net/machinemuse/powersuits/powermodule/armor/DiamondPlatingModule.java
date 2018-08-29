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

public class DiamondPlatingModule extends PowerModuleBase {
    public DiamondPlatingModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(this.getDataName(), MuseItemUtils.copyAndResize(ItemComponent.diamonddPlating, 1));
        addTradeoffPropertyDouble(MPSModuleConstants.ARMOR_PLATING_THICKNESS, MPSModuleConstants.ARMOR_VALUE_PHYSICAL, 6, MPSModuleConstants.POINTS);
        addTradeoffPropertyDouble(MPSModuleConstants.ARMOR_PLATING_THICKNESS, MuseHeatUtils.MAXIMUM_HEAT, 400, "");
        addBasePropertyDouble(MPSModuleConstants.SLOT_POINTS, 1);
        addIntTradeoffProperty(MPSModuleConstants.ARMOR_PLATING_THICKNESS, MPSModuleConstants.SLOT_POINTS, 4, "pts", 1, 0);
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_ARMOR;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_DIAMOND_PLATING__DATANAME;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.diamondPlating;
    }
}