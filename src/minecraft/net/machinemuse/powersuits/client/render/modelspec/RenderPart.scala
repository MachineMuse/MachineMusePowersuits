package net.machinemuse.powersuits.client.render.modelspec

import net.machinemuse.general.geometry.Colour
import net.minecraft.client.Minecraft
import net.machinemuse.powersuits.client.render.item.ArmorModel
import net.minecraft.nbt.NBTTagCompound
import net.machinemuse.utils.render.Render

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:16 AM, 29/04/13
 */
object RenderPart {
  def apply(nbt: NBTTagCompound, c: Array[Int], m: ArmorModel, slot: Int) {
    //MuseLogger.logDebug("rendering model " + nbt.getString("model") + ":" + nbt.getString("part"))
    ModelRegistry.getPart(nbt).map(f = part => {
      if (part.slot == slot) {
        val withMaybeGlow = if (part.getGlow(nbt)) {
          a: Render[_] => Render withGlow a
        } else {
          a: Render[_] => a
        }

        withMaybeGlow {
          Render.withPushedMatrix {
            Render pure {
              try {
                Minecraft.getMinecraft.renderEngine.bindTexture(part.getTexture(nbt))
                part.morph(m)
                val ix = part.getColourIndex(nbt)
                if (ix < c.size) {
                  Colour.doGLByInt(c(ix))
                }
                part.modelSpec.applyOffsetAndRotation
                part.modelSpec.model.renderPart(part.partName)
                Colour.WHITE.doGL()
              } catch {
                case e: Throwable =>
              }
            }
          }
        }.run()
      }
    })
  }
}
