package net.machinemuse.powersuits.client.render.modelspec

import net.machinemuse.general.NBTTagAccessor
import net.machinemuse.numina.geometry.Colour
import net.machinemuse.numina.render.MuseTextureUtils
import net.machinemuse.powersuits.client.render.item.ArmorModel
import net.machinemuse.utils.render.Render
import net.minecraft.client.model.{ModelBase, ModelRenderer}
import net.minecraft.nbt.NBTTagCompound
import org.lwjgl.opengl.GL11._

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
            part.modelSpec.applyOffsetAndRotation




//----------------------------------------------------------------------------
//             val tess: Tessellator = Tessellator.getInstance()
//
//            val wr: VertexBuffer  = tess.getBuffer();
//
//            wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
//
//            for (b: BakedQuad <- part.modelSpec.model.getQuads(null, null, 0)) {
//              wr.addVertexData(b.getVertexData());
//            }
//
//            tess.draw();
//
//            GlStateManager.popMatrix();
//---------------------------------------------------------------------------


//            if (parts.isEmpty()) { return; }
//            Tessellator tessellator = Tessellator.getInstance();
//            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
//            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
//            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);
//
//            for (IModelPart id : parts) {
//              if (id == null) { continue; }
//              ITexture texture = id.getTexture();
//              for (IQuad quad : id.getQuads()) {
//                Vect3d[] pos = quad.getVertex();
//                Vect2d[] uv = quad.getUV();
//                Vect3d[] norm = quad.getNormals();
//                for (int i = 0; i < 4; i++) {
//                  worldRenderer
//                    .pos(pos[i].getX(), pos[i].getY(), pos[i].getZ())
//                    .tex(texture.getSprite().getInterpolatedU(uv[i].getX()*16d),
//                      texture.getSprite().getInterpolatedV(uv[i].getY()*16d))
//                    .normal((float) norm[i].getX(), (float) norm[i].getY(), (float) norm[i].getZ())
//                  .endVertex();
//                }
//              }
//            }
//            tessellator.draw();











            /*
              Here a different approach is needed. Originally Wavefront models were rendered raw without optimizations and rendering hooks were built in. Now

              Models are now baked(optimized) early in the loading cycle
             */







//            part.modelSpec.model.renderPart(part.partName)
//            Colour.WHITE.doGL()


















            //======================================================================
            val thepart = part.modelSpec.model.getMatLib.getGroups.get(part.partName)





//            part.modelSpec.model.getMatLib.getGroups.get(part.partName).asInstanceOf[ModelBase] // haha , uh no

//              renderPart(part.partName)





            // not yet implemented
            // FIXME!!!!!
            /*
             * there needs to he a way to access parts of the model.
             */


//            part.modelSpec.model.renderPart(part.partName)




            /*
              MuseTextureUtils.pushTexture(Config.TEXTURE_PREFIX() + "models/thusters_uvw_2.png");
              glPushMatrix();
              glTranslated(x, y, z);
              double scale = 0.0625;
              glScaled(scale, scale, scale);


              worldRenderer.begin(GL11.GL_QUADS, Attributes.DEFAULT_BAKED_FORMAT);
              java.util.List<BakedQuad> generalQuads = getBakedFrameModel().getGeneralQuads();
              for (BakedQuad q : generalQuads)
              {
                int[] vd = q.getVertexData();
                worldRenderer.addVertexData(vd);
              }
              for (EnumFacing face : EnumFacing.values())
              {
                java.util.List<BakedQuad> faceQuads = getBakedFrameModel().getFaceQuads(face);
                for (BakedQuad q : faceQuads)
                {
                  int[] vd = q.getVertexData();
                  worldRenderer.addVertexData(vd);
                }
              }
              tessellator.draw();




              //getFrameModel().renderAll();
              RenderState.glowOn();
              new Colour(entity.red, entity.green, entity.blue, 1.0).doGL();
              //getLightModel().renderAll();
              RenderState.glowOff();
              glPopMatrix();
              MuseTextureUtils.popTexture();
            */











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
