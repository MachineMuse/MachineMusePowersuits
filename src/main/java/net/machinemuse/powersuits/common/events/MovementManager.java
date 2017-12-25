package net.machinemuse.powersuits.common.events;

import net.machinemuse.api.ModuleManager;
import net.machinemuse.general.sound.SoundDictionary;
import net.machinemuse.numina.client.sound.Musique;
import net.machinemuse.numina.common.NuminaSettings;
import net.machinemuse.powersuits.common.MPSConstants;
import net.machinemuse.powersuits.common.items.modules.movement.JumpAssistModule;
import net.machinemuse.powersuits.common.items.modules.movement.ShockAbsorberModule;
import net.machinemuse.powersuits.common.items.old.ItemPowerArmor;
import net.machinemuse.utils.ElectricItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;
import java.util.Map;

public class MovementManager {
    public static final Map<String, Double> playerJumpMultipliers = new HashMap();

    public static double getPlayerJumpMultiplier(EntityPlayer player) {

        if (playerJumpMultipliers.containsKey(player.getCommandSenderEntity().getName())) {
            return playerJumpMultipliers.get(player.getCommandSenderEntity().getName());
        } else {
            return 0;
        }
    }

    public static void setPlayerJumpTicks(EntityPlayer player, double number) {
        playerJumpMultipliers.put(player.getCommandSenderEntity().getName(), number);
    }

    @SubscribeEvent
    public void handleLivingJumpEvent(LivingJumpEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);

            if (stack != null && stack.getItem() instanceof ItemPowerArmor
                    && ModuleManager.itemHasActiveModule(stack, MPSConstants.MODULE_JUMP_ASSIST)) {
                double jumpAssist = ModuleManager.computeModularProperty(stack, JumpAssistModule.JUMP_MULTIPLIER) * 2;
                double drain = ModuleManager.computeModularProperty(stack, JumpAssistModule.JUMP_ENERGY_CONSUMPTION);
                double avail = ElectricItemUtils.getPlayerEnergy(player);
                if ((FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) && NuminaSettings.useSounds) {
                    Musique.playerSound(player, SoundDictionary.SOUND_EVENT_JUMP_ASSIST, SoundCategory.PLAYERS, (float) (jumpAssist / 8.0), (float) 1, false);
                }
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

    @SubscribeEvent
    public void handleFallEvent(LivingFallEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
            if (boots != null) {
                if (ModuleManager.itemHasActiveModule(boots, MPSConstants.MODULE_SHOCK_ABSORBER) && event.getDistance() > 3) {
                    double distanceAbsorb = event.getDistance() * ModuleManager.computeModularProperty(boots, ShockAbsorberModule.SHOCK_ABSORB_MULTIPLIER);
                    if ((FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) && NuminaSettings.useSounds) {
                        Musique.playerSound(player, SoundDictionary.SOUND_EVENT_GUI_INSTALL, SoundCategory.PLAYERS, (float) (distanceAbsorb), (float)1, false);
                    }

                    double drain = distanceAbsorb * ModuleManager.computeModularProperty(boots, ShockAbsorberModule.SHOCK_ABSORB_ENERGY_CONSUMPTION);
                    double avail = ElectricItemUtils.getPlayerEnergy(player);
                    if (drain < avail) {
                        ElectricItemUtils.drainPlayerEnergy(player, drain);
                        event.setDistance((float) (event.getDistance() - distanceAbsorb));
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
        return -0.5 * DEFAULT_GRAVITY * ticks * ticks;
    }
}