package net.machinemuse.utils.render

import net.machinemuse.powersuits.common.Config

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 8:57 PM, 5/15/13
 */
object MuseShaders {
  //val gaussianVertices = new MuseVertexShader(Config.RESOURCE_PREFIX + "shaders/gaussianblur.frag")
  val blurVertex = Config.RESOURCE_PREFIX + "shaders/gaussianblur.vert"
  val blurFragment = Config.RESOURCE_PREFIX + "shaders/gaussianblur.frag"
  val hBlurProgram = new ShaderProgram(blurVertex, blurFragment)
  val vBlurProgram = new ShaderProgram(blurVertex, blurFragment)

  val depthVertex = Config.RESOURCE_PREFIX + "shaders/depthocclusion.vert"
  val depthFragment = Config.RESOURCE_PREFIX + "shaders/depthocclusion.frag"
  val depthProgram = new ShaderProgram(depthVertex, depthFragment)

  val depthApplicatorFragment = Config.RESOURCE_PREFIX + "shaders/depthapplicator.frag"
  val depthApplicatorProgram = new ShaderProgram(depthVertex, depthApplicatorFragment)
}
