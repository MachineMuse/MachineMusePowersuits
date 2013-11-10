package net.machinemuse.utils.render

import org.lwjgl.opengl.GL11._
import net.minecraft.client.gui.ScaledResolution
import org.lwjgl.opengl.GL13._
import net.minecraft.client.Minecraft
import net.machinemuse.utils.render.Render._
import net.machinemuse.powersuits.common.Config
import net.machinemuse.numina.geometry.Colour
import net.machinemuse.numina.render.RenderState
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 6:02 PM, 5/15/13
 */
object GlowBuffer {
  // Texture dimension. Maybe make this configurable later
  val texDimension = 512

  val glowBuffer = new TextureBuffer(texDimension)
  val secondBuffer = new TextureBuffer(texDimension)
  val depthBuffer = new TextureBuffer(texDimension)

  val matrixBuffer = BufferUtils.createFloatBuffer(16)

  def draw[A](f: Render[A]): Render[A] = Render pure {
    glowBuffer.bindWrite()
    val a = f.run()
    glowBuffer.unbindWrite()
    a
  }

  def drawFullScreen(screen: ScaledResolution) {
    import Render._
    pushDualIdentityMatrix()
    RenderState.glowOn()
    RenderState.blendingOn()
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
            MuseShaders.depthApplicatorProgram.setTexUnit("u_Depth", 1)

            matrixBuffer.clear()
            glGetFloat(GL_MODELVIEW_MATRIX, matrixBuffer)
            matrixBuffer.flip()
            MuseShaders.depthApplicatorProgram.setUniformMatrix4("u_ModelViewProjectionMatrix", matrixBuffer)

            val u = Minecraft.getMinecraft.displayWidth.toDouble / texDimension.toDouble
            val v = Minecraft.getMinecraft.displayHeight.toDouble / texDimension.toDouble
            drawTexSquare(texDimension, u, v)
          }
        }
      }
    }.run()


    fromBuffer(glowBuffer) {
      toBuffer(secondBuffer) {
        withShaderProgram(MuseShaders.depthOcclusionProgram) {
          Render pure {
            glActiveTexture(GL_TEXTURE1)
            glBindTexture(GL_TEXTURE_2D, secondBuffer.depthRenderBufferID)
            glActiveTexture(GL_TEXTURE2)
            glBindTexture(GL_TEXTURE_2D, glowBuffer.depthRenderBufferID)
            glActiveTexture(GL_TEXTURE0)
            glBindTexture(GL_TEXTURE_2D, glowBuffer.colorTextureID)

            MuseShaders.depthOcclusionProgram.setTexUnit("u_Texture", 0)
            MuseShaders.depthOcclusionProgram.setTexUnit("u_Occlusion", 1)
            MuseShaders.depthOcclusionProgram.setTexUnit("u_Depth", 2)

            matrixBuffer.clear()
            glGetFloat(GL_MODELVIEW_MATRIX, matrixBuffer)
            matrixBuffer.flip()
            MuseShaders.depthOcclusionProgram.setUniformMatrix4("u_ModelViewProjectionMatrix", matrixBuffer)

            drawTexSquare(texDimension, 1, 1)
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
        drawTexSquare(texDimension, 1, 1)
      }
    }.run()


    glPopAttrib()
    RenderState.blendingOff()
    RenderState.glowOff()
    popDualIdentityMatrix()
  }

  def doBlurPasses() {
    // Vertical Pass
    fromBuffer(secondBuffer) {
      toBuffer(glowBuffer) {
        withShaderProgram(MuseShaders.vBlurProgram) {
          Render pure {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

            matrixBuffer.clear()
            glGetFloat(GL_MODELVIEW_MATRIX, matrixBuffer)
            matrixBuffer.flip()
            MuseShaders.vBlurProgram.setUniformMatrix4("u_ModelViewProjectionMatrix", matrixBuffer)

            MuseShaders.vBlurProgram.setUniform2f("u_Scale", 0, 1.0f / texDimension)
            MuseShaders.vBlurProgram.setTexUnit("u_Texture", 0)
            drawTexSquare(texDimension, 1, 1)
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

            matrixBuffer.clear()
            glGetFloat(GL_MODELVIEW_MATRIX, matrixBuffer)
            matrixBuffer.flip()
            MuseShaders.hBlurProgram.setUniformMatrix4("u_ModelViewProjectionMatrix", matrixBuffer)
            MuseShaders.hBlurProgram.setUniform2f("u_Scale", 1.0f / texDimension, 0)
            MuseShaders.hBlurProgram.setTexUnit("u_Texture", 0)
            drawTexSquare(texDimension, 1, 1)
          }
        }
      }
    }.run()
  }

  def drawTexSquare(td: Int, u: Double, v: Double) {
    //    MuseLogger.logDebug("Begin rendering")
    glBegin(GL_QUADS)
    vertUV(0, 0, 0, v)
    vertUV(0, td, 0, 0)
    vertUV(td, td, u, 0)
    vertUV(td, 0, u, v)
    glEnd()
    //    MuseLogger.logDebug("Done rendering")
  }

  private def vertUV(x: Double, y: Double, u: Double, v: Double) {
    GL20.glVertexAttrib2d(1, u, v)
    GL20.glVertexAttrib2d(0, x, y)
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
    glMatrixMode(GL_PROJECTION)
    glPopMatrix()

    glMatrixMode(GL_MODELVIEW)
    glPopMatrix()

    glMatrixMode(GL_PROJECTION)
  }


  def clear() {
    glowBuffer.clear()
    secondBuffer.clear()
  }

}
