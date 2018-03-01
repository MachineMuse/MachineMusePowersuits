package net.machinemuse.powersuits.utils;

import net.machinemuse.numina.api.item.IMuseItem;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.numina.math.MuseMathUtils;
import net.machinemuse.numina.player.NuminaPlayerUtils;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.control.PlayerInputMap;
import net.machinemuse.powersuits.item.module.movement.FlightControlModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

import java.lang.reflect.Field;

public class MusePlayerUtils {
    static final double root2 = Math.sqrt(2);

    public static double thrust(EntityPlayer player, double thrust, boolean flightControl) {
        PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.getCommandSenderEntity().getName());
        boolean jumpkey = movementInput.jumpKey;
        float forwardkey = movementInput.forwardKey;
        float strafekey = movementInput.strafeKey;
        boolean downkey = movementInput.downKey;
        boolean sneakkey = movementInput.sneakKey;
        double thrustUsed = 0;
        if (flightControl) {
            Vec3d desiredDirection = player.getLookVec().normalize();
            double strafeX = desiredDirection.z;
            double strafeZ = -desiredDirection.x;
            double scaleStrafe = (strafeX * strafeX + strafeZ * strafeZ);
            double flightVerticality = 0;
            ItemStack helm = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);;
            if (helm != null && helm.getItem() instanceof IMuseItem) {
                flightVerticality = ModuleManager.getInstance().computeModularPropertyDouble(helm, FlightControlModule.FLIGHT_VERTICALITY);
            }

            desiredDirection = new Vec3d(
                    (desiredDirection.x * Math.signum(forwardkey) + strafeX * Math.signum(strafekey)),
                    (flightVerticality * desiredDirection.y * Math.signum(forwardkey) + (jumpkey ? 1 : 0) - (downkey ? 1 : 0)),
                    (desiredDirection.z * Math.signum(forwardkey) + strafeZ * Math.signum(strafekey)));

            desiredDirection = desiredDirection.normalize();
            // Gave up on this... I suck at math apparently
            // double ux = player.motionX / thrust;
            // double uy = player.motionY / thrust;
            // double uz = player.motionZ / thrust;
            // double vx = desiredDirection.x;
            // double vy = desiredDirection.y;
            // double vz = desiredDirection.z;
            // double b = (2 * ux * vx + 2 * uy * vy + 2 * uz * vz);
            // double c = (ux * ux + uy * uy + uz * uz - 1);
            //
            // double actualThrust = (-b + Math.sqrt(b * b - 4 * c))
            // / (2);
            //
            // player.motionX = desiredDirection.x *
            // actualThrust;
            // player.motionY = desiredDirection.y *
            // actualThrust;
            // player.motionZ = desiredDirection.z *
            // actualThrust;

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
            if (Math.abs(player.motionX) > 0 && desiredDirection.lengthVector() == 0) {
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
            if (Math.abs(player.motionZ) > 0 && desiredDirection.lengthVector() == 0) {
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
            double vx = thrust * desiredDirection.x;
            double vy = thrust * desiredDirection.y;
            double vz = thrust * desiredDirection.z;
            player.motionX += vx;
            player.motionY += vy;
            player.motionZ += vz;
            thrustUsed += thrust;

        } else {
            Vec3d playerHorzFacing = player.getLookVec();
            playerHorzFacing = new Vec3d(playerHorzFacing.x, 0,  playerHorzFacing.z);
            playerHorzFacing.normalize();
            if (forwardkey == 0) {
                player.motionY += thrust;
            } else {
                player.motionY += thrust / root2;
                player.motionX += playerHorzFacing.x * thrust / root2 * Math.signum(forwardkey);
                player.motionZ += playerHorzFacing.z * thrust / root2 * Math.signum(forwardkey);
            }
            thrustUsed += thrust;
        }

        // Slow the player if they are going too fast
        double horzm2 = player.motionX * player.motionX + player.motionZ * player.motionZ;
        double horzmlim = MPSConfig.getInstance().getMaximumFlyingSpeedmps() * MPSConfig.getInstance().getMaximumFlyingSpeedmps() / 400;
        if (sneakkey && horzmlim > 0.05) {
            horzmlim = 0.05;
        }

        if (horzm2 > horzmlim) {
            double ratio = Math.sqrt(horzmlim / horzm2);
            player.motionX *= ratio;
            player.motionZ *= ratio;
        }
        NuminaPlayerUtils.resetFloatKickTicks(player);
        return thrustUsed;
    }



    public static EntityPlayer toPlayer(Object data) {
        EntityPlayer player = null;
        try {
            player = (EntityPlayer) data;
        } catch (ClassCastException e) {
            MuseLogger.logError("MMMPS: Player tick handler received invalid Player object");
            e.printStackTrace();
        }
        return player;
    }

    public static double getPlayerCoolingBasedOnMaterial(EntityPlayer player) {
        if (player.isInLava())
            return 0;

        Biome biome = getBiome(player);

//        System.out.println("biome temperature: " + biome.getTemperature(new BlockPos((int)player.posX, (int)player.posY, (int)player.posZ)));



        double cool = ((2.0 - biome.getTemperature(new BlockPos((int)player.posX, (int)player.posY, (int)player.posZ))/2)); // Algorithm that returns a value from 0.0 -> 1.0. Biome temperature is from 0.0 -> 2.0
        if (player.isInWater())
            cool += 0.5;

        // If high in the air, increase cooling
        if ((int)player.posY > 128)
            cool += 0.5;

        if (!player.world.isDaytime() && "Desert".equals(biome.getBiomeName())) { // If nighttime and in the desert, increase cooling
            cool += 0.8;
        }

        if (player.world.isRaining()) {
            cool += 0.2;
        }
        return cool;
    }

    public static Biome getBiome(EntityPlayer player) {
        Chunk chunk = player.world.getChunkFromBlockCoords(player.getPosition());
        return chunk.getBiome(new BlockPos((int) player.posX & 15, player.posY, (int) player.posZ & 15), player.world.getBiomeProvider());
    }

    public static void setFOVMult(EntityPlayer player, float fovmult) {
        Field movementfactor = getMovementFactorField();
        try {
            movementfactor.set(player, fovmult);
        } catch (IllegalAccessException e) {
            MuseLogger.logDebug("illegalAccessException");
        }
    }

    protected static Field movementfactorfieldinstance;

    public static Field getMovementFactorField() {
        if (movementfactorfieldinstance == null) {
            try {
                movementfactorfieldinstance = EntityPlayer.class.getDeclaredField("speedOnGround");
                movementfactorfieldinstance.setAccessible(true);
            } catch (NoSuchFieldException e) {
                try {
                    movementfactorfieldinstance = EntityPlayer.class.getDeclaredField("field_71108_cd");
                    movementfactorfieldinstance.setAccessible(true);
                } catch (NoSuchFieldException e1) {
                    try {
                        movementfactorfieldinstance = EntityPlayer.class.getDeclaredField("ci");
                        movementfactorfieldinstance.setAccessible(true);
                    } catch (NoSuchFieldException e2) {
                        MuseLogger.logDebug("Getting failed");
                    }
                }
            }
        }
        return movementfactorfieldinstance;
    }

    public static double computePlayerVelocity(EntityPlayer entityPlayer) {
        return MuseMathUtils.pythag(entityPlayer.motionX, entityPlayer.motionY, entityPlayer.motionZ);
    }
}