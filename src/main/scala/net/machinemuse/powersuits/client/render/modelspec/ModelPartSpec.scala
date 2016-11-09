//package net.machinemuse.powersuits.client.render.modelspec
//
//import net.minecraft.nbt.NBTTagCompound
//
///**
//  * Ported to Java by lehjr on 11/8/16.
//  */
//class ModelPartSpec(val modelSpec: ModelSpec,
//                    val morph: MorphTarget,
//                    val partName: String,
//                    val slot: Int,
//                    val defaultcolourindex: Int = 0,
//                    val defaultglow: Boolean = false,
//                    val displayName: String) {
//
//  def getTexture(nbt: NBTTagCompound): String = {
//    if (nbt hasKey "texture") nbt getString "texture" else modelSpec.textures.head
//  }
//
//  def setTexture(nbt: NBTTagCompound, s: String) {
//    if (s.equals("") || s.equalsIgnoreCase(modelSpec.textures.head)) nbt removeTag "texture" else nbt setString("texture", s)
//  }
//
//  def getColourIndex(nbt: NBTTagCompound): Int = {
//    if (nbt hasKey "colourindex") nbt getInteger "colourindex" else defaultcolourindex
//  }
//
//  def setColourIndex(nbt: NBTTagCompound, c: Int) {
//    if (c == defaultcolourindex) nbt removeTag "colourindex" else nbt setInteger("colourindex", c)
//  }
//
//  def getGlow(nbt: NBTTagCompound): Boolean = {
//    if (nbt hasKey "glow") nbt getBoolean "glow" else defaultglow
//  }
//
//  def setGlow(nbt: NBTTagCompound, g: Boolean) {
//    if (g == defaultglow) nbt removeTag "glow" else nbt setBoolean("glow", g)
//  }
//
//  def setModel(nbt: NBTTagCompound, model: ModelSpec) {
//    setModel(nbt, ModelRegistry.inverse.getOrElse(model, ""))
//  }
//
//  def setModel(nbt: NBTTagCompound, modelname: String) {
//    nbt setString("model", modelname)
//  }
//
//  def setPart(nbt: NBTTagCompound) {
//    nbt setString("part", partName)
//  }
//
//  def setPartAndModel(nbt: NBTTagCompound) {
//  }
//
//  def multiSet(nbt: NBTTagCompound, tex: Option[String], glow: Option[Boolean], c: Option[Int]): NBTTagCompound = {
//    setPart(nbt)
//    setModel(nbt, this.modelSpec)
//    setTexture(nbt, tex.getOrElse(""))
//    setGlow(nbt, glow.getOrElse(false))
//    setColourIndex(nbt, c.getOrElse(defaultcolourindex))
//    nbt
//  }
//
//}
