package net.machinemuse.powersuits.item.module.movement;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemModuleParachute extends ItemAbstractPowerModule implements IToggleableModule, IPlayerTickModule {
    public ItemModuleParachute(String regName) {
        super(regName, EnumModuleTarget.TORSOONLY, EnumModuleCategory.CATEGORY_MOVEMENT);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.parachute, 2));
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack itemStack) {
//        PlayerMovementInputWrapper.PlayerMovementInput playerInput = PlayerMovementInputWrapper.get(player);
//        boolean hasGlider = false;
//        NuminaPlayerUtils.resetFloatKickTicks(player);
//        if (playerInput.sneakKey && player.motionY < -0.1 && (!hasGlider || playerInput.moveForward <= 0)) {
//            double totalVelocity = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ + player.motionY * player.motionY);
//            if (totalVelocity > 0) {
//                player.motionX = player.motionX * 0.1 / totalVelocity;
//                player.motionY = player.motionY * 0.1 / totalVelocity;
//                player.motionZ = player.motionZ * 0.1 / totalVelocity;
//            }
//        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }
}