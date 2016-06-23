package net.machinemuse.powersuits.block

import javax.annotation.Nullable

import net.machinemuse.general.gui.MuseIcon
import net.machinemuse.powersuits.common.Config
import net.machinemuse.powersuits.common.ModularPowersuits
import net.minecraft.block.{Block, SoundType}
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{EnumFacing, EnumHand}
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side

/**
 * This is the tinkertable block. It doesn't do much except look pretty
 * (eventually) and provide a way for the player to access the TinkerTable GUI.
 *
 * @author MachineMuse
 *
 */
object BlockTinkerTable extends Block(Material.IRON) {
  this.setCreativeTab(Config.getCreativeTab)
  this.setHardness(1.5F)
  this.setResistance(1000.0F)
  this.setSoundType(SoundType.METAL)
  this.setLightOpacity(0)
  this.setLightLevel(0.4f)
  this.setTickRandomly(false)
  GameRegistry.registerTileEntity(classOf[TileEntityTinkerTable], "tinkerTable")
  this.setUnlocalizedName("tinkerTable")

  def setRenderType(id: Int) = {
    this.renderType = id
    this
  }

  /**
   * Called upon block activation (right click on the block.)
   */
  override def onBlockActivated(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer, hand: EnumHand, @Nullable heldItem: ItemStack, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean = {
    if (player.isSneaking) {
      return false
    }
    player.openGui(ModularPowersuits, 0, world, pos.getX, pos.getY, pos.getZ)
    true
  }

  /**
   * returns some value from 0 to 30 or so for different models. Since we're
   * using a custom renderer, we pass in a completely different ID: the
   * assigned block ID. It won't conflict with other mods, since Forge looks
   * it up in a table anyway, but it's still best to have different internal
   * IDs.
   */
  protected var renderType: Int = 0

  override def getRenderType: Int =  renderType

  /**
   * This method is called on a block after all other blocks gets already
   * created. You can use it to reference and configure something on the block
   * that needs the others ones.
   */
  protected def initializeBlock {
  }

  /**
   * If this block doesn't render as an ordinary block it will return False
   * (examples: signs, buttons, stairs, etc)
   */
  override def renderAsNormalBlock: Boolean = false

  /**
   * Returns Returns true if the given side of this block type should be
   * rendered (if it's solid or not), if the adjacent block is at the given
   * coordinates. Args: blockAccess, x, y, z, side
   */
  override def isBlockSolid(par1IBlockAccess: IBlockAccess, par2: Int, par3: Int, par4: Int, par5: Int): Boolean = true

  /**
   * Is this block (a) opaque and (b) a full 1m cube? This determines whether
   * or not to render the shared face of two adjacent blocks and also
   * whether the player can attach torches, redstone wire, etc to this block.
   */
  override def isOpaqueCube: Boolean = false
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
	 * Metadata of the current block
   * @return True if block has a tile entity, false otherwise
   */
  override def hasTileEntity(metadata: Int): Boolean = true

  override def createTileEntity(world: World, state: IBlockState): TileEntity = new TileEntityTinkerTable
}