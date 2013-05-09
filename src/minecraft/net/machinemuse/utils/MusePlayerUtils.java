package net.machinemuse.utils;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.general.MuseLogger;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.control.PlayerInputMap;
import net.machinemuse.powersuits.powermodule.movement.FlightControlModule;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

import java.lang.reflect.Field;
import java.util.List;

public class MusePlayerUtils {
    static final double root2 = Math.sqrt(2);

    public static MovingObjectPosition raytraceEntities(World world, EntityPlayer player, boolean collisionFlag, double reachDistance) {

        MovingObjectPosition pickedEntity = null;
        Vec3 playerPosition = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3 playerLook = player.getLookVec();

        Vec3 playerViewOffset = Vec3.createVectorHelper(playerPosition.xCoord + playerLook.xCoord * reachDistance, playerPosition.yCoord
                + playerLook.yCoord * reachDistance, playerPosition.zCoord + playerLook.zCoord * reachDistance);

        double playerBorder = 1.1 * reachDistance;
        AxisAlignedBB boxToScan = player.boundingBox.expand(playerBorder, playerBorder, playerBorder);
        // AxisAlignedBB boxToScan =
        // player.boundingBox.addCoord(playerLook.xCoord * reachDistance,
        // playerLook.yCoord * reachDistance, playerLook.zCoord
        // * reachDistance);

        List entitiesHit = world.getEntitiesWithinAABBExcludingEntity(player, boxToScan);
        double closestEntity = reachDistance;

        if (entitiesHit == null || entitiesHit.isEmpty()) {
            return null;
        }
        for (Entity entityHit : (Iterable<Entity>) entitiesHit) {
            if (entityHit != null && entityHit.canBeCollidedWith() && entityHit.boundingBox != null) {
                float border = entityHit.getCollisionBorderSize();
                AxisAlignedBB aabb = entityHit.boundingBox.expand((double) border, (double) border, (double) border);
                MovingObjectPosition hitMOP = aabb.calculateIntercept(playerPosition, playerViewOffset);

                if (hitMOP != null) {
                    if (aabb.isVecInside(playerPosition)) {
                        if (0.0D < closestEntity || closestEntity == 0.0D) {
                            pickedEntity = new MovingObjectPosition(entityHit);
                            if (pickedEntity != null) {
                                pickedEntity.hitVec = hitMOP.hitVec;
                                closestEntity = 0.0D;
                            }
                        }
                    } else {
                        double distance = playerPosition.distanceTo(hitMOP.hitVec);

                        if (distance < closestEntity || closestEntity == 0.0D) {
                            pickedEntity = new MovingObjectPosition(entityHit);
                            pickedEntity.hitVec = hitMOP.hitVec;
                            closestEntity = distance;
                        }
                    }
                }
            }
        }
        return pickedEntity;
    }

    public static MovingObjectPosition raytraceBlocks(World world, EntityPlayer player, boolean collisionFlag, double reachDistance) {
        Vec3 playerPosition = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3 playerLook = player.getLookVec();

        Vec3 playerViewOffset = Vec3.createVectorHelper(playerPosition.xCoord + playerLook.xCoord * reachDistance, playerPosition.yCoord
                + playerLook.yCoord * reachDistance, playerPosition.zCoord + playerLook.zCoord * reachDistance);
        return world.rayTraceBlocks_do_do(playerPosition, playerViewOffset, collisionFlag, !collisionFlag);
    }

    public static MovingObjectPosition doCustomRayTrace(World world, EntityPlayer player, boolean collisionFlag, double reachDistance) {
        // Somehow this destroys the playerPosition vector -.-
        MovingObjectPosition pickedBlock = raytraceBlocks(world, player, collisionFlag, reachDistance);
        MovingObjectPosition pickedEntity = raytraceEntities(world, player, collisionFlag, reachDistance);

        if (pickedBlock == null) {
            return pickedEntity;
        } else if (pickedEntity == null) {
            return pickedBlock;
        } else {
            Vec3 playerPosition = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);
            double dBlock = pickedBlock.hitVec.distanceTo(playerPosition);
            double dEntity = pickedEntity.hitVec.distanceTo(playerPosition);
            if (dEntity < dBlock) {
                return pickedEntity;
            } else {
                return pickedBlock;
            }
        }

