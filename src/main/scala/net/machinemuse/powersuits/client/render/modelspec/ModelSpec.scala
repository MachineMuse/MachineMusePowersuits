//package net.machinemuse.powersuits.client.render.modelspec
//
//import net.machinemuse.numina.scala.MuseRegistry
//import net.minecraft.util.Vec3
//import net.minecraftforge.client.model.obj.WavefrontObject
//
///**
//  * Ported to Java by lehjr on 11/8/16.
//  */
//class ModelSpec(val model: WavefrontObject,
//                val textures: Array[String],
//                val offset: Option[Vec3],
//                val rotation: Option[Vec3],
//                val filename: String
//               ) extends MuseRegistry[ModelPartSpec] {
//  def applyOffsetAndRotation() = {
//    // TODO: Implement
//  }
//
//  def getOwnName = {
//    ModelRegistry.getName(this).getOrElse("")
//  }
//}
//
