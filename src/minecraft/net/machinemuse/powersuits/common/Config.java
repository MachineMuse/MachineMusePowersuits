package net.machinemuse.powersuits.common;

import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.item.ModularCommon;
import net.machinemuse.powersuits.powermodule.ModuleManager;
import net.machinemuse.powersuits.powermodule.PowerModule;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import basiccomponents.common.BasicComponents;

/**
 * Initial attempt at storing all tweakable/configurable values in one class.
 * This got really messy really fast so it's in the process of being reworked.
 * 
 * @author MachineMuse
 * 
 */
public class Config extends Configuration {
	public static final String SEBK_ICON_PATH = "/resource/sebkicons.png";
	public static final String SEBK_ARMOR_PATH = "/resource/sebkarmor.png";
	public static final String SEBK_ARMORPANTS_PATH = "/resource/sebkarmorpants.png";
	public static final String WC_ICON_PATH = "/resource/watericons.png";
	public static final String TINKERTABLE_TEXTURE_PATH = "/resource/tinkertable.png";
	public static final String BLANK_ARMOR_MODEL_PATH = "/resource/blankarmor.png";

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
		return CreativeTabs.tabMisc;
	}

	/**
	 * Chance of each item being returned when salvaged.
	 * 
	 * @return percent chance, 0.0 to 1.0
	 */
	public static double getSalvageChance() {
		return 0.9;
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

		module = new PowerModule(ModularCommon.MODULE_IRON_PLATING, ARMORONLY, MuseIcon.ORB_1_GREEN, ModularCommon.CATEGORY_ARMOR)
				.setDescription("Iron plating is heavy but protective.")
				.addInstallCost(new ItemStack(Item.ingotIron, 5))
				.addTradeoffProperty("Plating Thickness", ModularCommon.ARMOR_VALUE_PHYSICAL, 5)
				.addTradeoffProperty("Plating Thickness", ModularCommon.WEIGHT, 10000);
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_DIAMOND_PLATING, ARMORONLY, MuseIcon.ORB_1_BLUE, ModularCommon.CATEGORY_ARMOR)
				.setDescription("Diamonds are lighter, harder, and more protective than Iron but much harder to find.")
				.addInstallCost(new ItemStack(Item.diamond, 5))
				.addTradeoffProperty("Plating Thickness", ModularCommon.ARMOR_VALUE_PHYSICAL, 6)
				.addTradeoffProperty("Plating Thickness", ModularCommon.WEIGHT, 6000);
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_ENERGY_SHIELD, ARMORONLY, MuseIcon.ENERGY_SHIELD, ModularCommon.CATEGORY_ARMOR)
				.setDescription("Energy shields are much lighter than plating, but consume energy.")
				.addInstallCost(new ItemStack(BasicComponents.itemCircuit, 3, 1))
				.addTradeoffProperty("Field Strength", ModularCommon.ARMOR_VALUE_ENERGY, 6)
				.addTradeoffProperty("Field Strength", ModularCommon.ARMOR_ENERGY_CONSUMPTION, 500);
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_SHOVEL, TOOLONLY, MuseIcon.TOOL_SHOVEL, ModularCommon.CATEGORY_TOOL)
				.setDescription("Shovels are good for soft materials like dirt and sand.")
				.addInstallCost(new ItemStack(Item.ingotIron, 3))
				.addBaseProperty(ModularCommon.SHOVEL_ENERGY_CONSUMPTION, 10)
				.addBaseProperty(ModularCommon.SHOVEL_HARVEST_SPEED, 2)
				.addTradeoffProperty("Overclock", ModularCommon.SHOVEL_ENERGY_CONSUMPTION, 990)
				.addTradeoffProperty("Overclock", ModularCommon.SHOVEL_HARVEST_SPEED, 18);
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_AXE, TOOLONLY, MuseIcon.TOOL_AXE, ModularCommon.CATEGORY_TOOL)
				.setDescription("Axes are mostly for chopping trees.")
				.addInstallCost(new ItemStack(Item.ingotIron, 3))
				.addBaseProperty(ModularCommon.AXE_ENERGY_CONSUMPTION, 10)
				.addBaseProperty(ModularCommon.AXE_HARVEST_SPEED, 2)
				.addTradeoffProperty("Overclock", ModularCommon.AXE_ENERGY_CONSUMPTION, 990)
				.addTradeoffProperty("Overclock", ModularCommon.AXE_HARVEST_SPEED, 18);
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_PICKAXE, TOOLONLY, MuseIcon.TOOL_PICK, ModularCommon.CATEGORY_TOOL)
				.setDescription("Picks are good for harder materials like stone and ore.")
				.addInstallCost(new ItemStack(Item.ingotIron, 3))
				.addBaseProperty(ModularCommon.PICKAXE_ENERGY_CONSUMPTION, 10)
				.addBaseProperty(ModularCommon.PICKAXE_HARVEST_SPEED, 2)
				.addTradeoffProperty("Overclock", ModularCommon.PICKAXE_ENERGY_CONSUMPTION, 990)
				.addTradeoffProperty("Overclock", ModularCommon.PICKAXE_HARVEST_SPEED, 18);
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_BATTERY_BASIC, ALLITEMS, MuseIcon.NEXUS_1_GREEN, ModularCommon.CATEGORY_ENERGY)
				.setDescription("Integrate a battery to allow the item to store energy.")
				.addInstallCost(new ItemStack(BasicComponents.itemBattery, 1));
		ModuleManager.addSimpleTradeoff(
				module, "Battery Size",
				ModularCommon.MAXIMUM_ENERGY, "J", 20000, 80000,
				ModularCommon.WEIGHT, "g", 2000, 8000);
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_DIAMOND_PICK_UPGRADE, TOOLONLY, MuseIcon.INDICATOR_1_BLUE, ModularCommon.CATEGORY_TOOL)
				.setDescription("Add diamonds to allow your pickaxe module to mine Obsidian.")
				.addInstallCost(new ItemStack(Item.diamond, 3));
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_TRANSPARENT_ARMOR, ARMORONLY, MuseIcon.PLATE_2_GREEN, ModularCommon.CATEGORY_COSMETIC)
				.setDescription("Show some skin.")
				.addInstallCost(new ItemStack(Block.glass, 3));
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_SPRINT_ASSIST, LEGSONLY, MuseIcon.SPRINT_ASSIST, ModularCommon.CATEGORY_MOVEMENT)
				.setDescription("A set of servo motors to help you sprint (double-tap forward) faster.")
				.addInstallCost(new ItemStack(BasicComponents.itemMotor, 4));
		ModuleManager.addSimpleTradeoff(
				module, "Power",
				ModularCommon.SPRINT_ENERGY_CONSUMPTION, "J", 0, 100,
				ModularCommon.SPRINT_SPEED_MULTIPLIER, "x", 1, 2);
		ModuleManager.addSimpleTradeoff(
				module, "Compensation",
				ModularCommon.SPRINT_ENERGY_CONSUMPTION, "J", 0, 10,
				ModularCommon.SPRINT_FOOD_COMPENSATION, "x", 0, 1);
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_JUMP_ASSIST, LEGSONLY, MuseIcon.JUMP_ASSIST, ModularCommon.CATEGORY_MOVEMENT)
				.setDescription("Another set of servo motors to help you jump higher.")
				.addInstallCost(new ItemStack(BasicComponents.itemMotor, 4));
		ModuleManager.addSimpleTradeoff(
				module, "Power",
				ModularCommon.JUMP_ENERGY_CONSUMPTION, "J", 0, 100,
				ModularCommon.JUMP_MULTIPLIER, "x", 1, 2);
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_SHOCK_ABSORBER, FEETONLY, MuseIcon.PLATE_1_RED, ModularCommon.CATEGORY_MOVEMENT)
				.setDescription("With some servos, springs, and padding, you should be able to negate a portion of fall damage.")
				.addInstallCost(new ItemStack(BasicComponents.itemMotor, 2))
				.addInstallCost(new ItemStack(Block.cloth, 2));
		ModuleManager.addSimpleTradeoff(
				module, "Power",
				ModularCommon.SHOCK_ABSORB_ENERGY_CONSUMPTION, "J", 0, 10,
				ModularCommon.SHOCK_ABSORB_MULTIPLIER, "x", 0, 1);
		ModuleManager.addModule(module);

		module = new PowerModule(ModularCommon.MODULE_GLIDER, TORSOONLY, MuseIcon.GLIDER, ModularCommon.CATEGORY_MOVEMENT)
				.setDescription("Tack on some wings so you can slow your fall and maybe fly a bit if you jump from a decent height.")
				.addInstallCost(new ItemStack(BasicComponents.itemSteelPlate, 2));
		ModuleManager.addModule(module);
	}

	/**
	 * An enum listing all the blocks that this mod adds. Used for assigning IDs
	 * and various other things.
	 * 
	 * @author MachineMuse
	 * 
	 */
	public static enum Blocks {
		TinkerTable(2477, 0, "tinkerTable", "Tinker Table");

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

}
