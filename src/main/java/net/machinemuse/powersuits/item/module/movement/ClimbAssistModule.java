package net.machinemuse.powersuits.item.module.movement;

import net.machinemuse.powersuits.item.module.PowerModuleBase;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IPlayerTickModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ClimbAssistModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
    public ClimbAssistModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.LEGSONLY, resourceDommain, UnlocalizedName);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_MOVEMENT;
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        player.stepHeight = 1.001F;
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
        if (player.stepHeight == 1.001F) {
            player.stepHeight = 0.5001F;
        }
    }
}