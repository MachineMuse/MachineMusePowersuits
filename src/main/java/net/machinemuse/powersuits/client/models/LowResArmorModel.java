package net.machinemuse.powersuits.client.models;

import com.google.common.collect.ImmutableList;
import net.machinemuse.powersuits.client.helpers.ModelTransformCalibration;
import net.machinemuse.powersuits.client.models.obj.OBJModelPlus;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

@SideOnly(Side.CLIENT)
public class LowResArmorModel implements IBakedModel {
    ModelTransformCalibration calibration;
    OBJModelPlus.OBJBakedModelPus armorModel;
    TextureAtlasSprite texture;


    public LowResArmorModel(OBJModelPlus.OBJBakedModelPus armorModel) {
        this.armorModel = armorModel;

    }

    public void SetTexture(TextureAtlasSprite texture) {
        this.texture = texture;


    }

    public List<BakedQuad> getQuadsforPart(String partName) {
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
        for (BakedQuad quad: this.armorModel.getQuadsforPart(partName))
            builder.add(new BakedQuadRetextured(quad, texture));
        return builder.build();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        return null;
    }

//    @Override
//    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
//        return Pair.of(ItemCameraTransforms.TransformType.NONE, calibration.getTransform().getMatrix());
//    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return null;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return null;
    }


}
