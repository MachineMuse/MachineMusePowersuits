package machinemuse.powersuits.common;

import machinemuse.powersuits.common.block.BlockTinkerTable;
import net.minecraft.block.Block;
import net.minecraft.block.StepSound;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.registry.GameRegistry;

public class Config extends Configuration {
	private static final int[] assignedItemIDs = new int[Items.values().length];
	private static final int[] assignedBlockIDs = new int[Blocks.values().length];
	private static Configuration config;

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

	public static String getModuleNBTTagPrefix() {
		return "mmmpsmod";
	}

	public static String getNetworkChannelName() {
		return "mmmPowerSuits";
	}

	public static CreativeTabs getCreativeTab() {
		return CreativeTabs.tabMisc;
	}

	public static int getAssignedItemID(Items item) {
		if (assignedItemIDs[item.ordinal()] == 0) {
			assignedItemIDs[item.ordinal()] = config.getItem(item.englishName,
					1002).getInt();
		}
		return assignedItemIDs[item.ordinal()];
	}

	public static int getAssignedBlockID(Blocks block) {
		if (assignedBlockIDs[block.ordinal()] == 0) {
			assignedBlockIDs[block.ordinal()] = config.getBlock(
					block.englishName, 5000).getInt();
		}
		return assignedBlockIDs[block.ordinal()];
	}

	public static int RegisterRecipes() {
		ItemStack iron = new ItemStack(Item.ingotIron);
		ItemStack lapis = new ItemStack(Item.dyePowder, 1);
		GameRegistry.addRecipe(new ItemStack(new BlockTinkerTable()), " I ",
				"ILI",
				" I ", 'I', iron, 'L', lapis);
		return 0;
	}

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
}
