package net.machinemuse.powersuits.client.render.modelspec

import org.lwjgl.opengl.GL11._
import net.machinemuse.utils.MuseRenderer
import net.machinemuse.general.geometry.Colour
import net.minecraft.client.Minecraft
import net.machinemuse.powersuits.client.render.item.ArmorModel
import net.minecraft.nbt.NBTTagCompound

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:16 AM, 29/04/13
 */
object RenderPart {
  def apply(nbt: NBTTagCompound, m: ArmorModel) {
    ModelRegistry.getPart(nbt).map(part => {

      Minecraft.getMinecraft.renderEngine.bindTexture(part.getTexture(nbt))
      glPushMatrix
      part.morph(m)
      if (part.getGlow(nbt)) MuseRenderer.glowOn
      part.getColour(nbt).doGL
      part.modelSpec.applyOffsetAndRotation
      part.modelSpec.model.renderPart(part.partName)

      Colour.WHITE.doGL
      if (part.getGlow(nbt)) MuseRenderer.glowOff
      glPopMatrix
    }
    )
  }
}
