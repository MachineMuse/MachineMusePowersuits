package net.machinemuse.utils

import org.lwjgl.opengl.{ARBShaderObjects, GL14, GLContext}
import org.lwjgl.opengl.EXTFramebufferObject._
import org.lwjgl.opengl.GL11._
import java.nio.{ByteBuffer, ByteOrder}
import net.minecraft.client.gui.ScaledResolution

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 6:02 PM, 5/15/13
 */
object GlowBuffer {
  // Texture dimension. Maybe make this configurable later
  val texDimension = 512

  val glowBuffer:TextureBuffer = new TextureBuffer(texDimension)
  val secondBuffer:TextureBuffer = new TextureBuffer(texDimension)

  def draw[A](f: () => A): A = {
    glowBuffer.bindWrite()
    val a = f()
    glowBuffer.unbindWrite()
    a
  }

  def drawFullScreen(screen:ScaledResolution) {
    pushDualIdentityMatrix()
    glowBuffer.bindRead()
    MuseShaders.gaussBlurProgram.bind()
    if (MuseShaders.gaussBlurProgram.compiled)
    glDisable(GL_DEPTH_TEST)


    val sw = screen.getScaledWidth
    val sh = screen.getScaledHeight
    glBegin(GL_QUADS)
    vertUV(0, 0, 0, 1)
    vertUV(0, texDimension, 0, 0)
    vertUV(texDimension, texDimension, 1, 0)
    vertUV(texDimension, 0, 1, 1)
    glEnd()

    MuseShaders.gaussBlurProgram.unbind()
    glowBuffer.unbindRead()
    popDualIdentityMatrix()
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

  private def vertUV(x: Double, y: Double, u:Double, v:Double) {
    glTexCoord2d(u, v)
    glVertex2d(x, y)
  }
  def clear() {
    glowBuffer.clear()
  }

}
