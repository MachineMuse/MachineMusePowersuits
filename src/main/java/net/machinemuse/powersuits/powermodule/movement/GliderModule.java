package net.machinemuse.powersuits.powermodule.movement;

import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.numina.player.NuminaPlayerUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.control.PlayerMovementInputWrapper;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

public class GliderModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
    public GliderModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.gliderWing, 2));
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_MOVEMENT;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_GLIDER__DATANAME;
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack itemStack) {
        Vec3d playerHorzFacing = player.getLookVec();
        playerHorzFacing = new Vec3d(playerHorzFacing.x, 0, playerHorzFacing.z);
        playerHorzFacing.normalize();
        PlayerMovementInputWrapper.PlayerMovementInput playerInput = PlayerMovementInputWrapper.get(player);

        NuminaPlayerUtils.resetFloatKickTicks(player);
        boolean hasParachute = ModuleManager.INSTANCE.itemHasActiveModule(itemStack, MPSModuleConstants.MODULE_PARACHUTE__DATANAME);
        if (playerInput.sneakKey && player.motionY < 0 && (!hasParachute || playerInput.moveForward > 0)) {
            if (player.motionY < -0.1) {
                float vol = (float) (player.motionX * player.motionX + player.motionZ * player.motionZ);
                double motionYchange = Math.min(0.08, -0.1 - player.motionY);
                player.motionY += motionYchange;
                player.motionX += playerHorzFacing.x * motionYchange;
                player.motionZ += playerHorzFacing.z * motionYchange;

                // sprinting speed
                player.jumpMovementFactor += 0.03f;
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.glider;
    }
}