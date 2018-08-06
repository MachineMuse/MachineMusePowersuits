package net.machinemuse.powersuits.powermodule.armor;

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

public class LeatherPlatingModule extends PowerModuleBase {
    public LeatherPlatingModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(this.getDataName(), MuseItemUtils.copyAndResize(ItemComponent.ironPlating, 1));
        addTradeoffProperty(MPSModuleConstants.ARMOR_PLATING_THICKNESS, MPSModuleConstants.ARMOR_VALUE_PHYSICAL, 3, MPSModuleConstants.POINTS);
        addTradeoffProperty(MPSModuleConstants.ARMOR_PLATING_THICKNESS, MPSModuleConstants.WEIGHT, 10000, "g");
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_IRON_PLATING;
    }

    @Override
    public String getUnlocalizedName() {
        return "leatherPlating";
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.basicPlating;
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_ARMOR;
    }
}