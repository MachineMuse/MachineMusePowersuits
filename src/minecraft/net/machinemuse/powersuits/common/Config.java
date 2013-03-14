package net.machinemuse.powersuits.common;

import java.util.Arrays;
import java.util.List;

import net.machinemuse.api.ElectricItemUtils;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.general.MuseStringUtils;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModule;
import net.machinemuse.powersuits.powermodule.ToggleablePowerModule;
import net.machinemuse.powersuits.powermodule.modules.BlinkDriveModule;
import net.machinemuse.powersuits.powermodule.modules.InPlaceAssemblerModule;
import net.machinemuse.powersuits.powermodule.modules.PlasmaCannonModule;
import net.machinemuse.powersuits.powermodule.modules.RailgunModule;
import net.machinemuse.powersuits.powermodule.modules.SprintAssistModule;
import net.machinemuse.powersuits.powermodule.modules.StepAssistModule;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

/**
 * Initial attempt at storing all tweakable/configurable values in one class.
 * This got really messy really fast so it's in the process of being reworked.
 * 
 * @author MachineMuse
 * 
 */
public class Config {
	public static final String SEBK_ICON_PATH = "/resources/machinemuse/sebkicons.png";
	public static final String SEBK_ARMOR_PATH = "/resources/machinemuse/sebkarmor.png";
	public static final String SEBK_ARMORPANTS_PATH = "/resources/machinemuse/sebkarmorpants.png";
	public static final String WC_ICON_PATH = "/resources/machinemuse/watericons.png";
	public static final String TINKERTABLE_TEXTURE_PATH = "/resources/machinemuse/tinkertable_tx.png";
	public static final String BLANK_ARMOR_MODEL_PATH = "/resources/machinemuse/blankarmor.png";
	public static final String MUSE_ICON_PATH = "/resources/machinemuse/museicons.png";
	public static final String SEBK_TOOL_TEXTURE = "/resources/machinemuse/tool.png";
	public static final String CITIZENJOE_ARMOR_PATH = "/resources/machinemuse/joearmor.png";
	public static final String CITIZENJOE_ARMORPANTS_PATH = "/resources/machinemuse/joearmorpants.png";

	private static final int[] assignedItemIDs = new int[Items.values().length];
	private static final int[] assignedBlockIDs = new int[Blocks.values().length];

	private static Configuration config;

	/**
	 * Called in the pre-init phase of initialization, informs Forge that we
	 * want the following blockIDs.
	 * 
	 * @param config
	 *            The Forge configuration object which will handle such
	 *            requests.
	 */
	public static void init(Configuration config) {
		Config.config = config;
		config.load();

		// Request block IDs
		for (Blocks b : Blocks.values()) {
			assignedBlockIDs[b.ordinal()] =
					config.getBlock(b.englishName, b.defaultBlockId).getInt();
		}

		// Request item IDs
		for (Items i : Items.values()) {
			assignedItemIDs[i.ordinal()] =
					config.getItem(i.englishName, i.defaultItemID).getInt();
		}
		config.save();
	}

	/**
	 * The packet channel for this mod. We will only listen for and send packets
	 * on this 'channel'. Max of 16 characters.
	 * 
	 * @return
	 */
	public static String getNetworkChannelName() {
		return "mmmPowerSuits";
	}

	/**
	 * The default creative tab to add all these items to. This behaviour may
	 * change if more items are added, but for now there are only 5 items and 1
	 * block, so misc is the most appropriate target.
	 * 
	 * @return
	 */
	public static CreativeTabs getCreativeTab() {
		return MuseCreativeTab.instance();
	}

	/**
	 * Chance of each item being returned when salvaged.
	 * 
	 * @return percent chance, 0.0 to 1.0
	 */
	public static double getSalvageChance() {
		return config.get(Configuration.CATEGORY_GENERAL, "Salvage Ratio", 0.9).getDouble(0.9);
	}
	
