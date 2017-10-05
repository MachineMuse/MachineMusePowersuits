package net.machinemuse_old.powersuits.powermodule.misc;

import net.machinemuse_old.api.IModularItem;
import net.machinemuse_old.api.moduletrigger.IToggleableModule;
import net.machinemuse_old.powersuits.item.ItemComponent;
import net.machinemuse_old.powersuits.powermodule.PowerModuleBase;
import net.machinemuse_old.utils.MuseCommonStrings;
import net.machinemuse_old.utils.MuseItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by User: Andrew2448
 * 11:12 PM 6/11/13
 */
public class CompassModule extends PowerModuleBase implements IToggleableModule {

    public static final String MODULE_COMPASS = "Compass";
    public static final ItemStack compass = new ItemStack(Items.COMPASS);

    public CompassModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
        addInstallCost(compass);
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(compass).getParticleTexture();
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_SPECIAL;
    }

    @Override
    public String getDataName() {
        return MODULE_COMPASS;
    }

    @Override
    public String getUnlocalizedName() {
        return "compass";
    }
}