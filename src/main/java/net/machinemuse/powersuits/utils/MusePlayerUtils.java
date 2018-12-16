package net.machinemuse.powersuits.utils;

import com.enderio.core.common.transform.EnderCoreMethods;
import net.machinemuse.numina.common.ModCompatibility;
import net.machinemuse.numina.general.MuseMathUtils;
import net.machinemuse.numina.item.IModularItem;
import net.machinemuse.numina.player.NuminaPlayerUtils;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.control.PlayerInputMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.List;

public class MusePlayerUtils {
    static final double root2 = Math.sqrt(2);
    protected static Field movementfactorfieldinstance;

    public static RayTraceResult raytraceEntities(World world, EntityPlayer player, boolean collisionFlag, double reachDistance) {
        RayTraceResult pickedEntity = null;
        Vec3d playerPosition = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3d playerLook = player.getLookVec();
        Vec3d playerViewOffset = new Vec3d(
                playerPosition.x + playerLook.x * reachDistance,
                playerPosition.y + playerLook.y * reachDistance,
                playerPosition.z + playerLook.z * reachDistance);

        //FIXME: reverted to an older boxToScan because newer one did not change based on direction facing for some reason
//        double playerBorder = 1.1 * reachDistance;
//        AxisAlignedBB boxToScan = player.getEntityBoundingBox().expand(playerBorder, playerBorder, playerBorder);

        AxisAlignedBB boxToScan =
                player.getEntityBoundingBox().expand(playerLook.x * reachDistance,
                        playerLook.y * reachDistance, playerLook.z
                                * reachDistance);

        List entitiesHit = world.getEntitiesWithinAABBExcludingEntity(player, boxToScan);
        double closestEntity = reachDistance;

        if (entitiesHit == null || entitiesHit.isEmpty()) {
            return null;
        }
        for (Entity entityHit : (Iterable<Entity>) entitiesHit) {
            if (entityHit != null && entityHit.canBeCollidedWith() && entityHit.getEntityBoundingBox() != null) {
                float border = entityHit.getCollisionBorderSize();
                AxisAlignedBB aabb = entityHit.getEntityBoundingBox().expand((double) border, (double) border, (double) border);
                RayTraceResult raytraceResult = aabb.calculateIntercept(playerPosition, playerViewOffset);
                if (raytraceResult != null) {
                    if (aabb.contains(playerPosition)) {
                        if (0.0D < closestEntity || closestEntity == 0.0D) {
                            pickedEntity = new RayTraceResult(entityHit);
                            if (pickedEntity != null) {
                                pickedEntity.hitVec = raytraceResult.hitVec;
                                closestEntity = 0.0D;
                            }
                        }
                    } else {
                        double distance = playerPosition.distanceTo(raytraceResult.hitVec);

                        if (distance < closestEntity || closestEntity == 0.0D) {
                            pickedEntity = new RayTraceResult(entityHit);
                            pickedEntity.hitVec = raytraceResult.hitVec;
                            closestEntity = distance;
                        }
                    }
                }
            }
        }
        return pickedEntity;
    }

    public static RayTraceResult raytraceBlocks(World world, EntityPlayer player, boolean collisionFlag, double reachDistance) {
        Vec3d playerPosition = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3d playerLook = player.getLookVec();

        Vec3d playerViewOffset = new Vec3d(playerPosition.x + playerLook.x * reachDistance, playerPosition.y
                + playerLook.y * reachDistance, playerPosition.z + playerLook.z * reachDistance);
        return world.rayTraceBlocks(playerPosition, playerViewOffset);
    }

    public static RayTraceResult doCustomRayTrace(World world, EntityPlayer player, boolean collisionFlag, double reachDistance) {
        // Somehow this destroys the playerPosition vector -.-
        RayTraceResult pickedBlock = raytraceBlocks(world, player, collisionFlag, reachDistance);
        RayTraceResult pickedEntity = raytraceEntities(world, player, collisionFlag, reachDistance);
        if (pickedBlock == null) {
            return pickedEntity;
        } else if (pickedEntity == null) {
            return pickedBlock;
        } else {
            Vec3d playerPosition = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
            double dBlock = pickedBlock.hitVec.distanceTo(playerPosition);
            double dEntity = pickedEntity.hitVec.distanceTo(playerPosition);
            if (dEntity < dBlock) {
                return pickedEntity;
            } else {
                return pickedBlock;
            }
        }
    }

