package net.machinemuse.utils.render

import org.lwjgl.opengl.GL11
import net.minecraft.client.renderer.{GLAllocation, RenderHelper, OpenGlHelper}

/**
 * Library for working with rendering.
 *
 * (c) 2013 Byron Shelden
 * Edited by Claire Semple
 */
trait Render[A] {
  def run(): A

  import Render._

  def compile(): Render[A] = Render.compile(this)

  def map[B](f: A => B) = mk {
    f(run())
  }

  def flatMap[B](f: A => Render[B]) = mk {
    f(run()).run()
  }
}

object Render {
  private def mk[A](act: => A): Render[A] = new Render[A] {
    def run() = act
  }

  def apply[A](act: => A): Render[A] = mk(act)

  def liftIO[A](act: => A): Render[A] = mk(act)

  def pure[A](a: => A): Render[A] = mk(a)

  def withPushedMatrix[A](r: Render[A]): Render[A] = mk {
    GL11.glPushMatrix()
    val a = r.run()
    GL11.glPopMatrix()
    a
  }

  def withShaderProgram[A](prog: ShaderProgram)(r: Render[A]): Render[A] = mk {
    prog.bind()
    //    MuseLogger.logDebug("Bound shader program")
    val a = r.run()
    //    MuseLogger.logDebug("Unbound shader program")
    prog.unbind()
    a
  }

  def fromBuffer[A](buf: TextureBuffer)(r: Render[A]): Render[A] = mk {
    buf.bindRead()
    //    MuseLogger.logDebug("Bound buffer for reading")
    val a = r.run()
    //    MuseLogger.logDebug("Unbound buffer for reading")
    buf.unbindRead()
    a
  }

  def toBuffer[A](buf: TextureBuffer)(r: Render[A]): Render[A] = mk {
    buf.bindWrite()
    //    MuseLogger.logDebug("Bound buffer for writing")
    val a = r.run()
    //    MuseLogger.logDebug("Unbound buffer for writing")
    buf.unbindWrite()
    a
  }

  def withPushedAttrib[A](mask: Int)(r: Render[A]): Render[A] = mk {
    GL11.glPushAttrib(mask)
    val a = r.run()
    GL11.glPopAttrib()
    a
  }


  /**
   * Compiles the given render into a display list.
   *
   * Note that the yield from the compiled render is captured and used as the yield of the call action.
   *
   * @return a render action that invokes the compiled display list.
   */
  def compile[A](r: Render[A]): Render[A] = {
    val listIndex = GLAllocation.generateDisplayLists(1)
    GL11.glNewList(listIndex, GL11.GL_COMPILE)
    val a = r.run()
    GL11.glEndList()

    mk {
      GL11.glCallList(listIndex)
      a
    }
  }

  def withGlow[A](r: Render[A]): Render[A] = {
    for {
      saved <- Render.liftIO {
        (OpenGlHelper.lastBrightnessX, OpenGlHelper.lastBrightnessY)
      }
      _ <- Render {
        GL11.glPushAttrib(GL11.GL_LIGHTING_BIT)
        RenderHelper.disableStandardItemLighting()
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F)
      }
      a <- r
      a <- GlowBuffer.draw(r)
      _ <- Render {
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, saved._1, saved._2)
        GL11.glPopAttrib()
      }
    } yield (a)
  }
}