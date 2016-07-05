package net.machinemuse.powersuits.client.render.modelspec

import net.machinemuse.powersuits.client.render.item.ArmorModel
import net.machinemuse.utils.render.Render
import net.minecraft.client.model.{ModelBase, ModelRenderer}
import net.machinemuse.general.NBTTagAccessor
import org.lwjgl.opengl.GL11._
import net.minecraft.nbt.NBTTagCompound
import net.machinemuse.numina.geometry.Colour
import net.machinemuse.numina.render.MuseTextureUtils

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:16 AM, 29/04/13
 */

class RenderPart(base: ModelBase, val parent: ModelRenderer) extends ModelRenderer(base) {
  override def render(scale: Float) {
    val renderSpec = ArmorModel.instance.renderSpec
    import scala.collection.JavaConverters._
    val colours = renderSpec.getIntArray("colours")

    for {
      nbt <- NBTTagAccessor.getValues(renderSpec).asScala
      part <- ModelRegistry.getPart(nbt)
      if part.slot == ArmorModel.instance.visibleSection
      if part.morph.apply(ArmorModel.instance) == parent
    } {
      withMaybeGlow(part, nbt) {
        Render.withPushedMatrix {
          Render {
            glScaled(scale, scale, scale)
            MuseTextureUtils.bindTexture(part.getTexture(nbt))
            applyTransform
            val ix = part.getColourIndex(nbt)
            if (ix < colours.size && ix >= 0) {
              Colour.doGLByInt(colours(ix))
            }
            part.modelSpec.applyOffsetAndRotation // not yet implemented
            // FIXME!!!!!

//            part.modelSpec.model.renderPart(part.partName)
            Colour.WHITE.doGL()
          }
        }
      }.run()

    }
  }

  val degrad = 180F / Math.PI.asInstanceOf[Float]

  def withMaybeGlow[A](part: ModelPartSpec, nbt: NBTTagCompound)(r: Render[A]): Render[A] = {
    if (part.getGlow(nbt)) {
      Render withGlow r
    } else {
      r
    }
  }

  def applyTransform {
    //    glTranslatef(rotationPointX, rotationPointY, rotationPointZ)
    //    glRotatef(rotateAngleZ * degrad, 0.0F, 0.0F, 1.0F)
    //    glRotatef(rotateAngleY * degrad, 0.0F, 1.0F, 0.0F)
    //    glRotatef(rotateAngleX * degrad, 1.0F, 0.0F, 0.0F)
    glRotatef(180, 1.0F, 0.0F, 0.0F)

    glTranslated(offsetX, offsetY - 26, offsetZ)

  }
}
