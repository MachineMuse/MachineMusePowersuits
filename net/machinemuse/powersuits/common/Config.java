package net.machinemuse.powersuits.common;

import java.util.HashMap;
import java.util.Map;

import net.machinemuse.powersuits.gui.MuseIcon;
import net.machinemuse.powersuits.item.*;
import net.minecraft.block.Block;
import net.minecraft.block.StepSound;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

/**
 * Initial attempt at storing all tweakable/configurable values in one class.
 * This got really messy really fast so it's in the process of being reworked.
 * 
 * @author MachineMuse
 * 
 */
public class Config extends Configuration {
	private static final int[] assignedItemIDs = new int[Items.values().length];
	private static final int[] assignedBlockIDs = new int[Blocks.values().length];
	private static final Map<String, TinkerAction> tinkerings = new HashMap();

	public static Map<String, TinkerAction> getTinkerings() {
		return tinkerings;
	}

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
					config.getBlock(b.englishName, 1002).getInt();
		}

		// Request item IDs
		for (Items i : Items.values()) {
			assignedItemIDs[i.ordinal()] =
					config.getItem(i.englishName, 5000).getInt();
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
	 * Once Forge has assigned IDs for all our items, this function will tell us
	 * what was actually assigned.
	 * 
	 * @param item
	 * @return
	 */
	public static int getAssignedItemID(Items item) {
		if (assignedItemIDs[item.ordinal()] == 0) {
			assignedItemIDs[item.ordinal()] = config.getItem(item.englishName,
					1002).getInt();
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
			assignedBlockIDs[block.ordinal()] = config.getBlock(
					block.englishName, 5000).getInt();
		}
		return assignedBlockIDs[block.ordinal()];
	}

	public static void addTinkerAction(TinkerAction action) {
		tinkerings.put(action.getName(), action);
	}

	/**
	 * Load all the tinkerings in the config file into memory. Eventually. For
	 * now, they are hardcoded.
	 */
	public static void loadTinkerings() {
		boolean[] ARMORONLY = { true, true, true, true, false };
		boolean[] TOOLONLY = { false, false, false, false, true };
		boolean[] ALLITEMS = { true, true, true, true, true };
		addTinkerAction(new TinkerAction("Add armor plating", ARMORONLY)
				.addCost(new ItemStack(Item.ingotIron))
				.addEffect(
						new TinkerEffectAdditive(
								ModularItemCommon.ARMOR_VALUE, 1.0, 2.0))
				.addEffect(
						new TinkerEffectAdditive(
								ModularItemCommon.ARMOR_DURABILITY, 1.0, 2.0))
				.addEffect(
						new TinkerEffectAdditive(
								ModularItemCommon.ARMOR_WEIGHT, 1.0, 2.0))
				.setDescription(
						"By adding some iron plating, you might be able to make this armor more protective.")
				.setIcon(new MuseIcon("/icons.png", 1)));
		addTinkerAction(new TinkerAction("Lighten armor plating", ARMORONLY)
				.addCost(new ItemStack(Item.lightStoneDust))
				.addEffect(
						new TinkerEffectMultiplicative(
								ModularItemCommon.ARMOR_VALUE, .95, 1))
				.addEffect(
						new TinkerEffectMultiplicative(
								ModularItemCommon.ARMOR_DURABILITY, .95, 1))
				.addEffect(
						new TinkerEffectMultiplicative(
								ModularItemCommon.ARMOR_WEIGHT, .9, .95))
				.addRequirement(
						new TinkerRequirement(ModularItemCommon.ARMOR_VALUE, '>', 2))
				.addRequirement(
						new TinkerRequirement(ModularItemCommon.ARMOR_WEIGHT, '>', 2))
				.setDescription(
						"Using the lightening effects of glowstone, you might be able to reduce the weight of this armor.")
				.setIcon(new MuseIcon("/icons.png", 4)));
		addTinkerAction(new TinkerAction("Install a battery", ALLITEMS)
				.addCost(new ItemStack(Item.redstone))
				.addEffect(
						new TinkerEffectAdditive(
								ModularItemCommon.MAXIMUM_ENERGY, 10000.0, 20000.0))
				.addEffect(
						new TinkerEffectAdditive(
								ModularItemCommon.ARMOR_WEIGHT, 0.5, 1))
				.addRequirement(
						new TinkerRequirement(
								ModularItemCommon.MAXIMUM_ENERGY, '=', 0))
				.setDescription(
						"By adding a battery, you might be able to have a source of energy on hand at all times.")
				.setIcon(new MuseIcon("/icons.png", 5)));
		addTinkerAction(new TinkerAction("Lighten the battery", ALLITEMS)
				.addCost(new ItemStack(Item.redstone))
				.addEffect(
						new TinkerEffectMultiplicative(
								ModularItemCommon.MAXIMUM_ENERGY, .95, 1))
				.addEffect(
						new TinkerEffectMultiplicative(
								ModularItemCommon.ARMOR_WEIGHT, .9, .95))
				.addRequirement(
						new TinkerRequirement(
								ModularItemCommon.MAXIMUM_ENERGY, '>', 10000))
				.setDescription(
						"Using lapis instead of redstone might allow you to store the same amount of energy in a smaller frame.")
				.setIcon(new MuseIcon("/icons.png", 9)));
	}

	/**
	 * An enum listing all the blocks that this mod adds. Used for assigning IDs
	 * and various other things.
	 * 
	 * @author MachineMuse
	 * 
	 */
	public static enum Blocks {
		TinkerTable(1002, 0, "tinkerTable", "Tinker Table",
				80.0F, Material.iron, Block.soundMetalFootstep,
				"pickaxe", 1);

		public final int defaultBlockId;
		public final int textureIndex;
		public final String idName;
		public final String englishName;
		public final float hardness;
		public final Material material;
		public final StepSound stepSound;
		public final String harvestTool;
		public final int harvestLevel;

		private Blocks(
				int defaultBlockId, int textureIndex, String idName,
				String englishName,
				float hardness, Material material,
				StepSound stepSound, String harvestTool, int harvestLevel) {
			this.defaultBlockId = defaultBlockId;
			this.textureIndex = textureIndex;
			this.idName = idName;
			this.englishName = englishName;
			this.hardness = hardness;
			this.material = material;
			this.stepSound = stepSound;
			this.harvestTool = harvestTool;
			this.harvestLevel = harvestLevel;
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
		PowerArmorHead(0, "powerArmorHead", "Power Armor Head"),
		PowerArmorTorso(1, "powerArmorTorso", "Power Armor Torso"),
		PowerArmorLegs(2, "powerArmorLegs", "Power Armor Legs"),
		PowerArmorFeet(3, "powerArmorFeet", "Power Armor Feet"),
		PowerTool(4, "powerTool", "Power Tool"),

		;

		public final int iconIndex;
		public final String idName;
		public final String englishName;

		Items(int iconIndex,
				String idName, String englishName) {
			this.iconIndex = iconIndex;
			this.idName = idName;
			this.englishName = englishName;
		}

	}

	/**
	 * An enum to describe the various GUI windows which can appear. IDs are
	 * less important here since this data isn't saved or synced.
	 * 
	 * @author Claire
	 * 
	 */
	public static enum Guis {
		GuiTinkerTable,
		GuiSuitManager;
	}

}
