package net.machinemuse.powersuits.client.render.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;

/**
 * Ported to Java by lehjr on 12/27/16.
 */
public class ModelLuxCapacitor implements IBakedModel, IPerspectiveAwareModel {
    private static final LuxCapModelHelper modelHelper = LuxCapModelHelper.getInstance();
    private static IBakedModel baseFrameModel; // used mainly for the Item model
    private static IBakedModel baseLightModel; // used mainly for the Item model

    public ModelLuxCapacitor(IBakedModel bakedModelIn) {
        this.baseFrameModel = bakedModelIn;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        baseFrameModel = modelHelper.getFrameForFacing(null);
        if (side != null) return ImmutableList.of(); // expected MPSTestingOBJBakedModel behaviour
        return modelHelper.getQuads((IExtendedBlockState) state);
    }

    @Override
    public boolean isAmbientOcclusion() {
        return baseFrameModel.isAmbientOcclusion();
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformTypeIn) {
        Matrix4f matrix;
        if (baseFrameModel != null && baseFrameModel instanceof IPerspectiveAwareModel) {
            matrix = ((IPerspectiveAwareModel) baseFrameModel).handlePerspective(cameraTransformTypeIn).getValue();
        } else {
            matrix = TRSRTransformation.identity().getMatrix();
        }
        return Pair.of(this, matrix);
    }

    @Override
    public boolean isBuiltInRenderer() {
        return baseFrameModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return modelHelper.getParticleTexture();
    }

    @Override
    public boolean isGui3d() {
        return baseFrameModel.isAmbientOcclusion();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return baseFrameModel.getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return baseFrameModel.getOverrides();
    }
}