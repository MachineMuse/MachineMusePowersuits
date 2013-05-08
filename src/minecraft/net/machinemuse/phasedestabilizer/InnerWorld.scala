package net.machinemuse.phasedestabilizer

import net.minecraft.world.{WorldSettings, WorldProvider, World}
import net.minecraft.entity.Entity
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.util.Vec3Pool
import net.minecraft.world.biome.BiomeGenBase
import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.storage.ISaveHandler
import net.minecraft.profiler.Profiler
import net.minecraft.logging.ILogAgent
import scala.collection.mutable
import net.minecraft.block.Block
import net.minecraftforge.common.ForgeDirection

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 11:18 AM, 5/8/13
 */
class InnerWorld(saveHandler: ISaveHandler, levelName: String, worldProvider: WorldProvider, settings: WorldSettings, profiler: Profiler, logger: ILogAgent)
  extends World(saveHandler, levelName, worldProvider, settings, profiler, logger) {
  val blocks: mutable.HashMap[BlockCoords, BlockEntry] = mutable.HashMap.empty

  protected def createChunkProvider(): IChunkProvider = null

  def getEntityByID(id: Int): Entity = null

  override def getBlockId(x: Int, y: Int, z: Int): Int =
    blocks.get(new BlockCoords(x, y, z)) match {
      case Some(e) => e.id
      case None => 0
    }

  override def getBlockTileEntity(x: Int, y: Int, z: Int): TileEntity =
    blocks.get(new BlockCoords(x, y, z)) match {
      case Some(e) => e.tileEntity
      case None => null
    }

  override def getLightBrightnessForSkyBlocks(x: Int, y: Int, z:Int, w: Int): Int = 1

  override def getBlockMetadata(x: Int, y: Int, z: Int): Int =
    blocks.get(new BlockCoords(x, y, z)) match {
      case Some(e) => e.meta
      case None => 0
    }

  override def getBrightness(i: Int, j: Int, k: Int, l: Int): Float = 1

  override def getLightBrightness(i: Int, j: Int, k: Int): Float = 1

  override def getBlockMaterial(i: Int, j: Int, k: Int): Material =
    blocks.get(new BlockCoords(i, j, k)) match {
      case Some(e) => Block.blocksList(e.id).blockMaterial
      case None => Material.air
    }

  override def isBlockOpaqueCube(i: Int, j: Int, k: Int): Boolean =
    blocks.get(new BlockCoords(i, j, k)) match {
      case Some(e) => Block.blocksList(e.id).isOpaqueCube
      case None => false
    }

  override def isBlockNormalCube(x: Int, y: Int, z: Int): Boolean =
    blocks.get(new BlockCoords(x, y, z)) match {
      case Some(e) => Block.blocksList(e.id).isBlockNormalCube(this,x,y,z)
      case None => false
    }

  override def isAirBlock(i: Int, j: Int, k: Int): Boolean =
    blocks.get(new BlockCoords(i, j, k)) match {
      case Some(e) => Block.blocksList(e.id).isAirBlock(this, i, j, k)
      case None => true
    }

  override def getBiomeGenForCoords(i: Int, j: Int): BiomeGenBase = null

  override def getHeight: Int = 256

  override def extendedLevelsInChunkCache(): Boolean = false

  override def doesBlockHaveSolidTopSurface(i: Int, j: Int, k: Int): Boolean =
    blocks.get(new BlockCoords(i, j, k)) match {
      case Some(e) => Block.blocksList(e.id).isBlockSolidOnSide(this, i, j, k, ForgeDirection.UP)
      case None => false
    }

}
