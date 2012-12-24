package machinemuse.powersuits.item;

import java.util.List;

import machinemuse.powersuits.common.Config;
import machinemuse.powersuits.common.Config.Items;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
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
public class ItemPowerTool extends ItemTool implements IModularItem {
	protected List<String> validAugTypes;

	/**
	 * Copied the comment for reference
	 */
	// /** Array of blocks the tool has extra effect against. */
	// private Block[] blocksEffectiveAgainst;
	// public float efficiencyOnProperMaterial = 4.0F;
	//
	// /** Damage versus entities. */
	// public int damageVsEntity;
	//
	// /** The material this tool is made from. */
	// protected EnumToolMaterial toolMaterial;

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
		setIconIndex(Config.Items.PowerTool.iconIndex);
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
	public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block) {
		// for (int var3 = 0; var3 < this.blocksEffectiveAgainst.length; ++var3)
		// {
		// if (this.blocksEffectiveAgainst[var3] == par2Block) {
		// return this.efficiencyOnProperMaterial;
		// }
		// }
		//
		return 1.0F;
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
			EntityLiving par7EntityLiving) {
		if (Block.blocksList[blockID]
				.getBlockHardness(world, x, y, z) != 0.0D) {
			stack.damageItem(1, par7EntityLiving);
		}

		return true;
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
		return this.toolMaterial.getToolCraftingMaterial() == par2ItemStack.itemID ? true
				: super.getIsRepairable(par1ItemStack, par2ItemStack);
	}

	/** FORGE: Overridden to allow custom tool effectiveness */
	@Override
	public float getStrVsBlock(ItemStack stack, Block block, int meta) {
		if (ForgeHooks.isToolEffective(stack, block, meta)) {
			return efficiencyOnProperMaterial;
		}
		return getStrVsBlock(stack, block);
	}

}
