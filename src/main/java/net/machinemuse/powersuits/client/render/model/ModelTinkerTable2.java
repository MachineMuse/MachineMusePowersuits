package net.machinemuse.powersuits.client.render.model;

import net.machinemuse.numina.geometry.Colour;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;

import static net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.NONE;

/**
 * Created by leon on 4/23/17.
 */
public class ModelTinkerTable2 implements IBakedModel, IPerspectiveAwareModel {
    IBakedModel tinkerTableModel;
    ItemCameraTransforms.TransformType cameraTransformType;

    Colour whitealpha = Colour.LIGHTBLUE.withAlpha(.5);



    public ModelTinkerTable2(IBakedModel tinkerTableModelIn) {
        if (tinkerTableModel instanceof ModelTinkerTable2) {
            tinkerTableModel = ((ModelTinkerTable2)tinkerTableModelIn).tinkerTableModel;
        } else {
            tinkerTableModel = tinkerTableModelIn;
        }
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformTypeIn) {
        cameraTransformType = cameraTransformTypeIn;

//        if (cameraTransformType != FIRST_PERSON_RIGHT_HAND && cameraTransformType != GUI)
//            System.out.println("type is " + cameraTransformType.name());



        Matrix4f matrix;

//        if (cameraTransformType == NONE) {
//            List<BakedQuad> quadList = tinkerTableModel.getQuads(null, null, 0);
//
//
//            GlStateManager.pushMatrix();
////            GlStateManager.scale(scale, scale, scale);
////            applyTransform();
//            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
////            part.modelSpec.applyOffsetAndRotation(); // not yet implemented
//
//            Tessellator tess = Tessellator.getInstance();
//            VertexBuffer buffer = tess.getBuffer();
//            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
//
//            for (BakedQuad quad : quadList) {
//                buffer.addVertexData(quad.getVertexData());
//                ForgeHooksClient.putQuadColor(buffer, quad, Colour.WHITE.getInt());
//
//
//            }
//            tess.draw();
//
//            GlStateManager.popMatrix();
//
//
//
//
//
//        }








        if (tinkerTableModel != null && tinkerTableModel instanceof IPerspectiveAwareModel) {
            matrix = ((IPerspectiveAwareModel) tinkerTableModel).handlePerspective(cameraTransformTypeIn).getValue();
        } else {
            matrix = TRSRTransformation.identity().getMatrix();
        }
        return Pair.of(this, matrix);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
//        if (cameraTransformType == NONE)
//            return null;

//        if (state != null) {
//            if (cameraTransformType != null)
//            System.out.print("camera transorm is: " + cameraTransformType.name());
            List<BakedQuad> quadList = tinkerTableModel.getQuads(state, side, rand);
//            quadList = ModelHelper.getColoredQuads(quadList, whitealpha);
            return quadList;
//        }




//        return tinkerTableModel.getQuads(state, side, rand);
    }

    @Override
    public boolean isAmbientOcclusion() {
        return tinkerTableModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return tinkerTableModel.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        if (cameraTransformType == NONE)
            return true;
        return tinkerTableModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return tinkerTableModel.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return tinkerTableModel.getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return tinkerTableModel.getOverrides();
    }
}