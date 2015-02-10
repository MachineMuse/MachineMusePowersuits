package net.machinemuse.powersuits.powermodule.misc;

import net.machinemuse.api.IModularItem;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class AirtightSealModule extends PowerModuleBase {
    public static final String AIRTIGHT_SEAL_MODULE = "Airtight Seal";

    public AirtightSealModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(new ItemStack(Blocks.glass));
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
    public String getLocalizedName() {
        return StatCollector.translateToLocal("module.airtightSeal.name");
    }

    @Override
    public String getDescription() {
        return StatCollector.translateToLocal("module.airtightSeal.desc");
    }

    @Override
    public String getTextureFile() {
        return "glasspane";
    }

}
