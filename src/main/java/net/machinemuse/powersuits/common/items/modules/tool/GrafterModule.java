package net.machinemuse.powersuits.common.items.modules.tool;

import net.machinemuse.numina.api.item.IModularItem;
import net.machinemuse.powersuits.common.items.modules.PowerModuleBase;
import net.machinemuse.numina.utils.string.MuseCommonStrings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(grafter).getParticleTexture();
    }
}