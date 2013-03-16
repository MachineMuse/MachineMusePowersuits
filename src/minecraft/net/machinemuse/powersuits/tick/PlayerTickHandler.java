/**
 * 
 */
package net.machinemuse.powersuits.tick;

import java.util.EnumSet;
import java.util.List;

import net.machinemuse.api.ElectricItemUtils;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPlayerTickModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.general.MuseMathUtils;
import net.machinemuse.powersuits.common.MuseLogger;
import net.machinemuse.powersuits.common.PlayerInputMap;
import net.machinemuse.powersuits.event.MovementManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

/**
 * Tick handler for Player update step. tickStart() is queued before the entity is updated, and tickEnd() is queued afterwards.
 * 
 * Player update step: "Called to update the entity's position/logic."
 * 
 * tickData: EntityPlayer of the entity being updated.
 * 
 * @author MachineMuse
 */
public class PlayerTickHandler implements ITickHandler {

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		EntityPlayer player = toPlayer(tickData[0]);
		handle(player);

	}

	// int gliderTicker = 0, swimTicker = 0;

	public void handle(EntityPlayer player) {
		List<ItemStack> modularItemsEquipped = MuseItemUtils.modularItemsEquipped(player);
		ItemStack helmet = player.getCurrentArmor(3);
		ItemStack torso = player.getCurrentArmor(2);
		ItemStack pants = player.getCurrentArmor(1);
		ItemStack boots = player.getCurrentArmor(0);
		ItemStack tool = player.getCurrentEquippedItem();

		double totalEnergy = ElectricItemUtils.getPlayerEnergy(player);
		double totalWeight = MuseItemUtils.getPlayerWeight(player);
		double weightCapacity = 25000;

		//double totalEnergyDrain = 0;
		double foodAdjustment = 0;

		//double landMovementFactor = 0.1;
		//double jumpMovementFactor = 0.02;

		//Vec3 playerHorzFacing = player.getLookVec();
		//playerHorzFacing.yCoord = 0;
		//playerHorzFacing.normalize();

		//PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.username);
		//boolean jumpkey = movementInput.jumpKey;
		//float forwardkey = movementInput.forwardKey;
		//float strafekey = movementInput.strafeKey;
		//boolean sneakkey = movementInput.sneakKey;
		//boolean downkey = movementInput.downKey;

		for (IPlayerTickModule module : ModuleManager.getPlayerTickModules()) {
			for (ItemStack itemStack : modularItemsEquipped) {
				if (module.isValidForItem(itemStack, player)) {
					if (MuseItemUtils.itemHasActiveModule(itemStack, module.getName())) {
						((IPlayerTickModule) module).onPlayerTickActive(player, itemStack);
					} else {
						((IPlayerTickModule) module).onPlayerTickInactive(player, itemStack);
					}
				}
			}
		}
		if (helmet != null && helmet.getItem() instanceof IModularItem) {
			if (helmet.getTagCompound().hasKey("ench")) {
				helmet.getTagCompound().removeTag("ench");
			}
		}
		if (pants != null && pants.getItem() instanceof IModularItem) {
			if (pants.getTagCompound().hasKey("ench")) {
				pants.getTagCompound().removeTag("ench");
			}
		}
		if (boots != null && boots.getItem() instanceof IModularItem) {
			if (boots.getTagCompound().hasKey("ench")) {
				boots.getTagCompound().removeTag("ench");
			}
		}
		if (torso != null && torso.getItem() instanceof IModularItem) {
			if (torso.getTagCompound().hasKey("ench")) {
				torso.getTagCompound().removeTag("ench");
			}
		}

		// Update fall distance for damage, energy drain, and
		// exhaustion this tick

//		if (totalEnergyDrain > 0) {
//			ElectricItemUtils.drainPlayerEnergy(player, totalEnergyDrain);
//		} else {
//			ElectricItemUtils.givePlayerEnergy(player, -totalEnergyDrain);
//		}

		player.getFoodStats().addExhaustion((float) (-foodAdjustment));
		player.fallDistance = (float) MovementManager.computeFallHeightFromVelocity(MuseMathUtils.clampDouble(player.motionY, -1000.0, 0.0));

		// Weight movement penalty
		if (totalWeight > weightCapacity) {
			player.motionX *= weightCapacity / totalWeight;
			player.motionZ *= weightCapacity / totalWeight;
		}
	}

	public static void thrust(EntityPlayer player, double thrust, double jetEnergy, boolean flightControl) {
		PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.username);
		boolean jumpkey = movementInput.jumpKey;
		float forwardkey = movementInput.forwardKey;
		float strafekey = movementInput.strafeKey;
		boolean downkey = movementInput.downKey;
		double totalEnergyDrain = 0;
		if (flightControl) {
			Vec3 desiredDirection = player.getLookVec().normalize();
			double strafeX = desiredDirection.zCoord;
			double strafeZ = -desiredDirection.xCoord;
			double scaleStrafe = (strafeX * strafeX + strafeZ * strafeZ);
			desiredDirection.xCoord = desiredDirection.xCoord * Math.signum(forwardkey) + strafeX * Math.signum(strafekey);
			desiredDirection.yCoord = desiredDirection.yCoord * Math.signum(forwardkey) + (jumpkey ? 1 : 0) - (downkey ? 1 : 0);
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
					totalEnergyDrain += jetEnergy * thrust;
					player.motionY += thrust;
					thrust = 0;
				} else {
					totalEnergyDrain += jetEnergy * Math.abs(player.motionY);
					thrust -= player.motionY;
					player.motionY = 0;
				}
			}
			if (player.motionY < -1) {
				totalEnergyDrain += jetEnergy * Math.abs(1 + player.motionY);
				thrust += 1 + player.motionY;
				player.motionY = -1;
			}
			if (Math.abs(player.motionX) > 0 && desiredDirection.lengthVector() == 0) {
				if (Math.abs(player.motionX) > thrust) {
					totalEnergyDrain += jetEnergy * thrust;
					player.motionX -= Math.signum(player.motionX) * thrust;
					thrust = 0;
				} else {
					totalEnergyDrain += jetEnergy * Math.abs(player.motionX);
					thrust -= Math.abs(player.motionX);
					player.motionX = 0;
				}
			}
			if (Math.abs(player.motionZ) > 0 && desiredDirection.lengthVector() == 0) {
				if (Math.abs(player.motionZ) > thrust) {
					totalEnergyDrain += jetEnergy * thrust;
					player.motionZ -= Math.signum(player.motionZ) * thrust;
					thrust = 0;
				} else {
					totalEnergyDrain += jetEnergy * Math.abs(player.motionZ);
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

			totalEnergyDrain += jetEnergy * (vx * vx + vy * vy + vz * vz);
			ElectricItemUtils.drainPlayerEnergy(player, totalEnergyDrain);
		} else {
			Vec3 playerHorzFacing = player.getLookVec();
			playerHorzFacing.yCoord = 0;
			playerHorzFacing.normalize();
			totalEnergyDrain += jetEnergy;
			if (forwardkey == 0) {
				player.motionY += thrust;
			} else {
				player.motionY += thrust / 2;
				player.motionX += playerHorzFacing.xCoord * thrust / 2 * Math.signum(forwardkey);
				player.motionZ += playerHorzFacing.zCoord * thrust / 2 * Math.signum(forwardkey);
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
		EntityPlayer player = toPlayer(tickData[0]);
		List<ItemStack> stacks = MuseItemUtils.getModularItemsInInventory(player.inventory);

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
