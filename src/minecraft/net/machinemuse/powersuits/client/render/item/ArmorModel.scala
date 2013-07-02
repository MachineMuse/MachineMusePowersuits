package net.machinemuse.powersuits.client.render.item

import net.machinemuse.general.MuseLogger
import net.machinemuse.powersuits.client.render.modelspec._
import net.minecraft.client.model.{ModelBiped, ModelRenderer}
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.client.model.obj.WavefrontObject
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.src.ModelPlayer

object ArmorModel {
  val instance:ArmorModel = try {
    new SMovingArmorModel
  } catch {
    case _:Throwable => new VanillaArmorModel
  }
}

class SMovingArmorModel extends ModelPlayer(0) with ArmorModel

class VanillaArmorModel extends ModelBiped(0) with ArmorModel

trait ArmorModel extends ModelBiped {
  var renderSpec: NBTTagCompound = null
  var visible: Int = 0

  def clearAndAddChildWithInitialOffsets(mr: ModelRenderer, xo: Float, yo: Float, zo: Float) {
    mr.cubeList.clear()
    val rp = new RenderPart(this, mr)
    mr.addChild(rp)
    setInitialOffsets(rp, xo, yo, zo)
  }

  clearAndAddChildWithInitialOffsets(bipedHead, 0.0F, 0.0F, 0.0F)
  clearAndAddChildWithInitialOffsets(bipedBody, 0.0F, 0.0F, 0.0F)
  clearAndAddChildWithInitialOffsets(bipedRightArm, 5, 2.0F, 0.0F)
  clearAndAddChildWithInitialOffsets(bipedLeftArm, -5, 2.0F, 0.0F)
  clearAndAddChildWithInitialOffsets(bipedRightLeg, 2, 12.0F, 0.0F)
  clearAndAddChildWithInitialOffsets(bipedLeftLeg, -2, 12.0F, 0.0F)

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


  def setInitialOffsets(r: ModelRenderer, x: Float, y: Float, z: Float) {
    r.field_82906_o = x
    r.field_82907_q = y
    r.field_82908_p = z
  }

  /**
   * Sets the models various rotation angles then renders the model.
   */
  override def render(entity: Entity, par2: Float, par3: Float, par4: Float, par5: Float, par6: Float, scale: Float) {
    try {
      val entLive: EntityLiving = entity.asInstanceOf[EntityLiving]
      val stack: ItemStack = entLive.getCurrentItemOrArmor(0)
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
    super.render(entity, par2, par3, par4, par5, par6, scale)
  }
}