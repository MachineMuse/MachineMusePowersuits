package net.machinemuse.powersuits.powermodule.movement;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.general.sound.Musique;
import net.machinemuse.general.sound.SoundLoader;
import net.machinemuse.powersuits.control.PlayerInputMap;
import net.machinemuse.powersuits.event.MovementManager;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.machinemuse.utils.MusePlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

public class JumpAssistModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
    public static final String MODULE_JUMP_ASSIST = "Jump Assist";
    public static final String JUMP_ENERGY_CONSUMPTION = "Jump Energy Consumption";
    public static final String JUMP_MULTIPLIER = "Jump Boost";
    public static final String JUMP_FOOD_COMPENSATION = "Jump Exhaustion Compensation";

    public JumpAssistModule(List<IModularItem> validItems) {
        super(validItems);
        addSimpleTradeoff(this, "Power", JUMP_ENERGY_CONSUMPTION, "J", 0, 25, JUMP_MULTIPLIER, "%", 1, 4);
        addSimpleTradeoff(this, "Compensation", JUMP_ENERGY_CONSUMPTION, "J", 0, 5, JUMP_FOOD_COMPENSATION, "%", 0, 1);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 4));
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_MOVEMENT;
    }

    @Override
    public String getName() {
        return MODULE_JUMP_ASSIST;
    }

    @Override
    public String getDescription() {
        return "Another set of servo motors to help you jump higher.";
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.username);
        boolean jumpkey = movementInput.jumpKey;
        if (jumpkey) {
            double multiplier = MovementManager.getPlayerJumpMultiplier(player);
            if (multiplier > 0) {
                player.motionY += 0.15 * Math.min(multiplier, 1) * MusePlayerUtils.getWeightPenaltyRatio(MuseItemUtils.getPlayerWeight(player), 25000);
                MovementManager.setPlayerJumpTicks(player, multiplier - 1);
            }
            player.jumpMovementFactor = player.landMovementFactor * .7f;
        } else {
            MovementManager.setPlayerJumpTicks(player, 0);
        }
        MusePlayerUtils.resetFloatKickTicks(player);
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public String getTextureFile() {
        return "jumpassist";
    }

}
