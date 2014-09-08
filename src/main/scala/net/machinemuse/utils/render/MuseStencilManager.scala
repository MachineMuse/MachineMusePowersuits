package net.machinemuse.utils.render

import org.lwjgl.opengl.GL11._

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 5:00 PM, 5/15/13
 */
object MuseStencilManager {
  val stencilMask = 0x10

  def stencilOn() {
    glStencilMask(stencilMask)
    glEnable(GL_STENCIL)
  }

  def stencilOff() {

  }
}