	/**
	 * The maximum amount of armor contribution allowed per armor piece. Total
	 * armor when the full set is worn can never exceed 4 times this amount.
	 * 
	 * @return
	 */
	public static double getMaximumArmorPerPiece() {
		// Clamp this value between 0 and 6 armor points.
		// The default of 6 will allow 24% reduction per piece.
		return Math.min(6.0, Math.max(0.0, config.get(Configuration.CATEGORY_GENERAL, "Maximum Armor per Piece", 6.0).getDouble(6.0)));
	}

	/**
	 * Whether or not to print debugging info.
	 * 
	 * @return
	 */
	public static boolean isDebugging() {
		return config.get(Configuration.CATEGORY_GENERAL, "Debugging info", false).getBoolean(false);
	}

	/**
	 * Once Forge has assigned IDs for all our items, this function will tell us
	 * what was actually assigned.
	 * 
	 * @param item
	 * @return
	 */
	public static int getAssignedItemID(Items item) {
		if (assignedItemIDs[item.ordinal()] == 0) {
			assignedItemIDs[item.ordinal()] = config.getItem(item.englishName, item.defaultItemID).getInt();
		}
		return assignedItemIDs[item.ordinal()];
	}

	/**
	 * Once Forge has assigned IDs for all our blocks, this function will tell
	 * us what was actually assigned.
	 * 
	 * @param item
	 * @return
	 */
	public static int getAssignedBlockID(Blocks block) {
		if (assignedBlockIDs[block.ordinal()] == 0) {
			assignedBlockIDs[block.ordinal()] = config.getBlock(block.englishName, block.defaultBlockId).getInt();
		}
		return assignedBlockIDs[block.ordinal()];
	}

	/**
	 * Helper function for making recipes. Returns a copy of the itemstack with
	 * the specified stacksize.
	 * 
	 * @param stack
	 *            Itemstack to copy
	 * @param number
	 *            New Stacksize
	 * @return A new itemstack with the specified properties
	 */
	public static ItemStack copyAndResize(ItemStack stack, int number) {
		ItemStack copy = stack.copy();
		copy.stackSize = number;
		return copy;
	}

	public static void addModule(IPowerModule module) {
		ModuleManager.addModule(module);
	}

