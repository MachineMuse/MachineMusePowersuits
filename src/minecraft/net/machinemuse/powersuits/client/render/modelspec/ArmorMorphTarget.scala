package net.machinemuse.powersuits.client.render.modelspec

import net.minecraft.client.model.{ModelBiped, ModelRenderer}
import org.lwjgl.opengl.GL11._

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 2:09 AM, 29/04/13
 */

sealed trait MorphTarget {
  val name: String
  val degrad = (180F / Math.PI.asInstanceOf[Float])

  def apply(m: ModelBiped): ModelRenderer

  def applyTransform(m: ModelRenderer) = {
    glTranslatef(m.rotationPointX, m.rotationPointY, m.rotationPointZ)
    glRotatef(m.rotateAngleZ * degrad, 0.0F, 0.0F, 1.0F)
    glRotatef(m.rotateAngleY * degrad, 0.0F, 1.0F, 0.0F)
    glRotatef(m.rotateAngleX * degrad + 180, 1.0F, 0.0F, 0.0F)
    glTranslated(m.field_82906_o, m.field_82907_q - 26, m.field_82908_p)
    m
  }
}

case object Cloak extends MorphTarget {
  val name = "Cloak"

  def apply(m: ModelBiped) = applyTransform(m.bipedCloak)
}

case object Head extends MorphTarget {
  val name = "Head"

  def apply(m: ModelBiped) = applyTransform(m.bipedHead)
}

case object Body extends MorphTarget {
  val name = "Body"

  def apply(m: ModelBiped) = applyTransform(m.bipedBody)
}

case object RightArm extends MorphTarget {
  val name = "RightArm"

  def apply(m: ModelBiped) = applyTransform(m.bipedRightArm)
}

case object LeftArm extends MorphTarget {
  val name = "LeftArm"

  def apply(m: ModelBiped) = applyTransform(m.bipedLeftArm)
}

case object RightLeg extends MorphTarget {
  val name = "RightLeg"

  def apply(m: ModelBiped) = applyTransform(m.bipedRightLeg)
}

case object LeftLeg extends MorphTarget {
  val name = "LeftLeg"

  def apply(m: ModelBiped) = applyTransform(m.bipedLeftLeg)
}