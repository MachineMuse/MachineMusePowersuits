package net.machinemuse.powersuits.item;

import java.util.ArrayList;
import java.util.List;

import net.machinemuse.general.MuseStringUtils;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.Config.Items;
import net.machinemuse.powersuits.powermodule.ModuleManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Describes the modular power tool.
 * 
 * @author MachineMuse
 */
public class ItemPowerTool extends ItemTool
		implements
		IModularItem {
	public static final ItemStack ironPickaxe = new ItemStack(Item.pickaxeSteel);
	public static final ItemStack ironAxe = new ItemStack(Item.axeSteel);
	public static final ItemStack ironShovel = new ItemStack(Item.shovelSteel);
	public static final ItemStack diamondPick = new ItemStack(Item.pickaxeDiamond);

	/**
	 * Constructor. Takes information from the Config.Items enum.
	 */
	public ItemPowerTool() {
		super( // ID
				Config.getAssignedItemID(Config.Items.PowerTool),
				// Damage bonus, added to the material's damage
				0,
				// Tool material, can be changed if necessary
				EnumToolMaterial.EMERALD,
				// not important since it's private and we will override the
				// getter
				new Block[0] // Block[] BlocksEffectiveAgainst
		);
		setMaxStackSize(1);
		setMaxDamage(0);
		this.damageVsEntity = 1;
		setCreativeTab(Config.getCreativeTab());
		setIconIndex(10);
		setTextureFile(MuseIcon.SEBK_ICON_PATH);
		setItemName(Config.Items.PowerTool.idName);
		LanguageRegistry.addName(this, Config.Items.PowerTool.englishName);
	}

	@Override
	public Items getItemType() {
		return Config.Items.PowerTool;
	}

	/**
	 * Returns the strength of the stack against a given block. 1.0F base,
	 * (Quality+1)*2 if correct blocktype, 1.5F if sword
	 */
	@Override
	public float getStrVsBlock(ItemStack stack, Block block) {
		// TODO: Make sure this is right
		return getStrVsBlock(stack, block, 0);
	}

	public static boolean canHarvestBlock(ItemStack stack, Block block, int meta, EntityPlayer player) {
		if (player != null) {
			double energy = ItemUtils.getPlayerEnergy(player);
			if ((ForgeHooks.canToolHarvestBlock(block, meta, ironPickaxe)
					|| block.blockMaterial == Material.iron
					|| block.blockMaterial == Material.anvil
					|| block.blockMaterial == Material.rock)
					&& ItemUtils.itemHasModule(stack, ModularCommon.MODULE_PICKAXE)
					&& energy > ModuleManager.computeModularProperty(stack, ModularCommon.PICKAXE_ENERGY_CONSUMPTION)) {
				return true;
			} else if ((ForgeHooks.canToolHarvestBlock(block, meta, ironShovel)
					|| block == Block.snow)
					&& ItemUtils.itemHasModule(stack, ModularCommon.MODULE_SHOVEL)
					&& energy > ModuleManager.computeModularProperty(stack, ModularCommon.SHOVEL_ENERGY_CONSUMPTION)) {
				return true;
			} else if (ForgeHooks.canToolHarvestBlock(block, meta, ironAxe) && ItemUtils.itemHasModule(stack, ModularCommon.MODULE_AXE)
					&& energy > ModuleManager.computeModularProperty(stack, ModularCommon.AXE_ENERGY_CONSUMPTION)) {
				return true;
			} else if (ForgeHooks.canToolHarvestBlock(block, meta, diamondPick)
					&& ItemUtils.itemHasModule(stack, ModularCommon.MODULE_DIAMOND_PICK_UPGRADE)
					&& energy > ModuleManager.computeModularProperty(stack, ModularCommon.PICKAXE_ENERGY_CONSUMPTION)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/** FORGE: Overridden to allow custom tool effectiveness */
	@Override
	public float getStrVsBlock(ItemStack stack, Block block, int meta) {
		return 1;
	}

	/**
	 * Current implementations of this method in child classes do not use the
	 * entry argument beside stack. They just raise the damage on the stack.
	 */
	@Override
	public boolean hitEntity(ItemStack stack,
			EntityLiving entityDoingHitting, EntityLiving entityBeingHit) {
		// stack.damageItem(2, entityBeingHit);
		return true;
	}

	/**
	 * Called when a block is destroyed using this tool.
	 */
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world,
			int blockID, int x, int y, int z,
			EntityLiving entity) {
		double drain = 1;
		Block block = Block.blocksList[blockID];
		int meta = 0;
		if (ForgeHooks.isToolEffective(diamondPick, block, meta)) {
			drain = ModuleManager.computeModularProperty(stack, ModularCommon.PICKAXE_ENERGY_CONSUMPTION);
		} else if (ForgeHooks.isToolEffective(ironShovel, block, meta)) {
			drain = ModuleManager.computeModularProperty(stack, ModularCommon.SHOVEL_ENERGY_CONSUMPTION);
		} else if (ForgeHooks.isToolEffective(ironAxe, block, meta)) {
			drain = ModuleManager.computeModularProperty(stack, ModularCommon.AXE_ENERGY_CONSUMPTION);
		} else {
			drain = 1;
		}
		if (entity instanceof EntityPlayer) {
			ItemUtils.drainPlayerEnergy((EntityPlayer) entity, drain);
		} else {
			onUse(drain, stack);
		}
		return true;
	}

	public static boolean useIronPickaxe(ItemStack stack, Block block, int meta) {
		if (ItemUtils.itemHasModule(stack, ModularCommon.MODULE_PICKAXE)) {
			if (ForgeHooks.isToolEffective(ironPickaxe, block, meta)) {
				return true;
			} else if (block.blockMaterial == Material.iron
					|| block.blockMaterial == Material.anvil
					|| block.blockMaterial == Material.rock) {
				return true;
			}
		}
		return false;
	}

	public static boolean useIronShovel(ItemStack stack, Block block, int meta) {
		if (ItemUtils.itemHasModule(stack, ModularCommon.MODULE_SHOVEL)) {
			if (ForgeHooks.isToolEffective(ironShovel, block, meta)) {
				return true;
			} else if (block.blockMaterial == Material.snow) {
				return true;
			}
		}
		return false;
	}

	public static boolean useIronAxe(ItemStack stack, Block block, int meta) {
		if (ItemUtils.itemHasModule(stack, ModularCommon.MODULE_AXE)) {
			if (ForgeHooks.isToolEffective(ironAxe, block, meta)) {
				return true;
			} else if (block.blockMaterial == Material.wood
					|| block.blockMaterial == Material.plants
					|| block.blockMaterial == Material.vine) {
				return true;
			}
		}
		return false;
	}

	public static boolean useDiamondPickaxe(ItemStack stack, Block block, int meta) {
		if (ItemUtils.itemHasModule(stack, ModularCommon.MODULE_DIAMOND_PICK_UPGRADE)) {
			if (ForgeHooks.isToolEffective(diamondPick, block, meta)) {
				return true;
			} else if (block.blockMaterial == Material.iron
					|| block.blockMaterial == Material.anvil
					|| block.blockMaterial == Material.rock) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the damage against a given entity.
	 */
	@Override
	public int getDamageVsEntity(Entity par1Entity) {
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Returns True is the item is renderer in full 3D when hold.
	 */
	public boolean isFull3D() {
		return true;
	}

	/**
	 * Return the enchantability factor of the item. In this case, 0. Might add
	 * an enchantability module later :P
	 */
	@Override
	public int getItemEnchantability() {
		return 0;
	}

	/**
	 * Return the name for this tool's material.
	 */
	@Override
	public String getToolMaterialName() {
		return this.toolMaterial.toString();
	}

	/**
	 * Return whether this item is repairable in an anvil.
	 */
	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack,
			ItemStack par2ItemStack) {
		return false;
	}

	public static String formatInfo(String string, double value) {
		return string + "\t" + MuseStringUtils.formatNumberShort(value);
	}

	@Override
	public List<String> getLongInfo(EntityPlayer player, ItemStack stack) {
		List<String> info = new ArrayList();
		NBTTagCompound itemProperties = ItemUtils
				.getMuseItemTag(stack);
		info.add("Detailed Summary");
		info.add("Material\t" + getToolMaterialName());
		info.add(formatInfo("Energy Storage", getMaxJoules(stack)) + "J");
		info.add(formatInfo("Weight", ModularCommon.getTotalWeight(stack)) + "g");
		return info;
	}

	/**
	 * Adds information to the item's tooltip when 'getting' it.
	 * 
	 * @param stack
	 *            The itemstack to get the tooltip for
	 * @param player
	 *            The player (client) viewing the tooltip
	 * @param currentTipList
	 *            A list of strings containing the existing tooltip. When
	 *            passed, it will just contain the name of the item;
	 *            enchantments and lore are appended afterwards.
	 * @param advancedToolTips
	 *            Whether or not the player has 'advanced tooltips' turned on in
	 *            their settings.
	 */
	@Override
	public void addInformation(ItemStack stack,
			EntityPlayer player, List currentTipList, boolean advancedToolTips) {
		ModularCommon.addInformation(stack, player, currentTipList,
				advancedToolTips);
	}

	// /////////////////////////////////////////// //
	// --- UNIVERSAL ELECTRICITY COMPATABILITY --- //
	// /////////////////////////////////////////// //
	@Override
	public double onReceive(double amps, double voltage, ItemStack itemStack) {
		return ModularCommon.onReceive(amps, voltage, itemStack);
	}

	@Override
	public double onUse(double joulesNeeded, ItemStack itemStack) {
		return ModularCommon.onUse(joulesNeeded, itemStack);
	}

	@Override
	public double getJoules(Object... data) {
		return ModularCommon.getJoules(getAsStack(data));
	}

	@Override
	public void setJoules(double joules, Object... data) {
		ModularCommon.setJoules(joules, getAsStack(data));
	}

	@Override
	public double getMaxJoules(Object... data) {
		return ModularCommon.getMaxJoules(getAsStack(data));
	}

	@Override
	public double getVoltage(Object... data) {
		return ModularCommon.getVoltage(getAsStack(data));
	}

	@Override
	public boolean canReceiveElectricity() {
		return true;
	}

	@Override
	public boolean canProduceElectricity() {
		return true;
	}

	/**
	 * Helper function to deal with UE's use of varargs
	 */
	private ItemStack getAsStack(Object[] data) {
		if (data[0] instanceof ItemStack) {
			return (ItemStack) data[0];
		} else {
			throw new IllegalArgumentException(
					"MusePowerSuits: Invalid ItemStack passed via UE interface");
		}
	}

}
