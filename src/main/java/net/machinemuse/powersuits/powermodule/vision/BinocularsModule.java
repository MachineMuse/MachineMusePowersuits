package net.machinemuse.powersuits.powermodule.vision;

import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 1:08 AM, 4/24/13
 *
 * Ported to Java by lehjr on 10/11/16.
 */
public class BinocularsModule extends PowerModuleBase implements IToggleableModule {
    public BinocularsModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.laserHologram, 1));
        addBasePropertyDouble(MPSModuleConstants.FOV_MULTIPLIER, 0.5);
        addTradeoffPropertyDouble("FOV multiplier", MPSModuleConstants.FOV_MULTIPLIER, 9.5, "%");
        addBasePropertyDouble(MPSModuleConstants.SLOT_POINTS, 5);
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_VISION;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.BINOCULARS_MODULE__DATANAME;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.binoculars;
    }
}