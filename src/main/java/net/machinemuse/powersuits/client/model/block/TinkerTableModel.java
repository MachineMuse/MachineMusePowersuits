package net.machinemuse.powersuits.client.model.block;

import com.google.common.collect.ImmutableMap;
import net.machinemuse.numina.client.model.helper.MuseModelHelper;
import net.machinemuse.powersuits.client.model.helper.ModelTransformCalibration;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class TinkerTableModel implements IBakedModel {
    IBakedModel bakedModel;
    ModelTransformCalibration transformCalibration;

    public TinkerTableModel(IBakedModel modelIn) {
        this.bakedModel = modelIn;
        transformCalibration = new ModelTransformCalibration(
0, 2.5f, 0, 75, -135, 0, 0.375f);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, Random rand) {
        return bakedModel.getQuads(state, side, rand);
    }


    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        TRSRTransformation transform = getModelState().apply(Optional.of(cameraTransformType)).orElse(TRSRTransformation.identity());
        if (transform != TRSRTransformation.identity())
            return Pair.of(this, transform.getMatrixVec());

        return Pair.of(this, transform.getMatrixVec());
    }



    public IModelState getModelState() {
        ImmutableMap.Builder<IModelPart, TRSRTransformation> builder = ImmutableMap.builder();

        // first person and third person models rotated to so that the side away from the player is the same as when it is placed
        builder.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND,
                MuseModelHelper.get(0, 0, 0, 0, 135, 0, 0.4f));

        builder.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND,
                MuseModelHelper.get(0, 0, 0, 0, 135, 0, 0.4f));

        builder.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND,
                MuseModelHelper.get(0, 2.5f, 0, 75, -135, 0, 0.375f));

        builder.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND,
                MuseModelHelper.get(0, 2.5f, 0, 75, -135, 0, 0.375f));

        builder.put(ItemCameraTransforms.TransformType.GUI,
                MuseModelHelper.get(-0.0625F, 0.25F, 0, 30, 225, 0, 0.625f));

        builder.put(ItemCameraTransforms.TransformType.GROUND,
                MuseModelHelper.get(0, 3, 0, 0, 0, 0, 0.25f));

        builder.put(ItemCameraTransforms.TransformType.FIXED,
                MuseModelHelper.get(0, 0, 0, 0, 0, 0, 0.5f));

        return new SimpleModelState(builder.build());
    }

    @Override
    public boolean isAmbientOcclusion() {
        return bakedModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return bakedModel.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return bakedModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return bakedModel.getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return bakedModel.getOverrides();
    }
}
