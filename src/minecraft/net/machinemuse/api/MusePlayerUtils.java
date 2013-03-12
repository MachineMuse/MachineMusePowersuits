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

	public static MovingObjectPosition doCustomRayTrace(World world, EntityPlayer player, boolean collisionFlag, double reachDistance)
	{
		MovingObjectPosition pickedItem = null;
		Vec3 playerPosition = player.getPosition(0);
		playerPosition.yCoord += player.getEyeHeight();
		Vec3 playerLook = player.getLook(0);

		Vec3 playerViewOffset = playerPosition.addVector(playerLook.xCoord * reachDistance, playerLook.yCoord * reachDistance, playerLook.zCoord
				* reachDistance);

		double playerBorder = 1.1 * reachDistance;

		List entitiesHit = world.getEntitiesWithinAABBExcludingEntity(player,
				player.boundingBox.expand(playerBorder, playerBorder, playerBorder));
		double closestDistance = reachDistance;

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
					if (0.0D < closestDistance || closestDistance == 0.0D)
					{
						pickedItem = new MovingObjectPosition(entityHit);
						closestDistance = 0.0D;
					}
				}
				else if (hitMOP != null)
				{
					double distance = playerPosition.distanceTo(hitMOP.hitVec);

					if (distance < closestDistance || closestDistance == 0.0D)
					{
						pickedItem = new MovingObjectPosition(entityHit);
						closestDistance = distance;
					}
				}
			}
		}
		if (pickedItem != null) {
			return pickedItem;
		} else {
			return player.rayTrace(reachDistance, 0);
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
