package net.machinemuse.powersuits.block;

import java.util.List;

import net.machinemuse.general.MuseStringUtils;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * This is the tinkertable block. It doesn't do much except look pretty
 * (eventually) and provide a way for the player to access the TinkerTable GUI.
 * 
 * @author MachineMuse
 * 
 */
public class BlockTinkerTable extends Block {
	protected int renderType;

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
				Config.getAssignedBlockID(Config.Blocks.TinkerTable),
				// Texture index (not used since we have a custom renderer)
				Config.Blocks.TinkerTable.textureIndex,
				// Material (used for various things like whether it can burn,
				// whether it requires a tool, and whether it can be moved by a
				// piston
				Material.iron);

		// Block's internal/ID name
		setBlockName(Config.Blocks.TinkerTable.idName);

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

		LanguageRegistry.addName(this, Config.Blocks.TinkerTable.englishName);

		// Harvest level for this block. par2 can be pickaxe, axe, or shovel, or
		// a different toolclass. par3 is the minimum level of item required to
		// break it:
		// 0=bare hands, 1=wood, 2=stone, 3=iron, 4=diamond
		MinecraftForge.setBlockHarvestLevel(this, "pickaxe", 0);

		// Register the tile entity, which is only used for rendering at the
		// moment
		GameRegistry.registerTileEntity(TileEntityTinkerTable.class,
				Config.Blocks.TinkerTable.idName);

		// Finally, register the block so that it appears in the game. New
		// standard requires a name to be passed.
		GameRegistry.registerBlock(this, Config.Blocks.TinkerTable.idName);

	}
	
	/**
	 * Called upon block activation (right click on the block.)
	 */
	@Override
	public boolean onBlockActivated(World world, int x, int y,
			int z, EntityPlayer player, int par6, float par7,
			float par8, float par9)
	{
		if (player.isSneaking()) {
			return false;
		}
		player.openGui(ModularPowersuits.instance,
				Config.Guis.GuiTinkerTable.ordinal(),
				world, x, y, z);
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
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	// public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2,
	// int par3, int par4)
	// {
	// return !this.blockMaterial.blocksMovement();
	// }
	//
	//
	// /**
	// * Sets how many hits it takes to break a block.
	// */
	// @Override
	// public Block setHardness(float par1)
	// {
	// this.blockHardness = par1;
	//
	// if (this.blockResistance < par1 * 5.0F)
	// {
	// this.blockResistance = par1 * 5.0F;
	// }
	//
	// return this;
	// }
	//
	// /**
	// * This method will make the hardness of the block equals to -1, and the
	// block is indestructible.
	// */
	// @Override
	// public Block setBlockUnbreakable()
	// {
	// this.setHardness(-1.0F);
	// return this;
	// }
	//
	// /**
	// * Returns the block hardness at a location. Args: world, x, y, z
	// */
	// @Override
	// public float getBlockHardness(World par1World, int par2, int par3, int
	// par4)
	// {
	// return this.blockHardness;
	// }
	//
	// @Override
	// @Deprecated //Forge: New Metadata sensitive version.
	// public boolean hasTileEntity()
	// {
	// return hasTileEntity(0);
	// }
	//
	// /**
	// * Sets the bounds of the block. minX, minY, minZ, maxX, maxY, maxZ
	// */
	// @Override
	// public final void setBlockBounds(float par1, float par2, float par3,
	// float par4, float par5, float par6)
	// {
	// this.minX = par1;
	// this.minY = par2;
	// this.minZ = par3;
	// this.maxX = par4;
	// this.maxY = par5;
	// this.maxZ = par6;
	// }
	//
	// @SideOnly(Side.CLIENT)
	//
	// /**
	// * How bright to render this block based on the light its receiving. Args:
	// iBlockAccess, x, y, z
	// */
	// public float getBlockBrightness(IBlockAccess par1IBlockAccess, int par2,
	// int par3, int par4)
	// {
	// return par1IBlockAccess.getBrightness(par2, par3, par4,
	// getLightValue(par1IBlockAccess, par2, par3, par4));
	// }
	//
	// @SideOnly(Side.CLIENT)
	//
	// /**
	// * Goes straight to getLightBrightnessForSkyBlocks for Blocks, does some
	// fancy computing for Fluids
	// */
	// public int getMixedBrightnessForBlock(IBlockAccess par1IBlockAccess, int
	// par2, int par3, int par4)
	// {
	// return par1IBlockAccess.getLightBrightnessForSkyBlocks(par2, par3, par4,
	// getLightValue(par1IBlockAccess, par2, par3, par4));
	// }
	//
	// @SideOnly(Side.CLIENT)
	//
	// /**
	// * Returns true if the given side of this block type should be rendered,
	// if the adjacent block is at the given
	// * coordinates. Args: blockAccess, x, y, z, side
	// */
	// public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int
	// par2, int par3, int par4, int par5)
	// {
	// return par5 == 0 && this.minY > 0.0D ? true : (par5 == 1 && this.maxY <
	// 1.0D ? true : (par5 == 2 && this.minZ > 0.0D ? true : (par5 == 3 &&
	// this.maxZ < 1.0D ? true : (par5 == 4 && this.minX > 0.0D ? true : (par5
	// == 5 && this.maxX < 1.0D ? true :
	// !par1IBlockAccess.isBlockOpaqueCube(par2, par3, par4))))));
	// }

	/**
	 * Returns Returns true if the given side of this block type should be
	 * rendered (if it's solid or not), if the adjacent block is at the given
	 * coordinates. Args: blockAccess, x, y, z, side
	 */
	@Override
	public boolean isBlockSolid(IBlockAccess par1IBlockAccess, int par2, int
			par3, int par4, int par5)
	{
		return true;
	}

	// @SideOnly(Side.CLIENT)
	//
	// /**
	// * Retrieves the block texture to use based on the display side. Args:
	// iBlockAccess, x, y, z, side
	// */
	// public int getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int
	// par3, int par4, int par5)
	// {
	// return this.getBlockTextureFromSideAndMetadata(par5,
	// par1IBlockAccess.getBlockMetadata(par2, par3, par4));
	// }
	//
	// /**
	// * From the specified side and block metadata retrieves the blocks
	// texture. Args: side, metadata
	// */
	// @Override
	// public int getBlockTextureFromSideAndMetadata(int par1, int par2)
	// {
	// return this.getBlockTextureFromSide(par1);
	// }
	//
	// /**
	// * Returns the block texture based on the side being looked at. Args: side
	// */
	// @Override
	// public int getBlockTextureFromSide(int par1)
	// {
	// return this.blockIndexInTexture;
	// }
	//
	// /**
	// * if the specified block is in the given AABB, add its collision bounding
	// box to the given list
	// */
	// public void addCollidingBlockToList(World par1World, int par2, int par3,
	// int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity
	// par7Entity)
	// {
	// AxisAlignedBB var8 = this.getCollisionBoundingBoxFromPool(par1World,
	// par2, par3, par4);
	//
	// if (var8 != null && par5AxisAlignedBB.intersectsWith(var8))
	// {
	// par6List.add(var8);
	// }
	// }
	//
	// @Override
	// @SideOnly(Side.CLIENT)
	//
	// /**
	// * Returns the bounding box of the wired rectangular prism to render.
	// */
	// public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int
	// par2, int par3, int par4)
	// {
	// return AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(par2 +
	// this.minX, par3 + this.minY, par4 + this.minZ, par2 + this.maxX, par3 +
	// this.maxY, par4 + this.maxZ);
	// }
	//
	// /**
	// * Returns a bounding box from the pool of bounding boxes (this means this
	// box can change after the pool has been
	// * cleared to be reused)
	// */
	// @Override
	// public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int
	// par2, int par3, int par4)
	// {
	// return AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(par2 +
	// this.minX, par3 + this.minY, par4 + this.minZ, par2 + this.maxX, par3 +
	// this.maxY, par4 + this.maxZ);
	// }
	//
	/**
	 * Is this block (a) opaque and (b) a full 1m cube? This determines whether
	 * or not to render the shared face of two adjacent blocks and also whether
	 * the player can attach torches, redstone wire, etc to this block.
	 */
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	// /**
	// * Returns whether this block is collideable based on the arguments passed
	// in Args: blockMetaData, unknownFlag
	// */
	// @Override
	// public boolean canCollideCheck(int par1, boolean par2)
	// {
	// return this.isCollidable();
	// }
	//
	// /**
	// * Returns if this block is collidable (only used by Fire). Args: x, y, z
	// */
	// @Override
	// public boolean isCollidable()
	// {
	// return true;
	// }
	//
	// /**
	// * Ticks the block if it's been scheduled
	// */
	// public void updateTick(World par1World, int par2, int par3, int par4,
	// Random par5Random) {}
	//
	// @SideOnly(Side.CLIENT)
	//
	// /**
	// * A randomly called display update to be able to add particles or other
	// items for display
	// */
	// public void randomDisplayTick(World par1World, int par2, int par3, int
	// par4, Random par5Random) {}
	//
	// /**
	// * Called right before the block is destroyed by a player. Args: world, x,
	// y, z, metaData
	// */
	// @Override
	// public void onBlockDestroyedByPlayer(World par1World, int par2, int par3,
	// int par4, int par5) {}
	//
	// /**
	// * Lets the block know when one of its neighbor changes. Doesn't know
	// which neighbor changed (coordinates passed are
	// * their own) Args: x, y, z, neighbor blockID
	// */
	// @Override
	// public void onNeighborBlockChange(World par1World, int par2, int par3,
	// int par4, int par5) {}
	//
	// /**
	// * How many world ticks before ticking
	// */
	// @Override
	// public int tickRate()
	// {
	// return 10;
	// }
	//
	// /**
	// * Called whenever the block is added into the world. Args: world, x, y, z
	// */
	// @Override
	// public void onBlockAdded(World par1World, int par2, int par3, int par4)
	// {}
	//
	// /**
	// * ejects contained items into the world, and notifies neighbours of an
	// update, as appropriate
	// */
	// @Override
	// public void breakBlock(World par1World, int par2, int par3, int par4, int
	// par5, int par6)
	// {
	// if (hasTileEntity(par6) && !(this instanceof BlockContainer))
	// {
	// par1World.removeBlockTileEntity(par2, par3, par4);
	// }
	// }
	//
	// /**
	// * Returns the quantity of items to drop on block destruction.
	// */
	// public int quantityDropped(Random par1Random)
	// {
	// return 1;
	// }
	//
	// /**
	// * Returns the ID of the items to drop on destruction.
	// */
	// public int idDropped(int par1, Random par2Random, int par3)
	// {
	// return this.blockID;
	// }
	//
	// /**
	// * Gets the hardness of block at the given coordinates in the given world,
	// relative to the ability of the given
	// * EntityPlayer.
	// */
	// @Override
	// public float getPlayerRelativeBlockHardness(EntityPlayer
	// par1EntityPlayer, World par2World, int par3, int par4, int par5)
	// {
	// return ForgeHooks.blockStrength(this, par1EntityPlayer, par2World, par3,
	// par4, par5);
	// }
	//
	// /**
	// * Drops the specified block items
	// */
	// @Override
	// public final void dropBlockAsItem(World par1World, int par2, int par3,
	// int par4, int par5, int par6)
	// {
	// this.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, 1.0F,
	// par6);
	// }
	//
	// /**
	// * Drops the block items with a specified chance of dropping the specified
	// items
	// */
	// @Override
	// public void dropBlockAsItemWithChance(World par1World, int par2, int
	// par3, int par4, int par5, float par6, int par7)
	// {
	// if (!par1World.isRemote)
	// {
	// ArrayList<ItemStack> items = getBlockDropped(par1World, par2, par3, par4,
	// par5, par7);
	//
	// for (ItemStack item : items)
	// {
	// if (par1World.rand.nextFloat() <= par6)
	// {
	// this.dropBlockAsItem_do(par1World, par2, par3, par4, item);
	// }
	// }
	// }
	// }
	//
	// /**
	// * Spawns EntityItem in the world for the given ItemStack if the world is
	// not remote.
	// */
	// @Override
	// protected void dropBlockAsItem_do(World par1World, int par2, int par3,
	// int par4, ItemStack par5ItemStack)
	// {
	// if (!par1World.isRemote &&
	// par1World.getGameRules().getGameRuleBooleanValue("doTileDrops"))
	// {
	// float var6 = 0.7F;
	// double var7 = par1World.rand.nextFloat() * var6 + (1.0F - var6) * 0.5D;
	// double var9 = par1World.rand.nextFloat() * var6 + (1.0F - var6) * 0.5D;
	// double var11 = par1World.rand.nextFloat() * var6 + (1.0F - var6) * 0.5D;
	// EntityItem var13 = new EntityItem(par1World, par2 + var7, par3 + var9,
	// par4 + var11, par5ItemStack);
	// var13.delayBeforeCanPickup = 10;
	// par1World.spawnEntityInWorld(var13);
	// }
	// }
	//
	// /**
	// * called by spawner, ore, redstoneOre blocks
	// */
	// @Override
	// protected void dropXpOnBlockBreak(World par1World, int par2, int par3,
	// int par4, int par5)
	// {
	// if (!par1World.isRemote)
	// {
	// while (par5 > 0)
	// {
	// int var6 = EntityXPOrb.getXPSplit(par5);
	// par5 -= var6;
	// par1World.spawnEntityInWorld(new EntityXPOrb(par1World, par2 + 0.5D, par3
	// + 0.5D, par4 + 0.5D, var6));
	// }
	// }
	// }
	//
	// /**
	// * Determines the damage on the item the block drops. Used in cloth and
	// wood.
	// */
	// @Override
	// public int damageDropped(int par1)
	// {
	// return 0;
	// }
	//
	// /**
	// * Returns how much this block can resist explosions from the passed in
	// entity.
	// */
	// public float getExplosionResistance(Entity par1Entity)
	// {
	// return this.blockResistance / 5.0F;
	// }
	//
	// /**
	// * Ray traces through the blocks collision from start vector to end vector
	// returning a ray trace hit. Args: world,
	// * x, y, z, startVec, endVec
	// */
	// public MovingObjectPosition collisionRayTrace(World par1World, int par2,
	// int par3, int par4, Vec3 par5Vec3, Vec3 par6Vec3)
	// {
	// this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
	// par5Vec3 = par5Vec3.addVector((double)(-par2), (double)(-par3),
	// (double)(-par4));
	// par6Vec3 = par6Vec3.addVector((double)(-par2), (double)(-par3),
	// (double)(-par4));
	// Vec3 var7 = par5Vec3.getIntermediateWithXValue(par6Vec3, this.minX);
	// Vec3 var8 = par5Vec3.getIntermediateWithXValue(par6Vec3, this.maxX);
	// Vec3 var9 = par5Vec3.getIntermediateWithYValue(par6Vec3, this.minY);
	// Vec3 var10 = par5Vec3.getIntermediateWithYValue(par6Vec3, this.maxY);
	// Vec3 var11 = par5Vec3.getIntermediateWithZValue(par6Vec3, this.minZ);
	// Vec3 var12 = par5Vec3.getIntermediateWithZValue(par6Vec3, this.maxZ);
	//
	// if (!this.isVecInsideYZBounds(var7))
	// {
	// var7 = null;
	// }
	//
	// if (!this.isVecInsideYZBounds(var8))
	// {
	// var8 = null;
	// }
	//
	// if (!this.isVecInsideXZBounds(var9))
	// {
	// var9 = null;
	// }
	//
	// if (!this.isVecInsideXZBounds(var10))
	// {
	// var10 = null;
	// }
	//
	// if (!this.isVecInsideXYBounds(var11))
	// {
	// var11 = null;
	// }
	//
	// if (!this.isVecInsideXYBounds(var12))
	// {
	// var12 = null;
	// }
	//
	// Vec3 var13 = null;
	//
	// if (var7 != null && (var13 == null || par5Vec3.squareDistanceTo(var7) <
	// par5Vec3.squareDistanceTo(var13)))
	// {
	// var13 = var7;
	// }
	//
	// if (var8 != null && (var13 == null || par5Vec3.squareDistanceTo(var8) <
	// par5Vec3.squareDistanceTo(var13)))
	// {
	// var13 = var8;
	// }
	//
	// if (var9 != null && (var13 == null || par5Vec3.squareDistanceTo(var9) <
	// par5Vec3.squareDistanceTo(var13)))
	// {
	// var13 = var9;
	// }
	//
	// if (var10 != null && (var13 == null || par5Vec3.squareDistanceTo(var10) <
	// par5Vec3.squareDistanceTo(var13)))
	// {
	// var13 = var10;
	// }
	//
	// if (var11 != null && (var13 == null || par5Vec3.squareDistanceTo(var11) <
	// par5Vec3.squareDistanceTo(var13)))
	// {
	// var13 = var11;
	// }
	//
	// if (var12 != null && (var13 == null || par5Vec3.squareDistanceTo(var12) <
	// par5Vec3.squareDistanceTo(var13)))
	// {
	// var13 = var12;
	// }
	//
	// if (var13 == null)
	// {
	// return null;
	// }
	// else
	// {
	// byte var14 = -1;
	//
	// if (var13 == var7)
	// {
	// var14 = 4;
	// }
	//
	// if (var13 == var8)
	// {
	// var14 = 5;
	// }
	//
	// if (var13 == var9)
	// {
	// var14 = 0;
	// }
	//
	// if (var13 == var10)
	// {
	// var14 = 1;
	// }
	//
	// if (var13 == var11)
	// {
	// var14 = 2;
	// }
	//
	// if (var13 == var12)
	// {
	// var14 = 3;
	// }
	//
	// return new MovingObjectPosition(par2, par3, par4, var14,
	// var13.addVector((double)par2, (double)par3, (double)par4));
	// }
	// }
	//
	// /**
	// * Checks if a vector is within the Y and Z bounds of the block.
	// */
	// private boolean isVecInsideYZBounds(Vec3 par1Vec3)
	// {
	// return par1Vec3 == null ? false : par1Vec3.yCoord >= this.minY &&
	// par1Vec3.yCoord <= this.maxY && par1Vec3.zCoord >= this.minZ &&
	// par1Vec3.zCoord <= this.maxZ;
	// }
	//
	// /**
	// * Checks if a vector is within the X and Z bounds of the block.
	// */
	// private boolean isVecInsideXZBounds(Vec3 par1Vec3)
	// {
	// return par1Vec3 == null ? false : par1Vec3.xCoord >= this.minX &&
	// par1Vec3.xCoord <= this.maxX && par1Vec3.zCoord >= this.minZ &&
	// par1Vec3.zCoord <= this.maxZ;
	// }
	//
	// /**
	// * Checks if a vector is within the X and Y bounds of the block.
	// */
	// private boolean isVecInsideXYBounds(Vec3 par1Vec3)
	// {
	// return par1Vec3 == null ? false : par1Vec3.xCoord >= this.minX &&
	// par1Vec3.xCoord <= this.maxX && par1Vec3.yCoord >= this.minY &&
	// par1Vec3.yCoord <= this.maxY;
	// }
	//
	// /**
	// * Called upon the block being destroyed by an explosion
	// */
	// @Override
	// public void onBlockDestroyedByExplosion(World par1World, int par2, int
	// par3, int par4) {}
	//
	// @Override
	// @SideOnly(Side.CLIENT)
	//
	// /**
	// * Returns which pass should this block be rendered on. 0 for solids and 1
	// for alpha
	// */
	// public int getRenderBlockPass()
	// {
	// return 0;
	// }
	//
	// /**
	// * checks to see if you can place this block can be placed on that side of
	// a block: BlockLever overrides
	// */
	// @Override
	// public boolean canPlaceBlockOnSide(World par1World, int par2, int par3,
	// int par4, int par5)
	// {
	// return this.canPlaceBlockAt(par1World, par2, par3, par4);
	// }
	//
	// /**
	// * Checks to see if its valid to put this block at the specified
	// coordinates. Args: world, x, y, z
	// */
	// @Override
	// public boolean canPlaceBlockAt(World par1World, int par2, int par3, int
	// par4)
	// {
	// int var5 = par1World.getBlockId(par2, par3, par4);
	// return var5 == 0 || blocksList[var5].blockMaterial.isReplaceable();
	// }
	//
	// /**
	// * Called upon block activation (right click on the block.)
	// */
	// public boolean onBlockActivated(World par1World, int par2, int par3, int
	// par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8,
	// float par9)
	// {
	// return false;
	// }
	//
	// /**
	// * Called whenever an entity is walking on top of this block. Args: world,
	// x, y, z, entity
	// */
	// public void onEntityWalking(World par1World, int par2, int par3, int
	// par4, Entity par5Entity) {}
	//
	// @Override
	// public int func_85104_a(World par1World, int par2, int par3, int par4,
	// int par5, float par6, float par7, float par8, int par9)
	// {
	// return par9;
	// }
	//
	// /**
	// * Called when the block is clicked by a player. Args: x, y, z,
	// entityPlayer
	// */
	// @Override
	// public void onBlockClicked(World par1World, int par2, int par3, int par4,
	// EntityPlayer par5EntityPlayer) {}
	//
	// /**
	// * Can add to the passed in vector for a movement vector to be applied to
	// the entity. Args: x, y, z, entity, vec3d
	// */
	// public void velocityToAddToEntity(World par1World, int par2, int par3,
	// int par4, Entity par5Entity, Vec3 par6Vec3) {}
	//
	// /**
	// * Updates the blocks bounds based on its current state. Args: world, x,
	// y, z
	// */
	// public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int
	// par2, int par3, int par4) {}
	//
	// /**
	// * returns the block bounderies minX value
	// */
	// @Override
	// public final double getBlockBoundsMinX()
	// {
	// return this.minX;
	// }
	//
	// /**
	// * returns the block bounderies maxX value
	// */
	// @Override
	// public final double getBlockBoundsMaxX()
	// {
	// return this.maxX;
	// }
	//
	// /**
	// * returns the block bounderies minY value
	// */
	// @Override
	// public final double getBlockBoundsMinY()
	// {
	// return this.minY;
	// }
	//
	// /**
	// * returns the block bounderies maxY value
	// */
	// @Override
	// public final double getBlockBoundsMaxY()
	// {
	// return this.maxY;
	// }
	//
	// /**
	// * returns the block bounderies minZ value
	// */
	// @Override
	// public final double getBlockBoundsMinZ()
	// {
	// return this.minZ;
	// }
	//
	// /**
	// * returns the block bounderies maxZ value
	// */
	// @Override
	// public final double getBlockBoundsMaxZ()
	// {
	// return this.maxZ;
	// }
	//
	// @Override
	// @SideOnly(Side.CLIENT)
	// public int getBlockColor()
	// {
	// return 16777215;
	// }
	//
	// @Override
	// @SideOnly(Side.CLIENT)
	//
	// /**
	// * Returns the color this block should be rendered. Used by leaves.
	// */
	// public int getRenderColor(int par1)
	// {
	// return 16777215;
	// }
	//
	// @SideOnly(Side.CLIENT)
	//
	// /**
	// * Returns a integer with hex for 0xrrggbb with this color multiplied
	// against the blocks color. Note only called
	// * when first determining what to render.
	// */
	// public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int
	// par3, int par4)
	// {
	// return 16777215;
	// }
	//
	// /**
	// * Returns true if the block is emitting indirect/weak redstone power on
	// the specified side. If isBlockNormalCube
	// * returns true, standard redstone propagation rules will apply instead
	// and this will not be called. Args: World, X,
	// * Y, Z, side
	// */
	// public boolean isProvidingWeakPower(IBlockAccess par1IBlockAccess, int
	// par2, int par3, int par4, int par5)
	// {
	// return false;
	// }
	//
	// /**
	// * Can this block provide power. Only wire currently seems to have this
	// change based on its state.
	// */
	// @Override
	// public boolean canProvidePower()
	// {
	// return false;
	// }
	//
	// /**
	// * Triggered whenever an entity collides with this block (enters into the
	// block). Args: world, x, y, z, entity
	// */
	// public void onEntityCollidedWithBlock(World par1World, int par2, int
	// par3, int par4, Entity par5Entity) {}
	//
	// /**
	// * Returns true if the block is emitting direct/strong redstone power on
	// the specified side. Args: World, X, Y, Z,
	// * side
	// */
	// public boolean isProvidingStrongPower(IBlockAccess par1IBlockAccess, int
	// par2, int par3, int par4, int par5)
	// {
	// return false;
	// }
	//
	// /**
	// * Sets the block's bounds for rendering it as an item
	// */
	// @Override
	// public void setBlockBoundsForItemRender() {}
	//
	// /**
	// * Called when the player destroys a block with an item that can harvest
	// it. (i, j, k) are the coordinates of the
	// * block and l is the block's subtype/damage.
	// */
	// @Override
	// public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer,
	// int par3, int par4, int par5, int par6)
	// {
	// par2EntityPlayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
	// par2EntityPlayer.addExhaustion(0.025F);
	//
	// if (this.canSilkHarvest(par1World, par2EntityPlayer, par3, par4, par5,
	// par6) && EnchantmentHelper.getSilkTouchModifier(par2EntityPlayer))
	// {
	// ItemStack var8 = this.createStackedBlock(par6);
	//
	// if (var8 != null)
	// {
	// this.dropBlockAsItem_do(par1World, par3, par4, par5, var8);
	// }
	// }
	// else
	// {
	// int var7 = EnchantmentHelper.getFortuneModifier(par2EntityPlayer);
	// this.dropBlockAsItem(par1World, par3, par4, par5, par6, var7);
	// }
	// }
	//
	// /**
	// * Return true if a player with Silk Touch can harvest this block
	// directly, and not its normal drops.
	// */
	// @Override
	// protected boolean canSilkHarvest()
	// {
	// return this.renderAsNormalBlock() && !this.isBlockContainer;
	// }
	//
	// /**
	// * Returns an item stack containing a single instance of the current block
	// type. 'i' is the block's subtype/damage
	// * and is ignored for blocks which do not support subtypes. Blocks which
	// cannot be harvested should return null.
	// */
	// @Override
	// protected ItemStack createStackedBlock(int par1)
	// {
	// int var2 = 0;
	//
	// if (this.blockID >= 0 && this.blockID < Item.itemsList.length &&
	// Item.itemsList[this.blockID].getHasSubtypes())
	// {
	// var2 = par1;
	// }
	//
	// return new ItemStack(this.blockID, 1, var2);
	// }
	//
	// /**
	// * Returns the usual quantity dropped by the block plus a bonus of 1 to
	// 'i' (inclusive).
	// */
	// public int quantityDroppedWithBonus(int par1, Random par2Random)
	// {
	// return this.quantityDropped(par2Random);
	// }
	//
	// /**
	// * Can this block stay at this position. Similar to canPlaceBlockAt except
	// gets checked often with plants.
	// */
	// @Override
	// public boolean canBlockStay(World par1World, int par2, int par3, int
	// par4)
	// {
	// return true;
	// }
	//
	// /**
	// * Called when the block is placed in the world.
	// */
	// public void onBlockPlacedBy(World par1World, int par2, int par3, int
	// par4, EntityLiving par5EntityLiving) {}
	//
	// @Override
	// public void func_85105_g(World par1World, int par2, int par3, int par4,
	// int par5) {}
	//
	// /**
	// * set name of block from language file
	// */
	// @Override
	// public Block setBlockName(String par1Str)
	// {
	// this.blockName = "tile." + par1Str;
	// return this;
	// }
	//
	// /**
	// * gets the localized version of the name of this block using
	// StatCollector.translateToLocal. Used for the statistic
	// * page.
	// */
	// @Override
	// public String translateBlockName()
	// {
	// return StatCollector.translateToLocal(this.getBlockName() + ".name");
	// }
	//
	// @Override
	// public String getBlockName()
	// {
	// return this.blockName;
	// }
	//
	// /**
	// * Called when the block receives a BlockEvent - see World.addBlockEvent.
	// By default, passes it on to the tile
	// * entity at this location. Args: world, x, y, z, blockID, EventID, event
	// parameter
	// */
	// @Override
	// public void onBlockEventReceived(World par1World, int par2, int par3, int
	// par4, int par5, int par6) {}
	//
	// /**
	// * Return the state of blocks statistics flags - if the block is counted
	// for mined and placed.
	// */
	// @Override
	// public boolean getEnableStats()
	// {
	// return this.enableStats;
	// }
	//
	// /**
	// * Disable statistics for the block, the block will no count for mined or
	// placed.
	// */
	// @Override
	// protected Block disableStats()
	// {
	// this.enableStats = false;
	// return this;
	// }
	//
	// /**
	// * Returns the mobility information of the block, 0 = free, 1 = can't push
	// but can move over, 2 = total immobility
	// * and stop pistons
	// */
	// @Override
	// public int getMobilityFlag()
	// {
	// return this.blockMaterial.getMaterialMobility();
	// }
	//
	// @SideOnly(Side.CLIENT)
	//
	// /**
	// * Returns the default ambient occlusion value based on block opacity
	// */
	// public float getAmbientOcclusionLightValue(IBlockAccess par1IBlockAccess,
	// int par2, int par3, int par4)
	// {
	// return par1IBlockAccess.isBlockNormalCube(par2, par3, par4) ? 0.2F :
	// 1.0F;
	// }
	//
	// /**
	// * Block's chance to react to an entity falling on it.
	// */
	// public void onFallenUpon(World par1World, int par2, int par3, int par4,
	// Entity par5Entity, float par6) {}
	//
	// @Override
	// @SideOnly(Side.CLIENT)
	//
	// /**
	// * only called by clickMiddleMouseButton , and passed to
	// inventory.setCurrentItem (along with isCreative)
	// */
	// public int idPicked(World par1World, int par2, int par3, int par4)
	// {
	// return this.blockID;
	// }
	//
	// /**
	// * Get the block's damage value (for use with pick block).
	// */
	// @Override
	// public int getDamageValue(World par1World, int par2, int par3, int par4)
	// {
	// return this.damageDropped(par1World.getBlockMetadata(par2, par3, par4));
	// }
	//
	// @SideOnly(Side.CLIENT)
	//
	// /**
	// * returns a list of blocks with the same ID, but different meta (eg: wood
	// returns 4 blocks)
	// */
	// public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List
	// par3List)
	// {
	// par3List.add(new ItemStack(par1, 1, 0));
	// }
	//
	// /**
	// * Sets the CreativeTab to display this block on.
	// */
	// public Block setCreativeTab(CreativeTabs par1CreativeTabs)
	// {
	// this.displayOnCreativeTab = par1CreativeTabs;
	// return this;
	// }
	//
	// /**
	// * Called when the block is attempted to be harvested
	// */
	// @Override
	// public void onBlockHarvested(World par1World, int par2, int par3, int
	// par4, int par5, EntityPlayer par6EntityPlayer) {}
	//
	// @Override
	// @SideOnly(Side.CLIENT)
	//
	// /**
	// * Returns the CreativeTab to display the given block on.
	// */
	// public CreativeTabs getCreativeTabToDisplayOn()
	// {
	// return this.displayOnCreativeTab;
	// }
	//
	// /**
	// * Called when this block is set (with meta data).
	// */
	// @Override
	// public void onSetBlockIDWithMetaData(World par1World, int par2, int par3,
	// int par4, int par5) {}
	//
	// /**
	// * currently only used by BlockCauldron to incrament meta-data during rain
	// */
	// @Override
	// public void fillWithRain(World par1World, int par2, int par3, int par4)
	// {}
	//
	// @Override
	// @SideOnly(Side.CLIENT)
	// public boolean func_82505_u_()
	// {
	// return false;
	// }
	//
	// @Override
	// public boolean func_82506_l()
	// {
	// return true;
	// }
	//
	// /**
	// * Return whether this block can drop from an explosion.
	// */
	// public boolean canDropFromExplosion(Explosion par1Explosion)
	// {
	// return true;
	// }
	//
	// static
	// {
	// Item.itemsList[cloth.blockID] = (new ItemCloth(cloth.blockID -
	// 256)).setItemName("cloth");
	// Item.itemsList[wood.blockID] = (new ItemMultiTextureTile(wood.blockID -
	// 256, wood, BlockLog.woodType)).setItemName("log");
	// Item.itemsList[planks.blockID] = (new ItemMultiTextureTile(planks.blockID
	// - 256, planks, BlockWood.woodType)).setItemName("wood");
	// Item.itemsList[silverfish.blockID] = (new
	// ItemMultiTextureTile(silverfish.blockID - 256, silverfish,
	// BlockSilverfish.silverfishStoneTypes)).setItemName("monsterStoneEgg");
	// Item.itemsList[stoneBrick.blockID] = (new
	// ItemMultiTextureTile(stoneBrick.blockID - 256, stoneBrick,
	// BlockStoneBrick.STONE_BRICK_TYPES)).setItemName("stonebricksmooth");
	// Item.itemsList[sandStone.blockID] = (new
	// ItemMultiTextureTile(sandStone.blockID - 256, sandStone,
	// BlockSandStone.SAND_STONE_TYPES)).setItemName("sandStone");
	// Item.itemsList[stoneSingleSlab.blockID] = (new
	// ItemSlab(stoneSingleSlab.blockID - 256, stoneSingleSlab, stoneDoubleSlab,
	// false)).setItemName("stoneSlab");
	// Item.itemsList[stoneDoubleSlab.blockID] = (new
	// ItemSlab(stoneDoubleSlab.blockID - 256, stoneSingleSlab, stoneDoubleSlab,
	// true)).setItemName("stoneSlab");
	// Item.itemsList[woodSingleSlab.blockID] = (new
	// ItemSlab(woodSingleSlab.blockID - 256, woodSingleSlab, woodDoubleSlab,
	// false)).setItemName("woodSlab");
	// Item.itemsList[woodDoubleSlab.blockID] = (new
	// ItemSlab(woodDoubleSlab.blockID - 256, woodSingleSlab, woodDoubleSlab,
	// true)).setItemName("woodSlab");
	// Item.itemsList[sapling.blockID] = (new
	// ItemMultiTextureTile(sapling.blockID - 256, sapling,
	// BlockSapling.WOOD_TYPES)).setItemName("sapling");
	// Item.itemsList[leaves.blockID] = (new ItemLeaves(leaves.blockID -
	// 256)).setItemName("leaves");
	// Item.itemsList[vine.blockID] = new ItemColored(vine.blockID - 256,
	// false);
	// Item.itemsList[tallGrass.blockID] = (new ItemColored(tallGrass.blockID -
	// 256, true)).setBlockNames(new String[] {"shrub", "grass", "fern"});
	// Item.itemsList[waterlily.blockID] = new ItemLilyPad(waterlily.blockID -
	// 256);
	// Item.itemsList[pistonBase.blockID] = new ItemPiston(pistonBase.blockID -
	// 256);
	// Item.itemsList[pistonStickyBase.blockID] = new
	// ItemPiston(pistonStickyBase.blockID - 256);
	// Item.itemsList[cobblestoneWall.blockID] = (new
	// ItemMultiTextureTile(cobblestoneWall.blockID - 256, cobblestoneWall,
	// BlockWall.types)).setItemName("cobbleWall");
	// Item.itemsList[anvil.blockID] = (new
	// ItemAnvilBlock(anvil)).setItemName("anvil");
	//
	// for (int var0 = 0; var0 < 256; ++var0)
	// {
	// if (blocksList[var0] != null)
	// {
	// if (Item.itemsList[var0] == null)
	// {
	// Item.itemsList[var0] = new ItemBlock(var0 - 256);
	// blocksList[var0].initializeBlock();
	// }
	//
	// boolean var1 = false;
	//
	// if (var0 > 0 && blocksList[var0].getRenderType() == 10)
	// {
	// var1 = true;
	// }
	//
	// if (var0 > 0 && blocksList[var0] instanceof BlockHalfSlab)
	// {
	// var1 = true;
	// }
	//
	// if (var0 == tilledField.blockID)
	// {
	// var1 = true;
	// }
	//
	// if (canBlockGrass[var0])
	// {
	// var1 = true;
	// }
	//
	// if (lightOpacity[var0] == 0)
	// {
	// var1 = true;
	// }
	//
	// useNeighborBrightness[var0] = var1;
	// }
	// }
	//
	// canBlockGrass[0] = true;
	// StatList.initBreakableStats();
	// }
	//
	// /* =================================================== FORGE START
	// =====================================*/
	// /**
	// * Get a light value for this block, normal ranges are between 0 and 15
	// *
	// * @param world The current world
	// * @param x X Position
	// * @param y Y position
	// * @param z Z position
	// * @return The light value
	// */
	// public int getLightValue(IBlockAccess world, int x, int y, int z)
	// {
	// return lightValue[blockID];
	// }
	//
	// /**
	// * Checks if a player or entity can use this block to 'climb' like a
	// ladder.
	// *
	// * @param world The current world
	// * @param x X Position
	// * @param y Y position
	// * @param z Z position
	// * @return True if the block should act like a ladder
	// */
	// @Override
	// public boolean isLadder(World world, int x, int y, int z)
	// {
	// return false;
	// }
	//
	// /**
	// * Return true if the block is a normal, solid cube. This
	// * determines indirect power state, entity ejection from blocks, and a few
	// * others.
	// *
	// * @param world The current world
	// * @param x X Position
	// * @param y Y position
	// * @param z Z position
	// * @return True if the block is a full cube
	// */
	// @Override
	// public boolean isBlockNormalCube(World world, int x, int y, int z)
	// {
	// return blockMaterial.isOpaque() && renderAsNormalBlock();
	// }
	//
	// /**
	// * Checks if the block is a solid face on the given side, used by
	// placement
	// * logic.
	// *
	// * @param world
	// * The current world
	// * @param x
	// * X Position
	// * @param y
	// * Y position
	// * @param z
	// * Z position
	// * @param side
	// * The side to check
	// * @return True if the block is solid on the specified side.
	// */
	// @Override
	// public boolean isBlockSolidOnSide(World world, int x, int y, int z,
	// ForgeDirection side)
	// {
	// return true;
	// }

	// /**
	// * Determines if a new block can be replace the space occupied by this
	// one,
	// * Used in the player's placement code to make the block act like water,
	// and lava.
	// *
	// * @param world The current world
	// * @param x X Position
	// * @param y Y position
	// * @param z Z position
	// * @return True if the block is replaceable by another block
	// */
	// @Override
	// public boolean isBlockReplaceable(World world, int x, int y, int z)
	// {
	// return false;
	// }
	//
	// /**
	// * Determines if this block should set fire and deal fire damage
	// * to entities coming into contact with it.
	// *
	// * @param world The current world
	// * @param x X Position
	// * @param y Y position
	// * @param z Z position
	// * @return True if the block should deal damage
	// */
	// @Override
	// public boolean isBlockBurning(World world, int x, int y, int z)
	// {
	// return false;
	// }
	//
	// /**
	// * Determines this block should be treated as an air block
	// * by the rest of the code. This method is primarily
	// * useful for creating pure logic-blocks that will be invisible
	// * to the player and otherwise interact as air would.
	// *
	// * @param world The current world
	// * @param x X Position
	// * @param y Y position
	// * @param z Z position
	// * @return True if the block considered air
	// */
	// @Override
	// public boolean isAirBlock(World world, int x, int y, int z)
	// {
	// return false;
	// }
	//
	// /**
	// * Determines if the player can harvest this block, obtaining it's drops
	// when the block is destroyed.
	// *
	// * @param player The player damaging the block, may be null
	// * @param meta The block's current metadata
	// * @return True to spawn the drops
	// */
	// @Override
	// public boolean canHarvestBlock(EntityPlayer player, int meta)
	// {
	// return ForgeHooks.canHarvestBlock(this, player, meta);
	// }
	//
	// /**
	// * Called when a player removes a block. This is responsible for
	// * actually destroying the block, and the block is intact at time of call.
	// * This is called regardless of whether the player can harvest the block
	// or
	// * not.
	// *
	// * Return true if the block is actually destroyed.
	// *
	// * Note: When used in multiplayer, this is called on both client and
	// * server sides!
	// *
	// * @param world The current world
	// * @param player The player damaging the block, may be null
	// * @param x X Position
	// * @param y Y position
	// * @param z Z position
	// * @return True if the block is actually destroyed.
	// */
	// @Override
	// public boolean removeBlockByPlayer(World world, EntityPlayer player, int
	// x, int y, int z)
	// {
	// return world.setBlockWithNotify(x, y, z, 0);
	// }
	//
	// /**
	// * Called when a new CreativeContainer is opened, populate the list
	// * with all of the items for this block you want a player in creative mode
	// * to have access to.
	// *
	// * @param itemList The list of items to display on the creative inventory.
	// */
	// public void addCreativeItems(ArrayList itemList)
	// {
	// }
	//
	// /**
	// * Chance that fire will spread and consume this block.
	// * 300 being a 100% chance, 0, being a 0% chance.
	// *
	// * @param world The current world
	// * @param x The blocks X position
	// * @param y The blocks Y position
	// * @param z The blocks Z position
	// * @param metadata The blocks current metadata
	// * @param face The face that the fire is coming from
	// * @return A number ranging from 0 to 300 relating used to determine if
	// the block will be consumed by fire
	// */
	// public int getFlammability(IBlockAccess world, int x, int y, int z, int
	// metadata, ForgeDirection face)
	// {
	// return blockFlammability[blockID];
	// }
	//
	// /**
	// * Called when fire is updating, checks if a block face can catch fire.
	// *
	// *
	// * @param world The current world
	// * @param x The blocks X position
	// * @param y The blocks Y position
	// * @param z The blocks Z position
	// * @param metadata The blocks current metadata
	// * @param face The face that the fire is coming from
	// * @return True if the face can be on fire, false otherwise.
	// */
	// public boolean isFlammable(IBlockAccess world, int x, int y, int z, int
	// metadata, ForgeDirection face)
	// {
	// return getFlammability(world, x, y, z, metadata, face) > 0;
	// }
	//
	// /**
	// * Called when fire is updating on a neighbor block.
	// * The higher the number returned, the faster fire will spread around this
	// block.
	// *
	// * @param world The current world
	// * @param x The blocks X position
	// * @param y The blocks Y position
	// * @param z The blocks Z position
	// * @param metadata The blocks current metadata
	// * @param face The face that the fire is coming from
	// * @return A number that is used to determine the speed of fire growth
	// around the block
	// */
	// public int getFireSpreadSpeed(World world, int x, int y, int z, int
	// metadata, ForgeDirection face)
	// {
	// return blockFireSpreadSpeed[blockID];
	// }
	//
	// /**
	// * Currently only called by fire when it is on top of this block.
	// * Returning true will prevent the fire from naturally dying during
	// updating.
	// * Also prevents firing from dying from rain.
	// *
	// * @param world The current world
	// * @param x The blocks X position
	// * @param y The blocks Y position
	// * @param z The blocks Z position
	// * @param metadata The blocks current metadata
	// * @param side The face that the fire is coming from
	// * @return True if this block sustains fire, meaning it will never go out.
	// */
	// public boolean isFireSource(World world, int x, int y, int z, int
	// metadata, ForgeDirection side)
	// {
	// if (blockID == Block.netherrack.blockID && side == UP)
	// {
	// return true;
	// }
	// if ((world.provider instanceof WorldProviderEnd) && blockID ==
	// Block.bedrock.blockID && side == UP)
	// {
	// return true;
	// }
	// return false;
	// }
	//
	// /**
	// * Called by BlockFire to setup the burn values of vanilla blocks.
	// * @param id The block id
	// * @param encouragement How much the block encourages fire to spread
	// * @param flammability how easy a block is to catch fire
	// */
	// public static void setBurnProperties(int id, int encouragement, int
	// flammability)
	// {
	// blockFireSpreadSpeed[id] = encouragement;
	// blockFlammability[id] = flammability;
	// }

	/**
	 * Called throughout the code as a replacement for block instanceof
	 * BlockContainer Moving this to the Block base class allows for mods that
	 * wish to extend vinella blocks, and also want to have a tile entity on
	 * that block, may.
	 * 
	 * Return true from this function to specify this block has a tile entity.
	 * 
	 * @param metadata
	 *            Metadata of the current block
	 * @return True if block has a tile entity, false otherwise
	 */
	@Override
	public boolean hasTileEntity(int metadata)
	{
		return true;
	}

	/**
	 * Called throughout the code as a replacement for
	 * BlockContainer.getBlockEntity Return the same thing you would from that
	 * function. This will fall back to BlockContainer.getBlockEntity if this
	 * block is a BlockContainer.
	 * 
	 * @param metadata
	 *            The Metadata of the current block
	 * @return A instance of a class extending TileEntity
	 */
	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		return new TileEntityTinkerTable();

	}

	// /**
	// * Metadata and fortune sensitive version, this replaces the old (int
	// meta, Random rand)
	// * version in 1.1.
	// *
	// * @param meta Blocks Metadata
	// * @param fortune Current item fortune level
	// * @param random Random number generator
	// * @return The number of items to drop
	// */
	// public int quantityDropped(int meta, int fortune, Random random)
	// {
	// return quantityDroppedWithBonus(fortune, random);
	// }
	//
	// /**
	// * This returns a complete list of items dropped from this block.
	// *
	// * @param world The current world
	// * @param x X Position
	// * @param y Y Position
	// * @param z Z Position
	// * @param metadata Current metadata
	// * @param fortune Breakers fortune level
	// * @return A ArrayList containing all items this block drops
	// */
	// @Override
	// public ArrayList<ItemStack> getBlockDropped(World world, int x, int y,
	// int z, int metadata, int fortune)
	// {
	// ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
	//
	// int count = quantityDropped(metadata, fortune, world.rand);
	// for(int i = 0; i < count; i++)
	// {
	// int id = idDropped(metadata, world.rand, 0);
	// if (id > 0)
	// {
	// ret.add(new ItemStack(id, 1, damageDropped(metadata)));
	// }
	// }
	// return ret;
	// }
	//
	// /**
	// * Return true from this function if the player with silk touch can
	// harvest this block directly, and not it's normal drops.
	// *
	// * @param world The world
	// * @param player The player doing the harvesting
	// * @param x X Position
	// * @param y Y Position
	// * @param z Z Position
	// * @param metadata The metadata
	// * @return True if the block can be directly harvested using silk touch
	// */
	// @Override
	// public boolean canSilkHarvest(World world, EntityPlayer player, int x,
	// int y, int z, int metadata)
	// {
	// if (this instanceof BlockGlass || this instanceof BlockEnderChest)
	// {
	// return true;
	// }
	// return renderAsNormalBlock() && !hasTileEntity(metadata);
	// }
	//
	// /**
	// * Determines if a specified mob type can spawn on this block, returning
	// false will
	// * prevent any mob from spawning on the block.
	// *
	// * @param type The Mob Category Type
	// * @param world The current world
	// * @param x The X Position
	// * @param y The Y Position
	// * @param z The Z Position
	// * @return True to allow a mob of the specified category to spawn, false
	// to prevent it.
	// */
	// public boolean canCreatureSpawn(EnumCreatureType type, World world, int
	// x, int y, int z)
	// {
	// int meta = world.getBlockMetadata(x, y, z);
	// if (this instanceof BlockStep)
	// {
	// if (MinecraftForge.SPAWNER_ALLOW_ON_INVERTED)
	// {
	// return (((meta & 8) == 8) || isOpaqueCube());
	// }
	// else
	// {
	// return isNormalCube(this.blockID);
	// }
	// }
	// else if (this instanceof BlockStairs)
	// {
	// if (MinecraftForge.SPAWNER_ALLOW_ON_INVERTED)
	// {
	// return ((meta & 4) != 0);
	// }
	// else
	// {
	// return isNormalCube(this.blockID);
	// }
	// }
	// return isBlockSolidOnSide(world, x, y, z, UP);
	// }
	//
	// /**
	// * Determines if this block is classified as a Bed, Allowing
	// * players to sleep in it, though the block has to specifically
	// * perform the sleeping functionality in it's activated event.
	// *
	// * @param world The current world
	// * @param x X Position
	// * @param y Y Position
	// * @param z Z Position
	// * @param player The player or camera entity, null in some cases.
	// * @return True to treat this as a bed
	// */
	// public boolean isBed(World world, int x, int y, int z, EntityLiving
	// player)
	// {
	// return blockID == Block.bed.blockID;
	// }
	//
	// /**
	// * Returns the position that the player is moved to upon
	// * waking up, or respawning at the bed.
	// *
	// * @param world The current world
	// * @param x X Position
	// * @param y Y Position
	// * @param z Z Position
	// * @param player The player or camera entity, null in some cases.
	// * @return The spawn position
	// */
	// @Override
	// public ChunkCoordinates getBedSpawnPosition(World world, int x, int y,
	// int z, EntityPlayer player)
	// {
	// return BlockBed.getNearestEmptyChunkCoordinates(world, x, y, z, 0);
	// }
	//
	// /**
	// * Called when a user either starts or stops sleeping in the bed.
	// *
	// * @param world The current world
	// * @param x X Position
	// * @param y Y Position
	// * @param z Z Position
	// * @param player The player or camera entity, null in some cases.
	// * @param occupied True if we are occupying the bed, or false if they are
	// stopping use of the bed
	// */
	// @Override
	// public void setBedOccupied(World world, int x, int y, int z, EntityPlayer
	// player, boolean occupied)
	// {
	// BlockBed.setBedOccupied(world, x, y, z, occupied);
	// }
	//
	// /**
	// * Returns the direction of the block. Same values that
	// * are returned by BlockDirectional
	// *
	// * @param world The current world
	// * @param x X Position
	// * @param y Y Position
	// * @param z Z Position
	// * @return Bed direction
	// */
	// public int getBedDirection(IBlockAccess world, int x, int y, int z)
	// {
	// return BlockBed.getDirection(world.getBlockMetadata(x, y, z));
	// }
	//
	// /**
	// * Determines if the current block is the foot half of the bed.
	// *
	// * @param world The current world
	// * @param x X Position
	// * @param y Y Position
	// * @param z Z Position
	// * @return True if the current block is the foot side of a bed.
	// */
	// public boolean isBedFoot(IBlockAccess world, int x, int y, int z)
	// {
	// return BlockBed.isBlockHeadOfBed(world.getBlockMetadata(x, y, z));
	// }
	//
	// /**
	// * Called when a leaf should start its decay process.
	// *
	// * @param world The current world
	// * @param x X Position
	// * @param y Y Position
	// * @param z Z Position
	// */
	// @Override
	// public void beginLeavesDecay(World world, int x, int y, int z){}
	//
	// /**
	// * Determines if this block can prevent leaves connected to it from
	// decaying.
	// *
	// * @param world The current world
	// * @param x X Position
	// * @param y Y Position
	// * @param z Z Position
	// * @return true if the presence this block can prevent leaves from
	// decaying.
	// */
	// @Override
	// public boolean canSustainLeaves(World world, int x, int y, int z)
	// {
	// return false;
	// }
	//
	// /**
	// * Determines if this block is considered a leaf block, used to apply the
	// leaf decay and generation system.
	// *
	// * @param world The current world
	// * @param x X Position
	// * @param y Y Position
	// * @param z Z Position
	// * @return true if this block is considered leaves.
	// */
	// @Override
	// public boolean isLeaves(World world, int x, int y, int z)
	// {
	// return false;
	// }
	//
	// /**
	// * Used during tree growth to determine if newly generated leaves can
	// replace this block.
	// *
	// * @param world The current world
	// * @param x X Position
	// * @param y Y Position
	// * @param z Z Position
	// * @return true if this block can be replaced by growing leaves.
	// */
	// @Override
	// public boolean canBeReplacedByLeaves(World world, int x, int y, int z)
	// {
	// return !Block.opaqueCubeLookup[this.blockID];
	// }
	//
	// /**
	// * Sets the current texture file for this block, used when rendering.
	// * Default is "/terrain.png"
	// *
	// * @param texture The texture file
	// */
	// @Override
	// public Block setTextureFile(String texture)
	// {
	// currentTexture = texture;
	// isDefaultTexture = false;
	// return this;
	// }
	//
	//
	// /**
	// * Location sensitive version of getExplosionRestance
	// *
	// * @param par1Entity The entity that caused the explosion
	// * @param world The current world
	// * @param x X Position
	// * @param y Y Position
	// * @param z Z Position
	// * @param explosionX Explosion source X Position
	// * @param explosionY Explosion source X Position
	// * @param explosionZ Explosion source X Position
	// * @return The amount of the explosion absorbed.
	// */
	// public float getExplosionResistance(Entity par1Entity, World world, int
	// x, int y, int z, double explosionX, double explosionY, double explosionZ)
	// {
	// return getExplosionResistance(par1Entity);
	// }
	//
	// /**
	// * Determine if this block can make a redstone connection on the side
	// provided,
	// * Useful to control which sides are inputs and outputs for redstone
	// wires.
	// *
	// * Side:
	// * -1: UP
	// * 0: NORTH
	// * 1: EAST
	// * 2: SOUTH
	// * 3: WEST
	// *
	// * @param world The current world
	// * @param x X Position
	// * @param y Y Position
	// * @param z Z Position
	// * @param side The side that is trying to make the connection
	// * @return True to make the connection
	// */
	// public boolean canConnectRedstone(IBlockAccess world, int x, int y, int
	// z, int side)
	// {
	// return Block.blocksList[blockID].canProvidePower() && side != -1;
	// }
	//
	// /**
	// * Determines if a torch can be placed on the top surface of this block.
	// * Useful for creating your own block that torches can be on, such as
	// fences.
	// *
	// * @param world The current world
	// * @param x X Position
	// * @param y Y Position
	// * @param z Z Position
	// * @return True to allow the torch to be placed
	// */
	// @Override
	// public boolean canPlaceTorchOnTop(World world, int x, int y, int z)
	// {
	// if (world.doesBlockHaveSolidTopSurface(x, y, z))
	// {
	// return true;
	// }
	// else
	// {
	// int id = world.getBlockId(x, y, z);
	// return id == Block.fence.blockID || id == Block.netherFence.blockID || id
	// == Block.glass.blockID || id == Block.cobblestoneWall.blockID;
	// }
	// }
	//
	//
	// /**
	// * Determines if this block should render in this pass.
	// *
	// * @param pass The pass in question
	// * @return True to render
	// */
	// @Override
	// public boolean canRenderInPass(int pass)
	// {
	// return pass == getRenderBlockPass();
	// }
	//
	// /**
	// * Called when a user uses the creative pick block button on this block
	// *
	// * @param target The full target the player is looking at
	// * @return A ItemStack to add to the player's inventory, Null if nothing
	// should be added.
	// */
	// public ItemStack getPickBlock(MovingObjectPosition target, World world,
	// int x, int y, int z)
	// {
	// int id = idPicked(world, x, y, z);
	//
	// if (id == 0)
	// {
	// return null;
	// }
	//
	// Item item = Item.itemsList[id];
	// if (item == null)
	// {
	// return null;
	// }
	//
	// return new ItemStack(id, 1, getDamageValue(world, x, y, z));
	// }
	//
	// /**
	// * Used by getTopSolidOrLiquidBlock while placing biome decorations,
	// villages, etc
	// * Also used to determine if the player can spawn on this block.
	// *
	// * @return False to disallow spawning
	// */
	// @Override
	// public boolean isBlockFoliage(World world, int x, int y, int z)
	// {
	// return false;
	// }
	//
	// /**
	// * Spawn a digging particle effect in the world, this is a wrapper
	// * around EffectRenderer.addBlockHitEffects to allow the block more
	// * control over the particles. Useful when you have entirely different
	// * texture sheets for different sides/locations in the world.
	// *
	// * @param world The current world
	// * @param target The target the player is looking at {x/y/z/side/sub}
	// * @param effectRenderer A reference to the current effect renderer.
	// * @return True to prevent vanilla digging particles form spawning.
	// */
	// @SideOnly(Side.CLIENT)
	// public boolean addBlockHitEffects(World worldObj, MovingObjectPosition
	// target, EffectRenderer effectRenderer)
	// {
	// return false;
	// }
	//
	// /**
	// * Spawn particles for when the block is destroyed. Due to the nature
	// * of how this is invoked, the x/y/z locations are not always guaranteed
	// * to host your block. So be sure to do proper sanity checks before
	// assuming
	// * that the location is this block.
	// *
	// * @param world The current world
	// * @param x X position to spawn the particle
	// * @param y Y position to spawn the particle
	// * @param z Z position to spawn the particle
	// * @param meta The metadata for the block before it was destroyed.
	// * @param effectRenderer A reference to the current effect renderer.
	// * @return True to prevent vanilla break particles from spawning.
	// */
	// @SideOnly(Side.CLIENT)
	// public boolean addBlockDestroyEffects(World world, int x, int y, int z,
	// int meta, EffectRenderer effectRenderer)
	// {
	// return false;
	// }
	//
	// /**
	// * Determines if this block can support the passed in plant, allowing it
	// to be planted and grow.
	// * Some examples:
	// * Reeds check if its a reed, or if its sand/dirt/grass and adjacent to
	// water
	// * Cacti checks if its a cacti, or if its sand
	// * Nether types check for soul sand
	// * Crops check for tilled soil
	// * Caves check if it's a colid surface
	// * Plains check if its grass or dirt
	// * Water check if its still water
	// *
	// * @param world The current world
	// * @param x X Position
	// * @param y Y Position
	// * @param z Z position
	// * @param direction The direction relative to the given position the plant
	// wants to be, typically its UP
	// * @param plant The plant that wants to check
	// * @return True to allow the plant to be planted/stay.
	// */
	// public boolean canSustainPlant(World world, int x, int y, int z,
	// ForgeDirection direction, IPlantable plant)
	// {
	// int plantID = plant.getPlantID(world, x, y + 1, z);
	// EnumPlantType plantType = plant.getPlantType(world, x, y + 1, z);
	//
	// if (plantID == cactus.blockID && blockID == cactus.blockID)
	// {
	// return true;
	// }
	//
	// if (plantID == reed.blockID && blockID == reed.blockID)
	// {
	// return true;
	// }
	//
	// if (plant instanceof BlockFlower &&
	// ((BlockFlower)plant).canThisPlantGrowOnThisBlockID(blockID))
	// {
	// return true;
	// }
	//
	// switch (plantType)
	// {
	// case Desert: return blockID == sand.blockID;
	// case Nether: return blockID == slowSand.blockID;
	// case Crop: return blockID == tilledField.blockID;
	// case Cave: return isBlockSolidOnSide(world, x, y, z, UP);
	// case Plains: return blockID == grass.blockID || blockID == dirt.blockID;
	// case Water: return world.getBlockMaterial(x, y, z) == Material.water &&
	// world.getBlockMetadata(x, y, z) == 0;
	// case Beach:
	// boolean isBeach = (blockID == Block.grass.blockID || blockID ==
	// Block.dirt.blockID || blockID == Block.sand.blockID);
	// boolean hasWater = (world.getBlockMaterial(x - 1, y, z ) ==
	// Material.water ||
	// world.getBlockMaterial(x + 1, y, z ) == Material.water ||
	// world.getBlockMaterial(x, y, z - 1) == Material.water ||
	// world.getBlockMaterial(x, y, z + 1) == Material.water);
	// return isBeach && hasWater;
	// }
	//
	// return false;
	// }
	//
	/**
	 * Location aware and overrideable version of the lightOpacity array, return
	 * the number to subtract from the light value when it passes through this
	 * block.
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
	public int getLightOpacity(World world, int x, int y, int z)
	{
		return 0;
	}
}
