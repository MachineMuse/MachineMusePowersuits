package net.machinemuse.powersuits.client.render.modelspec

import net.machinemuse.numina.general.MuseLogger
import net.machinemuse.numina.geometry.Colour
import net.machinemuse.powersuits.item.ItemPowerArmor
import net.machinemuse.utils.MuseStringUtils
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ResourceLocation

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 9:11 AM, 29/04/13
 */
object DefaultModelSpec {
  val normalcolour: Colour = Colour.WHITE
  val glowcolour: Colour = new Colour(17.0 / 255, 78.0 / 255, 1, 1)
  val tex = "/assets/powersuits/textures/models/diffuse.png"

  def loadDefaultModel: Option[ModelSpec] = {
    loadModel(new ResourceLocation("powersuits:models/mps_helm.obj"), tex.split(";")).map(model => {
      makeEntries(Head, EntityEquipmentSlot.HEAD, 0, false, "helm_main;helm_tube_entry1;helm_tubes;helm_tube_entry2".split(";"), model)
      makeEntries(Head, EntityEquipmentSlot.HEAD, 1, true, "visor".split(";"), model)
      model
    })
    loadModel(new ResourceLocation("powersuits:models/mps_arms.obj"), tex.split(";")).map(model => {
      makeEntries(RightArm, EntityEquipmentSlot.CHEST, 0, false, "arms3".split(";"), model)
      makeEntries(RightArm, EntityEquipmentSlot.CHEST, 1, true, "crystal_shoulder_2".split(";"), model)
      makeEntries(LeftArm, EntityEquipmentSlot.CHEST, 0, false, "arms2".split(";"), model)
      makeEntries(LeftArm, EntityEquipmentSlot.CHEST, 1, true, "crystal_shoulder_1".split(";"), model)
      model
    })
    loadModel(new ResourceLocation("powersuits:models/mps_chest.obj"), tex.split(";")).map(model => {
      makeEntries(Body, EntityEquipmentSlot.CHEST, 0, false, "belt;chest_main;polySurface36;backpack;chest_padding".split(";"), model)
      makeEntries(Body, EntityEquipmentSlot.CHEST, 1, true, "crystal_belt".split(";"), model)
      model
    })
    loadModel(new ResourceLocation("powersuits:models/mps_pantaloons.obj"), tex.split(";")).map(model => {
      makeEntries(RightLeg, EntityEquipmentSlot.LEGS, 0, false, "leg1".split(";"), model)
      makeEntries(LeftLeg, EntityEquipmentSlot.LEGS, 0, false, "leg2".split(";"), model)
      model
    })
    loadModel(new ResourceLocation("powersuits:models/mps_boots.obj"), tex.split(";")).map(model => {
      makeEntries(RightLeg, EntityEquipmentSlot.LEGS, 0, false, "boots1".split(";"), model)
      makeEntries(LeftLeg, EntityEquipmentSlot.LEGS, 0, false, "boots2".split(";"), model)
      model
    })
  }

  def loadModel(file: ResourceLocation, textures: Array[String]): Option[ModelSpec] = {
    ModelRegistry.loadModel(file) match {
      case Some(m) => Some(ModelRegistry.put(MuseStringUtils.extractName(file), new ModelSpec(m, textures, None, None, file.toString)))
      case None => MuseLogger.logError("Model file " + file + " not found! D:")
    }
  }

  def makeEntries(target: MorphTarget, slot: EntityEquipmentSlot, colourIndex: Int, glow: Boolean, names: Array[String], model: ModelSpec) {
    for (name <- names) {
      model.put(name, new ModelPartSpec(model, target, name, slot, colourIndex, glow, name))
    }
  }

  def makeModelPrefs(stack: ItemStack, slot: EntityEquipmentSlot): NBTTagCompound = {
    val item = stack.getItem().asInstanceOf[ItemPowerArmor]
    val normalcolour = item.getColorFromItemStack(stack)
    val glowcolour = item.getGlowFromItemStack(stack)
    val list = slot match {
      case EntityEquipmentSlot.HEAD => {
        makePrefs("mps_helm", "helm_main;helm_tube_entry1;helm_tubes;helm_tube_entry2".split(";"), 0, false) ++
          makePrefs("mps_helm", "visor".split(";"), 1, true)
      }
      case EntityEquipmentSlot.CHEST => {
        makePrefs("mps_arms", "arms2;arms3".split(";"), 0, false) ++
          makePrefs("mps_arms", "crystal_shoulder_2;crystal_shoulder_1".split(";"), 1, true) ++
          makePrefs("mps_chest", "belt;chest_main;polySurface36;backpack;chest_padding".split(";"), 0, false) ++
          makePrefs("mps_chest", "crystal_belt".split(";"), 1, true)
      }
      case EntityEquipmentSlot.LEGS => {
        makePrefs("mps_pantaloons", "leg1;leg2".split(";"), 0, false)
      }
      case EntityEquipmentSlot.FEET => {
        makePrefs("mps_boots", "boots1;boots2".split(";"), 0, false)
      }

        //TODO: Model default spec for powerfist left and right models
//      case EntityEquipmentSlot.OFFHAND => {
//
//      }
//      case EntityEquipmentSlot.MAINHAND => {
//
//      }

    }
    (new NBTTagCompound() /: list) {
      case (taglist, elem) => taglist.setTag(elem.getString("model") + "." + elem.getString("part"), elem); taglist
    }
  }

  def makePrefs(modelname: String, partnames: Array[String], colour: Int, glow: Boolean): Array[NBTTagCompound] = {
    ModelRegistry.get(modelname).map(model =>
      for (name <- partnames) yield makePref(model.get(name).get, Some(colour), Some(glow))
    ) getOrElse Array.empty[NBTTagCompound]
  }

  def makePref(partSpec: ModelPartSpec, colourindex: Option[Int], glow: Option[Boolean]): NBTTagCompound = {
    partSpec.multiSet(new NBTTagCompound(), None, glow, colourindex)
  }
}