    public static void teleportEntity(EntityPlayer entityPlayer, RayTraceResult rayTraceResult) {
        if (rayTraceResult != null && entityPlayer instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) entityPlayer;
            if (player.connection.netManager.isChannelOpen()) {
                switch (rayTraceResult.typeOfHit) {
                    case ENTITY:
                        player.setPositionAndUpdate(rayTraceResult.hitVec.x, rayTraceResult.hitVec.y, rayTraceResult.hitVec.z);
                        break;
                    case BLOCK:
                        double hitx = rayTraceResult.hitVec.x;
                        double hity = rayTraceResult.hitVec.y;
                        double hitz = rayTraceResult.hitVec.z;
                        switch (rayTraceResult.sideHit) {
                            case DOWN: // Bottom
                                hity -= 2;
                                break;
                            case UP: // Top
                                // hity += 1;
                                break;
                            case NORTH: // North
                                hitx -= 0.5;
                                break;
                            case SOUTH: // South
                                hitx += 0.5;
                                break;
                            case WEST: // West
                                hitz += 0.5;
                                break;
                            case EAST: // East
                                hitz -= 0.5;
                                break;
                        }

                        player.setPositionAndUpdate(hitx, hity, hitz);
                        break;
                    default:
                        break;

                }
            }
        }
    }

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
            double flightVerticality = 0;
            ItemStack helm = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            if (!helm.isEmpty() && helm.getItem() instanceof IModularItem) {
                flightVerticality = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(helm, MPSModuleConstants.FLIGHT_VERTICALITY);
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
            // double vx = desiredDirection.xCoord;
            // double vy = desiredDirection.yCoord;
            // double vz = desiredDirection.zCoord;
            // double b = (2 * ux * vx + 2 * uy * vy + 2 * uz * vz);
            // double c = (ux * ux + uy * uy + uz * uz - 1);
            //
            // double actualThrust = (-b + Math.sqrt(b * b - 4 * c))
            // / (2);
            //
            // player.motionX = desiredDirection.xCoord *
            // actualThrust;
            // player.motionY = desiredDirection.yCoord *
            // actualThrust;
            // player.motionZ = desiredDirection.zCoord *
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
            double vx = thrust * desiredDirection.x;
            double vy = thrust * desiredDirection.y;
            double vz = thrust * desiredDirection.z;
            player.motionX += vx;
            player.motionY += vy;
            player.motionZ += vz;
            thrustUsed += thrust;

        } else {
            Vec3d playerHorzFacing = player.getLookVec();
            playerHorzFacing = new Vec3d(playerHorzFacing.x, 0, playerHorzFacing.z);
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
        double horzmlim = MPSConfig.INSTANCE.getMaximumFlyingSpeedmps() * MPSConfig.INSTANCE.getMaximumFlyingSpeedmps() / 400;
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

    public static double getPlayerCoolingBasedOnMaterial(@Nonnull EntityPlayer player) {
        // cheaper method of checking if player is in lava. Described as "non-chunkloading copy of Entity.isInLava()"
        if (ModCompatibility.isEnderCoreLoaded()) {
            if (EnderCoreMethods.isInLavaSafe(player))
                return 0;
        } else {
            if (player.isInLava()) // not a cheap
                return 0;
        }

        double cool = ((2.0 - getBiome(player).getTemperature(new BlockPos((int) player.posX, (int) player.posY, (int) player.posZ)) / 2)); // Algorithm that returns a getValue from 0.0 -> 1.0. Biome temperature is from 0.0 -> 2.0

        if (player.isInWater())
            cool += 0.5;

        // If high in the air, increase cooling
        if ((int) player.posY > 128)
            cool += 0.5;

        // If nighttime and in the desert, increase cooling
        if (!player.world.isDaytime() && getBiome(player) instanceof BiomeDesert) {
            cool += 0.8;
        }

        // check for rain and if player is in the rain
        // check if rain can happen in the biome the player is in
        if (player.world.getBiome(player.getPosition()).canRain()
                // check if raining in the world
                && player.world.isRaining()
                // check if the player can see the sky
                && player.world.canBlockSeeSky(player.getPosition().add(0, 1, 0))) {
            cool += 0.2;
        }

        return cool;
    }

    public static Biome getBiome(EntityPlayer player) {
        Chunk chunk = player.world.getChunk(player.getPosition());
        return chunk.getBiome(player.getPosition(), player.world.getBiomeProvider());
    }

    public static void setFOVMult(EntityPlayer player, float fovmult) {
        Field movementfactor = getMovementFactorField();
        try {
            movementfactor.set(player, fovmult);
        } catch (IllegalAccessException e) {
            MuseLogger.logDebug("illegalAccessException");
        }
    }

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