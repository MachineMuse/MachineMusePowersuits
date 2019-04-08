package net.machinemuse.powersuits.item.module.movement;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemModuleGlider extends ItemAbstractPowerModule implements IToggleableModule, IPlayerTickModule {
    public ItemModuleGlider(String regName) {
        super(regName, EnumModuleTarget.TORSOONLY, EnumModuleCategory.CATEGORY_MOVEMENT);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.gliderWing, 2));
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack itemStack) {
//        Vec3d playerHorzFacing = player.getLookVec();
//        playerHorzFacing = new Vec3d(playerHorzFacing.x, 0, playerHorzFacing.z);
//        playerHorzFacing.normalize();
//        PlayerMovementInputWrapper.PlayerMovementInput playerInput = PlayerMovementInputWrapper.get(player);
//
//        NuminaPlayerUtils.resetFloatKickTicks(player);
//        boolean hasParachute = ModuleManager.INSTANCE.itemHasActiveModule(itemStack, MPSModuleConstants.MODULE_PARACHUTE__DATANAME);
//        if (playerInput.sneakKey && player.motionY < 0 && (!hasParachute || playerInput.moveForward > 0)) {
//            if (player.motionY < -0.1) {
//                float vol = (float) (player.motionX * player.motionX + player.motionZ * player.motionZ);
//                double motionYchange = Math.min(0.08, -0.1 - player.motionY);
//                player.motionY += motionYchange;
//                player.motionX += playerHorzFacing.x * motionYchange;
//                player.motionZ += playerHorzFacing.z * motionYchange;
//
//                // sprinting speed
//                player.jumpMovementFactor += 0.03f;
//            }
//        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }
}