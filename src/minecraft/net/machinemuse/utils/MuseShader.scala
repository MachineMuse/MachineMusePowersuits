package net.machinemuse.utils

import scala.Predef.String
import org.lwjgl.opengl.ARBShaderObjects._
import org.lwjgl.opengl._
import net.machinemuse.general.MuseLogger
import scala.io.Source
import java.net.URL
import net.machinemuse.powersuits.common.{CommonProxy, Config}

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 8:57 PM, 5/15/13
 */

object MuseShaders {
  //val gaussianVertices = new MuseVertexShader(Config.RESOURCE_PREFIX + "shaders/gaussianblur.frag")
  //val gaussianBlur = new MuseFragmentShader(Config.RESOURCE_PREFIX + "shaders/gaussianblur.frag")
  val testv = new MuseVertexShader(Config.RESOURCE_PREFIX + "shaders/test.vert")
  val testf = new MuseFragmentShader(Config.RESOURCE_PREFIX + "shaders/test.frag")
  val gaussBlurProgram = new ShaderProgram
  gaussBlurProgram.attachShader(testv)
  gaussBlurProgram.attachShader(testf)
  // gaussBlurProgram.attachShader(gaussianBlur)
  gaussBlurProgram.compile()

}

class ShaderProgram {
  val program: Int = ARBShaderObjects.glCreateProgramObjectARB
  var compiled: Boolean = false
  if (program == 0) MuseLogger.logError("No shader program ID")

  def attachShader(shader: ShaderPart) {
    glAttachObjectARB(program, shader.id)
  }

  def compile() {
    glLinkProgramARB(program)
    if (glGetObjectParameteriARB(program, GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
      MuseLogger.logError(Shader.getLogInfo(program))
    }
    glValidateProgramARB(program)
    if (glGetObjectParameteriARB(program, GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
      MuseLogger.logError(Shader.getLogInfo(program))
    }
    compiled = true
  }

  def bind() {
    ARBShaderObjects.glUseProgramObjectARB(program)
  }

  def unbind() {
    ARBShaderObjects.glUseProgramObjectARB(0)
  }
}

abstract class ShaderPart(source: String, shaderType: Int) {
  var id: Int = Shader.createShader(source, shaderType)
  if (id == 0) MuseLogger.logError("No shader buffer ID")

}

class MuseVertexShader(source: String) extends ShaderPart(source, ARBVertexShader.GL_VERTEX_SHADER_ARB) {}

class MuseFragmentShader(source: String) extends ShaderPart(source, ARBFragmentShader.GL_FRAGMENT_SHADER_ARB) {}


object Shader {
  def createShader(filename: String, shaderType: Int): Int = {
    val resource: URL = CommonProxy.getResource(filename)
    val shader = glCreateShaderObjectARB(shaderType)
    val shaderProg = Source.fromURL(resource).mkString
    MuseLogger.logDebug("Created shader object with ID " + shader + " and text: \n" + shaderProg)
    glShaderSourceARB(shader, shaderProg)
    glCompileShaderARB(shader)
    if (ARBShaderObjects.glGetObjectParameteriARB(shader, GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE) throw new RuntimeException("Error creating shader: " + getLogInfo(shader))
    shader
  }

  def getLogInfo(obj: Int): String = {
    glGetInfoLogARB(obj, glGetObjectParameteriARB(obj, GL_OBJECT_INFO_LOG_LENGTH_ARB))
  }
}