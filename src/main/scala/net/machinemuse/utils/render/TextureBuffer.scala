package net.machinemuse.utils.render

import org.lwjgl.opengl.GLContext
import java.nio.ByteBuffer
import org.lwjgl.opengl.EXTFramebufferObject._
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL14._

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 8:18 PM, 5/15/13
 */
class TextureBuffer(val texDimension: Int) {
  // check if FBO is enabled
  if (!GLContext.getCapabilities.GL_EXT_framebuffer_object) {
    throw new RuntimeException("Framebuffers not supported!")
  }

  // Allocate IDs for a FBO, colour buffer, and depth buffer
  val framebufferID = glGenFramebuffersEXT()
  val colorTextureID = glGenTextures()
  val depthRenderBufferID = glGenTextures()

  glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID)

  glBindTexture(GL_TEXTURE_2D, colorTextureID)
  glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
  glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
  glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, texDimension, texDimension, 0, GL_RGBA, GL_INT, null: ByteBuffer)
  glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, colorTextureID, 0)

  glBindTexture(GL_TEXTURE_2D, depthRenderBufferID)
  glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
  glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
  glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT24, texDimension, texDimension, 0, GL_DEPTH_COMPONENT, GL_INT, null: ByteBuffer)
  glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_TEXTURE_2D, depthRenderBufferID, 0)


  // Check the status and throw an exception if it didn't initialize properly
  glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT) match {
    case GL_FRAMEBUFFER_COMPLETE_EXT =>
    case GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT => throw new RuntimeException("FrameBuffer: " + framebufferID + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT exception")
    case GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT => throw new RuntimeException("FrameBuffer: " + framebufferID + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT exception")
    case GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT => throw new RuntimeException("FrameBuffer: " + framebufferID + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT exception")
    case GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT => throw new RuntimeException("FrameBuffer: " + framebufferID + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT exception")
    case GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT => throw new RuntimeException("FrameBuffer: " + framebufferID + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT exception")
    case GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT => throw new RuntimeException("FrameBuffer: " + framebufferID + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT exception")
    case x => throw new RuntimeException("Unexpected reply from glCheckFramebufferStatusEXT: " + x)
  }

  // Bind the display buffer
  glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0)

  def bindRead() {
    glPushAttrib(GL_TEXTURE_BIT)
    glBindTexture(GL_TEXTURE_2D, colorTextureID)
  }

  def unbindRead() {
    glPopAttrib()
  }

  def bindWrite() {
    glPushAttrib(GL_VIEWPORT_BIT)
    glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID)
    glViewport(0, 0, texDimension, texDimension)
  }

  def unbindWrite() {
    glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0)
    glPopAttrib()
  }


  def clear() {
    bindWrite()
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
    unbindWrite()
  }
}
