package net.machinemuse.powersuits.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.electricity.ElectricItemUtils;
import net.machinemuse.api.electricity.IC2ElectricAdapter;
import net.machinemuse.general.MuseStringUtils;
import net.machinemuse.powersuits.block.BlockTinkerTable;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.item.ItemPowerArmorBoots;
import net.machinemuse.powersuits.item.ItemPowerArmorChestplate;
import net.machinemuse.powersuits.item.ItemPowerArmorHelmet;
import net.machinemuse.powersuits.item.ItemPowerArmorLeggings;
import net.machinemuse.powersuits.item.ItemPowerGauntlet;
import net.machinemuse.powersuits.powermodule.PowerModule;
import net.machinemuse.powersuits.powermodule.ToggleablePowerModule;
import net.machinemuse.powersuits.powermodule.misc.InvisibilityModule;
import net.machinemuse.powersuits.powermodule.misc.NightVisionModule;
import net.machinemuse.powersuits.powermodule.misc.WaterElectrolyzerModule;
import net.machinemuse.powersuits.powermodule.movement.BlinkDriveModule;
import net.machinemuse.powersuits.powermodule.movement.ClimbAssistModule;
import net.machinemuse.powersuits.powermodule.movement.FlightControlModule;
import net.machinemuse.powersuits.powermodule.movement.GliderModule;
import net.machinemuse.powersuits.powermodule.movement.JetBootsModule;
import net.machinemuse.powersuits.powermodule.movement.JetPackModule;
import net.machinemuse.powersuits.powermodule.movement.JumpAssistModule;
import net.machinemuse.powersuits.powermodule.movement.ParachuteModule;
import net.machinemuse.powersuits.powermodule.movement.ShockAbsorberModule;
import net.machinemuse.powersuits.powermodule.movement.SprintAssistModule;
import net.machinemuse.powersuits.powermodule.movement.SwimAssistModule;
import net.machinemuse.powersuits.powermodule.tool.AquaAffinityModule;
import net.machinemuse.powersuits.powermodule.tool.AxeModule;
import net.machinemuse.powersuits.powermodule.tool.DiamondPickUpgradeModule;
import net.machinemuse.powersuits.powermodule.tool.HoeModule;
import net.machinemuse.powersuits.powermodule.tool.PickaxeModule;
import net.machinemuse.powersuits.powermodule.tool.ShearsModule;
import net.machinemuse.powersuits.powermodule.tool.ShovelModule;
import net.machinemuse.powersuits.powermodule.weapon.PlasmaCannonModule;
import net.machinemuse.powersuits.powermodule.weapon.RailgunModule;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

/**
 * Initial attempt at storing all tweakable/configurable values in one class. This got really messy really fast so it's in the process of being
 * reworked.
 * 
 * @author MachineMuse
 * 
 */
public class Config {
	public static final String RESOURCE_PREFIX = "/mods/mmmPowersuits/";
	public static final String TEXTURE_PREFIX = RESOURCE_PREFIX + "textures/";
	public static final String SOUND_PREFIX = RESOURCE_PREFIX + "sound/";
	// public static final String SEBK_ICON_PATH = "/mods/mmmPowersuits//machinemuse/sebkicons.png";
	public static final String SEBK_ARMOR_PATH = TEXTURE_PREFIX + "models/sebkarmor.png";
	public static final String SEBK_ARMORPANTS_PATH = TEXTURE_PREFIX + "models/sebkarmorpants.png";
	// public static final String WC_ICON_PATH = "/resources/machinemuse/watericons.png";
	public static final String TINKERTABLE_TEXTURE_PATH = TEXTURE_PREFIX + "models/tinkertable_tx.png";
	public static final String BLANK_ARMOR_MODEL_PATH = TEXTURE_PREFIX + "models/blankarmor.png";
	// public static final String MUSE_ICON_PATH = "/resources/machinemuse/museicons.png";
	public static final String SEBK_TOOL_TEXTURE = TEXTURE_PREFIX + "models/tool.png";
	public static final String CITIZENJOE_ARMOR_PATH = TEXTURE_PREFIX + "models/joearmor.png";
	public static final String CITIZENJOE_ARMORPANTS_PATH = TEXTURE_PREFIX + "models/joearmorpants.png";

