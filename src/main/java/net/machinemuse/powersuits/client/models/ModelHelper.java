package net.machinemuse.powersuits.client.models;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.UnmodifiableIterator;
import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.numina.geometry.Colour;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
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
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ModelHelper {
    static {
        new ModelHelper();
    }

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

    public static IModel getIModel(ResourceLocation location, int attempt) {
        // powersuits:models/item/armor/mps_helm.obj
        // powersuits:models/item/armor/mps_helm.obj

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
                MuseLogger.logError("Failed to load model. " + e);

                return getOBJModel(location, 0);
            }
        }
        return model;
    }

    public static IModel getOBJModel(ResourceLocation location, int attempt) {
        IModel model;
        try {
            model = OBJLoader.INSTANCE.loadModel(location);
            model = model.process(ImmutableMap.of("flip-v", "true"));
        } catch (Exception e) {
            model = ModelLoaderRegistry.getMissingModel();
            if (attempt < 6) {
                getIModel(location, attempt + 1);
                MuseLogger.logError("Model loading failed on attempt #" + attempt + "  :( " + location.toString());
            } else
                return model;
            MuseLogger.logError("Failed to load model. " + e);
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
     * With this we can generate an extended blockstate to get the quads of any group in a model without
     * having to rebake the model. In this perticular case, the setup is for gettting an extended state that
     * will hide all groups but one. However, this can easily be altered to hide fewer parts if needed.
     *
     * The biggest issue with this setup is that the code. There is a better way out there
     */
    @Nullable
    public static IExtendedBlockState getStateForPart(List<String> shownIn, OBJModel.OBJBakedModel objBakedModelIn, @Nullable TRSRTransformation transformation) {
        List<String> hidden = new ArrayList<>(objBakedModelIn.getModel().getMatLib().getGroups().keySet());
        if (transformation == null)
            transformation = TRSRTransformation.identity();
        return getStateForPart(shownIn, hidden, transformation);
    }

    @Nullable
    public static List<String> getPartNames(IBakedModel bakedModel) {
        if (bakedModel != null && bakedModel instanceof OBJModel.OBJBakedModel)
            return new ArrayList<String>(((OBJModel.OBJBakedModel) bakedModel).getModel().getMatLib().getGroups().keySet());
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
     * Here we can color the quads using the setup below. This is better than changing material colors
     * for Wavefront models because it means that you can use a single material for the entire model
     * instead of unique ones for each group. It also means you don't nescessarily need a Wavefront model.
     */
    public static List<BakedQuad> getColoredQuadsWithGlow(List<BakedQuad> quadList, Colour color, boolean glow) {
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
        quadList.forEach(quad-> builder.add(colorQuad(color, quad, !glow)));
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
        ColorTransformer transformer = new ColorTransformer(color, quad.getFormat(), applyDifuse);
        quad.pipe(transformer);
        return transformer.build();
    }

    private static class ColorTransformer extends VertexTransformer {
        private final float r, g, b, a;
        public ColorTransformer(Colour color, VertexFormat format, boolean applyDiffuse) {
            super(new UnpackedBakedQuad.Builder(format));
            parent.setApplyDiffuseLighting(applyDiffuse);

            this.r = (float) color.r;
            this.g = (float) color.g;
            this.b = (float) color.b;
            this.a = (float) color.a;
        }

        @Override
        public void put(int element, float... data) {
            VertexFormatElement.EnumUsage usage = parent.getVertexFormat().getElement(element).getUsage();
            // transform normals and position
            if (usage == VertexFormatElement.EnumUsage.COLOR && data.length >= 4) {
                data[0] = r;
                data[1] = g;
                data[2] = b;
                data[3] = a;
            }
            super.put(element, data);
        }

        public UnpackedBakedQuad build() {
            return ((UnpackedBakedQuad.Builder) parent).build();
        }
    }
}