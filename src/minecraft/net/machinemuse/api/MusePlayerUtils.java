package net.machinemuse.api;

import java.util.List;

import net.machinemuse.powersuits.common.MuseLogger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class MusePlayerUtils {

	public static MovingObjectPosition doCustomRayTrace(World world, EntityPlayer player, boolean collisionFlag, double reachDistance)
	{
		MovingObjectPosition pickedEntity = null;
		Vec3 playerPosition = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);
		Vec3 playerLook = player.getLookVec();
		MuseLogger.logDebug("Pos: " + playerPosition);
		MuseLogger.logDebug("Look: " + playerLook);

		Vec3 playerViewOffset = Vec3.createVectorHelper(
				playerPosition.xCoord + playerLook.xCoord * reachDistance,
				playerPosition.yCoord + playerLook.yCoord * reachDistance,
				playerPosition.zCoord + playerLook.zCoord * reachDistance);
		MuseLogger.logDebug("Ray: " + playerViewOffset);

		double playerBorder = 1.1 * reachDistance;

		List entitiesHit = world.getEntitiesWithinAABBExcludingEntity(player,
				player.boundingBox.expand(playerBorder, playerBorder, playerBorder));
		double closestDistance = reachDistance;

		for (int i = 0; i < entitiesHit.size(); ++i)
		{
			Entity entityHit = (Entity) entitiesHit.get(i);

			if (entityHit.canBeCollidedWith() && !entityHit.equals(player))
			{
				float border = entityHit.getCollisionBorderSize();
				AxisAlignedBB aabb = entityHit.boundingBox.expand((double) border, (double) border, (double) border);
				MovingObjectPosition hitMOP = aabb.calculateIntercept(playerPosition, playerViewOffset);

				if (aabb.isVecInside(playerPosition))
				{
					if (0.0D < closestDistance || closestDistance == 0.0D)
					{
						pickedEntity = new MovingObjectPosition(entityHit);
						closestDistance = 0.0D;
					}
				}
				else if (hitMOP != null)
				{
					double distance = playerPosition.distanceTo(hitMOP.hitVec);

					if (distance < closestDistance || closestDistance == 0.0D)
					{
						pickedEntity = new MovingObjectPosition(entityHit);
						closestDistance = distance;
					}
				}
			}
		}
		MovingObjectPosition pickedBlock = world.rayTraceBlocks_do_do(playerPosition, playerViewOffset, collisionFlag, !collisionFlag);
		if (pickedBlock == null) {
			return pickedEntity;
		} else if (pickedEntity == null) {
			return pickedBlock;
		} else if (pickedEntity.hitVec.distanceTo(playerPosition) > pickedBlock.hitVec.distanceTo(playerPosition) + 1) {
			return pickedBlock;
		} else {
			return pickedEntity;
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
}
