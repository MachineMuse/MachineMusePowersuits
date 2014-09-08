package net.machinemuse.utils.render

import java.nio.ByteBuffer
import org.lwjgl.opengl.GL11._

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 3:29 AM, 5/16/13
 */
class DepthBuffer(val texDimension: Int) {

  // Allocate IDs for a depth buffer
  val textureID = glGenTextures()

  // Bind each of the new buvfer IDs
  glBindTexture(GL_TEXTURE_2D, textureID)

  // Set up texture parameters
  glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)

  // Allocate space in gpu for the depth buffer
  glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, texDimension, texDimension, 0, GL_DEPTH_COMPONENT, GL_UNSIGNED_BYTE, null: ByteBuffer)
  glBindTexture(GL_TEXTURE_2D, 0)

  def bindRead() {
    glPushAttrib(GL_TEXTURE_BIT)
    glBindTexture(GL_TEXTURE_2D, textureID)
  }

  def unbindRead() {
    glPopAttrib()
  }

  def copyWrite() {
    glCopyTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, 0, 0, texDimension, texDimension)
  }

  def clear() {
    glClear(GL_DEPTH_BUFFER_BIT)
  }
}
