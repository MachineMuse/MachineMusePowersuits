/**
 * 
 */
package net.machinemuse.powersuits.tick;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModularCommon;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.powersuits.common.MuseLogger;
import net.machinemuse.powersuits.common.PlayerInputMap;
import net.machinemuse.powersuits.event.MovementManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.FoodStats;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

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

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		EntityPlayer player = toPlayer(tickData[0]);
		handle(player);

	}

	public void handle(EntityPlayer player) {
		ItemStack helmet = player.getCurrentArmor(3);
		ItemStack torso = player.getCurrentArmor(2);
		ItemStack pants = player.getCurrentArmor(1);
		ItemStack boots = player.getCurrentArmor(0);
		ItemStack tool = player.getCurrentEquippedItem();

		double totalEnergy = MuseItemUtils.getPlayerEnergy(player);
		double totalWeight = MuseItemUtils.getPlayerWeight(player);
		double weightCapacity = 25000;

		double totalEnergyDrain = 0;
		double foodAdjustment = 0;

		double landMovementFactor = 0.1;
		double jumpMovementFactor = 0.02;

		Vec3 playerHorzFacing = player.getLookVec();
		playerHorzFacing.yCoord = 0;
		playerHorzFacing.normalize();

		PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.username);
		boolean jumpkey = movementInput.jumpKey;
		float forwardkey = movementInput.forwardKey;
		float strafekey = movementInput.strafeKey;
		boolean sneakkey = movementInput.sneakKey;
		boolean downkey = movementInput.downKey;
		player.fallDistance = (float) movementInput.fallDistance;

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
		boolean hasFeeder = false;

		if (helmet != null && helmet.getItem() instanceof IModularItem) {
			hasNightVision = MuseItemUtils.itemHasActiveModule(helmet, ModularCommon.MODULE_NIGHT_VISION);
			hasFlightControl = MuseItemUtils.itemHasActiveModule(helmet, ModularCommon.MODULE_FLIGHT_CONTROL);
			hasFeeder = MuseItemUtils.itemHasActiveModule(helmet, ModularCommon.MODULE_AUTO_FEEDER);
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
		if (hasNightVision && totalEnergyDrain + 5 < totalEnergy) {
			if (nightVision == null || nightVision.getDuration() < 210) {
				player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 500, -337));
				totalEnergyDrain += 5;
			}
		} else {
			if (nightVision != null) {
				player.removePotionEffect(Potion.nightVision.id);
			}
		}

		if (hasInvis && totalEnergyDrain + 50 < totalEnergy) {
			if (invis == null || invis.getDuration() < 210) {
				player.addPotionEffect(new PotionEffect(Potion.invisibility.id, 500, 81));
				totalEnergyDrain += 50;
			}
		} else {
			if (invis != null) {
				player.removePotionEffect(Potion.invisibility.id);
			}
		}

		if (player.isInWater()) {
			if (hasSwimAssist && (forwardkey != 0 || strafekey != 0 || jumpkey || sneakkey)) {
				double moveRatio = 0;
				if (forwardkey != 0) {
					moveRatio += forwardkey * forwardkey;
				}
				if (strafekey != 0) {
					moveRatio += strafekey * strafekey;
				}
				if (jumpkey || sneakkey) {
					moveRatio += 0.2 * 0.2;
				}
				double swimAssistRate = ModuleManager.computeModularProperty(pants, ModularCommon.SWIM_BOOST_AMOUNT) * 0.05;
				double swimEnergyConsumption = ModuleManager.computeModularProperty(pants, ModularCommon.SWIM_BOOST_ENERGY_CONSUMPTION);
				if (swimEnergyConsumption + totalEnergyDrain < totalEnergy) {
					totalEnergyDrain += swimEnergyConsumption;
					// Forward/backward movement
					player.motionX += player.getLookVec().xCoord * swimAssistRate * forwardkey / moveRatio;
					player.motionY += player.getLookVec().yCoord * swimAssistRate * forwardkey / moveRatio;
					player.motionZ += player.getLookVec().zCoord * swimAssistRate * forwardkey / moveRatio;

					if (jumpkey) {
						player.motionY += swimAssistRate * 0.2 / moveRatio;
					}

					if (sneakkey) {
						player.motionY -= swimAssistRate * 0.2 / moveRatio;
					}
				}
			}
		} else {
			if (hasJumpAssist && jumpkey) {
				double multiplier = MovementManager.getPlayerJumpMultiplier(player);
				if (multiplier > 0) {
					player.motionY += 0.15 * Math.min(multiplier, 1) * getWeightPenaltyRatio(totalWeight, weightCapacity);
					MovementManager.setPlayerJumpTicks(player, multiplier - 1);
				}
				player.jumpMovementFactor = player.landMovementFactor * .7f;
			} else {
				MovementManager.setPlayerJumpTicks(player, 0);
			}

			// Jetpack & jetboots
			if (hasJetpack || hasJetboots) {
				double jetEnergy = 0;
				double thrust = 0;
				if (hasJetpack) {
					jetEnergy += ModuleManager.computeModularProperty(torso, ModularCommon.JET_ENERGY_CONSUMPTION);
					thrust += ModuleManager.computeModularProperty(torso, ModularCommon.JET_THRUST);
				}
				if (hasJetboots) {
					jetEnergy += ModuleManager.computeModularProperty(boots, ModularCommon.JET_ENERGY_CONSUMPTION);
					thrust += ModuleManager.computeModularProperty(boots, ModularCommon.JET_THRUST);
				}
				if (jetEnergy + totalEnergyDrain < totalEnergy) {
					thrust *= getWeightPenaltyRatio(totalWeight, weightCapacity);
					if (hasFlightControl && thrust > 0) {
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

					} else if (jumpkey && player.motionY < 0.5) {
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

			}

			// Glider
			if (hasGlider && sneakkey && player.motionY < -0.1 && (!hasParachute || forwardkey > 0)) {
				if (player.motionY < -0.1) {
					double motionYchange = Math.min(0.08, -0.1 - player.motionY);
					player.motionY += motionYchange;
					player.motionX += playerHorzFacing.xCoord * motionYchange;
					player.motionZ += playerHorzFacing.zCoord * motionYchange;

					// sprinting speed
					player.jumpMovementFactor += 0.03f;
				}
			}

			// Parachute
			if (hasParachute && sneakkey && player.motionY < -0.1 && (!hasGlider || forwardkey <= 0)) {
				double totalVelocity = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ + player.motionY * player.motionY)
						* getWeightPenaltyRatio(totalWeight, weightCapacity);
				if (totalVelocity > 0) {
					player.motionX = player.motionX * 0.1 / totalVelocity;
					player.motionY = player.motionY * 0.1 / totalVelocity;
					player.motionZ = player.motionZ * 0.1 / totalVelocity;
				}
			}

			// Sprint assist
			if (hasSprintAssist) {
				double horzMovement = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ);
				if (player.isSprinting()) {
					double exhaustion = Math.round(horzMovement * 100.0F) * 0.01;

					double sprintCost = ModuleManager.computeModularProperty(pants, ModularCommon.SPRINT_ENERGY_CONSUMPTION);
					if (sprintCost + totalEnergyDrain < totalEnergy) {
						double sprintMultiplier = ModuleManager.computeModularProperty(pants, ModularCommon.SPRINT_SPEED_MULTIPLIER);
						double exhaustionComp = ModuleManager.computeModularProperty(pants, ModularCommon.SPRINT_FOOD_COMPENSATION);
						totalEnergyDrain += sprintCost * horzMovement * 5;
						player.landMovementFactor *= sprintMultiplier;

						foodAdjustment += 0.01 * exhaustion * exhaustionComp;
						player.jumpMovementFactor = player.landMovementFactor * .5f;
					}
				} else {
					double cost = ModuleManager.computeModularProperty(pants, ModularCommon.WALKING_ENERGY_CONSUMPTION);
					if (cost + totalEnergyDrain < totalEnergy) {
						double walkMultiplier = ModuleManager.computeModularProperty(pants, ModularCommon.WALKING_SPEED_MULTIPLIER);
						totalEnergyDrain += cost * horzMovement * 5;
						player.landMovementFactor *= walkMultiplier;
						player.jumpMovementFactor = player.landMovementFactor * .5f;
					}

				}
			}
		}
		//Food Module
		if (hasFeeder) { 
			IInventory inv = player.inventory;
			int foodLevel = MuseItemUtils.getFoodLevel(helmet);
			for (int i = 0; i < inv.getSizeInventory(); i++) {
				ItemStack stack = inv.getStackInSlot(i);
				if (stack != null && stack.getItem() instanceof ItemFood) {
					ItemFood food = (ItemFood) stack.getItem();
					for (int a = 0; a < stack.stackSize; a++) {
						foodLevel += food.getHealAmount();
					}
					MuseItemUtils.setFoodLevel(helmet, foodLevel);
					player.inventory.setInventorySlotContents(i, null);
				}
			}
			FoodStats foodStats = player.getFoodStats();
			int foodNeeded = 20 - foodStats.getFoodLevel();
			if (foodNeeded > 0) {
				foodStats.addStats(foodNeeded, 0.0F);
				MuseItemUtils.setFoodLevel(helmet, MuseItemUtils.getFoodLevel(helmet) - foodNeeded);
				totalEnergyDrain += 100*foodNeeded;
			}
		}

		// Update fall distance for damage, energy drain, and
		// exhaustion this tick

		if (totalEnergyDrain > 0) {
			MuseItemUtils.drainPlayerEnergy(player, totalEnergyDrain);
		} else {
			MuseItemUtils.givePlayerEnergy(player, -totalEnergyDrain);
		}

		player.getFoodStats().addExhaustion((float) (-foodAdjustment));

		// Weight movement penalty
		if (totalWeight > weightCapacity) {
			player.motionX *= weightCapacity / totalWeight;
			player.motionZ *= weightCapacity / totalWeight;
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
