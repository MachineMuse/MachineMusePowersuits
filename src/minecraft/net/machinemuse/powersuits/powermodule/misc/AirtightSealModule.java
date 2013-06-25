package net.machinemuse.powersuits.powermodule.misc;

import net.machinemuse.api.IModularItem;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class AirtightSealModule extends PowerModuleBase {
    public static String AIRTIGHT_SEAL_MODULE;

    public AirtightSealModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(new ItemStack(Block.glass));
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_ENVIRONMENTAL;
    }

    @Override
    public String getName() {
        AIRTIGHT_SEAL_MODULE = StatCollector.translateToLocal("module.airtightSeal.name");
        return AIRTIGHT_SEAL_MODULE;
    }

    @Override
    public String getDescription() {
        return "Seal the suit against hostile atmospheres for venturing to other planets.";
    }

    @Override
    public String getTextureFile() {
        return "glasspane";
    }

}
