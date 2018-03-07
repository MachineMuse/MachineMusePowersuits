package net.machinemuse.powersuits.item.module.movement;

import net.machinemuse.powersuits.item.module.PowerModuleBase;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IPlayerTickModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.numina.player.NuminaPlayerUtils;
import net.machinemuse.numina.utils.module.helpers.WeightHelper;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.control.PlayerInputMap;
import net.machinemuse.powersuits.event.MovementManager;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class JumpAssistModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
    public static final String JUMP_ENERGY_CONSUMPTION = "Jump Energy Consumption";
    public static final String JUMP_MULTIPLIER = "Jump Boost";
    public static final String JUMP_FOOD_COMPENSATION = "Jump Exhaustion Compensation";

    public JumpAssistModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.LEGSONLY, resourceDommain, UnlocalizedName);
        addSimpleTradeoffDouble(this, "Power", JUMP_ENERGY_CONSUMPTION, "J", 0, 25, JUMP_MULTIPLIER, "%", 1, 4);
        addSimpleTradeoffDouble(this, "Compensation", JUMP_ENERGY_CONSUMPTION, "J", 0, 5, JUMP_FOOD_COMPENSATION, "%", 0, 1);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 4));
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_MOVEMENT;
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.getCommandSenderEntity().getName());
        boolean jumpkey = movementInput.jumpKey;
        if (jumpkey) {
            double multiplier = MovementManager.getPlayerJumpMultiplier(player);
            if (multiplier > 0) {
                player.motionY += 0.15 * Math.min(multiplier, 1) * WeightHelper.getWeightPenaltyRatio(WeightHelper.getPlayerWeight(player), 25000);
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
}