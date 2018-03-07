package net.machinemuse.powersuits.item.module.tool;

import net.machinemuse.powersuits.item.module.PowerModuleBase;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
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
    public static final String GRAFTER_ENERGY_CONSUMPTION = "Grafter Energy Consumption";
    public static final String GRAFTER_HEAT_GENERATION = "Grafter Heat Generation";
    private static ItemStack grafter = new ItemStack( Item.REGISTRY.getObject(new ResourceLocation("forestry", "grafter")), 1);

    public GrafterModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.TOOLONLY, resourceDommain, UnlocalizedName);
//        ItemStack stack = GameRegistry.findItemStack("Forestry", "grafter", 1);
        addInstallCost(grafter);
        addBasePropertyDouble(GRAFTER_ENERGY_CONSUMPTION, 1000, "J");
        addBasePropertyDouble(GRAFTER_HEAT_GENERATION, 20);
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_TOOL;
    }
}