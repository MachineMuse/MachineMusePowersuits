package net.machinemuse.powersuits.block

import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.machinemuse.general.gui.MuseIcon
import net.machinemuse.powersuits.common.Config
import net.machinemuse.powersuits.common.ModularPowersuits
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.IIcon
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge

/**
 * This is the tinkertable block. It doesn't do much except look pretty
 * (eventually) and provide a way for the player to access the TinkerTable GUI.
 *
 * @author MachineMuse
 *
 */
object BlockTinkerTable extends Block(Material.iron) {
  setCreativeTab(Config.getCreativeTab)
  setHardness(1.5F)
  setResistance(1000.0F)
  setStepSound(Block.soundTypeMetal)
  setLightOpacity(0)
  setLightLevel(0.4f)
  setTickRandomly(false)
  GameRegistry.registerTileEntity(classOf[TileEntityTinkerTable], "tinkerTable")
  setBlockName("tinkerTable")
  var energyIcon: IIcon = null

  def setRenderType(id: Int) = {
    this.renderType = id
    this
  }

  @SideOnly(Side.CLIENT) def registerIcons(iconRegister: IIconRegister) {
    this.blockIcon = iconRegister.registerIcon(MuseIcon.ICON_PREFIX + "heatresistantplating")
    energyIcon = blockIcon
  }

  /**
   * Called upon block activation (right click on the block.)
   */
  override def onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, par6: Int, par7: Float, par8: Float, par9: Float): Boolean = {
    if (player.isSneaking) {
      return false
    }
    player.openGui(ModularPowersuits, 0, world, x, y, z)
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

  /**
   * Called throughout the code as a replacement for
   * BlockContainer.getBlockEntity Return the same thing you would from that
   * function. This will
   * fall back to BlockContainer.getBlockEntity if this block is a
   * BlockContainer.
   *
   * @param metadata
	 * The Metadata of the current block
   * @return A instance of a class extending TileEntity
   */
  override def createTileEntity(world: World, metadata: Int): TileEntity = new TileEntityTinkerTable

}