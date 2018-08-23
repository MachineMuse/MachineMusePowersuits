package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by User: Andrew
 * Date: 4/21/13
 * Time: 2:02 PM
 */
public class GrafterModule extends PowerModuleBase {
    private static ItemStack grafter = new ItemStack( Item.REGISTRY.getObject(new ResourceLocation("forestry", "grafter")), 1);
    public GrafterModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
//        ItemStack stack = GameRegistry.findItemStack("Forestry", "grafter", 1);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), grafter);
        addBasePropertyInteger(MPSModuleConstants.GRAFTER_ENERGY_CONSUMPTION, 10000, "RF");
        addBasePropertyInteger(MPSModuleConstants.GRAFTER_HEAT_GENERATION, 20);
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_GRAFTER__DATANAME;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(grafter).getParticleTexture();
    }
}