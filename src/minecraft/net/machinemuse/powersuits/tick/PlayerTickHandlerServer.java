/**
 * 
 */
package net.machinemuse.powersuits.tick;

import java.util.Collection;
import java.util.EnumSet;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModularCommon;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.powersuits.common.MuseLogger;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
@SideOnly(Side.SERVER)
public class PlayerTickHandlerServer implements ITickHandler {

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		EntityPlayer rawPlayer = toPlayer(tickData[0]);
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (rawPlayer instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) rawPlayer;
			handleServer(player);
		}

	}

	public void handleClient(EntityClientPlayerMP player) {
		// ItemStack helmet = player.getCurrentArmor(3);
		// ItemStack torso = player.getCurrentArmor(2);
		// ItemStack pants = player.getCurrentArmor(1);
		// ItemStack boots = player.getCurrentArmor(0);
		// ItemStack tool = player.getCurrentEquippedItem();
		//
		// double totalEnergy = ItemUtils.getPlayerEnergy(player);
		// double totalWeight = ItemUtils.getPlayerWeight(player);
		// double weightCapacity = 25000;
		//
		// double totalEnergyDrain = 0;
		// double foodAdjustment = 0;
		//
		// double landMovementFactor = 0.1;
		// double jumpMovementFactor = 0.02;
		//
		// Vec3 playerHorzFacing = player.getLookVec();
		// playerHorzFacing.yCoord = 0;
		// playerHorzFacing.normalize();
		//
		// boolean jumpkey = player.movementInput.jump;
		// float forwardkey = player.movementInput.moveForward;
		// float strafekey = player.movementInput.moveStrafe;
		// boolean sneakkey = player.movementInput.sneak;
		//
		// boolean hasSprintAssist = false;
		// boolean hasGlider = false;
		// boolean hasParachute = false;
		// boolean hasJetpack = false;
		// boolean hasJetboots = false;
		// boolean hasJumpAssist = false;
		// boolean hasSwimAssist = false;
		//
		// if (pants != null && pants.getItem() instanceof IModularItem) {
		// hasSprintAssist = ItemUtils.itemHasActiveModule(pants,
		// ModularCommon.MODULE_SPRINT_ASSIST);
		// hasJumpAssist = ItemUtils.itemHasActiveModule(pants,
		// ModularCommon.MODULE_JUMP_ASSIST);
		// hasSwimAssist = ItemUtils.itemHasActiveModule(pants,
		// ModularCommon.MODULE_SWIM_BOOST);
		// }
		// if (boots != null && boots.getItem() instanceof IModularItem) {
		// hasJetboots = ItemUtils.itemHasActiveModule(boots,
		// ModularCommon.MODULE_JETBOOTS);
		// }
		// if (torso != null && torso.getItem() instanceof IModularItem) {
		// hasJetpack = ItemUtils.itemHasActiveModule(torso,
		// ModularCommon.MODULE_JETPACK);
		// hasGlider = ItemUtils.itemHasActiveModule(torso,
		// ModularCommon.MODULE_GLIDER);
		// hasParachute = ItemUtils.itemHasActiveModule(torso,
		// ModularCommon.MODULE_PARACHUTE);
		// }
		//
		// if (player.isInWater()) {
		// if (hasSwimAssist && (forwardkey != 0 || strafekey != 0 || jumpkey ||
		// sneakkey)) {
		// double moveRatio = 0;
		// if (forwardkey != 0) {
		// moveRatio += forwardkey * forwardkey;
		// }
		// if (strafekey != 0) {
		// moveRatio += strafekey * strafekey;
		// }
		// if (jumpkey || sneakkey) {
		// moveRatio += 0.2 * 0.2;
		// }
		// double swimAssistRate = ModuleManager.computeModularProperty(pants,
		// ModularCommon.SWIM_BOOST_AMOUNT) * 0.05;
		// double swimEnergyConsumption =
		// ModuleManager.computeModularProperty(pants,
		// ModularCommon.SWIM_BOOST_ENERGY_CONSUMPTION);
		// if (swimEnergyConsumption + totalEnergyDrain < totalEnergy) {
		// totalEnergyDrain += swimEnergyConsumption;
		// // Forward/backward movement
		// player.motionX += player.getLookVec().xCoord * swimAssistRate *
		// forwardkey / moveRatio;
		// player.motionY += player.getLookVec().yCoord * swimAssistRate *
		// forwardkey / moveRatio;
		// player.motionZ += player.getLookVec().zCoord * swimAssistRate *
		// forwardkey / moveRatio;
		//
		// if (jumpkey) {
		// player.motionY += swimAssistRate * 0.2 / moveRatio;
		// }
		//
		// if (sneakkey) {
		// player.motionY -= swimAssistRate * 0.2 / moveRatio;
		// }
		// }
		// }
		// } else {
		// if (hasJumpAssist && jumpkey) {
		// double multiplier = MovementManager.getPlayerJumpMultiplier(player);
		// if (multiplier > 0) {
		// player.motionY += 0.15 * Math.min(multiplier, 1) *
		// getWeightPenaltyRatio(totalWeight, weightCapacity);
		// MovementManager.setPlayerJumpTicks(player, multiplier - 1);
		// }
		// player.jumpMovementFactor = player.landMovementFactor;
		// } else {
		// MovementManager.setPlayerJumpTicks(player, 0);
		// }
		//
		// // Jetpack & jetboots
		// if ((hasJetpack || hasJetboots) && jumpkey && player.motionY < 0.5) {
		// double jetEnergy = 0;
		// double thrust = 0;
		// if (hasJetpack) {
		// jetEnergy += ModuleManager.computeModularProperty(torso,
		// ModularCommon.JET_ENERGY_CONSUMPTION);
		// thrust += ModuleManager.computeModularProperty(torso,
		// ModularCommon.JET_THRUST);
		// }
		// if (hasJetboots) {
		// jetEnergy += ModuleManager.computeModularProperty(boots,
		// ModularCommon.JET_ENERGY_CONSUMPTION);
		// thrust += ModuleManager.computeModularProperty(boots,
		// ModularCommon.JET_THRUST);
		// }
		// if (jetEnergy + totalEnergyDrain < totalEnergy) {
		// totalEnergyDrain += jetEnergy;
		// thrust *= getWeightPenaltyRatio(totalWeight, weightCapacity);
		// if (forwardkey == 0) {
		// player.motionY += thrust;
		// } else {
		// player.motionY += thrust / 2;
		// player.motionX += playerHorzFacing.xCoord * thrust / 2 *
		// Math.signum(forwardkey);
		// player.motionZ += playerHorzFacing.zCoord * thrust / 2 *
		// Math.signum(forwardkey);
		// }
		// }
		//
		// }
		//
		// // Glider
		// if (hasGlider && sneakkey && player.motionY < -0.1 && (!hasParachute
		// || forwardkey > 0)) {
		// if (player.motionY < -0.1) {
		// double motionYchange = Math.min(0.08, -0.1 - player.motionY);
		// player.motionY += motionYchange;
		// player.motionX += playerHorzFacing.xCoord * motionYchange;
		// player.motionZ += playerHorzFacing.zCoord * motionYchange;
		//
		// // sprinting speed
		// player.jumpMovementFactor += 0.03f;
		// }
		// }
		//
		// // Parachute
		// if (hasParachute && sneakkey && player.motionY < -0.1 && (!hasGlider
		// || forwardkey <= 0)) {
		// double totalVelocity = Math.sqrt(player.motionX * player.motionX +
		// player.motionZ * player.motionZ + player.motionY * player.motionY)
		// * getWeightPenaltyRatio(totalWeight, weightCapacity);
		// if (totalVelocity > 0) {
		// player.motionX = player.motionX * 0.1 / totalVelocity;
		// player.motionY = player.motionY * 0.1 / totalVelocity;
		// player.motionZ = player.motionZ * 0.1 / totalVelocity;
		// }
		// }
		//
		// // Sprint assist
		// if (hasSprintAssist && player.isSprinting()) {
		// double horzMovement = Math.sqrt(player.motionX * player.motionX +
		// player.motionZ * player.motionZ);
		// double exhaustion = Math.round(horzMovement * 100.0F) * 0.01;
		//
		// double sprintCost = ModuleManager.computeModularProperty(pants,
		// ModularCommon.SPRINT_ENERGY_CONSUMPTION);
		// if (sprintCost + totalEnergyDrain < totalEnergy) {
		// double sprintMultiplier = ModuleManager.computeModularProperty(pants,
		// ModularCommon.SPRINT_SPEED_MULTIPLIER);
		// double exhaustionComp = ModuleManager.computeModularProperty(pants,
		// ModularCommon.SPRINT_FOOD_COMPENSATION);
		// totalEnergyDrain += sprintCost;
		// player.landMovementFactor *= sprintMultiplier;
		//
		// foodAdjustment += 0.01 * exhaustion * exhaustionComp;
		// }
		// player.jumpMovementFactor = player.landMovementFactor;
		// }
		// }
		//
		// // Update fall distance for damage, energy drain, and
		// // exhaustion this tick
		// player.fallDistance = (float)
		// MovementManager.computeFallHeightFromVelocity(player.motionY);
		//
		// if (totalEnergyDrain > 0) {
		// ItemUtils.drainPlayerEnergy(player, totalEnergyDrain);
		// } else {
		// ItemUtils.givePlayerEnergy(player, -totalEnergyDrain);
		// }
		//
		// player.getFoodStats().addExhaustion((float) (-foodAdjustment));
		//
		// // Weight movement penalty
		// if (totalWeight > weightCapacity) {
		// player.motionX *= weightCapacity / totalWeight;
		// player.motionZ *= weightCapacity / totalWeight;
		// }
		//
		// MusePacket packet = new MusePacketPlayerUpdate(player,
		// -totalEnergyDrain, -foodAdjustment);
		// player.sendQueue.addToSendQueue(packet.getPacket250());

	}

	public void handleServer(EntityPlayerMP player) {
		ItemStack helmet = player.getCurrentArmor(3);
		ItemStack torso = player.getCurrentArmor(2);
		ItemStack pants = player.getCurrentArmor(1);
		ItemStack boots = player.getCurrentArmor(0);
		ItemStack tool = player.getCurrentEquippedItem();
		boolean hasSprintAssist = false;
		boolean hasGlider = false;
		boolean hasParachute = false;
		boolean hasJetpack = false;
		boolean hasJetboots = false;
		boolean hasJumpAssist = false;
		boolean hasSwimAssist = false;
		boolean hasNightVision = false;
		boolean hasInvis = false;
		boolean hasFlightControl = false;

		if (helmet != null && helmet.getItem() instanceof IModularItem) {
			hasNightVision = MuseItemUtils.itemHasActiveModule(helmet, ModularCommon.MODULE_NIGHT_VISION);
			hasFlightControl = MuseItemUtils.itemHasActiveModule(helmet, ModularCommon.MODULE_FLIGHT_CONTROL);
			if (helmet.getTagCompound().hasKey("ench")) {
				helmet.getTagCompound().removeTag("ench");
			}
		}
		if (pants != null && pants.getItem() instanceof IModularItem) {
			hasSprintAssist = MuseItemUtils.itemHasActiveModule(pants, ModularCommon.MODULE_SPRINT_ASSIST);
			hasJumpAssist = MuseItemUtils.itemHasActiveModule(pants, ModularCommon.MODULE_JUMP_ASSIST);
			hasSwimAssist = MuseItemUtils.itemHasActiveModule(pants, ModularCommon.MODULE_SWIM_BOOST);
			if (pants.getTagCompound().hasKey("ench")) {
				pants.getTagCompound().removeTag("ench");
			}
		}
		if (boots != null && boots.getItem() instanceof IModularItem) {
			hasJetboots = MuseItemUtils.itemHasActiveModule(boots, ModularCommon.MODULE_JETBOOTS);
			if (boots.getTagCompound().hasKey("ench")) {
				boots.getTagCompound().removeTag("ench");
			}
		}
		if (torso != null && torso.getItem() instanceof IModularItem) {
			hasInvis = MuseItemUtils.itemHasActiveModule(torso, ModularCommon.MODULE_ACTIVE_CAMOUFLAGE);
			hasJetpack = MuseItemUtils.itemHasActiveModule(torso, ModularCommon.MODULE_JETPACK);
			hasGlider = MuseItemUtils.itemHasActiveModule(torso, ModularCommon.MODULE_GLIDER);
			hasParachute = MuseItemUtils.itemHasActiveModule(torso, ModularCommon.MODULE_PARACHUTE);
			if (torso.getTagCompound().hasKey("ench")) {
				torso.getTagCompound().removeTag("ench");
			}
		}
		PotionEffect nightVision = null;
		PotionEffect invis = null;
		Collection<PotionEffect> effects = player.getActivePotionEffects();
		for (PotionEffect effect : effects) {
			if (effect.getAmplifier() == -337 && effect.getPotionID() == Potion.nightVision.id) {
				nightVision = effect;
				break;
			}
			if (effect.getAmplifier() == 81 && effect.getPotionID() == Potion.invisibility.id) {
				invis = effect;
				break;
			}
		}
		if (hasNightVision && 5 < MuseItemUtils.getPlayerEnergy(player)) {
			if (nightVision == null || nightVision.getDuration() < 210) {
				player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 500, -337));
				MuseItemUtils.drainPlayerEnergy(player, 5);
			}
		} else {
			if (nightVision != null) {
				player.removePotionEffect(Potion.nightVision.id);
			}
		}

		if (hasInvis && 50 < MuseItemUtils.getPlayerEnergy(player)) {
			if (invis == null || invis.getDuration() < 210) {
				player.addPotionEffect(new PotionEffect(Potion.invisibility.id, 500, 81));
				MuseItemUtils.drainPlayerEnergy(player, 50);
			}
		} else {
			if (invis != null) {
				player.removePotionEffect(Potion.invisibility.id);
			}
		}
	}

	public static double getWeightPenaltyRatio(double currentWeight, double capacity) {
		if (currentWeight < capacity) {
			return 1;
		} else {
			return capacity / currentWeight;
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
	}

	public static World toWorld(Object data) {
		World world = null;
		try {
			world = (World) data;
		} catch (ClassCastException e) {
			MuseLogger.logError("MMMPS: Player tick handler received invalid World object");
			e.printStackTrace();
		}
		return world;
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
