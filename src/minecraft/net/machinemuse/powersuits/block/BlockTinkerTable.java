package net.machinemuse.powersuits.block;

import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * This is the tinkertable block. It doesn't do much except look pretty
 * (eventually) and provide a way for the player to access the TinkerTable GUI.
 * 
 * @author MachineMuse
 * 
 */
public class BlockTinkerTable extends Block {
	protected int renderType;
	public static int assignedBlockID;
	public static Icon energyIcon;

	public BlockTinkerTable setRenderType(int id) {
		this.renderType = id;
		return this;
	}

	/**
	 * Constructor. Reads all the block info from Config.
	 */
	public BlockTinkerTable() {
		// Block constructor call
		super(
				// Block ID
				assignedBlockID,
				// Material (used for various things like whether it can burn,
				// whether it requires a tool, and whether it can be moved by a
				// piston
				Material.iron);

		// Block's internal/ID name
		// setBlockName(Config.Blocks.TinkerTable.idName);

		// Block's creative tab
		setCreativeTab(Config.getCreativeTab());

		// Block's hardness (base time to harvest it with the correct tool).
		// Sand = 0.5, Stone = 1.5, Ore = 3.0 Obsidian = 20
		setHardness(1.5F);

		// Block's resistance to explosions. Stone = 10, obsidian = 2000
		setResistance(1000.0F);

		// Sound to play when player steps on the block
		setStepSound(Block.soundMetalFootstep);

		// How much light is stopped by this block; 0 for air, 255 for fully
		// opaque.
		setLightOpacity(0);

		// Light level, 0-1. Gets multiplied by 15 and truncated to find the
		// actual light level for the block.
		setLightValue(0.4f);

		// Whether to receive random ticks e.g. plants
		setTickRandomly(false);

		LanguageRegistry.addName(this, "Power Armor Tinker Table");

		// Harvest level for this block. par2 can be pickaxe, axe, or shovel, or
		// a different toolclass. par3 is the minimum level of item required to
		// break it:
		// 0=bare hands, 1=wood, 2=stone, 3=iron, 4=diamond
		MinecraftForge.setBlockHarvestLevel(this, "pickaxe", 0);

		// Register the tile entity, which is only used for rendering at the
		// moment
		GameRegistry.registerTileEntity(TileEntityTinkerTable.class, "tinkerTable");

		// Finally, register the block so that it appears in the game. New
		// standard requires a name to be passed.
		GameRegistry.registerBlock(this, "tinkerTable");

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(MuseIcon.ICON_PREFIX + "energy");
		energyIcon = blockIcon;
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
		if (player.isSneaking()) {
			return false;
		}
		player.openGui(ModularPowersuits.instance, Config.Guis.GuiTinkerTable.ordinal(), world, x, y, z);
		return true;
	}

	/**
	 * returns some value from 0 to 30 or so for different models. Since we're
	 * using a custom renderer, we pass in a completely different ID: the
	 * assigned block ID. It won't conflict with other mods, since Forge looks
	 * it up in a table anyway, but it's still best to have different internal
	 * IDs.
	 */
	@Override
	public int getRenderType() {
		return renderType;
	}

	/**
	 * This method is called on a block after all other blocks gets already
	 * created. You can use it to reference and configure something on the block
	 * that needs the others ones.
	 */
	@Override
	protected void initializeBlock() {
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False
	 * (examples: signs, buttons, stairs, etc)
	 */
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	/**
	 * Returns Returns true if the given side of this block type should be
	 * rendered (if it's solid or not), if the adjacent block is at the given
	 * coordinates. Args: blockAccess, x, y, z, side
	 */
	@Override
	public boolean isBlockSolid(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return true;
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube? This determines whether
	 * or not to render the shared face of two adjacent blocks and also
	 * whether the player can attach torches, redstone wire, etc to this block.
	 */
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	/**
	 * Called throughout the code as a replacement for block instanceof
	 * BlockContainer Moving this to the Block base class allows for mods that
	 * wish
	 * to extend vinella blocks, and also want to have a tile entity on that
	 * block, may.
	 * 
	 * Return true from this function to specify this block has a tile entity.
	 * 
	 * @param metadata
	 *            Metadata of the current block
	 * @return True if block has a tile entity, false otherwise
	 */
	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	/**
	 * Called throughout the code as a replacement for
	 * BlockContainer.getBlockEntity Return the same thing you would from that
	 * function. This will
	 * fall back to BlockContainer.getBlockEntity if this block is a
	 * BlockContainer.
	 * 
	 * @param metadata
	 *            The Metadata of the current block
	 * @return A instance of a class extending TileEntity
	 */
	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileEntityTinkerTable();

	}

	/**
	 * Location aware and overrideable version of the lightOpacity array, return
	 * the number to subtract from the light value when it passes through
	 * this block.
	 * 
	 * This is not guaranteed to have the tile entity in place before this is
	 * called, so it is Recommended that you have your tile entity call relight
	 * after being placed if you rely on it for light info.
	 * 
	 * @param world
	 *            The current world
	 * @param x
	 *            X Position
	 * @param y
	 *            Y Position
	 * @param z
	 *            Z position
	 * @return The amount of light to block, 0 for air, 255 for fully opaque.
	 */
	@Override
	public int getLightOpacity(World world, int x, int y, int z) {
		return 0;
	}
}
