package net.machinemuse.powersuits.client.render.item

import net.machinemuse.powersuits.client.render.modelspec._
import net.minecraft.client.model.{ModelBiped, ModelRenderer}
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{EnumAction, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.machinemuse.numina.general.MuseLogger
import net.minecraft.client.model.ModelBiped.ArmPose
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.util.EnumHandSide
import net.minecraftforge.client.model.obj.OBJModel

object ArmorModel {
  def instance: ArmorModel = ArmorModelInstance.getInstance()
}

trait ArmorModel extends ModelBiped {
  var renderSpec: NBTTagCompound = null
  var visibleSection: EntityEquipmentSlot = EntityEquipmentSlot.HEAD // TODO is this the right one to start with?

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
//    bipedEars.cubeList.clear()
//    bipedCloak.cubeList.clear()
  }

  // FIXME: needs to
  private def logModelParts(model: OBJModel) {
//    MuseLogger.logDebug(model.toString + ":")
//    import scala.collection.JavaConversions._
//    for (group <- model.groupObjects) {
//      MuseLogger.logDebug("-" + group.name)
//    }
  }


  def setInitialOffsets(r: ModelRenderer, x: Float, y: Float, z: Float) {
    r.offsetX = x
    r.offsetY = y
    r.offsetZ = z
  }


  def prep(entity: Entity, par2: Float, par3: Float, par4: Float, par5: Float, par6: Float, scale: Float) {
    try {
      val entLive: EntityLivingBase = entity.asInstanceOf[EntityLivingBase]
      val stack: ItemStack = entLive.getActiveItemStack
      if (stack != null) {
        // FIXME:
//        Error:(65, 13) Implementation restriction: trait ArmorModel accesses protected method getMainHand inside a concrete trait method.
//        Add an accessor in a class extending class ModelBiped as a workaround.
//        if (getMainHand(entLive) == EnumHandSide.LEFT)


//        if (getMainHand(entLive) == EnumHandSide.LEFT)
//          this.leftArmPose = ArmPose.ITEM
//        else
//          this.rightArmPose = ArmPose.ITEM
//      } else {
//        if (getMainHand(entLive) == EnumHandSide.LEFT)
//          this.leftArmPose = ArmPose.EMPTY
//        else
//          this.rightArmPose = ArmPose.EMPTY
      }


      isSneak = entLive.isSneaking
      isRiding = entLive.isRiding
      val entPlayer = entLive.asInstanceOf[EntityPlayer]
      if ((stack != null) && (entPlayer.getItemInUseCount > 0))
      {
        val enumaction = stack.getItemUseAction
        if (enumaction == EnumAction.BLOCK) {
          // FIXME:
//          Error:(95, 15) Implementation restriction: trait ArmorModel accesses protected method getMainHand inside a concrete trait method.
//          Add an accessor in a class extending class ModelBiped as a workaround.
//          if (getMainHand(entLive) == EnumHandSide.LEFT)

//          if (getMainHand(entLive) == EnumHandSide.LEFT)
//            this.leftArmPose = ArmPose.BLOCK
//          else
//            this.rightArmPose = ArmPose.BLOCK
//        } else if (enumaction == EnumAction.BOW) {
//          if (getMainHand(entLive) == EnumHandSide.LEFT)
//            this.leftArmPose = ArmPose.BOW_AND_ARROW
//          else
//            this.rightArmPose = ArmPose.BOW_AND_ARROW
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
    leftArmPose = ArmPose.EMPTY
    rightArmPose = ArmPose.EMPTY
    isSneak = false
  }
}