	/**
	 * Load all the modules in the config file into memory. Eventually. For now,
	 * they are hardcoded.
	 */
	public static void loadPowerModules() {
		// loadModularProperties();
		List<IModularItem> ARMORONLY = Arrays.asList((IModularItem)
				ModularPowersuits.powerArmorHead, ModularPowersuits.powerArmorTorso,
				ModularPowersuits.powerArmorLegs, ModularPowersuits.powerArmorFeet);
		List<IModularItem> ALLITEMS = Arrays.asList((IModularItem)
				ModularPowersuits.powerArmorHead, ModularPowersuits.powerArmorTorso,
				ModularPowersuits.powerArmorLegs, ModularPowersuits.powerArmorFeet,
				ModularPowersuits.powerTool);
		List<IModularItem> HEADONLY = Arrays.asList((IModularItem) ModularPowersuits.powerArmorHead);
		List<IModularItem> TORSOONLY = Arrays.asList((IModularItem) ModularPowersuits.powerArmorTorso);
		List<IModularItem> LEGSONLY = Arrays.asList((IModularItem) ModularPowersuits.powerArmorLegs);
		List<IModularItem> FEETONLY = Arrays.asList((IModularItem) ModularPowersuits.powerArmorFeet);
		List<IModularItem> TOOLONLY = Arrays.asList((IModularItem) ModularPowersuits.powerTool);

		IPowerModule module;

		module = new ToggleablePowerModule(MuseCommonStrings.MODULE_NIGHT_VISION, HEADONLY, MuseIcon.SCANNER, MuseCommonStrings.CATEGORY_SPECIAL)
				.setDescription("A pair of augmented vision goggles to help you see at night and underwater.")
				.addInstallCost(copyAndResize(ItemComponent.laserHologram, 1))
				.addInstallCost(copyAndResize(ItemComponent.controlCircuit, 1));
		addModule(module);

		module = new ToggleablePowerModule(MuseCommonStrings.MODULE_ACTIVE_CAMOUFLAGE, TORSOONLY, MuseIcon.ORB_1_BLUE,
				MuseCommonStrings.CATEGORY_SPECIAL)
				.setDescription("Emit a hologram of your surroundings to make yourself almost imperceptible.")
				.addInstallCost(copyAndResize(ItemComponent.laserHologram, 4))
				.addInstallCost(copyAndResize(ItemComponent.fieldEmitter, 2))
				.addInstallCost(copyAndResize(ItemComponent.controlCircuit, 2));
		addModule(module);

		module = new ToggleablePowerModule(MuseCommonStrings.MODULE_FLIGHT_CONTROL, HEADONLY, MuseIcon.INDICATOR_1_GREEN,
				MuseCommonStrings.CATEGORY_SPECIAL)
				.setDescription("An integrated control circuit to help you fly better. Press Z to go down.")
				.addInstallCost(copyAndResize(ItemComponent.controlCircuit, 1));
		addModule(module);

		addModule(new PlasmaCannonModule(TOOLONLY));

		module = new ToggleablePowerModule(MuseCommonStrings.MODULE_MELEE_ASSIST, TOOLONLY, MuseIcon.PUNCHY, MuseCommonStrings.CATEGORY_WEAPON)
				.setDescription("A much simpler addon, makes your powertool punches hit harder.")
				.addBaseProperty(MuseCommonStrings.PUNCH_ENERGY, 10, "J")
				.addBaseProperty(MuseCommonStrings.PUNCH_DAMAGE, 2, "pt")
				.addTradeoffProperty("Impact", MuseCommonStrings.PUNCH_ENERGY, 100, "J")
				.addTradeoffProperty("Impact", MuseCommonStrings.PUNCH_DAMAGE, 8, "pt")
				.addTradeoffProperty("Carry-through", MuseCommonStrings.PUNCH_ENERGY, 20, "J")
				.addTradeoffProperty("Carry-through", MuseCommonStrings.PUNCH_KNOCKBACK, 1, "P")
				.addInstallCost(copyAndResize(ItemComponent.servoMotor, 2))
				.addInstallCost(copyAndResize(ItemComponent.lvcapacitor, 1));
		addModule(module);

		addModule(new RailgunModule(TOOLONLY));
		addModule(new InPlaceAssemblerModule(TOOLONLY));

		addModule(new BlinkDriveModule(TOOLONLY));

		module = new PowerModule(MuseCommonStrings.MODULE_KINETIC_GENERATOR, LEGSONLY, MuseIcon.NEXUS_1_RED, MuseCommonStrings.CATEGORY_ENERGY)
				.setDescription("Generate power with your movement.")
				.addBaseProperty(MuseCommonStrings.WEIGHT, 1000)
				.addBaseProperty(MuseCommonStrings.KINETIC_ENERGY_GENERATION, 250)
				.addTradeoffProperty("Energy Generated", MuseCommonStrings.KINETIC_ENERGY_GENERATION, 750, " Joules")
				.addTradeoffProperty("Energy Generated", MuseCommonStrings.WEIGHT, 3000, "g")
				.addInstallCost(copyAndResize(ItemComponent.servoMotor, 2))
				.addInstallCost(copyAndResize(ItemComponent.controlCircuit, 1));
		addModule(module);

		module = new PowerModule(MuseCommonStrings.MODULE_SOLAR_GENERATOR, HEADONLY, MuseIcon.NEXUS_1_GREEN, MuseCommonStrings.CATEGORY_ENERGY)
				.setDescription("Let the sun power your adventures.")
				.addBaseProperty(MuseCommonStrings.SOLAR_ENERGY_GENERATION_DAY, 2000)
				.addBaseProperty(MuseCommonStrings.SOLAR_ENERGY_GENERATION_NIGHT, 200)
				.addInstallCost(copyAndResize(ItemComponent.solarPanel, 1))
				.addInstallCost(copyAndResize(ItemComponent.controlCircuit, 2));
		addModule(module);

		module = new ToggleablePowerModule(MuseCommonStrings.MODULE_AUTO_FEEDER, HEADONLY, MuseIcon.NEXUS_1_BLUE,
				MuseCommonStrings.CATEGORY_ENVIRONMENTAL)
				.setDescription(
						"Whenever you're hungry, this module will grab the bottom-left-most food item from your inventory and feed it to you, storing the rest for later.")
				.addBaseProperty(MuseCommonStrings.EATING_ENERGY_CONSUMPTION, 100)
				.addBaseProperty(MuseCommonStrings.EATING_EFFICIENCY, 50)
				.addTradeoffProperty("Efficiency", MuseCommonStrings.EATING_ENERGY_CONSUMPTION, 100)
				.addTradeoffProperty("Efficiency", MuseCommonStrings.EATING_EFFICIENCY, 50)
				.addInstallCost(copyAndResize(ItemComponent.servoMotor, 2))
				.addInstallCost(copyAndResize(ItemComponent.controlCircuit, 1));
		addModule(module);

		module = new PowerModule(MuseCommonStrings.MODULE_BASIC_PLATING, ARMORONLY, MuseIcon.MODULE_IRON_PLATING, MuseCommonStrings.CATEGORY_ARMOR)
				.setDescription("Basic plating is heavy but protective.")
				.addInstallCost(copyAndResize(ItemComponent.basicPlating, 1))
				.addTradeoffProperty("Plating Thickness", MuseCommonStrings.ARMOR_VALUE_PHYSICAL, 5, " Points")
				.addTradeoffProperty("Plating Thickness", MuseCommonStrings.WEIGHT, 10000, "g");
		addModule(module);

		module = new PowerModule(MuseCommonStrings.MODULE_DIAMOND_PLATING, ARMORONLY, MuseIcon.MODULE_DIAMOND_PLATING,
				MuseCommonStrings.CATEGORY_ARMOR)
				.setDescription("Advanced plating is lighter, harder, and more protective than Basic but much harder to make.")
				.addInstallCost(copyAndResize(ItemComponent.advancedPlating, 1))
				.addTradeoffProperty("Plating Thickness", MuseCommonStrings.ARMOR_VALUE_PHYSICAL, 6, " Points")
				.addTradeoffProperty("Plating Thickness", MuseCommonStrings.WEIGHT, 6000, "g");
		addModule(module);

		module = new PowerModule(MuseCommonStrings.MODULE_ENERGY_SHIELD, ARMORONLY, MuseIcon.ENERGY_SHIELD, MuseCommonStrings.CATEGORY_ARMOR)
				.setDescription("Energy shields are much lighter than plating, but consume energy.")
				.addInstallCost(copyAndResize(ItemComponent.fieldEmitter, 2))
				.addTradeoffProperty("Field Strength", MuseCommonStrings.ARMOR_VALUE_ENERGY, 6, " Points")
				.addTradeoffProperty("Field Strength", MuseCommonStrings.ARMOR_ENERGY_CONSUMPTION, 500, "J");
		addModule(module);

		module = new ToggleablePowerModule(MuseCommonStrings.MODULE_SHOVEL, TOOLONLY, MuseIcon.TOOL_SHOVEL, MuseCommonStrings.CATEGORY_TOOL)
				.setDescription("Shovels are good for soft materials like dirt and sand.")
				.addInstallCost(new ItemStack(Item.ingotIron, 3))
				.addInstallCost(copyAndResize(ItemComponent.solenoid, 1))
				.addBaseProperty(MuseCommonStrings.SHOVEL_ENERGY_CONSUMPTION, 50, "J")
				.addBaseProperty(MuseCommonStrings.SHOVEL_HARVEST_SPEED, 8, "x")
				.addTradeoffProperty("Overclock", MuseCommonStrings.SHOVEL_ENERGY_CONSUMPTION, 950)
				.addTradeoffProperty("Overclock", MuseCommonStrings.SHOVEL_HARVEST_SPEED, 22);
		addModule(module);

		module = new ToggleablePowerModule(MuseCommonStrings.MODULE_PICKAXE, TOOLONLY, MuseIcon.TOOL_PICK, MuseCommonStrings.CATEGORY_TOOL)
				.setDescription("Picks are good for harder materials like stone and ore.")
				.addInstallCost(new ItemStack(Item.ingotIron, 3))
				.addInstallCost(copyAndResize(ItemComponent.solenoid, 1))
				.addBaseProperty(MuseCommonStrings.PICKAXE_ENERGY_CONSUMPTION, 50, "J")
				.addBaseProperty(MuseCommonStrings.PICKAXE_HARVEST_SPEED, 8, "x")
				.addTradeoffProperty("Overclock", MuseCommonStrings.PICKAXE_ENERGY_CONSUMPTION, 950)
				.addTradeoffProperty("Overclock", MuseCommonStrings.PICKAXE_HARVEST_SPEED, 22);
		addModule(module);

		module = new ToggleablePowerModule(MuseCommonStrings.MODULE_AXE, TOOLONLY, MuseIcon.TOOL_AXE, MuseCommonStrings.CATEGORY_TOOL)
				.setDescription("Axes are mostly for chopping trees.")
				.addInstallCost(new ItemStack(Item.ingotIron, 3))
				.addInstallCost(copyAndResize(ItemComponent.solenoid, 1))
				.addBaseProperty(MuseCommonStrings.AXE_ENERGY_CONSUMPTION, 50, "J")
				.addBaseProperty(MuseCommonStrings.AXE_HARVEST_SPEED, 8, "x")
				.addTradeoffProperty("Overclock", MuseCommonStrings.AXE_ENERGY_CONSUMPTION, 950)
				.addTradeoffProperty("Overclock", MuseCommonStrings.AXE_HARVEST_SPEED, 22);
		addModule(module);

		module = new PowerModule(MuseCommonStrings.MODULE_DIAMOND_PICK_UPGRADE, TOOLONLY, MuseIcon.DIAMOND_PICK, MuseCommonStrings.CATEGORY_SPECIAL)
				.setDescription("Add diamonds to allow your pickaxe module to mine Obsidian. *REQUIRES PICKAXE MODULE TO WORK*")
				.addInstallCost(copyAndResize(ItemComponent.solenoid, 1))
				.addInstallCost(new ItemStack(Item.diamond, 3));
		addModule(module);

		module = new ToggleablePowerModule(MuseCommonStrings.MODULE_AQUA_AFFINITY, TOOLONLY, MuseIcon.AQUA_AFFINITY,
				MuseCommonStrings.CATEGORY_SPECIAL)
				.setDescription("Reduces the speed penalty for using your tool underwater.")
				.addInstallCost(copyAndResize(ItemComponent.servoMotor, 1))
				.addBaseProperty(MuseCommonStrings.AQUA_AFFINITY_ENERGY_CONSUMPTION, 0, "J")
				.addBaseProperty(MuseCommonStrings.UNDERWATER_HARVEST_SPEED, 0.2, "%")
				.addTradeoffProperty("Power", MuseCommonStrings.AQUA_AFFINITY_ENERGY_CONSUMPTION, 100)
				.addTradeoffProperty("Power", MuseCommonStrings.UNDERWATER_HARVEST_SPEED, 0.8);
		addModule(module);

		module = new PowerModule(MuseCommonStrings.MODULE_BATTERY_BASIC, ALLITEMS, MuseIcon.BATTERY1, MuseCommonStrings.CATEGORY_ENERGY)
				.setDescription("Integrate a battery to allow the item to store energy.")
				.addInstallCost(copyAndResize(ItemComponent.lvcapacitor, 1))
				.addBaseProperty(ElectricItemUtils.MAXIMUM_ENERGY, 20000, "J")
				.addBaseProperty(MuseCommonStrings.WEIGHT, 2000, "g")
				.addTradeoffProperty("Battery Size", ElectricItemUtils.MAXIMUM_ENERGY, 80000)
				.addTradeoffProperty("Battery Size", MuseCommonStrings.WEIGHT, 8000);
		addModule(module);

		module = new PowerModule(MuseCommonStrings.MODULE_BATTERY_ADVANCED, ALLITEMS, MuseIcon.BATTERY2, MuseCommonStrings.CATEGORY_ENERGY)
				.setDescription("Integrate a more advanced battery to store more energy.")
				.addInstallCost(copyAndResize(ItemComponent.mvcapacitor, 1))
				.addBaseProperty(ElectricItemUtils.MAXIMUM_ENERGY, 100000, "J")
				.addBaseProperty(MuseCommonStrings.WEIGHT, 2000, "g")
				.addTradeoffProperty("Battery Size", ElectricItemUtils.MAXIMUM_ENERGY, 400000)
				.addTradeoffProperty("Battery Size", MuseCommonStrings.WEIGHT, 8000);
		addModule(module);

		module = new PowerModule(MuseCommonStrings.MODULE_BATTERY_ELITE, ALLITEMS, MuseIcon.BATTERYCRYSTAL, MuseCommonStrings.CATEGORY_ENERGY)
				.setDescription("Integrate a the most advanced battery to store an extensive amount of energy.")
				.addInstallCost(copyAndResize(ItemComponent.hvcapacitor, 1))
				.addBaseProperty(ElectricItemUtils.MAXIMUM_ENERGY, 750000, "J")
				.addBaseProperty(MuseCommonStrings.WEIGHT, 2000, "g")
				.addTradeoffProperty("Battery Size", ElectricItemUtils.MAXIMUM_ENERGY, 4250000)
				.addTradeoffProperty("Battery Size", MuseCommonStrings.WEIGHT, 8000);
		addModule(module);

		addModule(new SprintAssistModule());

		module = new ToggleablePowerModule(MuseCommonStrings.MODULE_JUMP_ASSIST, LEGSONLY, MuseIcon.JUMP_ASSIST, MuseCommonStrings.CATEGORY_MOVEMENT)
				.setDescription("Another set of servo motors to help you jump higher.")
				.addSimpleTradeoff(
						module, "Power",
						MuseCommonStrings.JUMP_ENERGY_CONSUMPTION, "J", 0, 25,
						MuseCommonStrings.JUMP_MULTIPLIER, "%", 1, 4)
				.addSimpleTradeoff(
						module, "Compensation",
						MuseCommonStrings.JUMP_ENERGY_CONSUMPTION, "J", 0, 5,
						MuseCommonStrings.JUMP_FOOD_COMPENSATION, "%", 0, 1)
				.addInstallCost(copyAndResize(ItemComponent.servoMotor, 4));
		addModule(module);

		module = new ToggleablePowerModule(MuseCommonStrings.MODULE_SHOCK_ABSORBER, FEETONLY, MuseIcon.SHOCK_ABSORBER,
				MuseCommonStrings.CATEGORY_MOVEMENT)
				.setDescription("With some servos, springs, and padding, you should be able to negate a portion of fall damage.")
				.addSimpleTradeoff(
						module, "Power",
						MuseCommonStrings.SHOCK_ABSORB_ENERGY_CONSUMPTION, "J/m", 0, 10,
						MuseCommonStrings.SHOCK_ABSORB_MULTIPLIER, "%", 0, 1)
				.addInstallCost(copyAndResize(ItemComponent.servoMotor, 2))
				.addInstallCost(new ItemStack(Block.cloth, 2));
		addModule(module);

		module = new ToggleablePowerModule(MuseCommonStrings.MODULE_GLIDER, TORSOONLY, MuseIcon.GLIDER, MuseCommonStrings.CATEGORY_MOVEMENT)
				.setDescription(
						"Tack on some wings to turn downward into forward momentum. Press sneak+forward while falling to activate.")
				.addInstallCost(copyAndResize(ItemComponent.gliderWing, 2));
		addModule(module);

		module = new PowerModule(MuseCommonStrings.MODULE_PARACHUTE, TORSOONLY, MuseIcon.PARACHUTE_MODULE, MuseCommonStrings.CATEGORY_MOVEMENT)
				.setDescription("Add a parachute to slow your descent. Activate by pressing sneak (defaults to Shift) in midair.")
				.addInstallCost(copyAndResize(ItemComponent.parachute, 2));
		addModule(module);

		module = new ToggleablePowerModule(MuseCommonStrings.MODULE_JETPACK, TORSOONLY, MuseIcon.JETPACK, MuseCommonStrings.CATEGORY_MOVEMENT)
				.setDescription("A jetpack should allow you to jump indefinitely, or at least until you run out of power.")
				.addInstallCost(copyAndResize(ItemComponent.ionThruster, 4))
				.addBaseProperty(MuseCommonStrings.JET_ENERGY_CONSUMPTION, 0, "J/t")
				.addBaseProperty(MuseCommonStrings.JET_THRUST, 0, "N")
				.addTradeoffProperty("Thrust", MuseCommonStrings.JET_ENERGY_CONSUMPTION, 150)
				.addTradeoffProperty("Thrust", MuseCommonStrings.JET_THRUST, 0.16);
		addModule(module);

		module = new ToggleablePowerModule(MuseCommonStrings.MODULE_JETBOOTS, FEETONLY, MuseIcon.JETBOOTS, MuseCommonStrings.CATEGORY_MOVEMENT)
				.setDescription("Jet boots are not as strong as a jetpack, but they should at least be strong enough to counteract gravity.")
				.addInstallCost(copyAndResize(ItemComponent.ionThruster, 2))
				.addBaseProperty(MuseCommonStrings.JET_ENERGY_CONSUMPTION, 0)
				.addBaseProperty(MuseCommonStrings.JET_THRUST, 0)
				.addTradeoffProperty("Thrust", MuseCommonStrings.JET_ENERGY_CONSUMPTION, 75)
				.addTradeoffProperty("Thrust", MuseCommonStrings.JET_THRUST, 0.08);
		addModule(module);

		module = new ToggleablePowerModule(MuseCommonStrings.MODULE_WATER_ELECTROLYZER, HEADONLY, MuseIcon.WATER_ELECTROLYZER,
				MuseCommonStrings.CATEGORY_ENVIRONMENTAL)
				.setDescription("When you run out of air, this module will jolt the water around you, electrolyzing a small bubble to breathe from.")
				.addInstallCost(copyAndResize(ItemComponent.lvcapacitor, 1))
				.addBaseProperty(MuseCommonStrings.WATERBREATHING_ENERGY_CONSUMPTION, 1000, "J");
		addModule(module);

		module = new ToggleablePowerModule(MuseCommonStrings.MODULE_SWIM_BOOST, LEGSONLY, MuseIcon.SWIM_BOOST, MuseCommonStrings.CATEGORY_MOVEMENT)
				.setDescription(
						"By refitting an ion thruster for underwater use, you may be able to add extra forward (or backward) thrust when underwater.")
				.addInstallCost(copyAndResize(ItemComponent.ionThruster, 1))
				.addInstallCost(copyAndResize(ItemComponent.solenoid, 2))
				.addTradeoffProperty("Thrust", MuseCommonStrings.SWIM_BOOST_ENERGY_CONSUMPTION, 100, "J")
				.addTradeoffProperty("Thrust", MuseCommonStrings.SWIM_BOOST_AMOUNT, 1, "m/s");
		addModule(module);

		addModule(new StepAssistModule(LEGSONLY));

		module = new ToggleablePowerModule(MuseCommonStrings.MODULE_TINT, ALLITEMS, MuseIcon.NETHERSTAR, MuseCommonStrings.CATEGORY_COSMETIC)
				.setDescription("Give your armor some coloured tinting to customize your armor's appearance.")
				.addInstallCost(copyAndResize(ItemComponent.laserHologram, 1))
				.addTradeoffProperty("Red Intensity", MuseCommonStrings.RED_TINT, 1, "%")
				.addTradeoffProperty("Green Intensity", MuseCommonStrings.GREEN_TINT, 1, "%")
				.addTradeoffProperty("Blue Intensity", MuseCommonStrings.BLUE_TINT, 1, "%");
		addModule(module);

		module = new ToggleablePowerModule(MuseCommonStrings.MODULE_TRANSPARENT_ARMOR, ARMORONLY, MuseIcon.TRANSPARENT_ARMOR,
				MuseCommonStrings.CATEGORY_COSMETIC)
				.setDescription("Make the item transparent, so you can show off your skin without losing armor.")
				.addInstallCost(copyAndResize(ItemComponent.laserHologram, 1));
		addModule(module);
		module = new PowerModule(MuseCommonStrings.CITIZEN_JOE_STYLE, ARMORONLY, MuseIcon.ORB_1_GREEN, MuseCommonStrings.CATEGORY_COSMETIC)
				.setDescription("An alternative armor texture, c/o CitizenJoe of IC2 forums.");

		addModule(module);
		
		// Make the maximum armor per piece value show up in config file
		getMaximumArmorPerPiece();
		
		// red = 1, green = 2, blue = 4
	}

