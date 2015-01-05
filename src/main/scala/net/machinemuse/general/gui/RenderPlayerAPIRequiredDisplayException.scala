package net.machinemuse.general.gui

import net.minecraft.client.gui.{GuiErrorScreen, FontRenderer}
import cpw.mods.fml.client.CustomModLoadingErrorDisplayException

class RenderPlayerAPIRequiredDisplayException extends CustomModLoadingErrorDisplayException {
  override def initGui(errorScreen: GuiErrorScreen, fontRenderer: FontRenderer) {
  }

  override def drawScreen(errorScreen: GuiErrorScreen, fontRenderer: FontRenderer, mouseRelX: Int, mouseRelY: Int, tickTime: Float) {
    errorScreen.drawDefaultBackground
    var offset: Int = 75
    errorScreen.drawCenteredString(fontRenderer, "A required mod is missing. Minecraft cannot continue loading.", errorScreen.width / 2, offset, 0xFFFFFF)
    offset += 25
    errorScreen.drawCenteredString(fontRenderer, "The mod: \"MachineMuse's Modular Powersuits\"", errorScreen.width / 2, offset, 0xEEEEEE)
    offset += 15
    errorScreen.drawCenteredString(fontRenderer, "requires an additional mod: \"RenderPlayerAPI\"", errorScreen.width / 2, offset, 0xEEEEEE)
    offset += 15
    errorScreen.drawCenteredString(fontRenderer, "when \"SmartMoving\" is loaded.", errorScreen.width / 2, offset, 0xEEEEEE)
    offset += 25
    errorScreen.drawCenteredString(fontRenderer, "Please install \"RenderPlayerAPI\" to continue.", errorScreen.width / 2, offset, 0xFFFFFF)
  }

}
