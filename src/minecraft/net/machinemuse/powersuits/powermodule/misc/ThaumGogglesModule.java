package net.machinemuse.powersuits.powermodule.misc;

import net.machinemuse.api.IModularItem;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.lang.reflect.Field;
import java.util.List;

public class ThaumGogglesModule extends PowerModuleBase {
    public static String MODULE_THAUM_GOGGLES;
    Class tcItems;
    ItemStack gogglesStack = null;

    public ThaumGogglesModule(List<IModularItem> validItems) {
        super(validItems);
        try {
            tcItems = Class.forName("thaumcraft.common.Config");
            Field itemGoggles = tcItems.getField("itemGoggles");
            Item goggles = (Item) itemGoggles.get(itemGoggles);
            gogglesStack = new ItemStack(goggles);
            addInstallCost(ItemComponent.laserHologram.copy()).addInstallCost(gogglesStack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_SPECIAL;
    }

    @Override
    public String getName() {
        MODULE_THAUM_GOGGLES = StatCollector.translateToLocal("module.aurameter.name");
        return MODULE_THAUM_GOGGLES;
    }

    @Override
    public String getDescription() {
        return "Connect up some Thaumic goggles to show the nearby aura values. (Does not reveal aura nodes, only shows the HUD)";
    }

    @Override
    public String getTextureFile() {
        return "bluestar";
    }
}
