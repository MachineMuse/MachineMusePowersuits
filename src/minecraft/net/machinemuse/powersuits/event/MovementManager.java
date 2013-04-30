package net.machinemuse.powersuits.event;

import net.machinemuse.api.ModuleManager;
import net.machinemuse.general.sound.Musique;
import net.machinemuse.general.sound.SoundLoader;
import net.machinemuse.powersuits.item.ItemPowerArmor;
import net.machinemuse.powersuits.powermodule.movement.JumpAssistModule;
import net.machinemuse.powersuits.powermodule.movement.ShockAbsorberModule;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;

import java.util.HashMap;
import java.util.Map;

public class MovementManager {
    public static Map<String, Double> playerJumpMultipliers = new HashMap();

    public static double getPlayerJumpMultiplier(EntityPlayer player) {

        if (playerJumpMultipliers.containsKey(player.username)) {
            return playerJumpMultipliers.get(player.username);
        } else {
            return 0;
        }
    }

    public static void setPlayerJumpTicks(EntityPlayer player, double number) {
        playerJumpMultipliers.put(player.username, number);
    }

    @ForgeSubscribe
    public void handleLivingJumpEvent(LivingJumpEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            ItemStack stack = player.getCurrentArmor(1);
            if (stack != null && stack.getItem() instanceof ItemPowerArmor
                    && MuseItemUtils.itemHasActiveModule(stack, JumpAssistModule.MODULE_JUMP_ASSIST)) {
                double jumpAssist = ModuleManager.computeModularProperty(stack, JumpAssistModule.JUMP_MULTIPLIER) * 2;
                double drain = ModuleManager.computeModularProperty(stack, JumpAssistModule.JUMP_ENERGY_CONSUMPTION);
                double avail = ElectricItemUtils.getPlayerEnergy(player);
                Musique.playerSound(player, SoundLoader.SOUND_JUMP_ASSIST, (float) (jumpAssist/8.0), 2, false);
                if (drain < avail) {
                    ElectricItemUtils.drainPlayerEnergy(player, drain);
                    setPlayerJumpTicks(player, jumpAssist);
                    double jumpCompensationRatio = ModuleManager.computeModularProperty(stack, JumpAssistModule.JUMP_FOOD_COMPENSATION);
                    if (player.isSprinting()) {
                        player.getFoodStats().addExhaustion((float) (-0.8 * jumpCompensationRatio));
                    } else {
                        player.getFoodStats().addExhaustion((float) (-0.2 * jumpCompensationRatio));
                    }

                }
            }

        }
    }

    @ForgeSubscribe
    public void handleFallEvent(LivingFallEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            ItemStack boots = player.getCurrentArmor(0);
            if (boots != null) {
                if (MuseItemUtils.itemHasActiveModule(boots, ShockAbsorberModule.MODULE_SHOCK_ABSORBER) && event.distance > 3) {
                    double distanceAbsorb = event.distance * ModuleManager.computeModularProperty(boots, ShockAbsorberModule.SHOCK_ABSORB_MULTIPLIER);
                    Musique.playerSound(player, SoundLoader.SOUND_GUI_INSTALL, (float) (distanceAbsorb), 2, false);

                    double drain = distanceAbsorb * ModuleManager.computeModularProperty(boots, ShockAbsorberModule.SHOCK_ABSORB_ENERGY_CONSUMPTION);
                    double avail = ElectricItemUtils.getPlayerEnergy(player);
                    if (drain < avail) {
                        ElectricItemUtils.drainPlayerEnergy(player, drain);
                        event.distance -= distanceAbsorb;
                    }
                }
            }
        }
    }

    /**
     * Gravity, in meters per tick per tick.
     */
    public static final double DEFAULT_GRAVITY = -0.0784000015258789;

    public static double computeFallHeightFromVelocity(double velocity) {
        double ticks = velocity / DEFAULT_GRAVITY;
        double distance = -0.5 * DEFAULT_GRAVITY * ticks * ticks;
        return distance;
    }
}
