package net.machinemuse.utils.render

import org.lwjgl.opengl.GL11._
import net.minecraft.client.gui.ScaledResolution
import net.machinemuse.general.geometry.Colour
import org.lwjgl.opengl.GL13._
import net.minecraft.client.Minecraft
import net.machinemuse.utils.render.Render._
import net.machinemuse.powersuits.common.Config

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 6:02 PM, 5/15/13
 */
object GlowBuffer {
  // Texture dimension. Maybe make this configurable later
  val texDimension = 1024

  val glowBuffer = new TextureBuffer(texDimension)
  val secondBuffer = new TextureBuffer(texDimension)
  val depthBuffer = new TextureBuffer(texDimension)

  def draw[A](f: Render[A]): Render[A] = Render pure {
    glowBuffer.bindWrite()
    val a = f.run()
    glowBuffer.unbindWrite()
    a
  }

  def drawFullScreen(screen: ScaledResolution) {
    import Render._
    pushDualIdentityMatrix()
    MuseRenderer.glowOn()
    MuseRenderer.blendingOn()
    glPushAttrib(GL_ENABLE_BIT)

    glDisable(GL_CULL_FACE)
    Colour.WHITE.doGL()
    glActiveTexture(GL_TEXTURE0)

    glBindTexture(GL_TEXTURE_2D, depthBuffer.depthRenderBufferID)
    glCopyTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, 0, 0, texDimension, texDimension)
    glBindTexture(GL_TEXTURE_2D, 0)

    fromBuffer(depthBuffer) {
      toBuffer(secondBuffer) {
        withShaderProgram(MuseShaders.depthApplicatorProgram) {
          Render pure {
            glActiveTexture(GL_TEXTURE1)
            glBindTexture(GL_TEXTURE_2D, depthBuffer.depthRenderBufferID)
            MuseShaders.depthProgram.setTexUnit("depth", 1)
            glBegin(GL_QUADS)
            val t = texDimension
            val w = Minecraft.getMinecraft.displayWidth.toDouble
            val h = Minecraft.getMinecraft.displayHeight.toDouble
            val u = w / texDimension.toDouble
            val v = h / texDimension.toDouble
            vertUV(0, 0, 0, v)
            vertUV(0, t, 0, 0)
            vertUV(t, t, u, 0)
            vertUV(t, 0, u, v)
            glEnd()
          }
        }
      }
    }.run()


    fromBuffer(glowBuffer) {
      toBuffer(secondBuffer) {
        withShaderProgram(MuseShaders.depthProgram) {
          Render pure {
            glActiveTexture(GL_TEXTURE1)
            glBindTexture(GL_TEXTURE_2D, secondBuffer.depthRenderBufferID)
            glActiveTexture(GL_TEXTURE2)
            glBindTexture(GL_TEXTURE_2D, glowBuffer.depthRenderBufferID)
            glActiveTexture(GL_TEXTURE0)
            glBindTexture(GL_TEXTURE_2D, glowBuffer.colorTextureID)
            MuseShaders.depthProgram.setTexUnit("depth", 2)
            MuseShaders.depthProgram.setTexUnit("occlusion", 1)
            MuseShaders.depthProgram.setTexUnit("texture", 0)
            drawTexSquare(texDimension)
          }
        }
      }
    }.run()

    glActiveTexture(GL_TEXTURE1)
    glBindTexture(GL_TEXTURE_2D, 0)
    glActiveTexture(GL_TEXTURE2)
    glBindTexture(GL_TEXTURE_2D, 0)
    glActiveTexture(GL_TEXTURE0)
    glDisable(GL_DEPTH_TEST)

    for (i <- 1 until Config.glowMultiplier) {
      doBlurPasses()
    }

    //    To Main display
    fromBuffer(secondBuffer) {
      Render.pure {
        drawTexSquare(texDimension)
      }
    }.run()


    glPopAttrib()
    MuseRenderer.blendingOff()
    MuseRenderer.glowOff()
    popDualIdentityMatrix()
  }

  def doBlurPasses() {
    // Vertical Pass
    fromBuffer(secondBuffer) {
      toBuffer(glowBuffer) {
        withShaderProgram(MuseShaders.vBlurProgram) {
          Render pure {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
            MuseShaders.vBlurProgram.setUniform2f("u_Scale", 0, 1.0f / texDimension)
            MuseShaders.vBlurProgram.setTexUnit("u_Texture0", 0)
            drawTexSquare(texDimension)
          }
        }
      }
    }.run()


    // Horizontal Pass
    fromBuffer(glowBuffer) {
      toBuffer(secondBuffer) {
        withShaderProgram(MuseShaders.hBlurProgram) {
          Render.pure {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
            MuseShaders.hBlurProgram.setUniform2f("u_Scale", 1.0f / texDimension, 0)
            MuseShaders.hBlurProgram.setTexUnit("u_Texture0", 0)
            drawTexSquare(texDimension)
          }
        }
      }
    }.run()
  }

  def drawTexSquare(td: Int) {
    //    MuseLogger.logDebug("Begin rendering")
    glBegin(GL_QUADS)
    vertUV(0, 0, 0, 1)
    vertUV(0, td, 0, 0)
    vertUV(td, td, 1, 0)
    vertUV(td, 0, 1, 1)
    glEnd()
    //    MuseLogger.logDebug("Done rendering")
  }

  private def vertUV(x: Double, y: Double, u: Double, v: Double) {
    glTexCoord2d(u, v)
    glVertex2d(x, y)
  }

  private def pushDualIdentityMatrix() {
    glMatrixMode(GL_MODELVIEW)
    glPushMatrix()
    glLoadIdentity()
    glOrtho(0.0f, texDimension, texDimension, 0.0f, -1.0f, 1.0f)

    glMatrixMode(GL_PROJECTION)
    glPushMatrix()
    glLoadIdentity()
  }

  private def popDualIdentityMatrix() {
    glMatrixMode(GL_MODELVIEW)
    glPopMatrix()

    glMatrixMode(GL_PROJECTION)
    glPopMatrix()
  }


  def clear() {
    glowBuffer.clear()
    secondBuffer.clear()
  }

}
