package net.machinemuse.powersuits.client.render.item

import net.machinemuse.powersuits.client.render.modelspec._
import net.minecraft.client.model.{ModelBiped, ModelRenderer}
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{EnumAction, ItemStack}
import net.minecraftforge.client.model.obj.WavefrontObject
import net.minecraft.nbt.NBTTagCompound
import net.machinemuse.numina.general.MuseLogger

object ArmorModel {
  def instance: ArmorModel = ArmorModelInstance.getInstance()
}

trait ArmorModel extends ModelBiped {
  var renderSpec: NBTTagCompound = null
  var visibleSection: Int = 0

  def clearAndAddChildWithInitialOffsets(mr: ModelRenderer, xo: Float, yo: Float, zo: Float) {
    mr.cubeList.clear()
    val rp = new RenderPart(this, mr)
    mr.addChild(rp)
    setInitialOffsets(rp, xo, yo, zo)
  }

  def init(): Unit = {
    clearAndAddChildWithInitialOffsets(bipedHead, 0.0F, 0.0F, 0.0F)
    clearAndAddChildWithInitialOffsets(bipedBody, 0.0F, 0.0F, 0.0F)
    clearAndAddChildWithInitialOffsets(bipedRightArm, 5, 2.0F, 0.0F)
    clearAndAddChildWithInitialOffsets(bipedLeftArm, -5, 2.0F, 0.0F)
    clearAndAddChildWithInitialOffsets(bipedRightLeg, 2, 12.0F, 0.0F)
    clearAndAddChildWithInitialOffsets(bipedLeftLeg, -2, 12.0F, 0.0F)
    bipedHeadwear.cubeList.clear()
    bipedEars.cubeList.clear()
    bipedCloak.cubeList.clear()
  }


  private def logModelParts(model: WavefrontObject) {
    MuseLogger.logDebug(model.toString + ":")
    import scala.collection.JavaConversions._
    for (group <- model.groupObjects) {
      MuseLogger.logDebug("-" + group.name)
    }
  }


  def setInitialOffsets(r: ModelRenderer, x: Float, y: Float, z: Float) {
    r.offsetX = x
    r.offsetY = y
    r.offsetZ = z
  }


  def prep(entity: Entity, par2: Float, par3: Float, par4: Float, par5: Float, par6: Float, scale: Float) {
    try {
      val entLive: EntityLivingBase = entity.asInstanceOf[EntityLivingBase]
      val stack: ItemStack = entLive.getEquipmentInSlot(0)
      heldItemRight = if (stack != null) 1 else 0
      isSneak = entLive.isSneaking
      isRiding = entLive.isRiding
      val entPlayer = entLive.asInstanceOf[EntityPlayer]
      if ((stack != null) && (entPlayer.getItemInUseCount > 0))
      {
        val enumaction = stack.getItemUseAction
        if (enumaction == EnumAction.block) {
          heldItemRight = 3
        } else if (enumaction == EnumAction.bow) {
          aimedBow = true
        }
      }
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
  }

  def post(entity: Entity, par2: Float, par3: Float, par4: Float, par5: Float, par6: Float, scale: Float): Unit = {
    aimedBow = false
    isSneak = false
    heldItemRight = 0
  }
}