/**
 * 
 */
package net.machinemuse.powersuits.tick;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.machinemuse.powersuits.common.MuseLogger;
import net.machinemuse.powersuits.event.MovementManager;
import net.machinemuse.powersuits.item.IModularItem;
import net.machinemuse.powersuits.item.ItemUtils;
import net.machinemuse.powersuits.item.ModularCommon;
import net.machinemuse.powersuits.network.MusePacket;
import net.machinemuse.powersuits.network.MusePacketEnergyAdjustment;
import net.machinemuse.powersuits.network.MusePacketFallDistance;
import net.machinemuse.powersuits.powermodule.ModuleManager;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;

/**
 * Tick handler for Player update step. tickStart() is queued before the entity
 * is updated, and tickEnd() is queued afterwards.
 * 
 * Player update step: "Called to update the entity's position/logic."
 * 
 * tickData: EntityPlayer of the entity being updated.
 * 
 * @author MachineMuse
 */
public class PlayerTickHandler implements ITickHandler {
	public static Map<String, Integer> playerJumpTicks = new HashMap();

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {

		EntityPlayer player = toPlayer(tickData[0]);

		ItemStack helmet = player.getCurrentArmor(3);
		ItemStack torso = player.getCurrentArmor(2);
		ItemStack pants = player.getCurrentArmor(1);
		ItemStack boots = player.getCurrentArmor(0);
		ItemStack tool = player.getCurrentEquippedItem();

		double totalEnergy = ItemUtils.getPlayerEnergy(player);
		double totalWeight = ItemUtils.getPlayerWeight(player);
		double weightCapacity = 25000;

		double landMovementFactor = 0.1;
		double jumpMovementFactor = 0.02;
		double sprintMultiplier = 1.0;
		double horzMovement = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ);
		double exhaustion = Math.round(horzMovement * 100.0F) * 0.01;

		Side side = FMLCommonHandler.instance().getEffectiveSide();

		if (pants != null && pants.getItem() instanceof IModularItem) {
			if (player.isSprinting() && ItemUtils.itemHasModule(pants, ModularCommon.MODULE_SPRINT_ASSIST)) {
				double sprintCost = ModuleManager.computeModularProperty(pants, ModularCommon.SPRINT_ENERGY_CONSUMPTION);
				if (sprintCost < totalEnergy) {
					totalEnergy -= sprintCost;
					ItemUtils.drainPlayerEnergy(player, sprintCost);
					sprintMultiplier = ModuleManager.computeModularProperty(pants, ModularCommon.SPRINT_SPEED_MULTIPLIER);
					player.landMovementFactor *= sprintMultiplier;
					double exhaustionComp = ModuleManager.computeModularProperty(pants, ModularCommon.SPRINT_FOOD_COMPENSATION);
					player.addExhaustion((float) (-0.01 * exhaustion * exhaustionComp));
					if (side == Side.CLIENT) {
						MusePacket packet = new MusePacketEnergyAdjustment(player, sprintCost);
						((EntityClientPlayerMP) player).sendQueue.addToSendQueue(packet.getPacket250());
					}
				}
			}
		}
		if (side == Side.CLIENT) {
			if (player instanceof EntityClientPlayerMP) {
				EntityClientPlayerMP clientplayer = (EntityClientPlayerMP) player;
				boolean jumpkey = clientplayer.movementInput.jump;
				float forwardkey = clientplayer.movementInput.moveForward;
				int jumpTicks;
				if (playerJumpTicks.containsKey(player.username)) {
					jumpTicks = playerJumpTicks.get(player.username);
				}
				if (jumpkey && torso != null) {
					boolean hasGlider = ItemUtils.itemHasModule(torso, ModularCommon.MODULE_GLIDER);
					boolean hasParachute = ItemUtils.itemHasModule(torso, ModularCommon.MODULE_PARACHUTE);
					boolean isFalling = player.motionY < -0.1;
					if (hasGlider && isFalling && (!hasParachute || forwardkey > 0)) {
						if (player.motionY < -0.1) {
							double motionYchange = Math.min(0.08, -0.1 - player.motionY);
							player.motionY += motionYchange;
							Vec3 playerLookVec = player.getLookVec();
							playerLookVec.yCoord = 0;
							playerLookVec.normalize();
							player.motionX += playerLookVec.xCoord * motionYchange;
							player.motionZ += playerLookVec.zCoord * motionYchange;

							// sprinting speed
							player.jumpMovementFactor += 0.03f;
						}
					}
					if (hasParachute && isFalling && (!hasGlider || forwardkey <= 0)) {
						double totalVelocity = Math.sqrt(horzMovement * horzMovement + player.motionY * player.motionY);
						player.motionX = player.motionX * 0.1 / totalVelocity;
						player.motionY = player.motionY * 0.1 / totalVelocity;
						player.motionZ = player.motionZ * 0.1 / totalVelocity;
					}

				}
				double fallDistance = MovementManager.computeFallHeightFromVelocity(player.motionY);
				MusePacket packet = new MusePacketFallDistance(player, fallDistance);
				clientplayer.sendQueue.addToSendQueue(packet.getPacket250());
			}
		}
		if (totalWeight > weightCapacity) {
			player.motionX *= weightCapacity / totalWeight;
			player.motionZ *= weightCapacity / totalWeight;
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		EntityPlayer player = toPlayer(tickData[0]);
		List<ItemStack> stacks = ItemUtils
				.getModularItemsInInventory(player.inventory);

	}

	public static World toWorld(Object data) {
		World world = null;
		try {
			world = (World) data;
		} catch (ClassCastException e) {
			MuseLogger.logDebug(
					"MMMPS: Player tick handler received invalid World object");
			e.printStackTrace();
		}
		return world;
	}

	public static EntityPlayer toPlayer(Object data) {
		EntityPlayer player = null;
		try {
			player = (EntityPlayer) data;
		} catch (ClassCastException e) {
			MuseLogger
					.logDebug(
					"MMMPS: Player tick handler received invalid Player object");
			e.printStackTrace();
		}
		return player;
	}

	/**
	 * Type of tick handled by this handler
	 */
	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.PLAYER);
	}

	/**
	 * Profiling label for this handler
	 */
	@Override
	public String getLabel() {
		return "MMMPS: Player Tick";
	}

}
