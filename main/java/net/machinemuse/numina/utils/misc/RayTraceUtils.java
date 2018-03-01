package net.machinemuse.numina.utils.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class RayTraceUtils {
    public static RayTraceResult raytraceEntities(World world, EntityPlayer player, boolean collisionFlag, double reachDistance) {
        RayTraceResult pickedEntity = null;
        Vec3d playerPosition = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3d playerLook = player.getLookVec();

        Vec3d playerViewOffset = new Vec3d(playerPosition.x + playerLook.x * reachDistance, playerPosition.y
                + playerLook.y * reachDistance, playerPosition.z + playerLook.z * reachDistance);

        double playerBorder = 1.1 * reachDistance;
        AxisAlignedBB boxToScan = player.getEntityBoundingBox().expand(playerBorder, playerBorder, playerBorder);
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
}
