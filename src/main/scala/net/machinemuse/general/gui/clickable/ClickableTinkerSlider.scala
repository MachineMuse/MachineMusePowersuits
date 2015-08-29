package net.machinemuse.general.gui.clickable

import net.machinemuse.numina.general.MuseMathUtils
import net.machinemuse.numina.geometry.MusePoint2D
import net.minecraft.nbt.NBTTagCompound

class ClickableTinkerSlider(topmiddle: MusePoint2D, width: Double, val moduleTag: NBTTagCompound, name: String) extends ClickableSlider(topmiddle, width, name) {

  override def value: Double = {
    if(moduleTag.hasKey(name)) moduleTag.getDouble(name) else 0
  }

  def moveSlider(x: Double, y: Double) {
    val xval: Double = position.x - x
    val xratio: Double = MuseMathUtils.clampDouble(0.5 - (xval / width), 0, 1)
    moduleTag.setDouble(name, xratio)
  }
}