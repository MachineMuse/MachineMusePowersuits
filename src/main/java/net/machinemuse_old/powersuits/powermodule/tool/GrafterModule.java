package net.machinemuse_old.powersuits.powermodule.tool;

import net.machinemuse_old.api.IModularItem;
import net.machinemuse_old.powersuits.powermodule.PowerModuleBase;
import net.machinemuse_old.utils.MuseCommonStrings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * Created by User: Andrew
 * Date: 4/21/13
 * Time: 2:02 PM
 */
public class GrafterModule extends PowerModuleBase {
    public static final String MODULE_GRAFTER = "Grafter";
    public static final String GRAFTER_ENERGY_CONSUMPTION = "Grafter Energy Consumption";
    public static final String GRAFTER_HEAT_GENERATION = "Grafter Heat Generation";
    private static ItemStack grafter = new ItemStack( Item.REGISTRY.getObject(new ResourceLocation("forestry", "grafter")), 1);

    public GrafterModule(List<IModularItem> validItems) {
        super(validItems);
//        ItemStack stack = GameRegistry.findItemStack("Forestry", "grafter", 1);
        addInstallCost(grafter);
        addBaseProperty(GRAFTER_ENERGY_CONSUMPTION, 1000, "J");
        addBaseProperty(GRAFTER_HEAT_GENERATION, 20);
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_GRAFTER;
    }

    @Override
    public String getUnlocalizedName() {
        return "grafter";
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(grafter).getParticleTexture();
    }
}