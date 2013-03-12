/**
 * 
 */
package net.machinemuse.powersuits.tick;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import net.machinemuse.api.ElectricItemUtils;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPlayerTickModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.general.MuseMathUtils;
import net.machinemuse.powersuits.common.MuseLogger;
import net.machinemuse.powersuits.common.PlayerInputMap;
import net.machinemuse.powersuits.event.MovementManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.FoodStats;
import net.minecraft.util.MathHelper;
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

		double totalEnergyDrain = 0;
		double foodAdjustment = 0;

		World world = player.worldObj;

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
		boolean hasSolarGeneration = false;
		boolean hasKineticGeneration = false;

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
			IModularItem modular = (IModularItem) helmet.getItem();

			hasNightVision = MuseItemUtils.itemHasActiveModule(helmet, MuseCommonStrings.MODULE_NIGHT_VISION);
			hasFlightControl = MuseItemUtils.itemHasActiveModule(helmet, MuseCommonStrings.MODULE_FLIGHT_CONTROL);
			hasFeeder = MuseItemUtils.itemHasActiveModule(helmet, MuseCommonStrings.MODULE_AUTO_FEEDER);
			hasSolarGeneration = MuseItemUtils.itemHasActiveModule(helmet, MuseCommonStrings.MODULE_SOLAR_GENERATOR);
			if (helmet.getTagCompound().hasKey("ench")) {
				helmet.getTagCompound().removeTag("ench");
			}
		}
		if (pants != null && pants.getItem() instanceof IModularItem) {
			hasSprintAssist = MuseItemUtils.itemHasActiveModule(pants, MuseCommonStrings.MODULE_SPRINT_ASSIST);
			hasJumpAssist = MuseItemUtils.itemHasActiveModule(pants, MuseCommonStrings.MODULE_JUMP_ASSIST);
			hasSwimAssist = MuseItemUtils.itemHasActiveModule(pants, MuseCommonStrings.MODULE_SWIM_BOOST);
			hasKineticGeneration = MuseItemUtils.itemHasActiveModule(pants, MuseCommonStrings.MODULE_KINETIC_GENERATOR);
			if (pants.getTagCompound().hasKey("ench")) {
				pants.getTagCompound().removeTag("ench");
			}
		}
		if (boots != null && boots.getItem() instanceof IModularItem) {
			hasJetboots = MuseItemUtils.itemHasActiveModule(boots, MuseCommonStrings.MODULE_JETBOOTS);
			if (boots.getTagCompound().hasKey("ench")) {
				boots.getTagCompound().removeTag("ench");
			}
		}
		if (torso != null && torso.getItem() instanceof IModularItem) {
			hasInvis = MuseItemUtils.itemHasActiveModule(torso, MuseCommonStrings.MODULE_ACTIVE_CAMOUFLAGE);
			hasJetpack = MuseItemUtils.itemHasActiveModule(torso, MuseCommonStrings.MODULE_JETPACK);
			hasGlider = MuseItemUtils.itemHasActiveModule(torso, MuseCommonStrings.MODULE_GLIDER);
			hasParachute = MuseItemUtils.itemHasActiveModule(torso, MuseCommonStrings.MODULE_PARACHUTE);
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
				double swimAssistRate = ModuleManager.computeModularProperty(pants, MuseCommonStrings.SWIM_BOOST_AMOUNT) * 0.05;
				double swimEnergyConsumption = ModuleManager.computeModularProperty(pants, MuseCommonStrings.SWIM_BOOST_ENERGY_CONSUMPTION);
				if (swimEnergyConsumption + totalEnergyDrain < totalEnergy) {
					totalEnergyDrain += swimEnergyConsumption;

					// if (swimTicker == 0) {
					// world.playSoundAtEntity(player,
					// MuseCommonStrings.SOUND_SWIM_ASSIST, 2.0F, 1.0F);
					// swimTicker++;
					// }
					// else {
					// swimTicker++;
					// if (swimTicker >= 60) {
					// swimTicker = 0;
					// }
					// }
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
					jetEnergy += ModuleManager.computeModularProperty(torso, MuseCommonStrings.JET_ENERGY_CONSUMPTION);
					thrust += ModuleManager.computeModularProperty(torso, MuseCommonStrings.JET_THRUST);
				}
				if (hasJetboots) {
					jetEnergy += ModuleManager.computeModularProperty(boots, MuseCommonStrings.JET_ENERGY_CONSUMPTION);
					thrust += ModuleManager.computeModularProperty(boots, MuseCommonStrings.JET_THRUST);
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

					// if (gliderTicker == 0) {
					// world.playSoundAtEntity(player,
					// MuseCommonStrings.SOUND_GLIDER, 5.0F, 1.0F);
					// gliderTicker++;
					// }
					// else {
					// gliderTicker++;
					// if (gliderTicker >= 35) {
					// gliderTicker = 0;
					// }
					// }
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
			if (hasSprintAssist) {}
		}
		// Food Module
		if (hasFeeder) {
			IInventory inv = player.inventory;
			double foodLevel = (double) MuseItemUtils.getFoodLevel(helmet);
			double saturationLevel = MuseItemUtils.getSaturationLevel(helmet);
			double efficiency = ModuleManager.computeModularProperty(helmet, MuseCommonStrings.EATING_EFFICIENCY);
			FoodStats foodStats = player.getFoodStats();
			int foodNeeded = 20 - foodStats.getFoodLevel();
			double eatingEnergyConsumption = foodNeeded * ModuleManager.computeModularProperty(helmet, MuseCommonStrings.EATING_ENERGY_CONSUMPTION);
			for (int i = 0; i < inv.getSizeInventory() && foodNeeded > foodLevel; i++) {
				ItemStack stack = inv.getStackInSlot(i);
				if (stack != null && stack.getItem() instanceof ItemFood) {
					ItemFood food = (ItemFood) stack.getItem();
					for (; stack.stackSize > 0 && foodNeeded > foodLevel; stack.stackSize--) {
						foodLevel += food.getHealAmount() * efficiency / 100.0;
						saturationLevel += food.getSaturationModifier() * efficiency / 100.0;
						player.sendChatToPlayer("Feeder module: Ate a " + food.getItemNameIS(stack));
					}
					if (stack.stackSize == 0) {
						player.inventory.setInventorySlotContents(i, null);
					}
				}
			}
			int foodConsumed = (int) Math.min(foodNeeded, Math.min(foodLevel, eatingEnergyConsumption * (totalEnergy - totalEnergyDrain)));
			if (foodConsumed > 0) {
				if (saturationLevel >= 1.0F) {
					foodStats.addStats(foodConsumed, 1.0F);
					saturationLevel -= 1.0F;
				}
				else {
					foodStats.addStats(foodConsumed, 0.0F);
				}
				foodLevel -= foodConsumed;
				totalEnergyDrain += eatingEnergyConsumption * foodConsumed;
			}
			MuseItemUtils.setFoodLevel(helmet, foodLevel);
			MuseItemUtils.setSaturationLevel(helmet, saturationLevel);
		}
		// Solar Generator
		if (hasSolarGeneration) {
			int xCoord = MathHelper.floor_double(player.posX);
			int zCoord = MathHelper.floor_double(player.posZ);
			boolean isRaining = false, canRain = true;
			if (world.getTotalWorldTime() % 20 == 0) {
				canRain = world.getWorldChunkManager().getBiomeGenAt(xCoord, zCoord).getIntRainfall() > 0;
			}

			isRaining = canRain && (world.isRaining() || world.isThundering());
			// Make sure you're not in desert - Thanks cpw :P
			boolean sunVisible = world.isDaytime() && !isRaining && world.canBlockSeeTheSky(xCoord, MathHelper.floor_double(player.posY) + 1, zCoord);
			boolean moonVisible = !world.isDaytime() && !isRaining
					&& world.canBlockSeeTheSky(xCoord, MathHelper.floor_double(player.posY) + 1, zCoord);
			if (!world.isRemote && !world.provider.hasNoSky && (world.getTotalWorldTime() % 80) == 0) {
				if (sunVisible) {
					ElectricItemUtils.givePlayerEnergy(player,
							ModuleManager.computeModularProperty(helmet, MuseCommonStrings.SOLAR_ENERGY_GENERATION_DAY));
				}
				else if (moonVisible) {
					ElectricItemUtils.givePlayerEnergy(player,
							ModuleManager.computeModularProperty(helmet, MuseCommonStrings.SOLAR_ENERGY_GENERATION_NIGHT));
				}
			}
		}
		// Static Generator
		if (hasKineticGeneration && !player.isAirBorne) {
			NBTTagCompound tag = MuseItemUtils.getMuseItemTag(pants);
			boolean isNotWalking = (player.ridingEntity != null) || (player.isInWater());
			if ((!tag.hasKey("x")) || (isNotWalking))
				tag.setInteger("x", (int) player.posX);
			if ((!tag.hasKey("z")) || (isNotWalking))
				tag.setInteger("z", (int) player.posZ);
			double distance = Math.sqrt((tag.getInteger("x") - (int) player.posX) * (tag.getInteger("x") - (int) player.posX)
					+ (tag.getInteger("z") - (int) player.posZ) * (tag.getInteger("z") - (int) player.posZ));
			if (distance >= 5.0D) {
				tag.setInteger("x", (int) player.posX);
				tag.setInteger("z", (int) player.posZ);
				ElectricItemUtils.givePlayerEnergy(player, ModuleManager.computeModularProperty(pants, MuseCommonStrings.KINETIC_ENERGY_GENERATION));
			}
		}

		// Update fall distance for damage, energy drain, and
		// exhaustion this tick

		if (totalEnergyDrain > 0) {
			ElectricItemUtils.drainPlayerEnergy(player, totalEnergyDrain);
		} else {
			ElectricItemUtils.givePlayerEnergy(player, -totalEnergyDrain);
		}

		player.getFoodStats().addExhaustion((float) (-foodAdjustment));
		player.fallDistance = (float) MovementManager.computeFallHeightFromVelocity(MuseMathUtils.clampDouble(player.motionY, -1000.0, 0.0));

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
