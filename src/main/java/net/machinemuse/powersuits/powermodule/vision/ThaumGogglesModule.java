package net.machinemuse.powersuits.powermodule.vision;

import net.machinemuse.numina.misc.ModCompatibility;
import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ThaumGogglesModule extends PowerModuleBase implements IToggleableModule {
    ItemStack gogglesStack = ItemStack.EMPTY;

    public ThaumGogglesModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        if (ModCompatibility.isThaumCraftLoaded()) {
            gogglesStack = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("thaumcraft", "goggles")));

            ModuleManager.INSTANCE.addInstallCost(getDataName(), ItemComponent.laserHologram.copy());
            ModuleManager.INSTANCE.addInstallCost(getDataName(), gogglesStack);
        }
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_VISION;
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