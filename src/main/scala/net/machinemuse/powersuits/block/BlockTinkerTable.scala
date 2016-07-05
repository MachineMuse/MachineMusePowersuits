package net.machinemuse.powersuits.block

import net.machinemuse.powersuits.common.{Config, ModularPowersuits}
import net.minecraft.block.{Block, SoundType}
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{EnumBlockRenderType, EnumFacing, EnumHand}
import net.minecraft.util.math.BlockPos
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.fml.common.registry.GameRegistry

/**
  * This is the tinkertable block. It doesn't do much except look pretty
  * (eventually) and provide a way for the player to access the TinkerTable GUI.
  *
  * @author MachineMuse
  *
  */

object BlockTinkerTable extends Block(Material.IRON) {
  this.setUnlocalizedName("tinkerTable")
  this.setHardness(1.5F)
  this.setResistance(1000.0F)
  this.setHarvestLevel("pickaxe", 2)
  this.setCreativeTab(Config.getCreativeTab)
  this.setSoundType(SoundType.METAL)
  this.setLightOpacity(0)
  this.setLightLevel(0.4f)
  this.setTickRandomly(false)

  var energyIcon: TextureAtlasSprite = null

  /**
    * Different render types do different things. Some will crash
    */
  override def getRenderType(state: IBlockState): EnumBlockRenderType = EnumBlockRenderType.MODEL

  /**
    * Called upon block activation (right click on the block.)
    */
  override def onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, heldItem: ItemStack, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean = {
    if (playerIn.isSneaking) {
      false
    }
    if (worldIn.isRemote) {
      playerIn.openGui(ModularPowersuits.INSTANCE, 0, worldIn, pos.getX, pos.getY, pos.getZ)
    }
    true
  }

  /**
    * This method is called on a block after all other block gets already
    * created. You can use it to reference and configure something on the block
    * that needs the others ones.
    */
  protected def initializeBlock: Unit = {
  }

  /**
    * Returns Returns true if the given side of this block type should be
    * rendered (if it's solid or not), if the adjacent block is at the given
    * coordinates. Args: blockAccess, x, y, z, side
    */
  override def isBlockSolid(worldIn: IBlockAccess, pos: BlockPos, side: EnumFacing): Boolean = true

  /**
    * Is this block (a) opaque and (b) a full 1m cube? This determines whether
    * or not to render the shared face of two adjacent block and also
    * whether the player can attach torches, redstone wire, etc to this block.
    */
  override def isVisuallyOpaque: Boolean = false

  /**
    * Called throughout the code as a replacement for block instanceof
    * BlockContainer Moving this to the Block base class allows for mods that
    * wish
    * to extend vinella block, and also want to have a tile entity on that
    * block, may.
    *
    * Return true from this function to specify this block has a tile entity.
    *
    * @param state
    * IBlockState of the current block
    * @return True if block has a tile entity, false otherwise
    */
  override def hasTileEntity (state: IBlockState): Boolean = true

  /**
    * Called throughout the code as a replacement for
    * BlockContainer.getBlockEntity Return the same thing you would from that
    * function. This will
    * fall back to BlockContainer.getBlockEntity if this block is a
    * BlockContainer.
    *
    * @param state
    * The BlockState of the current block
    * @return A INSTANCE of a class extending TileEntity
    */
  override def createTileEntity (world: World, state: IBlockState): TileEntity = new TileEntityTinkerTable
}