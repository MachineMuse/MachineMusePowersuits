package net.machinemuse.powersuits.client.render.modelspec

import net.minecraft.client.model.{ModelBiped, ModelRenderer}
import org.lwjgl.opengl.GL11._

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 2:09 AM, 29/04/13
 */

sealed trait MorphTarget {
  val name: String
  val degrad = 180F / Math.PI.asInstanceOf[Float]

  def apply(m: ModelBiped): ModelRenderer
}

//case object Cloak extends MorphTarget {
//  val name = "Cloak"
//
//  def apply(m: ModelBiped) = m.bipedCloak
//}

case object Head extends MorphTarget {
  val name = "Head"

  def apply(m: ModelBiped) = m.bipedHead
}

case object Body extends MorphTarget {
  val name = "Body"

  def apply(m: ModelBiped) = m.bipedBody
}

case object RightArm extends MorphTarget {
  val name = "RightArm"

  def apply(m: ModelBiped) = m.bipedRightArm
}

case object LeftArm extends MorphTarget {
  val name = "LeftArm"

  def apply(m: ModelBiped) = m.bipedLeftArm
}

case object RightLeg extends MorphTarget {
  val name = "RightLeg"

  def apply(m: ModelBiped) = m.bipedRightLeg
}

case object LeftLeg extends MorphTarget {
  val name = "LeftLeg"

  def apply(m: ModelBiped) = m.bipedLeftLeg
}