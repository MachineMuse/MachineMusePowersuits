package net.machinemuse.powersuits.item.module.armor;

import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

public class EnergyShieldModule extends LeatherPlatingModule {
    public EnergyShieldModule(String resourceDommain, String UnlocalizedName) {
        super(resourceDommain, UnlocalizedName);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.fieldEmitter, 2));
        addTradeoffPropertyDouble("Field Strength", MPSModuleConstants.ARMOR_VALUE_ENERGY, 6, " Points");
        addTradeoffPropertyDouble("Field Strength", MPSModuleConstants.ARMOR_ENERGY_CONSUMPTION, 500, "J");
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_ARMOR;
    }
}