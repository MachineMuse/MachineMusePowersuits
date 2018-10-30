package net.machinemuse.powersuits.event;

import net.machinemuse.numina.client.sound.Musique;
import net.machinemuse.numina.common.config.NuminaConfig;
import net.machinemuse.numina.utils.energy.ElectricItemUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.client.sound.SoundDictionary;
import net.machinemuse.powersuits.item.armor.ItemPowerArmor;
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
    /**
     * Gravity, in meters per tick per tick.
     */
    public static final double DEFAULT_GRAVITY = -0.0784000015258789;

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

    public static double computeFallHeightFromVelocity(double velocity) {
        double ticks = velocity / DEFAULT_GRAVITY;
        return -0.5 * DEFAULT_GRAVITY * ticks * ticks;
    }

    @SubscribeEvent
    public void handleLivingJumpEvent(LivingJumpEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);

            if (!stack.isEmpty() && stack.getItem() instanceof ItemPowerArmor
                    && ModuleManager.INSTANCE.itemHasActiveModule(stack, MPSModuleConstants.MODULE_JUMP_ASSIST__DATANAME)) {
                double jumpAssist = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.JUMP_MULTIPLIER) * 2;
                double drain = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.JUMP_ENERGY_CONSUMPTION);
                int avail = ElectricItemUtils.getPlayerEnergy(player);
                if ((FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) && NuminaConfig.useSounds()) {
                    Musique.playerSound(player, SoundDictionary.SOUND_EVENT_JUMP_ASSIST, SoundCategory.PLAYERS, (float) (jumpAssist / 8.0), (float) 1, false);
                }
                if (drain < avail) {
                    ElectricItemUtils.drainPlayerEnergy(player, (int) drain);
                    setPlayerJumpTicks(player, jumpAssist);
                    double jumpCompensationRatio = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.JUMP_FOOD_COMPENSATION);
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
        if (event.getEntityLiving() instanceof EntityPlayer && event.getDistance() > 3.0) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
            if (!boots.isEmpty()) {
                if (ModuleManager.INSTANCE.itemHasActiveModule(boots, MPSModuleConstants.MODULE_SHOCK_ABSORBER__DATANAME)) {
                    double distanceAbsorb = event.getDistance() * ModuleManager.INSTANCE.getOrSetModularPropertyDouble(boots, MPSModuleConstants.SHOCK_ABSORB_MULTIPLIER);
                    if ((FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) && NuminaConfig.useSounds()) {
                        Musique.playerSound(player, SoundDictionary.SOUND_EVENT_GUI_INSTALL, SoundCategory.PLAYERS, (float) (distanceAbsorb), (float) 1, false);
                    }

                    double drain = distanceAbsorb * ModuleManager.INSTANCE.getOrSetModularPropertyDouble(boots, MPSModuleConstants.SHOCK_ABSORB_ENERGY_CONSUMPTION);
                    int avail = ElectricItemUtils.getPlayerEnergy(player);
                    if (drain < avail) {
                        ElectricItemUtils.drainPlayerEnergy(player, (int) drain);
                        event.setDistance((float) (event.getDistance() - distanceAbsorb));
                    }
                }

            }
        }
    }
}