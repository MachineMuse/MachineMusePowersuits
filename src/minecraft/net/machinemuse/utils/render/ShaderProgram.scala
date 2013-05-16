package net.machinemuse.utils.render

import org.lwjgl.opengl._
import net.machinemuse.general.MuseLogger
import org.lwjgl.opengl.ARBShaderObjects._
import java.net.URL
import net.machinemuse.powersuits.common.CommonProxy
import scala.io.Source

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 3:31 AM, 5/16/13
 */
class ShaderProgram(vertSource: String, fragSource: String) {
  var compiled: Boolean = false
  val program: Int = ARBShaderObjects.glCreateProgramObjectARB
  if (program == 0) MuseLogger.logError("No shader program ID")
  val vert = mk(vertSource, ARBVertexShader.GL_VERTEX_SHADER_ARB)
  val frag = mk(fragSource, ARBFragmentShader.GL_FRAGMENT_SHADER_ARB)
  glAttachObjectARB(program, vert)
  glAttachObjectARB(program, frag)
  compile()


  def compile() {
    glLinkProgramARB(program)
    if (glGetObjectParameteriARB(program, GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
      MuseLogger.logError(getLogInfo(program))
    }
    glValidateProgramARB(program)
    if (glGetObjectParameteriARB(program, GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
      MuseLogger.logError(getLogInfo(program))
    }
    compiled = true
  }

  def bind() {
    if (compiled) {
      ARBShaderObjects.glUseProgramObjectARB(program)
    }
  }

  def unbind() {
    if (compiled) {
      ARBShaderObjects.glUseProgramObjectARB(0)
    }
  }

  def setUniform2f(name: String, f1: Float, f2: Float) {
    val pointer = GL20.glGetUniformLocation(program, name)
    if (pointer < 1) MuseLogger.logError("UNABLE TO ACCESS FLOATS!!!")
    GL20.glUniform2f(pointer, f1, f2)
  }

  def setTexUnit(name: String, i: Int) {
    val pointer = GL20.glGetUniformLocation(program, name)
    if (pointer < 1) MuseLogger.logError("UNABLE TO ACCESS TEX UNIT!!!")
    GL20.glUniform1i(pointer, i)
  }

  def getLogInfo(obj: Int): String = {
    glGetInfoLogARB(obj, glGetObjectParameteriARB(obj, GL_OBJECT_INFO_LOG_LENGTH_ARB))
  }

  def mk(filename: String, shaderType: Int): Int = {
    val resource: URL = CommonProxy.getResource(filename)
    val shader = glCreateShaderObjectARB(shaderType)
    val shaderProg = Source.fromURL(resource).mkString
    MuseLogger.logDebug("Created shader object with ID " + shader + " and text: \n" + shaderProg)
    glShaderSourceARB(shader, shaderProg)
    glCompileShaderARB(shader)
    if (ARBShaderObjects.glGetObjectParameteriARB(shader, GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE) throw new RuntimeException("Error creating shader: " + getLogInfo(shader))
    shader
  }
}
