package net.machinemuse.powersuits.item.module.vision;

import net.machinemuse.item.powersuits.module.PowerModuleBase;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 1:08 AM, 4/24/13
 *
 * Ported to Java by lehjr on 10/11/16.
 */
public class BinocularsModule extends PowerModuleBase implements IToggleableModule {
    public static final String FOV_MULTIPLIER = "Field of View";

    public BinocularsModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.HEADONLY, resourceDommain, UnlocalizedName);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.laserHologram, 1));
        addBasePropertyDouble(BinocularsModule.FOV_MULTIPLIER, 0.5);
        addTradeoffPropertyDouble("FOV multiplier", BinocularsModule.FOV_MULTIPLIER, 9.5, "%");
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_VISION;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.binoculars;
    }
}