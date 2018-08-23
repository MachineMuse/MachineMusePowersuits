package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

/**
 * Created by User: Andrew2448
 * 7:21 PM 4/25/13
 */
public class MFFSFieldTeleporterModule extends PowerModuleBase {
    public static final String MODULE_FIELD_TELEPORTER = "MFFS Field Teleporter";
    public static final String FIELD_TELEPORTER_ENERGY_CONSUMPTION = "Field Teleporter Energy Consumption";

    public MFFSFieldTeleporterModule(EnumModuleTarget moduleTarget) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        super(moduleTarget);
        addBasePropertyDouble(FIELD_TELEPORTER_ENERGY_CONSUMPTION, 20000, "J");
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_FIELD_TELEPORTER;
    }

    @Override
    public String getUnlocalizedName() {
        return "mffsFieldTeleporter";
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.mffsFieldTeleporter;
    }
}