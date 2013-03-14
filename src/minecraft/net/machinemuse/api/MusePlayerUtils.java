package net.machinemuse.api;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class MusePlayerUtils {
	public static MovingObjectPosition raytraceEntities(World world, EntityPlayer player, boolean collisionFlag, double reachDistance) {

		MovingObjectPosition pickedEntity = null;
		Vec3 playerPosition = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);
		Vec3 playerLook = player.getLookVec();

		Vec3 playerViewOffset = Vec3.createVectorHelper(
				playerPosition.xCoord + playerLook.xCoord * reachDistance,
				playerPosition.yCoord + playerLook.yCoord * reachDistance,
				playerPosition.zCoord + playerLook.zCoord * reachDistance);

		double playerBorder = 1.1 * reachDistance;
		AxisAlignedBB boxToScan = player.boundingBox.expand(playerBorder, playerBorder, playerBorder);
		// AxisAlignedBB boxToScan =
		// player.boundingBox.addCoord(playerLook.xCoord * reachDistance,
		// playerLook.yCoord * reachDistance, playerLook.zCoord
		// * reachDistance);

		List entitiesHit = world.getEntitiesWithinAABBExcludingEntity(player, boxToScan);
		double closestEntity = reachDistance;

		for (int i = 0; i < entitiesHit.size(); ++i)
		{
			Entity entityHit = (Entity) entitiesHit.get(i);

			if (entityHit.canBeCollidedWith())
			{
				float border = entityHit.getCollisionBorderSize();
				AxisAlignedBB aabb = entityHit.boundingBox.expand((double) border, (double) border, (double) border);
				MovingObjectPosition hitMOP = aabb.calculateIntercept(playerPosition, playerViewOffset);

				if (aabb.isVecInside(playerPosition))
				{
					if (0.0D < closestEntity || closestEntity == 0.0D)
					{
						pickedEntity = new MovingObjectPosition(entityHit);
						pickedEntity.hitVec = hitMOP.hitVec;
						closestEntity = 0.0D;
					}
				}
				else if (hitMOP != null)
				{
					double distance = playerPosition.distanceTo(hitMOP.hitVec);

					if (distance < closestEntity || closestEntity == 0.0D)
					{
						pickedEntity = new MovingObjectPosition(entityHit);
						pickedEntity.hitVec = hitMOP.hitVec;
						closestEntity = distance;
					}
				}
			}
		}
		return pickedEntity;
	}

	public static MovingObjectPosition raytraceBlocks(World world, EntityPlayer player, boolean collisionFlag, double reachDistance) {
		Vec3 playerPosition = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);
		Vec3 playerLook = player.getLookVec();

		Vec3 playerViewOffset = Vec3.createVectorHelper(
				playerPosition.xCoord + playerLook.xCoord * reachDistance,
				playerPosition.yCoord + playerLook.yCoord * reachDistance,
				playerPosition.zCoord + playerLook.zCoord * reachDistance);
		return world.rayTraceBlocks_do_do(playerPosition, playerViewOffset, collisionFlag, !collisionFlag);
	}

	public static MovingObjectPosition doCustomRayTrace(World world, EntityPlayer player, boolean collisionFlag, double reachDistance)
	{
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
}
