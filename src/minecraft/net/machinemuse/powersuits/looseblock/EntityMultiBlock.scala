package net.machinemuse.powersuits.looseblock

import scala.collection.mutable
import net.minecraft.tileentity.TileEntity
import net.minecraft.block.Block

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:21 PM, 07/05/13
 */
class EntityMultiBlock {
  val blocks: mutable.Buffer[BlockForEntity] = mutable.Buffer.empty

}

class BlockForEntity(val x: Int, val y: Int, val z: Int, val id: Int, val meta: Int, val tileEntity: Option[TileEntity]) {
  def block: Block = Block.blocksList(id)

  def render() {
    block.renderAsNormalBlock()
  }
}