	/**
	 * An enum listing all the blocks that this mod adds. Used for assigning IDs
	 * and various other things.
	 * 
	 * @author MachineMuse
	 * 
	 */
	public static enum Blocks {
		TinkerTable(2477, 0, "tinkerTable", "Power Armor Tinker Table");

		public final int defaultBlockId;
		public final int textureIndex;
		public final String idName;
		public final String englishName;

		private Blocks(
				int defaultBlockId, int textureIndex, String idName,
				String englishName) {
			this.defaultBlockId = defaultBlockId;
			this.textureIndex = textureIndex;
			this.idName = idName;
			this.englishName = englishName;
		}

	}

	/**
	 * An enum listing all the items that this mod adds. Used for assigning IDs
	 * and various other things.
	 * 
	 * @author MachineMuse
	 * 
	 */
	public static enum Items {
		// Icon index, ID name, English name, Armor Type
		PowerArmorHead(24770, 0, "powerArmorHead", "Power Armor Head"),
		PowerArmorTorso(24771, 1, "powerArmorTorso", "Power Armor Torso"),
		PowerArmorLegs(24772, 2, "powerArmorLegs", "Power Armor Legs"),
		PowerArmorFeet(24773, 3, "powerArmorFeet", "Power Armor Feet"),
		PowerTool(24774, 4, "powerTool", "Power Tool"),
		PowerArmorComponent(24775, 5, "powerArmorComponent", "Power Armor Component"),

		;

		public final int defaultItemID;
		public final int iconIndex;
		public final String idName;
		public final String englishName;

		Items(int defaultItemID, int iconIndex,
				String idName, String englishName) {
			this.defaultItemID = defaultItemID;
			this.iconIndex = iconIndex;
			this.idName = idName;
			this.englishName = englishName;
		}

	}

	/**
	 * An enum to describe the various GUI windows which can appear. IDs are
	 * less important here since this data isn't saved or synced.
	 * 
	 * @author MachineMuse
	 * 
	 */
	public static enum Guis {
		GuiTinkerTable,
		GuiSuitManager,
		GuiPortableCrafting;
	}

	public static Configuration getConfig() {
		return config;
	}

	public static boolean doAdditionalInfo() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				return true;
			}
		}
		return false;
	}

	public static Object additionalInfoInstructions() {
		String message = "Press SHIFT for more information.";
		message = MuseStringUtils.wrapMultipleFormatTags(
				message,
				MuseStringUtils.FormatCodes.Grey,
				MuseStringUtils.FormatCodes.Italic);
		return message;
	}
}
