package net.machinemuse.powersuits.client.render.modelspec

import net.machinemuse.general.geometry.Colour
import net.minecraft.client.Minecraft
import net.machinemuse.powersuits.client.render.item.ArmorModel
import net.minecraft.nbt.NBTTagCompound
import net.machinemuse.utils.render.Render
import net.minecraft.client.model.{ModelBase, ModelRenderer}
import net.machinemuse.general.NBTTagAccessor
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL11

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:16 AM, 29/04/13
 */

class RenderPart(base: ModelBase) extends ModelRenderer(base) {
  override def render(par1: Float) {
    val renderSpec = ArmorModel.instance.renderSpec
    import scala.collection.JavaConverters._
    val colours = renderSpec.getIntArray("colours")

    for (nbt <- NBTTagAccessor.getValues(renderSpec).asScala) {
      ModelRegistry.getPart(nbt).map(part => {
        if (part.slot == ArmorModel.instance.visible && part.morph.apply(ArmorModel.instance) == this) {
          withMaybeGlow(part, nbt) {
            Render.withPushedMatrix {
              Render pure {
                GL11.glScaled(par1, par1, par1)
                try {
                  Minecraft.getMinecraft.renderEngine.bindTexture(part.getTexture(nbt))
                  applyTransform
                  val ix = part.getColourIndex(nbt)
                  if (ix < colours.size) {
                    Colour.doGLByInt(colours(ix))
                  }
                  part.modelSpec.applyOffsetAndRotation // not yet implemented
                  part.modelSpec.model.renderPart(part.partName)
                  Colour.WHITE.doGL()
                } catch {
                  case e: Throwable =>
                }
              }
            }
          }.run()
        }
      })
    }
  }

  val degrad = 180F / Math.PI.asInstanceOf[Float]

  def withMaybeGlow(part: ModelPartSpec, nbt: NBTTagCompound) = if (part.getGlow(nbt)) {
    a: Render[_] => Render withGlow a
  } else {
    a: Render[_] => a
  }

  def applyTransform {
    glTranslatef(rotationPointX, rotationPointY, rotationPointZ)
    glRotatef(rotateAngleZ * degrad, 0.0F, 0.0F, 1.0F)
    glRotatef(rotateAngleY * degrad, 0.0F, 1.0F, 0.0F)
    glRotatef(rotateAngleX * degrad + 180, 1.0F, 0.0F, 0.0F)
    glTranslated(field_82906_o, field_82907_q - 26, field_82908_p)

  }
}
