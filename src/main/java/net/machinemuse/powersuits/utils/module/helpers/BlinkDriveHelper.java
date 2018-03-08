package net.machinemuse.powersuits.utils.module.helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.RayTraceResult;

public class BlinkDriveHelper {
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
}
