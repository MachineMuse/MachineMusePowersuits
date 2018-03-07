package net.machinemuse.powersuits.item.module.special;

import net.machinemuse.powersuits.item.module.PowerModuleBase;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * Created by User: Andrew2448
 * 11:12 PM 6/11/13
 */
public class ClockModule extends PowerModuleBase implements IToggleableModule {
    public static final ItemStack clock = new ItemStack(Items.CLOCK);

    public ClockModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.HEADONLY, resourceDommain, UnlocalizedName);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
        addInstallCost(clock);
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_SPECIAL;
    }
}