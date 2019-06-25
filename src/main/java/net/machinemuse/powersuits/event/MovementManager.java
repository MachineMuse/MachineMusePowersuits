package net.machinemuse.powersuits.event;

import net.machinemuse.numina.client.sound.Musique;
import net.machinemuse.numina.config.NuminaConfig;
import net.machinemuse.numina.energy.ElectricItemUtils;
import net.machinemuse.numina.item.IModularItem;
import net.machinemuse.numina.math.MuseMathUtils;
import net.machinemuse.numina.nbt.MuseNBTUtils;
import net.machinemuse.numina.player.NuminaPlayerUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.sound.SoundDictionary;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.control.PlayerMovementInputWrapper;
import net.machinemuse.powersuits.item.armor.ItemPowerArmor;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MovementManager {
    static final double root2 = Math.sqrt(2);
    public static final Map<UUID, Double> playerJumpMultipliers = new HashMap();
    /**
     * Gravity, in meters per tick per tick.
     */
    public static final double DEFAULT_GRAVITY = -0.0784000015258789;

    public static double getPlayerJumpMultiplier(EntityPlayer player) {
        if (playerJumpMultipliers.containsKey(player.getCommandSenderEntity().getUniqueID())) {
            return playerJumpMultipliers.get(player.getCommandSenderEntity().getUniqueID());
        } else {
            return 0;
        }
    }

    public static void setPlayerJumpTicks(EntityPlayer player, double number) {
        playerJumpMultipliers.put(player.getCommandSenderEntity().getUniqueID(), number);
    }

    public static double computeFallHeightFromVelocity(double velocity) {
        double ticks = velocity / DEFAULT_GRAVITY;
        return -0.5 * DEFAULT_GRAVITY * ticks * ticks;
    }

    // moved here so it is still accessible if sprint assist module isn't installed.
    public static void setMovementModifier(ItemStack itemStack, double multiplier, EntityPlayer player) {
        // reduce player speed according to Kinetic Energy Generator setting
        if (ModuleManager.INSTANCE.itemHasActiveModule(itemStack, MPSModuleConstants.MODULE_KINETIC_GENERATOR__DATANAME)) {
            double movementResistance = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.KINETIC_ENERGY_MOVEMENT_RESISTANCE);
            multiplier -= movementResistance;
        }

        // player walking speed: 0.10000000149011612
        // player sprintint speed: 0.13000001
        double additive = multiplier * (player.isSprinting() ? 0.13 : 0.1)/2;
        NBTTagCompound itemNBT = MuseNBTUtils.getNBTTag(itemStack);
        boolean hasAttribute = false;
        if (itemNBT.hasKey("AttributeModifiers", Constants.NBT.TAG_LIST)) {
            NBTTagList nbttaglist = itemNBT.getTagList("AttributeModifiers", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound attributeTag = nbttaglist.getCompoundTagAt(i);
                if (attributeTag.getString("Name").equals(SharedMonsterAttributes.MOVEMENT_SPEED.getName())) {
                    attributeTag.setDouble("Amount", additive);
                    hasAttribute = true;
                    break;
                }
            }
        }
        if (!hasAttribute && additive != 0)
            itemStack.addAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), additive, 0), EntityEquipmentSlot.LEGS);
    }

    public static double thrust(EntityPlayer player, double thrust, boolean flightControl) {
        PlayerMovementInputWrapper.PlayerMovementInput playerInput = PlayerMovementInputWrapper.get(player);
        double thrustUsed = 0;
        if (flightControl) {
            Vec3d desiredDirection = player.getLookVec().normalize();
            double strafeX = desiredDirection.z;
            double strafeZ = -desiredDirection.x;
            double flightVerticality = 0;
            ItemStack helm = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            if (!helm.isEmpty() && helm.getItem() instanceof IModularItem) {
                flightVerticality = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(helm, MPSModuleConstants.FLIGHT_VERTICALITY);
            }

            desiredDirection = new Vec3d(
                    (desiredDirection.x * Math.signum(playerInput.moveForward) + strafeX * Math.signum(playerInput.moveStrafe)),
                    (flightVerticality * desiredDirection.y * Math.signum(playerInput.moveForward) + (playerInput.jumpKey ? 1 : 0) - (playerInput.downKey ? 1 : 0)),
                    (desiredDirection.z * Math.signum(playerInput.moveForward) + strafeZ * Math.signum(playerInput.moveStrafe)));

            desiredDirection = desiredDirection.normalize();

            // Brakes
            if (player.motionY < 0 && desiredDirection.y >= 0) {
                if (-player.motionY > thrust) {
                    player.motionY += thrust;
                    thrustUsed += thrust;
                    thrust = 0;
                } else {
                    thrust -= player.motionY;
                    thrustUsed += player.motionY;
                    player.motionY = 0;
                }
            }
            if (player.motionY < -1) {
                thrust += 1 + player.motionY;
                thrustUsed -= 1 + player.motionY;
                player.motionY = -1;
            }
            if (Math.abs(player.motionX) > 0 && desiredDirection.length() == 0) {
                if (Math.abs(player.motionX) > thrust) {
                    player.motionX -= Math.signum(player.motionX) * thrust;
                    thrustUsed += thrust;
                    thrust = 0;
                } else {
                    thrust -= Math.abs(player.motionX);
                    thrustUsed += Math.abs(player.motionX);
                    player.motionX = 0;
                }
            }
            if (Math.abs(player.motionZ) > 0 && desiredDirection.length() == 0) {
                if (Math.abs(player.motionZ) > thrust) {
                    player.motionZ -= Math.signum(player.motionZ) * thrust;
                    thrustUsed += thrust;
                    thrust = 0;
                } else {
                    thrustUsed += Math.abs(player.motionZ);
                    thrust -= Math.abs(player.motionZ);
                    player.motionZ = 0;
                }
            }

            // Thrusting, finally :V
            player.motionX += thrust * desiredDirection.x;
            player.motionY += thrust * desiredDirection.y;
            player.motionZ += thrust * desiredDirection.z;
            thrustUsed += thrust;

        } else {
            Vec3d playerHorzFacing = player.getLookVec();
            playerHorzFacing = new Vec3d(playerHorzFacing.x, 0, playerHorzFacing.z);
            playerHorzFacing.normalize();
            if (playerInput.moveForward == 0) {
                player.motionY += thrust;
            } else {
                player.motionY += thrust / root2;
                player.motionX += playerHorzFacing.x * thrust / root2 * Math.signum(playerInput.moveForward);
                player.motionZ += playerHorzFacing.z * thrust / root2 * Math.signum(playerInput.moveForward);
            }
            thrustUsed += thrust;
        }

        // Slow the player if they are going too fast
        double horzm2 = player.motionX * player.motionX + player.motionZ * player.motionZ;

        // currently comes out to 0.0625
        double horizontalLimit = MPSConfig.INSTANCE.getMaximumFlyingSpeedmps() * MPSConfig.INSTANCE.getMaximumFlyingSpeedmps() / 400;

//        double playerVelocity = Math.abs(player.motionX) + Math.abs(player.motionY) + Math.abs(player.motionZ);

        if (playerInput.sneakKey && horizontalLimit > 0.05) {
            horizontalLimit = 0.05;
        }

        if (horzm2 > horizontalLimit) {
            double ratio = Math.sqrt(horizontalLimit / horzm2);
            player.motionX *= ratio;
            player.motionZ *= ratio;
        }
        NuminaPlayerUtils.resetFloatKickTicks(player);
        return thrustUsed;
    }

    public static double computePlayerVelocity(EntityPlayer entityPlayer) {
        return MuseMathUtils.pythag(entityPlayer.motionX, entityPlayer.motionY, entityPlayer.motionZ);
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
                        player.getFoodStats().addExhaustion((float) (-0.2F * jumpCompensationRatio));
                    } else {
                        player.getFoodStats().addExhaustion((float) (-0.05F * jumpCompensationRatio));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void handleFallEvent(LivingFallEvent event) {
//        event.getEntityLiving().sendMessage(new TextComponentString("unmodified fall settings: [ damage : " + event.getDamageMultiplier() + " ], [ distance : " + event.getDistance() + " ]"));

        if (event.getEntityLiving() instanceof EntityPlayer && event.getDistance() > 3.0F) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);

            if (!boots.isEmpty()) {
                if (ModuleManager.INSTANCE.itemHasActiveModule(boots, MPSModuleConstants.MODULE_SHOCK_ABSORBER__DATANAME)) {
                    double distanceAbsorb =
                            MuseMathUtils.clampDouble(event.getDistance() * ModuleManager.INSTANCE.getOrSetModularPropertyDouble(boots, MPSModuleConstants.SHOCK_ABSORB_MULTIPLIER),
                            0, event.getDistance());

                    if (player.world.isRemote && NuminaConfig.useSounds()) {
                        Musique.playerSound(player, SoundDictionary.SOUND_EVENT_GUI_INSTALL, SoundCategory.PLAYERS, (float) (distanceAbsorb), (float) 1, false);
                    }

                    double drain = distanceAbsorb * ModuleManager.INSTANCE.getOrSetModularPropertyDouble(boots, MPSModuleConstants.SHOCK_ABSORB_ENERGY_CONSUMPTION);
                    int avail = ElectricItemUtils.getPlayerEnergy(player);
                    if (drain < avail) {
                        ElectricItemUtils.drainPlayerEnergy(player, (int) drain);
                        event.setDistance((float) (event.getDistance() - distanceAbsorb));
//                        event.getEntityLiving().sendMessage(new TextComponentString("modified fall settings: [ damage : " + event.getDamageMultiplier() + " ], [ distance : " + event.getDistance() + " ]"));

                    }
                }
            }
        }
    }
}