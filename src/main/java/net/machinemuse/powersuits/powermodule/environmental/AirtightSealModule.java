package net.machinemuse.powersuits.powermodule.environmental;

import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class AirtightSealModule extends PowerModuleBase {
    public AirtightSealModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), new ItemStack(Blocks.GLASS));
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_ENVIRONMENTAL;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.AIRTIGHT_SEAL_MODULE__DATANAME;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.airtightSeal;
    }
}