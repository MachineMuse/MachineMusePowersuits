package net.machinemuse.powersuits.client.render.item

import api.player.render.RenderPlayerAPI
import net.minecraft.entity.Entity
import api.player.model.ModelPlayer
import net.smart.render.SmartRenderModel
import net.smart.render.playerapi.SmartRender

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 9:23 PM, 11/07/13
 */
class SMovingArmorModel extends ModelPlayer(0) with ArmorModel {
  val renderPlayerBase = SmartRender.getPlayerBase(this)
  val model = renderPlayerBase.getRenderModel()

  this.bipedHead = model.bipedHead
  this.bipedBody = model.bipedBody
  this.bipedRightArm = model.bipedRightArm
  this.bipedLeftArm = model.bipedLeftArm
  this.bipedRightLeg = model.bipedRightLeg
  this.bipedLeftLeg = model.bipedLeftLeg
  this.bipedHeadwear = model.bipedHeadwear
  this.bipedEars = model.bipedEars
  this.bipedCloak = model.bipedCloak
  init()

  /**
   * Sets the models various rotation angles then renders the model.
   */
  override def render(entity: Entity, par2: Float, par3: Float, par4: Float, par5: Float, par6: Float, scale: Float) {
    prep(entity, par2, par3, par4, par5, par6, scale)
    renderPlayerBase.setRotationAngles(par2, par3, par4, par5, par6, scale, entity)
    renderPlayerBase.render(entity, par2, par3, par4, par5, par6, scale)
    post(entity, par2, par3, par4, par5, par6, scale)
  }
}