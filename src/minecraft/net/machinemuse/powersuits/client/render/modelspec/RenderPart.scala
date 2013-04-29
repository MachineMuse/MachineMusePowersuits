package net.machinemuse.powersuits.client.render.modelspec

import org.lwjgl.opengl.GL11._
import net.machinemuse.utils.MuseRenderer
import net.machinemuse.general.geometry.Colour
import net.minecraft.client.Minecraft
import net.machinemuse.powersuits.client.render.item.ArmorModel

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:16 AM, 29/04/13
 */
object RenderPart {
  def apply(part: ModelPartDisplayPrefs, m: ArmorModel) {
    Minecraft.getMinecraft.renderEngine.bindTexture(part.getTexture)
    glPushMatrix
    part.partSpec.morph(m)
    if (part.getGlow) MuseRenderer.glowOn
    part.getColour.doGL
    part.partSpec.modelSpec.applyOffsetAndRotation
    part.partSpec.modelSpec.model.renderPart(part.partSpec.partName)

    Colour.WHITE.doGL
    if (part.getGlow) MuseRenderer.glowOff
    glPopMatrix
  }
}
