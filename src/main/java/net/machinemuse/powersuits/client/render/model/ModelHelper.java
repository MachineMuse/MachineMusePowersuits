package net.machinemuse.powersuits.client.render.model;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.UnmodifiableIterator;
import com.sun.org.apache.regexp.internal.RE;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.client.render.modelspec.ModelRegistry;
import net.machinemuse.powersuits.common.MPSItems;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.client.model.pipeline.VertexTransformer;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.Models;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.*;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import java.util.*;
import java.util.Properties;


public class ModelHelper {
    /*
      * This is a slightly modified version of Forge's example (@author shadekiller666) for the Tesseract model.
      * With this we can generate an extended blockstate to get the quads of any group in a model without
      * having to rebake the model.
      *
     */
    @Nullable
    public static IExtendedBlockState getStateForPart(String shownIn, OBJModel.OBJBakedModel objBakedModelIn) {
        BlockStateContainer stateContainer = new ExtendedBlockState(null, new IProperty[0], new IUnlistedProperty[] {net.minecraftforge.common.property.Properties.AnimationProperty});
        List<String> hidden = new ArrayList<>(objBakedModelIn.getModel().getMatLib().getGroups().keySet());
        hidden.remove(shownIn);

        try {
            IModelState state = new IModelState() {
                private final Optional<TRSRTransformation> value = Optional.of(TRSRTransformation.identity());

                @Override
                public Optional<TRSRTransformation> apply(Optional<? extends IModelPart> part) {
                    if (part.isPresent()) {
                        UnmodifiableIterator<String> parts = Models.getParts(part.get());
                        if (parts.hasNext()) {
                            String name = parts.next();
                            // only interested in the root level
                            if (!parts.hasNext() && hidden.contains(name)) return value;
                        }
                    }
                    return Optional.absent();
                }
            };
            return ((IExtendedBlockState)stateContainer.getBaseState()).withProperty(net.minecraftforge.common.property.Properties.AnimationProperty, state);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }














//    public static final IModelState LUXCAPACITOR_ITEM_STATE;
//
//
//    public static final TRSRTransformation BLOCK_THIRD_PERSON_RIGHT;
//    public static final TRSRTransformation BLOCK_THIRD_PERSON_LEFT;
//
//    public static TextureAtlasSprite getTextureFromBlock(Block block, int meta) {
//        IBlockState state = block.getStateFromMeta(meta);
//        return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state);
//    }
//
//    public static TextureAtlasSprite getTextureFromBlockstate(IBlockState state) {
//        return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state);
//    }
//

//
//    private static TRSRTransformation get(float offsetX, float offsetY, float offsetZ, float angleX, float angleY, float angleZ, float scale) {
//        return TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
//                new Vector3f(offsetX / 16, offsetY / 16, offsetZ / 16),
//                TRSRTransformation.quatFromXYZDegrees(new Vector3f(angleX, angleY, angleZ)),
//                new Vector3f(scale, scale, scale),
//                null));
//    }
//
//    static {
//        {
//            ImmutableMap.Builder<IModelPart, TRSRTransformation> builder = ImmutableMap.builder();
//            builder.put(ItemCameraTransforms.TransformType.GROUND, get(0, 0.1625f, 0, 0,0,0, 0.1563f));
//            builder.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, get(0, 0.15625f, 0, 75, 45, 0, 0.0234375f));
//            builder.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, get(0, 0.15625f, 0, 75, 45, 0, 0.0234375f));
//            builder.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, get(0, 0, 0, 0, 0, 0, 0.025f));
//            builder.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, get(0, 0, 0, 0, 0, 0, 0.025f));
//            builder.put(ItemCameraTransforms.TransformType.GUI,  get(0, 0.025f, 0, 30, 225, 0, 0.0390625f));
//            builder.put(ItemCameraTransforms.TransformType.FIXED, get(0, 0, 0, 0, 0, 0, 0.03125f));
//            LUXCAPACITOR_ITEM_STATE = new SimpleModelState(builder.build());
//        }
//        {
//            BLOCK_THIRD_PERSON_RIGHT = get(0, 2.5f, 0, 75, 45, 0, 0.375f);
//            BLOCK_THIRD_PERSON_LEFT = get(0, 0, 0, 0, 255, 0, 0.4f);
//        }
//    }
//
//    /**
//     * We need our own because the default set is based on the default=facing north
//     * Our model is default facing up
//     */
//    public static TRSRTransformation getLuxCapacitorBlockTransform(EnumFacing side) {
//        Matrix4f matrix;
//
//        switch(side.getOpposite()) {
//            case DOWN:
//                matrix = TRSRTransformation.identity().getMatrix();
//                matrix.setTranslation(new Vector3f(0.0f, -0.4f, 0.0f));
//                break;
//            case UP:
//                matrix = ModelRotation.X180_Y0.getMatrix();
//                matrix.setTranslation(new Vector3f(0.0f, 0.4f, 0.0f));
//                break;
//            case NORTH:
//                matrix = ModelRotation.X90_Y0.getMatrix();
//                matrix.setTranslation(new Vector3f(0.0f, 0.0f, -0.4f));
//                break;
//            case SOUTH:
//                matrix = ModelRotation.X270_Y0.getMatrix();
//                matrix.setTranslation(new Vector3f(0.0f, 0.0f, 0.4f));
//                break;
//            case WEST:
//                matrix = ModelRotation.X90_Y270.getMatrix();
//                matrix.setTranslation(new Vector3f(-0.4f, 0.0f, -0.0f));
//                break;
//            case EAST:
//                matrix = ModelRotation.X90_Y90.getMatrix();
//                break;
//            default:
//                matrix = new Matrix4f();
//                break;
//        }
//        matrix.setScale(0.0625f);
//        return new TRSRTransformation(matrix);
//    }


    /*
     * Here we can color the quads using the setup below. This is better than changing material colors
     * for Wavefront models because it means that you can use a single material for the entire model
     * instead of unique ones for each group. It also means you don't nescessarily need a Wavefront model.
     */
    public static List<BakedQuad> getColoredQuads(List<BakedQuad> quadList, Colour color) {
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
        for (BakedQuad quad : quadList) {
            builder.add(colorQuad(color, quad));
        }
        return builder.build();
    }

//slimeknights.mantle.client.ModelHelper;
    public static BakedQuad colorQuad(Colour color, BakedQuad quad) {
        ColorTransformer transformer = new ColorTransformer(color, quad.getFormat());
        quad.pipe(transformer);
        return transformer.build();
    }

    private static class ColorTransformer extends VertexTransformer {
        private final float r,g,b,a;

        public ColorTransformer(Colour color, VertexFormat format) {
            super(new UnpackedBakedQuad.Builder(format));
            this.r = (float)color.r;
            this.g = (float)color.g;
            this.b = (float)color.b;
            this.a = (float)color.a;
        }

        @Override
        public void put(int element, float... data) {
            VertexFormatElement.EnumUsage usage = parent.getVertexFormat().getElement(element).getUsage();
            // transform normals and position
            if(usage == VertexFormatElement.EnumUsage.COLOR && data.length >= 4) {
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