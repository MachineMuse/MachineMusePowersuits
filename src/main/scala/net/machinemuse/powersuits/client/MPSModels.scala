package net.machinemuse.powersuits.client

import java.io.IOException

import com.google.common.base.Function
import com.google.common.collect.ImmutableMap
import net.machinemuse.numina.geometry.Colour
import net.machinemuse.powersuits.block.ModelLuxCapatitor
import net.machinemuse.powersuits.common.{Config, ModularPowersuits}
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.util.{EnumFacing, ResourceLocation}
import net.minecraftforge.client.model.obj.OBJLoader
import net.minecraftforge.client.model.{Attributes, IModel, IRetexturableModel, ModelLoaderRegistry}
import net.minecraftforge.common.model.TRSRTransformation

/**
  * Created by leon on 8/4/16.
  */
object MPSModels {
  var powerFistGUImodel: IBakedModel = _
  var powerFistLeftmodel: IBakedModel = _
  var powerFistLeftFiringmodel: IBakedModel = _
  var powerFistRightmodel: IBakedModel = _
  var powerFistRightFiringmodel: IBakedModel = _


  private val modelLuxCapatitor: ModelLuxCapatitor = ModelLuxCapatitor.instance
  def luxCapacitormodel(color: Colour, facing: EnumFacing): IBakedModel = modelLuxCapatitor.getModel(color, facing)


  var modelToLoad: IModel =_

  val textureLocation: ResourceLocation = new ResourceLocation(ModularPowersuits.MODID, "models/tool")

  def loadModel(modelLocation: ResourceLocation):IModel = {
    try {
       modelToLoad = OBJLoader.INSTANCE.loadModel(modelLocation)
       if (modelToLoad.isInstanceOf[IRetexturableModel])
         modelToLoad.asInstanceOf[IRetexturableModel].retexture(ImmutableMap.of("layer0", textureLocation.toString))
      modelToLoad
    } catch {
      case e: IOException => {
        e.printStackTrace()
        println("Loading model failed. Using missing model!!")
        ModelLoaderRegistry.getMissingModel()
      }
    }
  }

  var textureGetter: Function[ResourceLocation, TextureAtlasSprite] = new Function[ResourceLocation, TextureAtlasSprite]() {
    def apply(location: ResourceLocation): TextureAtlasSprite = {
//      println("texture location is: " + Minecraft.getMinecraft.getTextureMapBlocks.getAtlasSprite(location.toString))



      Minecraft.getMinecraft.getTextureMapBlocks.getAtlasSprite(location.toString)
//      Minecraft.getMinecraft.getTextureMapBlocks.getAtlasSprite(textureLocation.toString)

//      MuseIcon.powerFistTexture.asInstanceOf[TextureAtlasSprite]
    }
  }

  def bakeModel(model: IModel): IBakedModel = {
    try {
//      if (model.isInstanceOf[IRetexturableModel])
//        println(" model is IRetexturableModel")
//      else
//        println(" model NOT IRetexturableModel")

      model.getTextures().clear();
      model.getTextures.add(textureLocation)
      model.bake(TRSRTransformation.identity(), Attributes.DEFAULT_BAKED_FORMAT, textureGetter)



    } catch {
      case e: IOException => {
        e.printStackTrace()
        println("Baking model failed. Using missing model!!")
        ModelLoaderRegistry.getMissingModel().bake(TRSRTransformation.identity(), Attributes.DEFAULT_BAKED_FORMAT, textureGetter)
      }
    }
  }

  /*



    public static void replacePatternModel(ResourceLocation locPattern, ResourceLocation modelLocation, ModelBakeEvent event, String baseString, Iterable<Item> items, int color) {
    try {
      IModel model = ModelLoaderRegistry.getModel(modelLocation);
      if(model instanceof IRetexturableModel) {
        IRetexturableModel itemModel = (IRetexturableModel) model;

        for(Item item : items) {
          String suffix = Pattern.getTextureIdentifier(item);
          // get texture
          String partPatternLocation = locPattern.toString() + suffix;
          String partPatternTexture = baseString + suffix;
          IModel partPatternModel = itemModel.retexture(ImmutableMap.of("layer0", partPatternTexture));
          IBakedModel baked = partPatternModel.bake(partPatternModel.getDefaultState(), DefaultVertexFormats.ITEM, textureGetter);
          if(color > -1) {
            ImmutableList.Builder<BakedQuad> quads = ImmutableList.builder();
            // ItemLayerModel.BakedModel only uses general quads
            for(BakedQuad quad : baked.getQuads(null, null, 0)) {
              quads.add(ModelHelper.colorQuad(color, quad));
            }
            baked = new BakedSimple.Wrapper(quads.build(), ((IPerspectiveAwareModel) baked));
          }
          event.getModelRegistry().putObject(new ModelResourceLocation(partPatternLocation, "inventory"), baked);
        }
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }


   */



















  private val powerFistLeft: IModel = loadModel(new ResourceLocation(Config.RESOURCE_PREFIX + "models/item/powerfist/powerfistLeft.obj"))
  private val powerFistLeftFiring: IModel = loadModel(new ResourceLocation(Config.RESOURCE_PREFIX + "models/item/powerfist/powerfistLeftFiring.obj"))
  private val powerFistRight: IModel = loadModel(new ResourceLocation(Config.RESOURCE_PREFIX + "models/item/powerfist/powerfistRight.obj"))
  private val powerFistRightFiring: IModel = loadModel(new ResourceLocation(Config.RESOURCE_PREFIX + "models/item/powerfist/powerfistRightFiring.obj"))

  val powerFistLeftBaked: IBakedModel = bakeModel(powerFistLeft)
  val powerFistLeftFiringBaked: IBakedModel = bakeModel(powerFistLeftFiring)
  val powerFistRightBaked: IBakedModel = bakeModel(powerFistRight)
  val powerFistRightFiringBaked: IBakedModel = bakeModel(powerFistRightFiring)
}