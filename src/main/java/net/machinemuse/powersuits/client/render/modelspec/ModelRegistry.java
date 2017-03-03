package net.machinemuse.powersuits.client.render.modelspec;

import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.numina.scala.MuseRegistry;
import net.machinemuse.powersuits.common.MPSItems;
import net.machinemuse.powersuits.item.DummyItem;
import net.machinemuse.utils.MuseStringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:44 AM, 4/28/13
 *
 * Ported to Java by lehjr on 11/8/16.
 *
 * Note: make sure to have null checks in place.
 */
public class ModelRegistry extends MuseRegistry<ModelSpec> {
    private static Map<String, String> customData = new HashMap<String, String>();
    private ModelRegistry(){
    }

    private static ModelRegistry INSTANCE;

    public static ModelRegistry getInstance() {
        if (INSTANCE == null)
            INSTANCE = new ModelRegistry();
        return INSTANCE;
    }

    public IBakedModel loadModel(ResourceLocation resource) {
        String name = MuseStringUtils.extractName(resource);
        ModelSpec spec = get(name);
        if (spec == null)
            return wrap(resource);
        return spec.getModel();
    }


    public IBakedModel wrap(ResourceLocation resource) {
        // FIXME: using



//        System.out.println("model location: " + resource.toString());
//
//
//        IModel testModel = null;
//
//        try {
//            testModel = ModelLoaderRegistry.getModel(resource);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        if (!customData.containsKey("flip-v"))
//            customData.put("flip-v", "true");
//        MuseLogger.logDebug("Loading " + resource + " as " + MuseStringUtils.extractName(resource));
//        OBJModel model = null;
//        try {
//            if (!(testModel instanceof OBJModel)) {
//                model = (OBJModel) OBJLoader.INSTANCE.loadModel(resource);
//                System.out.println("loaded model through OBJLoader");
//            } else {
//                System.out.println("loaded model through model loader registry");
//                model = (OBJModel) testModel;
//            }
//
//            model = (OBJModel) model.process(ImmutableMap.copyOf(customData));
//        } catch (Exception e) {
//            e.printStackTrace();
//            MuseLogger.logError("Model loading failed :( " + resource);
//            return null;
//        }
//
//        ResourceLocation textureLocation = null;
//
//        for (ResourceLocation location: model.getTextures()) {
//            Minecraft.getMinecraft().getTextureMapBlocks().registerSprite(location);
//            System.out.println("texture location: " + location.toString());
//            textureLocation = location;
//        }
//
//
//
////        Function<ResourceLocation, TextureAtlasSprite> textureGetter = new Function<ResourceLocation, TextureAtlasSprite>() {
////            public TextureAtlasSprite apply(ResourceLocation resourceLocation) {
////                System.out.println("resource location: " + resourceLocation.toString());
////
////                return Minecraft.getMinecraft().getTextureMapBlocks().registerSprite(resourceLocation);
////            }
////        };
//
//
//        IBakedModel bakedModel = model.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM,
//                new Function<ResourceLocation, TextureAtlasSprite>() {
//                    public TextureAtlasSprite apply(ResourceLocation resourceLocation) {
//                        System.out.println("resource location: " + resourceLocation.toString());
//
//                        return Minecraft.getMinecraft().getTextureMapBlocks().registerSprite(resourceLocation);
//                    }
//                });
////                location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));

        IBakedModel bakedModel = ((DummyItem)MPSItems.dummies).getModel(resource);


        for (ResourceLocation location: ((OBJModel.OBJBakedModel)bakedModel).getModel().getTextures()) {
            Minecraft.getMinecraft().getTextureMapBlocks().registerSprite(location);
            System.out.println("texture location: " + location.toString());
        }


        System.out.println("DONE--------------------");

        if (bakedModel instanceof OBJModel.OBJBakedModel)
            return bakedModel;
        MuseLogger.logError("Model loading failed :( " + resource);
        return null;
    }

    public ModelSpec getModel(NBTTagCompound nbt) {
        return get(nbt.getString("model"));
    }

    public ModelPartSpec getPart(NBTTagCompound nbt, ModelSpec model) {
        return model.get(nbt.getString("part"));
    }

    public ModelPartSpec getPart(NBTTagCompound nbt) {
        return getPart(nbt, getModel(nbt));
    }

    public NBTTagCompound getSpecTag(NBTTagCompound museRenderTag, ModelPartSpec spec) {
        String name = makeName(spec);
        return (museRenderTag.hasKey(name)) ? (museRenderTag.getCompoundTag(name)) : null;
    }

    public String makeName(ModelPartSpec spec) {
        return spec.modelSpec.getOwnName() + "." + spec.partName;
    }
}