package net.machinemuse.powersuits.common.items.modules.misc;

import net.machinemuse.numina.api.item.IModularItem;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.items.ItemComponent;
import net.machinemuse.powersuits.common.items.modules.PowerModuleBase;
import net.machinemuse.numina.utils.string.MuseCommonStrings;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

import java.util.List;

import static net.machinemuse.powersuits.common.MPSConstants.BINOCULARS_MODULE;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 1:08 AM, 4/24/13
 *
 * Ported to Java by lehjr on 10/11/16.
 */
public class BinocularsModule extends PowerModuleBase implements IToggleableModule {
    public static final String FOV_MULTIPLIER = "Field of View";

    public BinocularsModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.laserHologram, 1));
        addBaseProperty(BinocularsModule.FOV_MULTIPLIER, 0.5);
        addTradeoffProperty("FOV multiplier", BinocularsModule.FOV_MULTIPLIER, 9.5, "%");
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_VISION;
    }

    @Override
    public String getDataName() {
        return BINOCULARS_MODULE;
    }

    @Override
    public String getUnlocalizedName() {
        return "binoculars";
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.binoculars;
    }
}