package net.machinemuse.powersuits.client.render.item

import net.machinemuse.general.{NBTTagAccessor, MuseLogger}
import net.machinemuse.general.geometry.Colour
import net.machinemuse.powersuits.client.render.modelspec._
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.model.ModelRenderer
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.client.model.obj.WavefrontObject
import org.lwjgl.opengl.GL11._
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.src.ModelPlayer

object ArmorModel {
  val instance = new ArmorModel(0.0F, 0.0f, 64, 32)
}

class ArmorModel(par1: Float, par2: Float, par3: Int, par4: Int) extends ModelPlayer(0.0F) {
  var renderSpec: NBTTagCompound = null
  var visible: Int = 0
  bipedHeadwear.showModel = false
  bipedHead = new RenderPart(this)
  bipedBody = new RenderPart(this)
  bipedRightArm = new RenderPart(this)
  bipedLeftArm = new RenderPart(this)
  bipedRightLeg = new RenderPart(this)
  bipedLeftLeg = new RenderPart(this)
  setInitialOffsets(bipedHead, 0.0F, 0.0F + par2, 0.0F)
  setInitialOffsets(bipedBody, 0.0F, 0.0F + par2, 0.0F)
  setInitialOffsets(bipedRightArm, 5, 2.0F + par2, 0.0F)
  setInitialOffsets(bipedLeftArm, -5, 2.0F + par2, 0.0F)
  setInitialOffsets(bipedRightLeg, 2, 12.0F + par2, 0.0F)
  setInitialOffsets(bipedLeftLeg, -2, 12.0F + par2, 0.0F)
  this.bipedHead.setRotationPoint(0.0F, 0.0F + par2, 0.0F)
  this.bipedBody.setRotationPoint(0.0F, 0.0F + par2, 0.0F)
  this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + par2, 0.0F)
  this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + par2, 0.0F)
  this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + par2, 0.0F)
  this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + par2, 0.0F)

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