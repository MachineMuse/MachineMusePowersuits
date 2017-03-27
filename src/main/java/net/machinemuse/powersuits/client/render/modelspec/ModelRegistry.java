package net.machinemuse.powersuits.client.render.modelspec;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.numina.scala.MuseRegistry;
import net.machinemuse.powersuits.common.MPSItems;
import net.machinemuse.powersuits.item.DummyItem;
import net.machinemuse.utils.MuseStringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;

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
    private static final Map<String, String> customData = ImmutableMap.of("flip-v", "true");
    private ModelRegistry(){
    }

    private static ModelRegistry INSTANCE;

    public static ModelRegistry getInstance() {
        if (INSTANCE == null)
            INSTANCE = new ModelRegistry();
        return INSTANCE;
    }

    /*
     *  replacement methods adding the ResourceLocation for the model texture
     *  going to try to brute force the textures to pair up with the models.
     *
     */
    public IBakedModel loadModel(ResourceLocation modelLocation, ResourceLocation textureLocation) {
        String name = MuseStringUtils.extractName(modelLocation);
        ModelSpec spec = get(name);
        if (spec == null)
            return wrap(modelLocation, textureLocation);
        return spec.getModel();
    }

    public IBakedModel wrap(ResourceLocation modelLocation, ResourceLocation textureLocation){
        IModel model = null;
        try {
            model = ModelLoaderRegistry.getModel(modelLocation);
        } catch (Exception error) {
            try {
                if (!(model instanceof OBJModel)) {
                    model = (OBJModel) OBJLoader.INSTANCE.loadModel(modelLocation);
                    System.out.println("loaded model through OBJLoader");
                } else {
                    System.out.println("loaded model through model loader registry");
                    model = (OBJModel) model;
                }

                model = ((OBJModel) model).process(ImmutableMap.copyOf(ImmutableMap.of("flip-v", "true")));
            } catch (Exception e) {
                e.printStackTrace();
                MuseLogger.logError("Model loading failed :( " + modelLocation);
                return null;
            }
        }

        IBakedModel bakedModel = model.bake(model.getDefaultState(), DefaultVertexFormats.ITEM,

        // just pick one of these:
//                defaultTextureGetter());
//                location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
                new Function<ResourceLocation, TextureAtlasSprite>() {
                    public TextureAtlasSprite apply(ResourceLocation resourceLocation) {
                        System.out.println("resource location: " + resourceLocation.toString());
                        System.out.println("resource location: " + textureLocation.toString());

                        return Minecraft.getMinecraft().getTextureMapBlocks().registerSprite(textureLocation != null ? textureLocation : resourceLocation);
                    }
                });
        return bakedModel;
    }




    public IBakedModel loadModel(ResourceLocation resource) {
        String name = MuseStringUtils.extractName(resource);
        ModelSpec spec = get(name);
        if (spec == null)
            return wrap(resource);
        return spec.getModel();
    }




//    protected ResourceLocation getItemLocation(ResourceLocation resourceLocation) {
//        String location = resourceLocation.toString();
//        ResourceLocation resourcelocation = new ResourceLocation(location.replaceAll("#.*", "").
//                replaceAll("item/", "models/item/"));//.replaceAll(".obj.*", ""));
//
//        return resourcelocation;
////        return new ResourceLocation(resourcelocation.getResourceDomain(), "item/armor/json/" + resourcelocation.getResourcePath());
//    }



    public IBakedModel wrap(ResourceLocation resource) {
        Item dummies = MPSItems.dummies;
        IBakedModel bakedModel = ((DummyItem)dummies).getModel(resource);

        if (bakedModel instanceof OBJModel.OBJBakedModel)
            return bakedModel;
        MuseLogger.logError("Model loading failed :( " + resource);
        return null;

        // TODO: fix texture issue so models can be loaded without hacky workaround
//        resource = getItemLocation(resource);
//
//
//        IModel model = null;
//        try {
//            model = ModelLoaderRegistry.getModel(resource);
//        } catch (Exception error) {
//            try {
//                if (!(model instanceof OBJModel)) {
//                    model = (OBJModel) OBJLoader.INSTANCE.loadModel(resource);
//                    System.out.println("loaded model through OBJLoader");
//                } else {
//                    System.out.println("loaded model through model loader registry");
//                    model = (OBJModel) model;
//                }
//
//                model = ((OBJModel) model).process(ImmutableMap.copyOf(ImmutableMap.of("flip-v", "true")));
//            } catch (Exception e) {
//                e.printStackTrace();
//                MuseLogger.logError("Model loading failed :( " + resource);
//                return null;
//            }
//        }
//
//        IBakedModel bakedModel = model.bake(model.getDefaultState(), DefaultVertexFormats.ITEM,
//
//        // just pick one of these:
////                defaultTextureGetter());
////                location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
//                new Function<ResourceLocation, TextureAtlasSprite>() {
//                    public TextureAtlasSprite apply(ResourceLocation resourceLocation) {
//                        System.out.println("resource location: " + resourceLocation.toString());
//                        return Minecraft.getMinecraft().getTextureMapBlocks().registerSprite(resourceLocation);
//                    }
//                });

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