//package net.machinemuse.powersuits.block
//
//import com.google.common.base.Function
//import com.google.common.collect.ImmutableMap
//import net.machinemuse.numina.geometry.Colour
//import net.machinemuse.powersuits.common.ModularPowersuits
//import net.minecraft.client.Minecraft
//import net.minecraft.client.renderer.block.model.IBakedModel
//import net.minecraft.client.renderer.block.model.ModelRotation
//import net.minecraft.client.renderer.texture.TextureAtlasSprite
//import net.minecraft.client.renderer.vertex.DefaultVertexFormats
//import net.minecraft.util.EnumFacing
//import net.minecraft.util.ResourceLocation
//import net.minecraftforge.client.model.ModelLoaderRegistry
//import net.minecraftforge.client.model.obj.OBJLoader
//import net.minecraftforge.client.model.obj.OBJModel
//import net.minecraftforge.common.model.TRSRTransformation
//import net.minecraftforge.fml.relauncher.Side
//import net.minecraftforge.fml.relauncher.SideOnly
//import javax.vecmath.Matrix4f
//import javax.vecmath.Vector4f
//import java.util.HashMap
//import java.util.Map
//
///**
//  * Created by leon on 9/14/16.
//  */
//object ModelLuxCapatitor {
//  private val modelLocation: ResourceLocation = new ResourceLocation(ModularPowersuits.MODID.toLowerCase, "models/block/luxcapacitor.obj")
//  private val materialName: String = "Material__3.003" // material of the lens
//  private val customData: util.Map[String, String] = new util.HashMap[String, String]() {}
//}
//
//class ModelLuxCapatitor {
//  private val registeredModels: util.Map[Vector4f, util.Map[EnumFacing, IBakedModel]] = new util.HashMap[Vector4f, util.Map[EnumFacing, IBakedModel]]
//
//  private def getOBJModel: OBJModel = {
//    var lightModel: OBJModel = null
//    try {
//      lightModel = OBJLoader.INSTANCE.loadModel(ModelLuxCapatitor.modelLocation).asInstanceOf[OBJModel]
//      lightModel = lightModel.process(ImmutableMap.copyOf(ModelLuxCapatitor.customData)).asInstanceOf[OBJModel]
//    }
//    catch {
//      case e: Exception => {
//        System.out.println("loading model faild!!")
//        lightModel = ModelLoaderRegistry.getMissingModel.asInstanceOf[OBJModel]
//      }
//    }
//    if (lightModel == null) lightModel = ModelLoaderRegistry.getMissingModel.asInstanceOf[OBJModel]
//    return lightModel
//  }
//
//  /**
//    * We need our own because the default set is based on the default=facing north
//    * Our model is default facing up
//    */
//  def getTransform(side: EnumFacing): TRSRTransformation = {
//    var matrix: Matrix4f = null
//    side match {
//      case EnumFacing.DOWN => new TRSRTransformation(ModelRotation.X180_Y0.getMatrix)
////      case EnumFacing.UP =>new TRSRTransformation(TRSRTransformation.identity.getMatrix)
//
//      case NORTH =>
//        matrix = ModelRotation.X90_Y0.getMatrix
//        break //todo: break is not supported
//      case SOUTH =>
//        matrix = ModelRotation.X270_Y0.getMatrix
//        break //todo: break is not supported
//      case WEST =>
//        matrix = ModelRotation.X90_Y270.getMatrix
//        break //todo: break is not supported
//      case EAST =>
//        matrix = ModelRotation.X90_Y90.getMatrix
//        break //todo: break is not supported
//      case _ =>
//        matrix = new Matrix4f
//    }
//    return new TRSRTransformation(matrix)
//  }
//
//  private def bakedLuxCapModel(transformation: TRSRTransformation, facing: EnumFacing, color: Vector4f): IBakedModel = {
//    val model: OBJModel = getOBJModel
//    model.getMatLib.getMaterial(ModelLuxCapatitor.materialName).setColor(color)
//    val bakedModel: IBakedModel = model.bake(transformation, DefaultVertexFormats.BLOCK, new Function[ResourceLocation, TextureAtlasSprite]() {
//      def apply(location: ResourceLocation): TextureAtlasSprite = {
//        val mc: Minecraft = Minecraft.getMinecraft
//        System.out.println("texture location is: " + location.toString)
//        return mc.getTextureMapBlocks.getAtlasSprite(location.toString)
//      }
//    })
//    return bakedModel
//  }
//
//  def getModel(color: Colour, facing: EnumFacing): IBakedModel = {
//    val vector4f: Vector4f = new Vector4f(color.r.toFloat, color.g.toFloat, color.g.toFloat, color.a.toFloat)
//    var luxCapBakedModel: IBakedModel = null
//    try {
//      luxCapBakedModel = registeredModels.get(color).get(facing)
//    }
//    catch {
//      case e: Exception => {
//      }
//    }
//    if (luxCapBakedModel == null) {
//      val tempMap: util.Map[EnumFacing, IBakedModel] = new util.HashMap[EnumFacing, IBakedModel]
//      var tempBaked: IBakedModel = null
//      var i: Int = 0
//      while (i < EnumFacing.VALUES.length) {
//        {
//          val side: EnumFacing = EnumFacing.getFront(i)
//          tempBaked = bakedLuxCapModel(getTransform(side), side, vector4f)
//          if (side eq facing) luxCapBakedModel = tempBaked
//          tempMap.put(side, tempBaked)
//        }
//        {
//          i += 1; i - 1
//        }
//      }
//      registeredModels.put(vector4f, tempMap)
//    }
//    return luxCapBakedModel
//  }
//}