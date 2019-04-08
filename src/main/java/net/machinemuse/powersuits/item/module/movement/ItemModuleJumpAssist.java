package net.machinemuse.powersuits.item.module.movement;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemModuleJumpAssist extends ItemAbstractPowerModule implements IToggleableModule, IPlayerTickModule {
    public ItemModuleJumpAssist(String regName) {
        super(regName, EnumModuleTarget.LEGSONLY, EnumModuleCategory.CATEGORY_MOVEMENT);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 4));
//
//        addBasePropertyDouble(MPSModuleConstants.JUMP_ENERGY_CONSUMPTION, 0, "RF");
//        addTradeoffPropertyDouble(MPSModuleConstants.POWER, MPSModuleConstants.JUMP_ENERGY_CONSUMPTION, 250);
//        addBasePropertyDouble(MPSModuleConstants.JUMP_MULTIPLIER, 1, "%");
//        addTradeoffPropertyDouble(MPSModuleConstants.POWER, MPSModuleConstants.JUMP_MULTIPLIER, 4);
//
//        addBasePropertyDouble(MPSModuleConstants.JUMP_ENERGY_CONSUMPTION, 0, "RF");
//        addTradeoffPropertyDouble(MPSModuleConstants.COMPENSATION, MPSModuleConstants.JUMP_ENERGY_CONSUMPTION, 50);
//        addBasePropertyDouble(MPSModuleConstants.JUMP_FOOD_COMPENSATION, 0, "%");
//        addTradeoffPropertyDouble(MPSModuleConstants.COMPENSATION, MPSModuleConstants.JUMP_FOOD_COMPENSATION, 1);
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
//        PlayerMovementInputWrapper.PlayerMovementInput playerInput = PlayerMovementInputWrapper.get(player);
//        if (playerInput.jumpKey) {
//            double multiplier = MovementManager.getPlayerJumpMultiplier(player);
//            if (multiplier > 0) {
//                player.motionY += 0.15 * Math.min(multiplier, 1);
//                MovementManager.setPlayerJumpTicks(player, multiplier - 1);
//            }
//            player.jumpMovementFactor = player.getAIMoveSpeed() * .2f;
//        } else {
//            MovementManager.setPlayerJumpTicks(player, 0);
//        }
//        NuminaPlayerUtils.resetFloatKickTicks(player);
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }
}
