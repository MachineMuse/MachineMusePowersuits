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
import net.machinemuse.powersuits.event.MovementManager;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class JumpAssistModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
    public JumpAssistModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 4));

        addBasePropertyDouble(MPSModuleConstants.JUMP_ENERGY_CONSUMPTION, 0, "RF");
        addTradeoffPropertyDouble(MPSModuleConstants.POWER, MPSModuleConstants.JUMP_ENERGY_CONSUMPTION, 250);
        addBasePropertyDouble(MPSModuleConstants.JUMP_MULTIPLIER, 1, "%");
        addTradeoffPropertyDouble(MPSModuleConstants.POWER, MPSModuleConstants.JUMP_MULTIPLIER, 4);

        addBasePropertyDouble(MPSModuleConstants.JUMP_ENERGY_CONSUMPTION, 0, "RF");
        addTradeoffPropertyDouble(MPSModuleConstants.COMPENSATION, MPSModuleConstants.JUMP_ENERGY_CONSUMPTION, 50);
        addBasePropertyDouble(MPSModuleConstants.JUMP_FOOD_COMPENSATION, 0, "%");
        addTradeoffPropertyDouble(MPSModuleConstants.COMPENSATION, MPSModuleConstants.JUMP_FOOD_COMPENSATION, 1);
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_MOVEMENT;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_JUMP_ASSIST__DATANAME;
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        PlayerMovementInputWrapper.PlayerMovementInput playerInput = PlayerMovementInputWrapper.get(player);
        if (playerInput.jumpKey) {
            double multiplier = MovementManager.getPlayerJumpMultiplier(player);
            if (multiplier > 0) {
                player.motionY += 0.15 * Math.min(multiplier, 1);
                MovementManager.setPlayerJumpTicks(player, multiplier - 1);
            }
            player.jumpMovementFactor = player.getAIMoveSpeed() * .2f;
        } else {
            MovementManager.setPlayerJumpTicks(player, 0);
        }
        NuminaPlayerUtils.resetFloatKickTicks(player);
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.jumpAssist;
    }
}
