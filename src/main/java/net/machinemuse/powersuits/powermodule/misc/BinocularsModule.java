package net.machinemuse.powersuits.powermodule.misc;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;

import java.util.List;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 1:08 AM, 4/24/13
 *
 * Ported to Java by lehjr on 10/11/16.
 */
public class BinocularsModule extends PowerModuleBase implements IToggleableModule {
    public static final String BINOCULARS_MODULE = "Binoculars";
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
        return BinocularsModule.BINOCULARS_MODULE;
    }

    @Override
    public String getUnlocalizedName() {
        return "binoculars";
    }

    @Override
    public String getDescription() {
        return "With the problems that have been plaguing Optifine lately, you've decided to take that Zoom ability into your own hands.";
    }

    @Override
    public String getTextureFile() {
        return "binoculars";
    }
}