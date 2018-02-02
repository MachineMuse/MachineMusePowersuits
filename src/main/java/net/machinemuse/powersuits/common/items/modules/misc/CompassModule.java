package net.machinemuse.powersuits.common.items.modules.misc;

import net.machinemuse.numina.api.item.IModularItem;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.powersuits.common.items.ItemComponent;
import net.machinemuse.powersuits.common.items.modules.PowerModuleBase;
import net.machinemuse.numina.utils.string.MuseCommonStrings;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.List;

import static net.machinemuse.powersuits.common.MPSConstants.MODULE_COMPASS;

/**
 * Created by User: Andrew2448
 * 11:12 PM 6/11/13
 */
public class CompassModule extends PowerModuleBase implements IToggleableModule {
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