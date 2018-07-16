package net.machinemuse.powersuits.powermodule.cosmetic;

import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

public class CitizenJoeStyle extends PowerModuleBase {
    public static final String CITIZEN_JOE_STYLE = "Citizen Joe Style";

    public CitizenJoeStyle(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
    }

    @Override
    public String getDataName() {
        return CITIZEN_JOE_STYLE;
    }

    @Override
    public String getUnlocalizedName() { return "citizenJoe";
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.citizenJoe;
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_COSMETIC;
    }
}