package net.machinemuse.phasedestabilizer

import net.minecraft.tileentity.TileEntity
import net.minecraft.block.Block

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 11:31 AM, 5/8/13
 */
class BlockEntry(val coords:BlockCoords, val id:Int, val meta:Int=0, var tileEntity:TileEntity=null) {
  val block = Block.blocksList(id)
}

class BlockCoords(x: Int, y: Int, z: Int, val hashSize: Int = 1290) extends Tuple3(x,y,z) {
  override val hashCode = _1 * hashSize * hashSize + _2 * hashSize + _3

  override def equals(other: Any) = {
    other match {
      case o: BlockCoords => _1 == o._1 && _2 == o._2 && _3 == o._3
      case _ => other == this
    }
  }
}