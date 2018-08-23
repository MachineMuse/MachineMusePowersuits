package net.machinemuse.powersuits.powermodule.vision;

import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Method;

public class ThaumGogglesModule extends PowerModuleBase implements IToggleableModule {
    ItemStack gogglesStack = null;

    public ThaumGogglesModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        try {
            Class tcItems = Class.forName("thaumcraft.api.ItemApi");
            Method getItem = tcItems.getDeclaredMethod("getItem", String.class, int.class);
            gogglesStack = (ItemStack) getItem.invoke(null, "itemGoggles", 0);
            ModuleManager.INSTANCE.addInstallCost(getDataName(), ItemComponent.laserHologram.copy());
            ModuleManager.INSTANCE.addInstallCost(getDataName(), gogglesStack);
        } catch (Exception e) {
            e.printStackTrace();
            this.setIsAllowed(false);
        }
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_SPECIAL;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_THAUM_GOGGLES__DATANAME;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.aurameter;
    }
}