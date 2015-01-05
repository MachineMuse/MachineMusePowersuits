package net.machinemuse.powersuits.client.render.item

import api.player.model.ModelPlayer
import net.smart.render.SmartRenderModel
import net.smart.render.playerapi.{SmartRender, SmartRenderModelPlayerBase}
import net.machinemuse.numina.general.MuseLogger
import net.machinemuse.powersuits.client.render.modelspec._
import net.minecraft.client.model.{ModelBiped, ModelRenderer}
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.client.model.obj.WavefrontObject
/**
* Author: MachineMuse (Claire Semple)
* Created: 9:23 PM, 11/07/13
*/
class SMovingArmorModel(paramFloat: Float) extends ModelPlayer(paramFloat) with ArmorModel {
  def this() = this(0)

  private val renderPlayerBase: SmartRenderModelPlayerBase = SmartRender.getPlayerBase(this)
  private val model: SmartRenderModel = renderPlayerBase.getRenderModel()

  private def initPart(mr: ModelRenderer, xo: Float, yo: Float, zo: Float) {
    mr.cubeList.clear()
    val rp = new RenderPart(this, mr)
    mr.addChild(rp)
    rp.offsetX = xo
    rp.offsetY = yo
    rp.offsetZ = zo
  }

  initPart(model.bipedHead, 0.0F, 0.0F, 0.0F)
  initPart(model.bipedBody, 0.0F, 0.0F, 0.0F)
  initPart(model.bipedRightArm, 5, 2.0F, 0.0F)
  initPart(model.bipedLeftArm, -5, 2.0F, 0.0F)
  initPart(model.bipedRightLeg, 2, 12.0F, 0.0F)
  initPart(model.bipedLeftLeg, -2, 12.0F, 0.0F)

  model.bipedHeadwear.cubeList.clear()
  model.bipedEars.cubeList.clear()
  model.bipedCloak.cubeList.clear()

  private def logModelParts(model: WavefrontObject) {
    MuseLogger.logDebug(model.toString + ":")
    import scala.collection.JavaConversions._
    for (group <- model.groupObjects) {
      MuseLogger.logDebug("-" + group.name)
    }
  }

  private def prep(entity: Entity, par2: Float, par3: Float, par4: Float, par5: Float, par6: Float, scale: Float) {
    try {
      val entLive: EntityLivingBase = entity.asInstanceOf[EntityLivingBase]
      val stack: ItemStack = entLive.getEquipmentInSlot(0)
      this.heldItemRight = if (stack != null) 1 else 0
      this.isSneak = entLive.isSneaking
      this.aimedBow = entLive.asInstanceOf[EntityPlayer].getItemInUse != null
    } catch {
      case _: Exception =>
    }

    model.bipedHead.isHidden = false
    model.bipedBody.isHidden = false
    model.bipedRightArm.isHidden = false
    model.bipedLeftArm.isHidden = false
    model.bipedRightLeg.isHidden = false
    model.bipedLeftLeg.isHidden = false
    model.bipedHead.showModel = true
    model.bipedBody.showModel = true
    model.bipedRightArm.showModel = true
    model.bipedLeftArm.showModel = true
    model.bipedRightLeg.showModel = true
    model.bipedLeftLeg.showModel = true

    renderPlayerBase.setRotationAngles(par2, par3, par4, par5, par6, scale, entity)

  }

  /**
  * Sets the models various rotation angles then renders the model.
  */
  override def render(entity: Entity, par2: Float, par3: Float, par4: Float, par5: Float, par6: Float, scale: Float) {
    prep(entity, par2, par3, par4, par5, par6, scale)
    renderPlayerBase.render(entity, par2, par3, par4, par5, par6, scale)
  }
}
