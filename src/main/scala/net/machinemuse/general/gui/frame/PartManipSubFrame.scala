//package net.machinemuse.general.gui.frame
//
//import net.machinemuse.general.gui.clickable.ClickableItem
//import net.machinemuse.numina.general.MuseLogger
//import net.machinemuse.numina.geometry.{Colour, MuseRect, MuseRelativeRect}
//import net.machinemuse.numina.network.PacketSender
//import net.machinemuse.numina.render.RenderState
//import net.machinemuse.powersuits.client.render.modelspec.{ModelPartSpec, ModelRegistry, ModelSpec}
//import net.machinemuse.powersuits.network.packets.MusePacketCosmeticInfo
//import net.machinemuse.utils.MuseItemUtils
//import net.machinemuse.utils.render.{GuiIcons, MuseRenderer}
//import net.minecraft.client.Minecraft
//import net.minecraft.item.ItemArmor
//import net.minecraft.nbt.NBTTagCompound
//import org.lwjgl.opengl.GL11._
//
///**
//  * Author: MachineMuse (Claire Semple)
//  * Created: 1:46 AM, 30/04/13
//  */
//class PartManipSubFrame(val model: ModelSpec, val colourframe: ColourPickerFrame, val itemSelector: ItemSelectionFrame, val border: MuseRelativeRect) {
//
//  // see the java filter function in the recipes for an example on how to implement a filter in Java
//
//
//
//  var specs: Array[ModelPartSpec] = model.apply.values.filter(spec => isValidArmor(getSelectedItem, spec.slot)).toArray
//  var open: Boolean = true
//  var mousex: Double = 0
//  var mousey: Double = 0
//
//
//  def getArmorSlot = getSelectedItem.getItem.getItem.asInstanceOf[ItemArmor].armorType
//
//  def getSelectedItem = itemSelector.getSelectedItem
//
//  def getRenderTag: NBTTagCompound = MuseItemUtils.getMuseRenderTag(getSelectedItem.getItem, getArmorSlot)
//
//  def getItemTag = MuseItemUtils.getMuseItemTag(getSelectedItem.getItem)
//
//  def isValidArmor(clickie: ClickableItem, slot: Int): Boolean = if (clickie == null) false else clickie.getItem.getItem.isValidArmor(clickie.getItem, slot, Minecraft.getMinecraft.thePlayer)
//
//  def getSpecTag(spec: ModelPartSpec) = getRenderTag.getCompoundTag(ModelRegistry.getInstance().makeName(spec))
//
//  def getOrDontGetSpecTag(spec: ModelPartSpec): Option[NBTTagCompound] = {
//    val name = ModelRegistry.getInstance().makeName(spec)
//    if (!getRenderTag.hasKey(name)) None
//    else Some(getRenderTag.getCompoundTag(name))
//  }
//
//  def getOrMakeSpecTag(spec: ModelPartSpec): NBTTagCompound = {
//    val name = ModelRegistry.getInstance().makeName(spec)
//    if (!getRenderTag.hasKey(name)) {
//      val k = new NBTTagCompound()
//      spec.multiSet(k, None, None, None)
//      getRenderTag.setTag(name, k)
//      k
//    } else {
//      getRenderTag.getCompoundTag(name)
//    }
//  }
//
//  def updateItems() {
//    specs = model.apply.values.filter(spec => isValidArmor(getSelectedItem, spec.slot)).toArray
//    border.setHeight(if (specs.size > 0) specs.size * 8 + 10 else 0)
//  }
//
//  def drawPartial(min: Double, max: Double) {
//    if (specs.size > 0) {
//      ModelRegistry.getInstance().getName(model).foreach(s => MuseRenderer.drawString(s, border.left + 8, border.top))
//      drawOpenArrow(min, max)
//      if (open) {
//        ((border.top + 8) /: specs) {
//          case (y, spec) => {
//            drawSpecPartial(border.left, y, spec, min, max)
//            y + 8
//          }
//        }
//      }
//    }
//  }
//
//  def decrAbove(index: Int) {
//    for (spec <- specs) {
//      val tagname = ModelRegistry.getInstance().makeName(spec)
//      val player = Minecraft.getMinecraft.thePlayer
//      val tagdata = getOrDontGetSpecTag(spec)
//      tagdata.foreach(e => {
//        val oldindex = spec.getColourIndex(e)
//        if (oldindex >= index && oldindex > 0) {
//          spec.setColourIndex(e, oldindex - 1)
//          if (player.worldObj.isRemote) PacketSender.sendToServer(new MusePacketCosmeticInfo(player, getSelectedItem.inventorySlot, tagname, e).getPacket131)
//        }
//      })
//
//    }
//  }
//
//  def drawSpecPartial(x: Double, y: Double, spec: ModelPartSpec, ymino: Double, ymaxo: Double) = {
//    val tag = getSpecTag(spec)
//    val selcomp = if (tag.hasNoTags) 0 else if (spec.getGlow(tag)) 2 else 1
//    val selcolour = spec.getColourIndex(tag)
//    new GuiIcons.TransparentArmor(x, y, null, null, ymino, null, ymaxo)
//    new GuiIcons.NormalArmor(x + 8, y, null, null, ymino, null, ymaxo)
//    new GuiIcons.GlowArmor(x + 16, y, null, null, ymino, null, ymaxo)
//    new GuiIcons.SelectedArmorOverlay(x + selcomp * 8, y, null, null, ymino, null, ymaxo)
//    val textstartx: Double = ((x + 28) /: colourframe.colours) {
//      case (acc, colour) =>
//        new GuiIcons.ArmourColourPatch(acc, y, new Colour(colour), null, ymino, null, ymaxo)
//        acc + 8
//    }
//
//    /*
//        val textstartx = ((x + 28) /: colourframe.colours) {
//      case (acc, colour) =>
//        new GuiIcons.ArmourColourPatch(acc, y, new Colour(colour), null, ymino, null, ymaxo)
//        acc + 8
//    }
//
//     */
//
//
//    if (selcomp > 0) {
//      new GuiIcons.SelectedArmorOverlay(x + 28 + selcolour * 8, y, null, null, ymino, null, ymaxo)
//    }
//
//
//    MuseRenderer.drawString(spec.displayName, textstartx + 4, y)
//  }
//
//  def drawOpenArrow(min: Double, max: Double) {
//    RenderState.texturelessOn()
//    Colour.LIGHTBLUE.doGL()
//    glBegin(GL_TRIANGLES)
//    import net.machinemuse.numina.general.MuseMathUtils._
//    if (open) {
//      glVertex2d(border.left + 3, clampDouble(border.top + 3, min, max))
//      glVertex2d(border.left + 5, clampDouble(border.top + 7, min, max))
//      glVertex2d(border.left + 7, clampDouble(border.top + 3, min, max))
//    } else {
//      glVertex2d(border.left + 3, clampDouble(border.top + 3, min, max))
//      glVertex2d(border.left + 3, clampDouble(border.top + 7, min, max))
//      glVertex2d(border.left + 7, clampDouble(border.top + 5, min, max))
//    }
//    glEnd()
//    Colour.WHITE.doGL()
//    RenderState.texturelessOff()
//  }
//
//  def getBorder: MuseRect = {
//    if (open) border.setHeight(9 + 9 * specs.size)
//    else border.setHeight(9)
//    border
//  }
//
//  def tryMouseClick(x: Double, y: Double): Boolean = {
//    if (x < border.left || x > border.right || y < border.top || y > border.bottom) false
//    else if (x > border.left + 2 && x < border.left + 8 && y > border.top + 2 && y < border.top + 8) {
//      open = !open
//      getBorder
//      true
//    } else if (x < border.left + 24 && y > border.top + 8) {
//      val lineNumber = ((y - border.top - 8) / 8).toInt
//      val columnNumber = ((x - border.left) / 8).toInt
//      val spec = specs(lineNumber.min(specs.size - 1).max(0))
//      MuseLogger.logDebug("Line " + lineNumber + " Column " + columnNumber)
//      columnNumber match {
//        case 0 => {
//          val renderTag = getRenderTag
//          val tagname = ModelRegistry.getInstance().makeName(spec)
//          val player = Minecraft.getMinecraft.thePlayer
//          renderTag.removeTag(ModelRegistry.getInstance().makeName(spec))
//          if (player.worldObj.isRemote) PacketSender.sendToServer(new MusePacketCosmeticInfo(player, getSelectedItem.inventorySlot, tagname, new NBTTagCompound()).getPacket131)
//          updateItems()
//          true
//        }
//        case 1 => {
//          val tagname = ModelRegistry.getInstance().makeName(spec)
//          val player = Minecraft.getMinecraft.thePlayer
//          val tagdata = getOrMakeSpecTag(spec)
//          spec.setGlow(tagdata, false)
//          if (player.worldObj.isRemote) PacketSender.sendToServer(new MusePacketCosmeticInfo(player, getSelectedItem.inventorySlot, tagname, tagdata).getPacket131)
//          updateItems()
//          true
//        }
//        case 2 => {
//          val tagname = ModelRegistry.getInstance().makeName(spec)
//          val player = Minecraft.getMinecraft.thePlayer
//          val tagdata = getOrMakeSpecTag(spec)
//          spec.setGlow(tagdata, true)
//          if (player.worldObj.isRemote) PacketSender.sendToServer(new MusePacketCosmeticInfo(player, getSelectedItem.inventorySlot, tagname, tagdata).getPacket131)
//          updateItems()
//          true
//        }
//        case _ => false
//      }
//    } else if (x > border.left + 28 && x < border.left + 28 + colourframe.colours.size * 8) {
//      val lineNumber = ((y - border.top - 8) / 8).toInt
//      val columnNumber = ((x - border.left - 28) / 8).toInt
//      val spec = specs(lineNumber.min(specs.size - 1).max(0))
//      val tagname = ModelRegistry.getInstance().makeName(spec)
//      val player = Minecraft.getMinecraft.thePlayer
//      val tagdata = getOrMakeSpecTag(spec)
//      spec.setColourIndex(tagdata, columnNumber)
//      if (player.worldObj.isRemote) PacketSender.sendToServer(new MusePacketCosmeticInfo(player, getSelectedItem.inventorySlot, tagname, tagdata).getPacket131)
//      true
//    }
//    else false
//  }
//
//}
