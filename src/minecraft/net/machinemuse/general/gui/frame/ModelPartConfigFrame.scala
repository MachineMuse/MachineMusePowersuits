package net.machinemuse.general.gui.frame

import net.machinemuse.general.geometry.{MuseRelativeRect, MusePoint2D, Colour}
import net.machinemuse.powersuits.client.render.modelspec.ModelRegistry
import net.minecraft.item.ItemStack

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 6:39 PM, 29/04/13
 */
class ModelPartConfigFrame(val stack: ItemStack, topleft: MusePoint2D, bottomright: MusePoint2D, borderColour: Colour, insideColour: Colour)
  extends ScrollableFrame(topleft, bottomright, borderColour, insideColour) {

  val modelframes = {
    var prev: ModelPartSelectionSubframe = null
    for (modelspec <- ModelRegistry.apply.values) yield {
      prev = new ModelPartSelectionSubframe(modelspec,
        new MuseRelativeRect(topleft.x, topleft.y, bottomright.x, topleft.y)
          .setBelow(Option(prev).map(p => p.border).getOrElse(null)),
        Colour.LIGHTBLUE, stack)
      prev
    }
  }

  //  override def onMouseDown(x: Double, y: Double, button: Int) {}
  //
  //  override def onMouseUp(x: Double, y: Double, button: Int) {}
  //
  override def update(mousex: Double, mousey: Double) {
    super.update(mousex, mousey)
  }

  //
  //  override def draw() {}
  //
  //  override def getToolTip(x: Int, y: Int): util.List[String] = ???
}
