package net.machinemuse.powersuits.powermodule.misc;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Method;
import java.util.List;

public class ThaumGogglesModule extends PowerModuleBase implements IToggleableModule {
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
    public String getUnlocalizedName() {
        return "aurameter";
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.aurameter;
    }
}