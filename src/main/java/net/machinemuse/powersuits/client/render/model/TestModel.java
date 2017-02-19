package net.machinemuse.powersuits.client.render.model;


import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.List;

public class TestModel implements IBakedModel, IPerspectiveAwareModel {

    /*
        Notes:
        can do something kinda similar to this here: https://github.com/aidancbrady/Mekanism/blob/1.10/src/main/java/mekanism/client/render/item/BakedCustomItemModel.java
        but for our baked models; kinda like what I was using when I got side tracked with fixing 1.7.10

        Of course we'll still need to use our model registry for storing and retrieving uniquely colored parts, but we can probably do that from here.


        * The tinker table item model is still modelbase, just reduced scale.
        * The armor models use an icon for everything but equipped.
        *







     */





    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {

        /*
                NONE,
        THIRD_PERSON_LEFT_HAND,
        THIRD_PERSON_RIGHT_HAND,
        FIRST_PERSON_LEFT_HAND,
        FIRST_PERSON_RIGHT_HAND,
        HEAD, <--- for items equipped on head
        GUI,
        GROUND,
        FIXED; <-- for things like frames
         */






        return null;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {


        List<BakedQuad> emptyQuadList= new ArrayList<>();
        return emptyQuadList;

    }

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
    public ItemCameraTransforms getItemCameraTransforms() {
        return null;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return null;
    }
}