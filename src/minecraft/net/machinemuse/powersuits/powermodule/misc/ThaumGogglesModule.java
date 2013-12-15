package net.machinemuse.powersuits.powermodule.misc;

import net.machinemuse.api.IModularItem;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.lang.reflect.Method;
import java.util.List;

public class ThaumGogglesModule extends PowerModuleBase {
    public static final String MODULE_THAUM_GOGGLES = "Aurameter";
    ItemStack gogglesStack = null;

    public ThaumGogglesModule(List<IModularItem> validItems) {
        super(validItems);
        try {
            Class tcItems = Class.forName("thaumcraft.api.ItemApi");
            Method getItem = tcItems.getDeclaredMethod("getItem", String.class, int.class);
            gogglesStack = (ItemStack) getItem.invoke(null, "itemGoggles", 0);
            addInstallCost(ItemComponent.laserHologram.copy()).addInstallCost(gogglesStack);
        } catch (Exception e) {
            e.printStackTrace();
            this.setIsAllowed(false);
        }
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_SPECIAL;
    }

    @Override
    public String getDataName() {
        return MODULE_THAUM_GOGGLES;
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal("module.aurameter.name");
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