        // float one = 1.0F;
        // float pitch = player.prevRotationPitch + (player.rotationPitch -
        // player.prevRotationPitch) * one;
        // float yaw = player.prevRotationYaw + (player.rotationYaw -
        // player.prevRotationYaw) * one;
        // double posx = player.prevPosX + (player.posX - player.prevPosX) *
        // (double) one;
        // double posy = player.prevPosY + (player.posY - player.prevPosY) *
        // (double) one + 1.62D - (double) player.yOffset;
        // double posz = player.prevPosZ + (player.posZ - player.prevPosZ) *
        // (double) one;
        // Vec3 posVector = World.getWorldVec3Pool().getVecFromPool(posx, posy,
        // posz);
        // float yawz = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        // float yawx = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        // float pitchhorizontal = -MathHelper.cos(-pitch * 0.017453292F);
        // float pitchvertical = MathHelper.sin(-pitch * 0.017453292F);
        // float directionx = yawx * pitchhorizontal;
        // float directionz = yawz * pitchhorizontal;
        // Vec3 rayToCheck = posVector.addVector((double) directionx *
        // reachDistance, (double) pitchvertical * reachDistance, (double)
        // directionz
        // * reachDistance);
        // return World.rayTraceBlocks_do_do(posVector, rayToCheck,
        // collisionFlag, !collisionFlag);
    }

    public static void teleportEntity(EntityPlayer entityPlayer, MovingObjectPosition hitMOP) {
        if (hitMOP != null && entityPlayer instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) entityPlayer;
            if (!player.playerNetServerHandler.connectionClosed) {
                switch (hitMOP.typeOfHit) {
                    case ENTITY:
                        player.setPositionAndUpdate(hitMOP.hitVec.xCoord, hitMOP.hitVec.yCoord, hitMOP.hitVec.zCoord);
                        break;
                    case TILE:
                        double hitx = hitMOP.hitVec.xCoord;
                        double hity = hitMOP.hitVec.yCoord;
                        double hitz = hitMOP.hitVec.zCoord;
                        switch (hitMOP.sideHit) {
                            case 0: // Bottom
                                hity -= 2;
                                break;
                            case 1: // Top
                                // hity += 1;
                                break;
                            case 2: // East
                                hitz -= 0.5;
                                break;
                            case 3: // West
                                hitz += 0.5;
                                break;
                            case 4: // North
                                hitx -= 0.5;
                                break;
                            case 5: // South
                                hitx += 0.5;
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
        PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.username);
        boolean jumpkey = movementInput.jumpKey;
        float forwardkey = movementInput.forwardKey;
        float strafekey = movementInput.strafeKey;
        boolean downkey = movementInput.downKey;
        boolean sneakkey = movementInput.sneakKey;
        double thrustUsed = 0;
        if (flightControl) {
            Vec3 desiredDirection = player.getLookVec().normalize();
            double strafeX = desiredDirection.zCoord;
            double strafeZ = -desiredDirection.xCoord;
            double scaleStrafe = (strafeX * strafeX + strafeZ * strafeZ);
            double flightVerticality = 0;
            ItemStack helm = player.getCurrentArmor(3);
            if (helm != null && helm.getItem() instanceof IModularItem) {
                flightVerticality = ModuleManager.computeModularProperty(helm, FlightControlModule.FLIGHT_VERTICALITY);
            }
            desiredDirection.xCoord = desiredDirection.xCoord * Math.signum(forwardkey) + strafeX * Math.signum(strafekey);
            desiredDirection.yCoord = flightVerticality * desiredDirection.yCoord * Math.signum(forwardkey) + (jumpkey ? 1 : 0) - (downkey ? 1 : 0);
            desiredDirection.zCoord = desiredDirection.zCoord * Math.signum(forwardkey) + strafeZ * Math.signum(strafekey);

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
            if (player.motionY < 0 && desiredDirection.yCoord >= 0) {
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
            double vx = thrust * desiredDirection.xCoord;
            double vy = thrust * desiredDirection.yCoord;
            double vz = thrust * desiredDirection.zCoord;
            player.motionX += vx;
            player.motionY += vy;
            player.motionZ += vz;
            thrustUsed += thrust;

        } else {
            Vec3 playerHorzFacing = player.getLookVec();
            playerHorzFacing.yCoord = 0;
            playerHorzFacing.normalize();
            if (forwardkey == 0) {
                player.motionY += thrust;
            } else {
                player.motionY += thrust / root2;
                player.motionX += playerHorzFacing.xCoord * thrust / root2 * Math.signum(forwardkey);
                player.motionZ += playerHorzFacing.zCoord * thrust / root2 * Math.signum(forwardkey);
            }
            thrustUsed += thrust;
        }

        // Slow the player if they are going too fast
        double horzm2 = player.motionX * player.motionX + player.motionZ * player.motionZ;
        double horzmlim = Config.getMaximumFlyingSpeedmps() * Config.getMaximumFlyingSpeedmps() / 400;
        if (sneakkey && horzmlim > 0.05) {
            horzmlim = 0.05;
        }

        if (horzm2 > horzmlim) {
            double ratio = Math.sqrt(horzmlim / horzm2);
            player.motionX *= ratio;
            player.motionZ *= ratio;
        }
        resetFloatKickTicks(player);
        return thrustUsed;
    }

    public static double getWeightPenaltyRatio(double currentWeight, double capacity) {
        if (currentWeight < capacity) {
            return 1;
        } else {
            return capacity / currentWeight;
        }
    }

    public static void resetFloatKickTicks(EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            ((EntityPlayerMP) player).playerNetServerHandler.ticksForFloatKick = 0;
        }
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
        double cool = 0;
        if (player.isInWater()) {
            cool += 0.5;
        } else if (player.isInsideOfMaterial(Material.lava)) {
            return 0;
        }
        cool += ((2.0 - getBiome(player).getFloatTemperature())/2); // Algorithm that returns a value from 0.0 -> 1.0. Biome temperature is from 0.0 -> 2.0
        if ((int)player.posY > 128) { // If high in the air, increase cooling
            cool += 0.5;
        }
        if (!player.worldObj.isDaytime() && getBiome(player).biomeName.equals("Desert")) { // If nighttime and in the desert, increase cooling
            cool += 0.8;
        }
        if (player.worldObj.isRaining()) {
            cool += 0.2;
        }
        return cool;
    }

    public static BiomeGenBase getBiome(EntityPlayer player) {
        Chunk chunk = player.worldObj.getChunkFromBlockCoords((int) player.posX, (int) player.posZ);
        return chunk.getBiomeGenForWorldCoords((int) player.posX & 15, (int) player.posZ & 15, player.worldObj.getWorldChunkManager());
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
}
