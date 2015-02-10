package net.machinemuse.general.gui

import net.minecraft.client.gui.{GuiErrorScreen, FontRenderer}
import net.minecraft.util.StatCollector
import cpw.mods.fml.client.CustomModLoadingErrorDisplayException

class RenderPlayerAPIRequiredDisplayException extends CustomModLoadingErrorDisplayException {
  override def initGui(errorScreen: GuiErrorScreen, fontRenderer: FontRenderer) {
  }

  override def drawScreen(errorScreen: GuiErrorScreen, fontRenderer: FontRenderer, mouseRelX: Int, mouseRelY: Int, tickTime: Float) {
    errorScreen.drawDefaultBackground
    var offset: Int = 75
    errorScreen.drawCenteredString(fontRenderer, StatCollector.translateToLocal("powersuits.exceptionRenderPlayerAPI.lineone"), 
                                            errorScreen.width / 2, offset, 0xFFFFFF)
    offset += 25
    errorScreen.drawCenteredString(fontRenderer, StatCollector.translateToLocal("powersuits.exceptionRenderPlayerAPI.linetwo"), 
                                            errorScreen.width / 2, offset, 0xEEEEEE)
    offset += 15
    errorScreen.drawCenteredString(fontRenderer, StatCollector.translateToLocal("powersuits.exceptionRenderPlayerAPI.linethree"), 
                                            errorScreen.width / 2, offset, 0xEEEEEE)
    offset += 15
    errorScreen.drawCenteredString(fontRenderer, StatCollector.translateToLocal("powersuits.exceptionRenderPlayerAPI.linefour"), 
                                            errorScreen.width / 2, offset, 0xEEEEEE)
    offset += 25
    errorScreen.drawCenteredString(fontRenderer, StatCollector.translateToLocal("powersuits.exceptionRenderPlayerAPI.linefive"), 
                                            errorScreen.width / 2, offset, 0xFFFFFF)
  }

}
