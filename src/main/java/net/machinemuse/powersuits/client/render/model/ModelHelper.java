package net.machinemuse.powersuits.client.render.model;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.UnmodifiableIterator;
import com.sun.org.apache.regexp.internal.RE;
import net.machinemuse.powersuits.client.render.modelspec.ModelRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelRotation;
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

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import java.util.*;

/*
 * slimeknights.mantle.client;
 *
 *
 */
public class ModelHelper {










// armor model stuff


//// this will be a little tricky
//// map partName IModelState
////    public Map<String, >
//
//
//

//
//
//
//
//    // Example of "display" only a specific group
//    // Group "group1", to display only "group2"
//    final List <String> visibleGroups = Arrays.asList (new String [] { "group1", "group2"});
//    IBakedModel bakedModel;
//
//		try {
//        IModel model = ModelLoaderRegistry.getModel (resourceLocation);
//        IModelState state = new IModelState () {
//
//            @Override
//            public Optional <TRSRTransformation> apply (Optional <? extends IModelPart> part) {
//                if (part.isPresent ()) {
//                    UnmodifiableIterator <String> parts = Models.getParts (part.get ());
//
//                    if (parts.hasNext ()) {
//                        String name = parts.next ();
//
//                        // To compare only the first element of the parts
//                        if (! parts.hasNext () && visibleGroups.contains (name)) {
//                            // Return the absent in the group to be displayed
//                            return Optional.absent ();
//                        }
//                        // It returns the result to be a isPresent the not display group
//                        return Optional.of (TRSRTransformation.identity ());
//
//                    }
//                }
//
//                // Part returns absent at the time of absent
//                return Optional.absent ();
//            }
//
//        };
//
//        bakedModel = model.bake (state,
//                DefaultVertexFormats.ITEM,
//                new Function <ResourceLocation, TextureAtlasSprite> () {
//
//                    @Override
//                    public TextureAtlasSprite apply (ResourceLocation location) {
//                        Minecraft mc = Minecraft.getMinecraft ();
//							. Return mc.getTextureMapBlocks () getAtlasSprite (location.toString ());
//                    }
//
//                });
//    } Catch (Exception e) {
//        e.printStackTrace ();
//    }

















    public static final IModelState LUXCAPACITOR_ITEM_STATE;


    public static final TRSRTransformation BLOCK_THIRD_PERSON_RIGHT;
    public static final TRSRTransformation BLOCK_THIRD_PERSON_LEFT;

    public static TextureAtlasSprite getTextureFromBlock(Block block, int meta) {
        IBlockState state = block.getStateFromMeta(meta);
        return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state);
    }

    public static TextureAtlasSprite getTextureFromBlockstate(IBlockState state) {
        return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state);
    }

    public static BakedQuad colorQuad(int color, BakedQuad quad) {
        ColorTransformer transformer = new ColorTransformer(color, quad.getFormat());
        quad.pipe(transformer);
        return transformer.build();
    }

    private static TRSRTransformation get(float offsetX, float offsetY, float offsetZ, float angleX, float angleY, float angleZ, float scale) {
        return TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
                new Vector3f(offsetX / 16, offsetY / 16, offsetZ / 16),
                TRSRTransformation.quatFromXYZDegrees(new Vector3f(angleX, angleY, angleZ)),
                new Vector3f(scale, scale, scale),
                null));
    }

    static {
        {
            ImmutableMap.Builder<IModelPart, TRSRTransformation> builder = ImmutableMap.builder();
            builder.put(ItemCameraTransforms.TransformType.GROUND, get(0, 0.1625f, 0, 0,0,0, 0.1563f));
            builder.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, get(0, 0.15625f, 0, 75, 45, 0, 0.0234375f));
            builder.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, get(0, 0.15625f, 0, 75, 45, 0, 0.0234375f));
            builder.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, get(0, 0, 0, 0, 0, 0, 0.025f));
            builder.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, get(0, 0, 0, 0, 0, 0, 0.025f));
            builder.put(ItemCameraTransforms.TransformType.GUI,  get(0, 0.025f, 0, 30, 225, 0, 0.0390625f));
            builder.put(ItemCameraTransforms.TransformType.FIXED, get(0, 0, 0, 0, 0, 0, 0.03125f));
            LUXCAPACITOR_ITEM_STATE = new SimpleModelState(builder.build());
        }
        {
            BLOCK_THIRD_PERSON_RIGHT = get(0, 2.5f, 0, 75, 45, 0, 0.375f);
            BLOCK_THIRD_PERSON_LEFT = get(0, 0, 0, 0, 255, 0, 0.4f);
        }
    }

    /**
     * We need our own because the default set is based on the default=facing north
     * Our model is default facing up
     */
    public static TRSRTransformation getLuxCapacitorBlockTransform(EnumFacing side) {
        Matrix4f matrix;

        switch(side.getOpposite()) {
            case DOWN:
                matrix = TRSRTransformation.identity().getMatrix();
                matrix.setTranslation(new Vector3f(0.0f, -0.4f, 0.0f));
                break;
            case UP:
                matrix = ModelRotation.X180_Y0.getMatrix();
                matrix.setTranslation(new Vector3f(0.0f, 0.4f, 0.0f));
                break;
            case NORTH:
                matrix = ModelRotation.X90_Y0.getMatrix();
                matrix.setTranslation(new Vector3f(0.0f, 0.0f, -0.4f));
                break;
            case SOUTH:
                matrix = ModelRotation.X270_Y0.getMatrix();
                matrix.setTranslation(new Vector3f(0.0f, 0.0f, 0.4f));
                break;
            case WEST:
                matrix = ModelRotation.X90_Y270.getMatrix();
                matrix.setTranslation(new Vector3f(-0.4f, 0.0f, -0.0f));
                break;
            case EAST:
                matrix = ModelRotation.X90_Y90.getMatrix();
                break;
            default:
                matrix = new Matrix4f();
                break;
        }
        matrix.setScale(0.0625f);
        return new TRSRTransformation(matrix);
    }





    private static class ColorTransformer extends VertexTransformer {

        private final float r,g,b,a;

        public ColorTransformer(int color, VertexFormat format) {
            super(new UnpackedBakedQuad.Builder(format));

            int a = (color >> 24);
            if(a == 0) {
                a = 255;
            }
            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = (color >> 0) & 0xFF;

            this.r = (float)r/255f;
            this.g = (float)g/255f;
            this.b = (float)b/255f;
            this.a = (float)a/255f;
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
