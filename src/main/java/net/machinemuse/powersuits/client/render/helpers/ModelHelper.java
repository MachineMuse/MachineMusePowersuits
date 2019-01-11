//package net.machinemuse.powersuits.client.render.helpers;
//
//import com.google.common.base.Function;
//import com.google.common.base.Optional;
//import com.google.common.collect.ImmutableList;
//import com.google.common.collect.ImmutableMap;
//import com.google.common.collect.UnmodifiableIterator;
//import net.machinemuse.numina.utils.MuseLogger;
//import net.machinemuse.numina.utils.math.Colour;
//import net.machinemuse.powersuits.client.model.obj.OBJPlusLoader;
//import net.machinemuse.powersuits.client.model.obj.OBJModelPlus;
//import net.machinemuse.powersuits.client.render.modelspec.ModelSpecXMLReader;
//import net.minecraft.block.properties.IProperty;
//import net.minecraft.block.state.BlockStateContainer;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.block.model.BakedQuad;
//import net.minecraft.client.renderer.block.model.IBakedModel;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
//import net.minecraft.client.renderer.vertex.VertexFormat;
//import net.minecraft.client.renderer.vertex.VertexFormatElement;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.client.model.IModel;
//import net.minecraftforge.client.model.ModelLoaderRegistry;
//import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
//import net.minecraftforge.client.model.pipeline.VertexTransformer;
//import net.minecraftforge.common.model.IModelPart;
//import net.minecraftforge.common.model.IModelState;
//import net.minecraftforge.common.model.Models;
//import net.minecraftforge.common.model.TRSRTransformation;
//import net.minecraftforge.common.property.ExtendedBlockState;
//import net.minecraftforge.common.property.IExtendedBlockState;
//import net.minecraftforge.common.property.IUnlistedProperty;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import javax.annotation.Nullable;
//import javax.vecmath.Vector3f;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
//@OnlyIn(Dist.CLIENT)
//public class ModelHelper {
//    static {
//        new ModelHelper();
//    }
//
//    // One pass just to register the textures called from texture stitch event
//    // another to register the models called from model bake event (second run)
//    public static void loadArmorModels(boolean loadModels) {
//        URL resource = ModelHelper.class.getResource("/assets/powersuits/models/item/armor/modelspec.xml");
//        ModelSpecXMLReader.getINSTANCE().parseFile(resource, loadModels);
//        URL otherResource = ModelHelper.class.getResource("/assets/powersuits/models/item/armor/armor2.xml");
//        ModelSpecXMLReader.getINSTANCE().parseFile(otherResource, loadModels);
//
//        ModelPowerFistHelper.getInstance().loadPowerFistModels(loadModels);
//    }
//
//    public static IModel getIModel(ResourceLocation resource){
//        IModel model = null;
//        try {
//            model = (OBJModelPlus) OBJPlusLoader.INSTANCE.loadModel(resource);
//            model = ((OBJModelPlus) model).process(ImmutableMap.of("flip-v", "true"));
//        } catch (Exception e) {
//            e.printStackTrace();
//            MuseLogger.logError("Model loading failed :( " + resource);
//        }
//        return model;
//    }
//
//    public static IBakedModel loadBakedModel(ResourceLocation resource, IModelState state) {
//        IModel model = getIModel(resource);
//        if (model != null) {
//            IBakedModel bakedModel = model.bake(state,
//                    DefaultVertexFormats.ITEM,
//                    new Function<ResourceLocation, TextureAtlasSprite>() {
//                        public TextureAtlasSprite apply(ResourceLocation resourceLocation) {
//                            return Minecraft.getInstance().getTextureMapBlocks().registerSprite(resourceLocation);
//                        }
//                    });
//            return bakedModel;
//        }
//        return null;
//    }
//
//    public static IBakedModel loadBakedModel(ResourceLocation resource) {
//        IModel model = getIModel(resource);
//        if (model != null) {
//            IBakedModel bakedModel = model.bake(model.getDefaultState(),
//                    DefaultVertexFormats.ITEM,
//                    new Function<ResourceLocation, TextureAtlasSprite>() {
//                        public TextureAtlasSprite apply(ResourceLocation resourceLocation) {
//                            return Minecraft.getInstance().getTextureMapBlocks().registerSprite(resourceLocation);
//                        }
//                    });
//            return bakedModel;
//        }
//        return null;
//    }
//
//    public static IBakedModel getBakedModel(ResourceLocation modellocation, IModelState modelState) {
//        IModel model = getIModel(modellocation);
//
//        try {
//            return model.bake(modelState, DefaultVertexFormats.ITEM,
//                    location -> Minecraft.getInstance().getTextureMapBlocks().getAtlasSprite(location.toString()));
//        } catch (Exception e) {
//            MuseLogger.logError("Failed to bake model. " + e);
//        }
//        return ModelLoaderRegistry.getMissingModel().bake(ModelLoaderRegistry.getMissingModel().getDefaultState(), DefaultVertexFormats.ITEM,
//                location -> Minecraft.getInstance().getTextureMapBlocks().getAtlasSprite(location.toString()));
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
//                private final Optional<TRSRTransformation> getValue = Optional.of(TRSRTransformation.identity());
//
//                @Override
//                public Optional<TRSRTransformation> apply(Optional<? extends IModelPart> part) {
//                    if (part.isPresent()) {
//                        UnmodifiableIterator<String> parts = Models.getParts(part.get());
//                        if (parts.hasNext()) {
//                            String id = parts.next();
//                            // only interested in the root level
//                            if (!parts.hasNext() && hiddenIn.contains(id)) return getValue;
//                        }
//                    }
//                    return Optional.absent();
//                }
//            };
//            return ((IExtendedBlockState)stateContainer.getBaseState()).withProperty(net.minecraftforge.common.property.Properties.AnimationProperty, state);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public static TRSRTransformation get(float transformX, float transformY, float transformZ, float angleX, float angleY, float angleZ, float scaleX, float scaleY, float scaleZ) {
//        return new TRSRTransformation(
//                // Transform
//                new Vector3f(transformX / 16, transformY / 16, transformZ / 16),
//                // Angles
//                TRSRTransformation.quatFromXYZDegrees(new Vector3f(angleX, angleY, angleZ)),
//                // Scale
//                new Vector3f(scaleX, scaleY, scaleZ),
//                null);
//    }
//
//    public static TRSRTransformation get(float transformX, float transformY, float transformZ, float angleX, float angleY, float angleZ, float scale) {
//        return get(transformX, transformY, transformZ, angleX, angleY, angleZ, scale, scale, scale);
//    }
//
//    /*
//     * Here we can color the quads using the setup below. This is better than changing material colors
//     * for Wavefront models because it means that you can use a single material for the entire model
//     * instead of unique ones for each group. It also means you don't nescessarily need a Wavefront model.
//     */
//    public static List<BakedQuad> getColoredQuadsWithGlow(List<BakedQuad> quadList, Colour color, boolean glow) {
//        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
//        quadList.forEach(quad-> builder.add(colorQuad(color, quad, !glow)));
//        return builder.build();
//    }
//
//    public static List<BakedQuad> getColoredQuads(List<BakedQuad> quadList, Colour color) {
//        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
//        for (BakedQuad quad : quadList) {
//            builder.add(colorQuad(color, quad, quad.shouldApplyDiffuseLighting()));
//        }
//        return builder.build();
//    }
//
//    public static BakedQuad colorQuad(Colour color, BakedQuad quad, boolean applyDifuse) {
//        ColorTransformer transformer = new ColorTransformer(color, quad.getFormat(), applyDifuse);
//        quad.pipe(transformer);
//        return transformer.build();
//    }
//
//    private static class ColorTransformer extends VertexTransformer {
//        private final float r, g, b, a;
//        boolean applyDiffuse;
//
//        public ColorTransformer(Colour color, VertexFormat format, boolean applyDiffuse) {
//            super(new UnpackedBakedQuad.Builder(format));
//            this.r = (float) color.r;
//            this.g = (float) color.g;
//            this.b = (float) color.b;
//            this.a = (float) color.a;
//            this.applyDiffuse = applyDiffuse;
//        }
//
//        @Override
//        public void put(int element, float... data) {
//            VertexFormatElement.EnumUsage usage = parent.getVertexFormat().getElement(element).getUsage();
//            // transform normals and position
//            if(usage == VertexFormatElement.EnumUsage.COLOR && data.length >= 4) {
//                data[0] = r;
//                data[1] = g;
//                data[2] = b;
//                data[3] = a;
//            }
//            super.put(element, data);
//        }
//
//        @Override
//        public void setApplyDiffuseLighting(boolean diffuse) {
//            super.setApplyDiffuseLighting(applyDiffuse);
//        }
//
//        public UnpackedBakedQuad build() {
//            return ((UnpackedBakedQuad.Builder) parent).build();
//        }
//    }
//}