	private static Configuration config;

	/**
	 * Called in the pre-init phase of initialization, informs Forge that we want the following blockIDs.
	 * 
	 * @param config
	 *            The Forge configuration object which will handle such requests.
	 */
	public static void init(Configuration config) {
		Config.config = config;
		config.load();

		BlockTinkerTable.assignedBlockID = config.getBlock("Power Armor Tinker Table", 2477).getInt();

		ItemComponent.assignedItemID = config.getItem("Power Armor Component", 24770).getInt();
		ItemPowerArmorHelmet.assignedItemID = config.getItem("Power Armor Head", 24771).getInt();
		ItemPowerArmorChestplate.assignedItemID = config.getItem("Power Armor Torso", 24772).getInt();
		ItemPowerArmorLeggings.assignedItemID = config.getItem("Power Armor Legs", 24773).getInt();
		ItemPowerArmorBoots.assignedItemID = config.getItem("Power Armor Feet", 24774).getInt();
		ItemPowerGauntlet.assignedItemID = config.getItem("Power Tool", 24775).getInt();

		config.save();
	}

	/**
	 * The packet channel for this mod. We will only listen for and send packets on this 'channel'. Max of 16 characters.
	 * 
	 * @return
	 */
	public static String getNetworkChannelName() {
		return "mmmPowerSuits";
	}

	/**
	 * The default creative tab to add all these items to. This behaviour may change if more items are added, but for now there are only 5 items and 1
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
	 * The maximum amount of armor contribution allowed per armor piece. Total armor when the full set is worn can never exceed 4 times this amount.
	 * 
	 * @return
	 */
	public static double getMaximumArmorPerPiece() {
		// Clamp this value between 0 and 6 armor points.
		// The default of 6 will allow 24% reduction per piece.
		return Math.max(0.0, config.get(Configuration.CATEGORY_GENERAL, "Maximum Armor per Piece", 6.0).getDouble(6.0));
	}

