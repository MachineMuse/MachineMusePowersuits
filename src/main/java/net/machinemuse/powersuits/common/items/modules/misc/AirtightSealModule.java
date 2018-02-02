package net.machinemuse.powersuits.common.items.modules.misc;

import net.machinemuse.numina.api.item.IModularItem;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.items.modules.PowerModuleBase;
import net.machinemuse.numina.utils.string.MuseCommonStrings;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.List;

public class AirtightSealModule extends PowerModuleBase {
    public static final String AIRTIGHT_SEAL_MODULE = "Airtight Seal";

    public AirtightSealModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(new ItemStack(Blocks.GLASS));
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_ENVIRONMENTAL;
    }

    @Override
    public String getDataName() {
        return AIRTIGHT_SEAL_MODULE;
    }

    @Override
    public String getUnlocalizedName() {
        return "airtightSeal";
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.airtightSeal;
    }
}