package net.machinemuse.powersuits.common;

import org.lwjgl.input.Keyboard;

import net.machinemuse.general.MuseStringUtils;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.item.ModularCommon;
import net.machinemuse.powersuits.powermodule.ModuleManager;
import net.machinemuse.powersuits.powermodule.PowerModule;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
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
	public static final String SEBK_ICON_PATH = "/resource/sebkicons.png";
	public static final String SEBK_ARMOR_PATH = "/resource/sebkarmor.png";
	public static final String SEBK_ARMORPANTS_PATH = "/resource/sebkarmorpants.png";
	public static final String WC_ICON_PATH = "/resource/watericons.png";
	public static final String TINKERTABLE_TEXTURE_PATH = "/resource/tinkertable.png";
	public static final String BLANK_ARMOR_MODEL_PATH = "/resource/blankarmor.png";
	public static final String MUSE_ICON_PATH = "/resource/museicons.png";
	public static final String SEBK_TOOL_TEXTURE = "/resource/tool.png";

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

	public static ItemStack copyAndResize(ItemStack stack, int number) {
		ItemStack copy = stack.copy();
		copy.stackSize = number;
		return copy;
	}

	/**
	 * Load all the modules in the config file into memory. Eventually. For now,
	 * they are hardcoded.
	 */
	public static void loadPowerModules() {
		// loadModularProperties();
		boolean[] ARMORONLY = { true, true, true, true, false };
		boolean[] HEADONLY = { true, false, false, false, false };
		boolean[] TORSOONLY = { false, true, false, false, false };
		boolean[] LEGSONLY = { false, false, true, false, false };
		boolean[] FEETONLY = { false, false, false, true, false };
		boolean[] TOOLONLY = { false, false, false, false, true };
		boolean[] ALLITEMS = { true, true, true, true, true };
		PowerModule module;

		module = new PowerModule(ModularCommon.MODULE_BASIC_PLATING, ARMORONLY, MuseIcon.IRON_PLATING, ModularCommon.CATEGORY_ARMOR)
				.setDescription("Basic plating is heavy but protective.")
				.addInstallCost(copyAndResize(ItemComponent.basicPlating, 1))
				.addTradeoffProperty("Plating Thickness", ModularCommon.ARMOR_VALUE_PHYSICAL, 5, " Points")
				.addTradeoffProperty("Plating Thickness", ModularCommon.WEIGHT, 10000, "g");
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_DIAMOND_PLATING, ARMORONLY, MuseIcon.DIAMOND_PLATING, ModularCommon.CATEGORY_ARMOR)
				.setDescription("Advanced plating is lighter, harder, and more protective than Basic but much harder to make.")
				.addInstallCost(copyAndResize(ItemComponent.advancedPlating, 1))
				.addTradeoffProperty("Plating Thickness", ModularCommon.ARMOR_VALUE_PHYSICAL, 6, " Points")
				.addTradeoffProperty("Plating Thickness", ModularCommon.WEIGHT, 6000, "g");
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_ENERGY_SHIELD, ARMORONLY, MuseIcon.ENERGY_SHIELD, ModularCommon.CATEGORY_ARMOR)
				.setDescription("Energy shields are much lighter than plating, but consume energy.")
				.addInstallCost(copyAndResize(ItemComponent.fieldEmitter, 4))
				.addTradeoffProperty("Field Strength", ModularCommon.ARMOR_VALUE_ENERGY, 6, " Points")
				.addTradeoffProperty("Field Strength", ModularCommon.ARMOR_ENERGY_CONSUMPTION, 500, "J");
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_SHOVEL, TOOLONLY, MuseIcon.TOOL_SHOVEL, ModularCommon.CATEGORY_TOOL)
				.setDescription("Shovels are good for soft materials like dirt and sand.")
				.addInstallCost(new ItemStack(Item.ingotIron, 3))
				.addInstallCost(copyAndResize(ItemComponent.solenoid, 1))
				.addBaseProperty(ModularCommon.SHOVEL_ENERGY_CONSUMPTION, 10, "J")
				.addBaseProperty(ModularCommon.SHOVEL_HARVEST_SPEED, 2, "%")
				.addTradeoffProperty("Overclock", ModularCommon.SHOVEL_ENERGY_CONSUMPTION, 990)
				.addTradeoffProperty("Overclock", ModularCommon.SHOVEL_HARVEST_SPEED, 18);
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_AXE, TOOLONLY, MuseIcon.TOOL_AXE, ModularCommon.CATEGORY_TOOL)
				.setDescription("Axes are mostly for chopping trees.")
				.addInstallCost(new ItemStack(Item.ingotIron, 3))
				.addInstallCost(copyAndResize(ItemComponent.solenoid, 1))
				.addBaseProperty(ModularCommon.AXE_ENERGY_CONSUMPTION, 10, "J")
				.addBaseProperty(ModularCommon.AXE_HARVEST_SPEED, 2, "%")
				.addTradeoffProperty("Overclock", ModularCommon.AXE_ENERGY_CONSUMPTION, 990)
				.addTradeoffProperty("Overclock", ModularCommon.AXE_HARVEST_SPEED, 18);
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_PICKAXE, TOOLONLY, MuseIcon.TOOL_PICK, ModularCommon.CATEGORY_TOOL)
				.setDescription("Picks are good for harder materials like stone and ore.")
				.addInstallCost(new ItemStack(Item.ingotIron, 3))
				.addInstallCost(copyAndResize(ItemComponent.solenoid, 1))
				.addBaseProperty(ModularCommon.PICKAXE_ENERGY_CONSUMPTION, 10, "J")
				.addBaseProperty(ModularCommon.PICKAXE_HARVEST_SPEED, 2, "%")
				.addTradeoffProperty("Overclock", ModularCommon.PICKAXE_ENERGY_CONSUMPTION, 990)
				.addTradeoffProperty("Overclock", ModularCommon.PICKAXE_HARVEST_SPEED, 18);
		ModuleManager.addModule(module);


		module = new PowerModule(ModularCommon.MODULE_DIAMOND_PICK_UPGRADE, TOOLONLY, MuseIcon.DIAMOND_PICK, ModularCommon.CATEGORY_TOOL_ADDON)
				.setDescription("Add diamonds to allow your pickaxe module to mine Obsidian. *REQUIRES PICKAXE MODULE TO WORK*")
				.addInstallCost(copyAndResize(ItemComponent.solenoid, 1))
				.addInstallCost(new ItemStack(Item.diamond, 3));
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_AQUA_AFFINITY, TOOLONLY, MuseIcon.AQUA_AFFINITY, ModularCommon.CATEGORY_TOOL_ADDON)
				.setDescription("Reduces the speed penalty for using your tool underwater.")
				.addInstallCost(copyAndResize(ItemComponent.servoMotor, 1))
				.addBaseProperty(ModularCommon.AQUA_AFFINITY_ENERGY_CONSUMPTION, 0, "J")
				.addBaseProperty(ModularCommon.UNDERWATER_HARVEST_SPEED, 0.2, "%")
				.addTradeoffProperty("Power", ModularCommon.AQUA_AFFINITY_ENERGY_CONSUMPTION, 100)
				.addTradeoffProperty("Power", ModularCommon.UNDERWATER_HARVEST_SPEED, 0.8);
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_BATTERY_BASIC, ALLITEMS, MuseIcon.BATTERY1, ModularCommon.CATEGORY_ENERGY)
				.setDescription("Integrate a battery to allow the item to store energy.")
				.addInstallCost(copyAndResize(ItemComponent.lvcapacitor, 1))
				.addBaseProperty(ModularCommon.MAXIMUM_ENERGY, 20000, "J")
				.addBaseProperty(ModularCommon.WEIGHT, 2000, "g")
				.addTradeoffProperty("Battery Size", ModularCommon.MAXIMUM_ENERGY, 80000)
				.addTradeoffProperty("Battery Size", ModularCommon.WEIGHT, 8000);
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_BATTERY_ADVANCED, ALLITEMS, MuseIcon.BATTERY2, ModularCommon.CATEGORY_ENERGY)
				.setDescription("Integrate a more advanced battery to store more energy.")
				.addInstallCost(copyAndResize(ItemComponent.mvcapacitor, 1))
				.addBaseProperty(ModularCommon.MAXIMUM_ENERGY, 100000, "J")
				.addBaseProperty(ModularCommon.WEIGHT, 2000, "g")
				.addTradeoffProperty("Battery Size", ModularCommon.MAXIMUM_ENERGY, 400000)
				.addTradeoffProperty("Battery Size", ModularCommon.WEIGHT, 8000);
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_BATTERY_ELITE, ALLITEMS, MuseIcon.BATTERYCRYSTAL, ModularCommon.CATEGORY_ENERGY)
				.setDescription("Integrate a the most advanced battery to store an extensive amount of energy.")
				.addInstallCost(copyAndResize(ItemComponent.hvcapacitor, 1))
				.addBaseProperty(ModularCommon.MAXIMUM_ENERGY, 750000, "J")
				.addBaseProperty(ModularCommon.WEIGHT, 2000, "g")
				.addTradeoffProperty("Battery Size", ModularCommon.MAXIMUM_ENERGY, 4250000)
				.addTradeoffProperty("Battery Size", ModularCommon.WEIGHT, 8000);
		ModuleManager.addModule(module);
		
		module = new PowerModule(ModularCommon.MODULE_SPRINT_ASSIST, LEGSONLY, MuseIcon.SPRINT_ASSIST, ModularCommon.CATEGORY_MOVEMENT)
				.setDescription("A set of servo motors to help you sprint (double-tap forward) faster.")
				.addInstallCost(copyAndResize(ItemComponent.servoMotor, 4));
		ModuleManager.addSimpleTradeoff(
				module, "Power",
				ModularCommon.SPRINT_ENERGY_CONSUMPTION, "J", 0, 5,
				ModularCommon.SPRINT_SPEED_MULTIPLIER, "%", 1, 2);
		ModuleManager.addSimpleTradeoff(
				module, "Compensation",
				ModularCommon.SPRINT_ENERGY_CONSUMPTION, "J", 0, 1,
				ModularCommon.SPRINT_FOOD_COMPENSATION, "%", 0, 1);
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_JUMP_ASSIST, LEGSONLY, MuseIcon.JUMP_ASSIST, ModularCommon.CATEGORY_MOVEMENT)
				.setDescription("Another set of servo motors to help you jump higher.")
				.addInstallCost(copyAndResize(ItemComponent.servoMotor, 4));
		ModuleManager.addSimpleTradeoff(
				module, "Power",
				ModularCommon.JUMP_ENERGY_CONSUMPTION, "J", 0, 5,
				ModularCommon.JUMP_MULTIPLIER, "%", 1, 4);
		ModuleManager.addSimpleTradeoff(
				module, "Compensation",
				ModularCommon.JUMP_ENERGY_CONSUMPTION, "J", 0, 1,
				ModularCommon.JUMP_FOOD_COMPENSATION, "%", 0, 1);
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_SHOCK_ABSORBER, FEETONLY, MuseIcon.SHOCK_ABSORBER, ModularCommon.CATEGORY_MOVEMENT)
				.setDescription("With some servos, springs, and padding, you should be able to negate a portion of fall damage.")
				.addInstallCost(copyAndResize(ItemComponent.servoMotor, 2))
				.addInstallCost(new ItemStack(Block.cloth, 2));
		ModuleManager.addSimpleTradeoff(
				module, "Power",
				ModularCommon.SHOCK_ABSORB_ENERGY_CONSUMPTION, "J/m", 0, 1,
				ModularCommon.SHOCK_ABSORB_MULTIPLIER, "%", 0, 1);
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_GLIDER, TORSOONLY, MuseIcon.GLIDER, ModularCommon.CATEGORY_MOVEMENT)
				.setDescription(
						"Tack on some wings to turn downward into forward momentum. Press sneak+forward while falling to activate.")
				.addInstallCost(copyAndResize(ItemComponent.gliderWing, 2));
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_PARACHUTE, TORSOONLY, MuseIcon.PARACHUTE_MODULE, ModularCommon.CATEGORY_MOVEMENT)
				.setDescription("Add a parachute to slow your descent. Activate by pressing sneak (defaults to Shift) in midair.")
				.addInstallCost(copyAndResize(ItemComponent.parachute, 2));
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_JETPACK, TORSOONLY, MuseIcon.JETPACK, ModularCommon.CATEGORY_MOVEMENT)
				.setDescription("A jetpack should allow you to jump indefinitely, or at least until you run out of power.")
				.addInstallCost(copyAndResize(ItemComponent.ionThruster, 4))
				.addBaseProperty(ModularCommon.JET_ENERGY_CONSUMPTION, 0, "J/s")
				.addBaseProperty(ModularCommon.JET_THRUST, 0, "N")
				.addTradeoffProperty("Thrust", ModularCommon.JET_ENERGY_CONSUMPTION, 150)
				.addTradeoffProperty("Thrust", ModularCommon.JET_THRUST, 0.16);
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_JETBOOTS, FEETONLY, MuseIcon.JETBOOTS, ModularCommon.CATEGORY_MOVEMENT)
				.setDescription("Jet boots are not as strong as a jetpack, but they should at least be strong enough to counteract gravity.")
				.addInstallCost(copyAndResize(ItemComponent.ionThruster, 2))
				.addBaseProperty(ModularCommon.JET_ENERGY_CONSUMPTION, 0)
				.addBaseProperty(ModularCommon.JET_THRUST, 0)
				.addTradeoffProperty("Thrust", ModularCommon.JET_ENERGY_CONSUMPTION, 75)
				.addTradeoffProperty("Thrust", ModularCommon.JET_THRUST, 0.08);
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_WATER_ELECTROLYZER, HEADONLY, MuseIcon.WATER_ELECTROLYZER, ModularCommon.CATEGORY_ENVIRONMENTAL)
				.setDescription("When you run out of air, this module will jolt the water around you, electrolyzing a small bubble to breathe from.")
				.addInstallCost(copyAndResize(ItemComponent.lvcapacitor, 1))
				.addBaseProperty(ModularCommon.WATERBREATHING_ENERGY_CONSUMPTION, 1000, "J");
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_SWIM_BOOST, LEGSONLY, MuseIcon.SWIM_BOOST, ModularCommon.CATEGORY_MOVEMENT)
				.setDescription(
						"By refitting an ion thruster for underwater use, you may be able to add extra forward (or backward) thrust when underwater.")
				.addInstallCost(copyAndResize(ItemComponent.ionThruster, 1))
				.addInstallCost(copyAndResize(ItemComponent.solenoid, 2))
				.addTradeoffProperty("Thrust", ModularCommon.SWIM_BOOST_ENERGY_CONSUMPTION, 100, "J")
				.addTradeoffProperty("Thrust", ModularCommon.SWIM_BOOST_AMOUNT, 1, "m/s");
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_CLIMB_ASSIST, LEGSONLY, MuseIcon.STEP_ASSIST, ModularCommon.CATEGORY_MOVEMENT)
				.setDescription("A pair of dedicated servos allow you to effortlessly step up 1m-high ledges.")
				.addInstallCost(copyAndResize(ItemComponent.servoMotor, 2));
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_TINT, ARMORONLY, MuseIcon.NETHERSTAR, ModularCommon.CATEGORY_COSMETIC)
				.setDescription("Give your armor some coloured tinting to customize your armor's appearance.")
				.addInstallCost(copyAndResize(ItemComponent.laserHologram, 1))
				.addTradeoffProperty("Red Intensity", ModularCommon.RED_TINT, 1, "%")
				.addTradeoffProperty("Green Intensity", ModularCommon.GREEN_TINT, 1, "%")
				.addTradeoffProperty("Blue Intensity", ModularCommon.BLUE_TINT, 1, "%");
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_TRANSPARENT_ARMOR, ARMORONLY, MuseIcon.TRANSPARENT_ARMOR, ModularCommon.CATEGORY_COSMETIC)
				.setDescription("Make the item transparent, so you can show off your skin without losing armor.")
				.addInstallCost(copyAndResize(ItemComponent.laserHologram, 1));
		ModuleManager.addModule(module);

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
		GuiSuitManager;
	}
	
	public static Configuration getConfig() {
		return config;
	}

	public static boolean doAdditionalInfo() {
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT){
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
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
