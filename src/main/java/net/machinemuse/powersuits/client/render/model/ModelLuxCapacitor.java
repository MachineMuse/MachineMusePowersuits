package net.machinemuse.powersuits.client.render.model;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ported to Java by lehjr on 12/27/16.
 */
public class ModelLuxCapacitor implements IBakedModel, IPerspectiveAwareModel {
    int lensColor;
    IBakedModel baseModel;
    ItemCameraTransforms.TransformType cameraTransformType;

    public ModelLuxCapacitor(IBakedModel baseModel, int lensColor) {
        this.lensColor = lensColor;
        this.baseModel = baseModel;
    }


    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformTypeIn) {
        this.cameraTransformType = cameraTransformTypeIn;
        Matrix4f matrix;

        if (baseModel != null && baseModel instanceof IPerspectiveAwareModel) {
            matrix = ((IPerspectiveAwareModel) baseModel).handlePerspective(cameraTransformTypeIn).getValue();
        } else {
            matrix = TRSRTransformation.identity().getMatrix();
        }
        return Pair.of(this, matrix);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        return baseModel.getQuads(state, side, rand);


    }

    @Override
    public boolean isAmbientOcclusion() {
        return baseModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return baseModel.isAmbientOcclusion();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return baseModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return baseModel.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return baseModel.getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return baseModel.getOverrides();
    }
}
