package net.machinemuse.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class MusePlayerUtils {

	public static MovingObjectPosition doCustomRayTrace(World World, EntityPlayer player, boolean collisionFlag, double reachDistance)
	{
		float one = 1.0F;
		float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * one;
		float yaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * one;
		double posx = player.prevPosX + (player.posX - player.prevPosX) * (double) one;
		double posy = player.prevPosY + (player.posY - player.prevPosY) * (double) one + 1.62D - (double) player.yOffset;
		double posz = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) one;
		Vec3 posVector = World.getWorldVec3Pool().getVecFromPool(posx, posy, posz);
		float yawz = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
		float yawx = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
		float pitchhorizontal = -MathHelper.cos(-pitch * 0.017453292F);
		float pitchvertical = MathHelper.sin(-pitch * 0.017453292F);
		float directionx = yawx * pitchhorizontal;
		float directionz = yawz * pitchhorizontal;
		Vec3 rayToCheck = posVector.addVector((double) directionx * reachDistance, (double) pitchvertical * reachDistance, (double) directionz
				* reachDistance);
		return World.rayTraceBlocks_do_do(posVector, rayToCheck, collisionFlag, !collisionFlag);
	}

	public static void teleportEntity(EntityPlayer entityPlayer, MovingObjectPosition hitMOP) {
		if (hitMOP != null && entityPlayer instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) entityPlayer;
			if (!player.playerNetServerHandler.connectionClosed) {
				switch (hitMOP.typeOfHit) {
				case ENTITY:
					player.setPositionAndUpdate(hitMOP.entityHit.posX, hitMOP.entityHit.posY, hitMOP.entityHit.posZ);
					break;
				case TILE:
					int hitx = hitMOP.blockX;
					int hity = hitMOP.blockY;
					int hitz = hitMOP.blockZ;
					switch (hitMOP.sideHit) {
					case 0: // Bottom
						hity -= 2;
						break;
					case 1: // Top
						hity += 1;
						break;
					case 2: // East
						hitz -= 1;
						break;
					case 3: // West
						hitz += 1;
						break;
					case 4: // North
						hitx -= 1;
						break;
					case 5: // South
						hitx += 1;
						break;
					}
					player.setPositionAndUpdate(hitx + 0.5, hity, hitz + 0.5);
					break;
				default:
					break;

				}
			}
		}
	}
}