	public static double getMaximumFlyingSpeedmps() {
		return config.get(Configuration.CATEGORY_GENERAL, "Maximum flight speed (in m/s)", 25.0).getDouble(25.0);
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
	 * Helper function for making recipes. Returns a copy of the itemstack with the specified stacksize.
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
	 * Load all the modules in the config file into memory. Eventually. For now, they are hardcoded.
	 */
	public static void loadPowerModules() {
		// loadModularProperties();
		List<IModularItem> ARMORONLY = Arrays.asList((IModularItem) ModularPowersuits.powerArmorHead, ModularPowersuits.powerArmorTorso,
				ModularPowersuits.powerArmorLegs, ModularPowersuits.powerArmorFeet);
		List<IModularItem> ALLITEMS = Arrays.asList((IModularItem) ModularPowersuits.powerArmorHead, ModularPowersuits.powerArmorTorso,
				ModularPowersuits.powerArmorLegs, ModularPowersuits.powerArmorFeet, ModularPowersuits.powerTool);
		List<IModularItem> HEADONLY = Collections.singletonList((IModularItem) ModularPowersuits.powerArmorHead);
		List<IModularItem> TORSOONLY = Collections.singletonList((IModularItem) ModularPowersuits.powerArmorTorso);
		List<IModularItem> LEGSONLY = Collections.singletonList((IModularItem) ModularPowersuits.powerArmorLegs);
		List<IModularItem> FEETONLY = Collections.singletonList((IModularItem) ModularPowersuits.powerArmorFeet);
		List<IModularItem> TOOLONLY = Collections.singletonList((IModularItem) ModularPowersuits.powerTool);

		IPowerModule module;

		// Armor
		module = new PowerModule(MuseCommonStrings.MODULE_BASIC_PLATING, ARMORONLY, "basicplating2", MuseCommonStrings.CATEGORY_ARMOR)
				.setDescription("Basic plating is heavy but protective.")
				.addInstallCost(copyAndResize(ItemComponent.basicPlating, 1))
				.addTradeoffProperty("Plating Thickness", MuseCommonStrings.ARMOR_VALUE_PHYSICAL, 5, " Points")
				.addTradeoffProperty("Plating Thickness", MuseCommonStrings.WEIGHT, 10000, "g");
		addModule(module);

		module = new PowerModule(MuseCommonStrings.MODULE_DIAMOND_PLATING, ARMORONLY, "advancedplating2", MuseCommonStrings.CATEGORY_ARMOR)
				.setDescription("Advanced plating is lighter, harder, and more protective than Basic but much harder to make.")
				.addInstallCost(copyAndResize(ItemComponent.advancedPlating, 1))
				.addTradeoffProperty("Plating Thickness", MuseCommonStrings.ARMOR_VALUE_PHYSICAL, 6, " Points")
				.addTradeoffProperty("Plating Thickness", MuseCommonStrings.WEIGHT, 6000, "g");
		addModule(module);

		module = new PowerModule(MuseCommonStrings.MODULE_ENERGY_SHIELD, ARMORONLY, "energyshield", MuseCommonStrings.CATEGORY_ARMOR)
				.setDescription("Energy shields are much lighter than plating, but consume energy.")
				.addInstallCost(copyAndResize(ItemComponent.fieldEmitter, 2))
				.addTradeoffProperty("Field Strength", MuseCommonStrings.ARMOR_VALUE_ENERGY, 6, " Points")
				.addTradeoffProperty("Field Strength", MuseCommonStrings.ARMOR_ENERGY_CONSUMPTION, 500, "J");
		addModule(module);

		// Tool
		addModule(new AxeModule(TOOLONLY));
		addModule(new PickaxeModule(TOOLONLY));
		addModule(new ShovelModule(TOOLONLY));
		addModule(new ShearsModule(TOOLONLY));
		addModule(new HoeModule(TOOLONLY));

		// Weapon
		module = new ToggleablePowerModule(MuseCommonStrings.MODULE_MELEE_ASSIST, TOOLONLY, "toolfist", MuseCommonStrings.CATEGORY_WEAPON)
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
		addModule(new PlasmaCannonModule(TOOLONLY));
		addModule(new RailgunModule(TOOLONLY));

		// Energy
		module = new PowerModule(MuseCommonStrings.MODULE_BATTERY_BASIC, ALLITEMS, "lvbattery", MuseCommonStrings.CATEGORY_ENERGY)
				.setDescription("Integrate a battery to allow the item to store energy.").addInstallCost(copyAndResize(ItemComponent.lvcapacitor, 1))
				.addBaseProperty(ElectricItemUtils.MAXIMUM_ENERGY, 20000, "J")
				.addBaseProperty(MuseCommonStrings.WEIGHT, 2000, "g")
				.addTradeoffProperty("Battery Size", ElectricItemUtils.MAXIMUM_ENERGY, 80000)
				.addTradeoffProperty("Battery Size", MuseCommonStrings.WEIGHT, 8000)
				.addBaseProperty(IC2ElectricAdapter.IC2_TIER, 1)
				.addTradeoffProperty("IC2 Tier", IC2ElectricAdapter.IC2_TIER, 2);
		addModule(module);

		module = new PowerModule(MuseCommonStrings.MODULE_BATTERY_ADVANCED, ALLITEMS, "mvbattery", MuseCommonStrings.CATEGORY_ENERGY)
				.setDescription("Integrate a more advanced battery to store more energy.")
				.addInstallCost(copyAndResize(ItemComponent.mvcapacitor, 1))
				.addBaseProperty(ElectricItemUtils.MAXIMUM_ENERGY, 100000, "J")
				.addBaseProperty(MuseCommonStrings.WEIGHT, 2000, "g")
				.addTradeoffProperty("Battery Size", ElectricItemUtils.MAXIMUM_ENERGY, 400000)
				.addTradeoffProperty("Battery Size", MuseCommonStrings.WEIGHT, 8000)
				.addBaseProperty(IC2ElectricAdapter.IC2_TIER, 1)
				.addTradeoffProperty("IC2 Tier", IC2ElectricAdapter.IC2_TIER, 2);
		addModule(module);

		module = new PowerModule(MuseCommonStrings.MODULE_BATTERY_ELITE, ALLITEMS, "crystalcapacitor", MuseCommonStrings.CATEGORY_ENERGY)
				.setDescription("Integrate a the most advanced battery to store an extensive amount of energy.")
				.addInstallCost(copyAndResize(ItemComponent.hvcapacitor, 1))
				.addBaseProperty(ElectricItemUtils.MAXIMUM_ENERGY, 750000, "J")
				.addBaseProperty(MuseCommonStrings.WEIGHT, 2000, "g")
				.addTradeoffProperty("Battery Size", ElectricItemUtils.MAXIMUM_ENERGY, 4250000)
				.addTradeoffProperty("Battery Size", MuseCommonStrings.WEIGHT, 8000)
				.addBaseProperty(IC2ElectricAdapter.IC2_TIER, 1)
				.addTradeoffProperty("IC2 Tier", IC2ElectricAdapter.IC2_TIER, 2);
		addModule(module);

		// Movement
		addModule(new ParachuteModule(TORSOONLY));
		addModule(new GliderModule(TORSOONLY));
		addModule(new JetPackModule(TORSOONLY));
		addModule(new SprintAssistModule(LEGSONLY));
		addModule(new JumpAssistModule(LEGSONLY));
		addModule(new SwimAssistModule(LEGSONLY));
		addModule(new ClimbAssistModule(LEGSONLY));
		addModule(new JetBootsModule(FEETONLY));
		addModule(new ShockAbsorberModule(FEETONLY));
		addModule(new WaterElectrolyzerModule(HEADONLY));

		// Special
		addModule(new NightVisionModule(HEADONLY));
		addModule(new FlightControlModule(HEADONLY));
		addModule(new InvisibilityModule(TORSOONLY));
		addModule(new BlinkDriveModule(TOOLONLY));
		addModule(new DiamondPickUpgradeModule(TOOLONLY));
		addModule(new AquaAffinityModule(TOOLONLY));

		// Cosmetic
		module = new ToggleablePowerModule(MuseCommonStrings.MODULE_TINT, ALLITEMS, "netherstar", MuseCommonStrings.CATEGORY_COSMETIC)
				.setDescription("Give your armor some coloured tinting to customize your armor's appearance.")
				.addInstallCost(copyAndResize(ItemComponent.laserHologram, 1))
				.addTradeoffProperty("Red Intensity", MuseCommonStrings.RED_TINT, 1, "%")
				.addTradeoffProperty("Green Intensity", MuseCommonStrings.GREEN_TINT, 1, "%")
				.addTradeoffProperty("Blue Intensity", MuseCommonStrings.BLUE_TINT, 1, "%");
		addModule(module);

		module = new ToggleablePowerModule(MuseCommonStrings.MODULE_TRANSPARENT_ARMOR, ARMORONLY, "transparentarmor",
				MuseCommonStrings.CATEGORY_COSMETIC).setDescription("Make the item transparent, so you can show off your skin without losing armor.")
				.addInstallCost(copyAndResize(ItemComponent.laserHologram, 1));
		addModule(module);
		module = new PowerModule(MuseCommonStrings.CITIZEN_JOE_STYLE, ARMORONLY, "greendrone", MuseCommonStrings.CATEGORY_COSMETIC)
				.setDescription("An alternative armor texture, c/o CitizenJoe of IC2 forums.");
		addModule(module);

	}

	/**
	 * An enum to describe the various GUI windows which can appear. IDs are less important here since this data isn't saved or synced.
	 * 
	 * @author MachineMuse
	 * 
	 */
	public static enum Guis {
		GuiTinkerTable, GuiSuitManager, GuiPortableCrafting;
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
		message = MuseStringUtils.wrapMultipleFormatTags(message, MuseStringUtils.FormatCodes.Grey, MuseStringUtils.FormatCodes.Italic);
		return message;
	}
}
