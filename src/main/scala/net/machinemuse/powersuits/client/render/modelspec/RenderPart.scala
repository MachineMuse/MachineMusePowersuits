package net.machinemuse.powersuits.client.render.modelspec

import net.machinemuse.general.NBTTagAccessor
import net.machinemuse.numina.geometry.Colour
import net.machinemuse.numina.render.MuseTextureUtils
import net.machinemuse.powersuits.client.render.item.ArmorModel
import net.machinemuse.utils.render.Render
import net.minecraft.client.model.{ModelBase, ModelRenderer}
import net.minecraft.client.renderer.{OpenGlHelper, RenderHelper}
import net.minecraft.nbt.NBTTagCompound
import org.lwjgl.opengl.GL11

/**
  * Author: MachineMuse (Claire Semple)
  * Created: 4:16 AM, 29/04/13
  */

class RenderPart(base: ModelBase, val parent: ModelRenderer) extends ModelRenderer(base) {
  override def render(scale: Float) {
    val renderSpec = ArmorModel.instance.renderSpec
    import scala.collection.JavaConverters._
    val colours = renderSpec.getIntArray("colours")

    for (nbt <- NBTTagAccessor.getValues(renderSpec).asScala) {
      val part = ModelRegistry.getPart(nbt)
      if (part.isDefined) {
        if (part.get.slot == ArmorModel.instance.visibleSection && part.get.morph.apply(ArmorModel.instance) == parent) {
          var prevBrightX: Float = OpenGlHelper.lastBrightnessX;
          var prevBrightY: Float = OpenGlHelper.lastBrightnessY;

          // GLOW stuff on
          if (part.get.getGlow(nbt)) {
            GL11.glPushAttrib(GL11.GL_LIGHTING_BIT)
            RenderHelper.disableStandardItemLighting()
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F)
          }

          GL11.glPushMatrix()
          GL11.glScaled(scale, scale, scale)
          MuseTextureUtils.bindTexture(part.get.getTexture(nbt))
          applyTransform()
          val ix = part.get.getColourIndex(nbt)
          if (ix < colours.size && ix >= 0) {
            Colour.doGLByInt(colours(ix))
          }
          part.get.modelSpec.applyOffsetAndRotation() // not yet implemented
          part.get.modelSpec.model.renderPart(part.get.partName)
          Colour.WHITE.doGL()
          GL11.glPopMatrix()

          // GLOW stuff off
          if (part.get.getGlow(nbt)) {
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, prevBrightX, prevBrightY)
            GL11.glPopAttrib()
          }
        }
      }
    }
  }

  val degrad = 180F / Math.PI.asInstanceOf[Float]

//  def withMaybeGlow[A](part: ModelPartSpec, nbt: NBTTagCompound)(r: Render[A]): Render[A] = {
//    if (part.getGlow(nbt)) {
//      Render withGlow r
//    } else {
//      r
//    }
//  }

  def applyTransform() {
    //    glTranslatef(rotationPointX, rotationPointY, rotationPointZ)
    //    glRotatef(rotateAngleZ * degrad, 0.0F, 0.0F, 1.0F)
    //    glRotatef(rotateAngleY * degrad, 0.0F, 1.0F, 0.0F)
    //    glRotatef(rotateAngleX * degrad, 1.0F, 0.0F, 0.0F)
    GL11.glRotatef(180, 1.0F, 0.0F, 0.0F)

    GL11.glTranslated(offsetX, offsetY - 26, offsetZ)

  }
}
