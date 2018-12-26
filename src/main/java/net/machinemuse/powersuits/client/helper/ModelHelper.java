package net.machinemuse.powersuits.client.helper;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.UnmodifiableIterator;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.numina.utils.math.Colour;
import net.machinemuse.powersuits.client.model.obj.OBJModelPlus;
import net.machinemuse.powersuits.client.model.obj.OBJPlusLoader;
import net.machinemuse.powersuits.client.render.helpers.ModelPowerFistHelper;
import net.machinemuse.powersuits.client.render.modelspec.ModelSpecXMLReader;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.client.model.pipeline.VertexTransformer;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.Models;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class ModelHelper {
    static {
        new ModelHelper();
    }

    // One pass just to register the textures called from texture stitch event
    // another to register the models called from model bake event (second run)
    public static void loadArmorModels(@Nullable TextureStitchEvent event) {
        ArrayList<String> resourceList = new ArrayList<String>() {{
            add("/assets/powersuits/modelspec/armor2.xml");
            add("/assets/powersuits/modelspec/default_armor.xml");
            add("/assets/powersuits/modelspec/default_armorskin.xml");
            add("/assets/powersuits/modelspec/armor_skin2.xml");
            add("/assets/powersuits/modelspec/default_powerfist.xml");
        }};

        for (String resourceString : resourceList) {
            parseSpecFile(resourceString, event);
        }
//
//        URL resource = ModelHelper.class.getResource("/assets/powersuits/models/item/armor/modelspec.xml");
//        ModelSpecXMLReader.INSTANCE.parseFile(resource, event);
//        URL otherResource = ModelHelper.class.getResource("/assets/powersuits/models/item/armor/armor2.xml");
//        ModelSpecXMLReader.INSTANCE.parseFile(otherResource, event);

        ModelPowerFistHelper.INSTANCE.loadPowerFistModels(event);
    }

    public static void parseSpecFile(String resourceString, @Nullable TextureStitchEvent event) {
        URL resource = ModelHelper.class.getResource(resourceString);
        ModelSpecXMLReader.INSTANCE.parseFile(resource, event);
    }


    //-------------------------------------
    public static TRSRTransformation get(float transformX, float transformY, float transformZ, float angleX, float angleY, float angleZ, float scaleX, float scaleY, float scaleZ) {
        return new TRSRTransformation(
                // Transform
                new Vector3f(transformX / 16, transformY / 16, transformZ / 16),
                // Angles
                TRSRTransformation.quatFromXYZDegrees(new Vector3f(angleX, angleY, angleZ)),
                // Scale
                new Vector3f(scaleX, scaleY, scaleZ),
                null);
    }

    public static TRSRTransformation get(float transformX, float transformY, float transformZ, float angleX, float angleY, float angleZ, float scale) {
        return get(transformX, transformY, transformZ, angleX, angleY, angleZ, scale, scale, scale);
    }


    //-----------------------------------
//
//
//
//
//
    public static IModel getModel(ResourceLocation resource) {
        IModel model = null;
        try {
            model = (OBJModelPlus) OBJPlusLoader.INSTANCE.loadModel(resource);
            model = ((OBJModelPlus) model).process(ImmutableMap.of("flip-v", "true"));
        } catch (Exception e) {
            e.printStackTrace();
            MuseLogger.logError("Model loading failed :( " + resource);
        }
        return model;
    }

    public static IBakedModel loadBakedModel(ResourceLocation resource, IModelState state) {
        IModel model = getModel(resource);
        if (model != null) {
            IBakedModel bakedModel = model.bake(state,
                    DefaultVertexFormats.ITEM,
                    new Function<ResourceLocation, TextureAtlasSprite>() {
                        public TextureAtlasSprite apply(ResourceLocation resourceLocation) {
                            return Minecraft.getMinecraft().getTextureMapBlocks().registerSprite(resourceLocation);
                        }
                    });
            return bakedModel;
        }
        return null;
    }

    public static IBakedModel loadBakedModel(ResourceLocation resource) {
        IModel model = getModel(resource);
        if (model != null) {
            IBakedModel bakedModel = model.bake(model.getDefaultState(),
                    DefaultVertexFormats.ITEM,
                    new Function<ResourceLocation, TextureAtlasSprite>() {
                        public TextureAtlasSprite apply(ResourceLocation resourceLocation) {
                            return Minecraft.getMinecraft().getTextureMapBlocks().registerSprite(resourceLocation);
                        }
                    });
            return bakedModel;
        }
        return null;
    }

//    public static IBakedModel getBakedModel(ResourceLocation modellocation, IModelState modelState) {
//        IModel model = getIModel(modellocation);
//
//        try {
//            return model.bake(modelState, DefaultVertexFormats.ITEM,
//                    location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
//        } catch (Exception e) {
//            MuseLogger.logError("Failed to bake model. " + e);
//        }
//        return ModelLoaderRegistry.getMissingModel().bake(ModelLoaderRegistry.getMissingModel().getDefaultState(), DefaultVertexFormats.ITEM,
//                location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
//    }
//
//    /*
//     * This is a slightly modified version of Forge's example (@author shadekiller666) for the Tesseract model.
//     * With this we can generate an extended blockstates to get the quads of any group in a model without
//     * having to rebake the model. In this perticular case, the setup is for gettting an extended state that
//     * will hide all groups but one. However, this can easily be altered to hide fewer parts if needed.
//     *
//     * The biggest issue with this setup is that the code. There is a better way out there
//     */
//    @Nullable
//    public static IExtendedBlockState getStateForPart(String shownIn, OBJModelPlus.OBJBakedModelPus objBakedModelIn) {
//        List<String> hidden = new ArrayList<>(objBakedModelIn.getIModel().getMatLib().getGroups().keySet());
//        return getStateForPart(shownIn, hidden);
//    }
//
//    public static IExtendedBlockState getStateForPart(String shownIn, List<String> hiddenIn) {
//        BlockStateContainer stateContainer = new ExtendedBlockState(null, new IProperty[0], new IUnlistedProperty[] {net.minecraftforge.common.property.Properties.AnimationProperty});
//
//        hiddenIn.remove(shownIn);
//
//        try {
//            IModelState state = new IModelState() {
//                private final java.util.Optional<TRSRTransformation> getValue = java.util.Optional.of(TRSRTransformation.identity());
//
//                @Override
//                public java.util.Optional<TRSRTransformation> apply(java.util.Optional<? extends IModelPart> part) {
//                    if (part.isPresent()) {
//                        UnmodifiableIterator<String> parts = Models.getParts(part.get());
//                        if (parts.hasNext()) {
//                            String id = parts.next();
//                            // only interested in the root level
//                            if (!parts.hasNext() && hiddenIn.contains(id)) return getValue;
//                        }
//                    }
//                    return java.util.Optional.empty();
//                }
//            };
//            return ((IExtendedBlockState)stateContainer.getBaseState()).withProperty(net.minecraftforge.common.property.Properties.AnimationProperty, state);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    //--


    public static IModel getIModel(ResourceLocation location, int attempt) {
        String domain = location.getNamespace();
        String resourePath = location.getPath().replaceFirst("^models/models", "models");

        location = new ResourceLocation(domain, resourePath);
        IModel model;
        try {
            model = ModelLoaderRegistry.getModel(location);
            model = model.process(ImmutableMap.of("flip-v", "true"));
        } catch (Exception e) {
            model = ModelLoaderRegistry.getMissingModel();
            if (attempt < 6) {
                model = getIModel(location, attempt + 1);
                MuseLogger.logError("Model loading failed on attempt #" + attempt + "  :( " + location.toString());
            } else {
                MuseLogger.logError("Failed to loadButton model. " + e);
                return getOBJModel(location, 0);
            }
        }
        return model;
    }

    public static IModel getOBJModel(ResourceLocation location, int attempt) {
        String domain = location.getNamespace();
        String resourePath = location.getPath().replaceFirst("^models/models", "models");

        location = new ResourceLocation(domain, resourePath);
        IModel model;
        try {
            model = OBJPlusLoader.INSTANCE.loadModel(location);
            model = model.process(ImmutableMap.of("flip-v", "true"));
        } catch (Exception e) {
            model = ModelLoaderRegistry.getMissingModel();
            if (attempt < 6) {
                getOBJModel(location, attempt + 1);
                MuseLogger.logError("Model loading failed on attempt #" + attempt + "  :( " + location.toString());
            } else
                return model;
            MuseLogger.logError("Failed to loadButton model. " + e);
        }
        return model;
    }

    public static IBakedModel getBakedModel(ResourceLocation modellocation, IModelState modelState) {
        IModel model = getIModel(modellocation, 0);

        try {
            return model.bake(modelState, DefaultVertexFormats.ITEM,
                    location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
        } catch (Exception e) {
            MuseLogger.logError("Failed to bake model. " + e);
        }
        return ModelLoaderRegistry.getMissingModel().bake(ModelLoaderRegistry.getMissingModel().getDefaultState(), DefaultVertexFormats.ITEM,
                location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
    }

    /*
     * This is a slightly modified version of Forge's example (@author shadekiller666) for the Tesseract model.
     * With this we can generate an extended blockstates to get the quads of any group in a model without
     * having to rebake the model. In this perticular case, the setup is for gettting an extended state that
     * will hide all groups but one. However, this can easily be altered to hide fewer parts if needed.
     *
     * The biggest issue with this setup is that the code. There is a better way out there
     */
    @Nullable
    public static IExtendedBlockState getStateForPart(List<String> shownIn, OBJModelPlus.OBJBakedModelPus objBakedModelIn, @Nullable TRSRTransformation transformation) {
        List<String> hidden = new ArrayList<>(objBakedModelIn.getModel().getMatLib().getGroups().keySet());
        if (transformation == null)
            transformation = TRSRTransformation.identity();
        return getStateForPart(shownIn, hidden, transformation);
    }

    @Nullable
    public static List<String> getPartNames(IBakedModel bakedModel) {
        if (bakedModel != null && bakedModel instanceof OBJModelPlus.OBJBakedModelPus)
            return new ArrayList<String>(((OBJModelPlus.OBJBakedModelPus) bakedModel).getModel().getMatLib().getGroups().keySet());
        return null;
    }

    public static IExtendedBlockState getStateForPart(List<String> shownIn, List<String> hiddenIn, final TRSRTransformation transformation) {
        BlockStateContainer stateContainer = new ExtendedBlockState(null, new IProperty[0], new IUnlistedProperty[]{net.minecraftforge.common.property.Properties.AnimationProperty});
        for (String shown : shownIn)
            hiddenIn.remove(shown);

        try {
            IModelState state = new IModelState() {
                private final java.util.Optional<TRSRTransformation> value = java.util.Optional.of(transformation);

                @Override
                public java.util.Optional<TRSRTransformation> apply(java.util.Optional<? extends IModelPart> part) {
                    if (part.isPresent()) {
                        UnmodifiableIterator<String> parts = Models.getParts(part.get());
                        if (parts.hasNext()) {
                            String name = parts.next();
                            // only interested in the root level
                            if (!parts.hasNext() && hiddenIn.contains(name)) return value;
                        }
                    }
                    return java.util.Optional.empty();
                }
            };
            return ((IExtendedBlockState) stateContainer.getBaseState()).withProperty(net.minecraftforge.common.property.Properties.AnimationProperty, state);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Here we can color the quads or change the transform using the setup below.
     * This is better than changing material colors for Wavefront models because it means that you can use a single material for the entire model
     * instead of unique ones for each group. It also means you don't nescessarily need a Wavefront model.
     */
    public static List<BakedQuad> getColouredQuadsWithGlowAndTransform(List<BakedQuad> quadList, Colour colour, final TRSRTransformation transform, boolean glow) {
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
        quadList.forEach(quad -> builder.add(colouredQuadWithGlowAndTransform(colour, quad, !glow, transform)));
        return builder.build();
    }

    public static BakedQuad colouredQuadWithGlowAndTransform(Colour colour, BakedQuad quad, boolean applyDifuse, TRSRTransformation transform) {
        QuadTransformer transformer = new QuadTransformer(colour, transform, quad.getFormat(), applyDifuse);
        quad.pipe(transformer);
        return transformer.build();
    }

    public static List<BakedQuad> getColoredQuadsWithGlow(List<BakedQuad> quadList, Colour color, boolean glow) {
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
        quadList.forEach(quad -> builder.add(colorQuad(color, quad, !glow)));
        return builder.build();
    }

    public static List<BakedQuad> getColoredQuads(List<BakedQuad> quadList, Colour color) {
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
        for (BakedQuad quad : quadList) {
            builder.add(colorQuad(color, quad, quad.shouldApplyDiffuseLighting()));
        }
        return builder.build();
    }

    public static BakedQuad colorQuad(Colour color, BakedQuad quad, boolean applyDifuse) {
        QuadTransformer transformer = new QuadTransformer(color, quad.getFormat(), applyDifuse);
        quad.pipe(transformer);
        return transformer.build();
    }

    private static class QuadTransformer extends VertexTransformer {
        Colour colour;
        Boolean applyDiffuse;
        TRSRTransformation transform;

        public QuadTransformer(Colour colour, VertexFormat format, boolean applyDiffuse) {
            super(new UnpackedBakedQuad.Builder(format));
            this.colour = colour;
            this.applyDiffuse = applyDiffuse;
        }

        public QuadTransformer(Colour colour, final TRSRTransformation transform, VertexFormat format, boolean applyDiffuse) {
            super(new UnpackedBakedQuad.Builder(format));
            this.transform = transform;
            this.colour = colour;
            this.applyDiffuse = applyDiffuse;
        }

        @Override
        public void put(int element, float... data) {
            VertexFormatElement.EnumUsage usage = parent.getVertexFormat().getElement(element).getUsage();
//            System.out.println("element: " + element);
//            System.out.println("usage: " + usage.getDisplayName());
            // change color
            if (colour != null &&
                    usage == VertexFormatElement.EnumUsage.COLOR &&
                    data.length >= 4) {
                data[0] = (float) colour.r;
                data[1] = (float) colour.g;
                data[2] = (float) colour.b;
                data[3] = (float) colour.a;
                super.put(element, data);
                // transform normals and position
            } else if (transform != null &&
                    usage == VertexFormatElement.EnumUsage.POSITION &&
                    data.length >= 4) {
                float[] newData = new float[4];
                Vector4f vec = new Vector4f(data);
                transform.getMatrix().transform(vec);
                vec.get(newData);
                parent.put(element, newData);
            } else
                super.put(element, data);
        }

        @Override
        public void setApplyDiffuseLighting(boolean diffuse) {
            super.setApplyDiffuseLighting(applyDiffuse != null ? applyDiffuse : diffuse);
        }

        public UnpackedBakedQuad build() {
            return ((UnpackedBakedQuad.Builder) parent).build();
        }
    }
}