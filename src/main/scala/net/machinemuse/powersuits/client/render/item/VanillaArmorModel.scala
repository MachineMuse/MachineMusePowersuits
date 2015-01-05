package net.machinemuse.powersuits.client.render.item

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
* Created: 9:24 PM, 11/07/13
*/
class VanillaArmorModel extends ModelBiped with ArmorModel {
  private def initPart(mr: ModelRenderer, xo: Float, yo: Float, zo: Float) {
    mr.cubeList.clear()
    val rp = new RenderPart(this, mr)
    mr.addChild(rp)
    rp.offsetX = xo
    rp.offsetY = yo
    rp.offsetZ = zo
  }

  initPart(bipedHead, 0.0F, 0.0F, 0.0F)
  initPart(bipedBody, 0.0F, 0.0F, 0.0F)
  initPart(bipedRightArm, 5, 2.0F, 0.0F)
  initPart(bipedLeftArm, -5, 2.0F, 0.0F)
  initPart(bipedRightLeg, 2, 12.0F, 0.0F)
  initPart(bipedLeftLeg, -2, 12.0F, 0.0F)

  bipedHeadwear.cubeList.clear()
  bipedEars.cubeList.clear()
  bipedCloak.cubeList.clear()

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

    bipedHead.isHidden = false
    bipedBody.isHidden = false
    bipedRightArm.isHidden = false
    bipedLeftArm.isHidden = false
    bipedRightLeg.isHidden = false
    bipedLeftLeg.isHidden = false
    bipedHead.showModel = true
    bipedBody.showModel = true
    bipedRightArm.showModel = true
    bipedLeftArm.showModel = true
    bipedRightLeg.showModel = true
    bipedLeftLeg.showModel = true

    setRotationAngles(par2, par3, par4, par5, par6, scale, entity)

  }

/**
  * Sets the models various rotation angles then renders the model.
  */
  override def render(entity: Entity, par2: Float, par3: Float, par4: Float, par5: Float, par6: Float, scale: Float) {
    prep(entity, par2, par3, par4, par5, par6, scale)
    super.render(entity, par2, par3, par4, par5, par6, scale)
  }
}
