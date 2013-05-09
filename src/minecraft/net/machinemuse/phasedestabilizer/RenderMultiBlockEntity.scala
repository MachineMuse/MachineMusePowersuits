package net.machinemuse.phasedestabilizer

import net.minecraft.client.renderer.entity.Render
import net.minecraft.entity.Entity

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 1:08 PM, 5/8/13
 */
class RenderMultiBlockEntity extends Render {


  def doRender(entity: EntityMultiBlock, x: Double, y: Double, z: Double, yaw: Float, partialTickTime: Float) {
    for(b<-entity.innerWorld.blocks.values) {

    }

  }

  def doRender(entity: Entity, x: Double, y: Double, z: Double, yaw: Float, partialTickTime: Float) {
    if (entity.isInstanceOf[EntityMultiBlock]) {
      doRender(entity.asInstanceOf[EntityMultiBlock], x, y, z, yaw, partialTickTime)
    }
  }
}
