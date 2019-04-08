package net.machinemuse.powersuits.client.model.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.machinemuse.numina.client.model.helper.MuseModelHelper;
import net.machinemuse.numina.math.Colour;
import net.machinemuse.powersuits.basemod.MPSItems;
import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.client.model.helper.ColoredQuadHelperThingie;
import net.machinemuse.powersuits.client.model.helper.ModelLuxCapacitorHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static net.minecraft.block.BlockDirectional.FACING;

@OnlyIn(Dist.CLIENT)
public class ModelLuxCapacitor implements IBakedModel {
    final IModelState modelState;
    public IBakedModel wrapper;
    Colour colour;
    private LuxCapacitorItemOverrideList overrides;
    TextureAtlasSprite particleTexture = null;


    public ModelLuxCapacitor() {
        this.overrides = new LuxCapacitorItemOverrideList();
        this.wrapper = this;
        this.modelState = getModelState();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, Random rand) {
        if (side != null)
            return Collections.emptyList();

        EnumFacing facing = EnumFacing.NORTH; // both NORTH and items use TRSRTransformation.Identity because I finally rotated the model
        colour = BlockLuxCapacitor.defaultColor;

        if (state != null) {
            facing = state.get(FACING);
            if (state instanceof IExtendedBlockState)
                if (((IExtendedBlockState) state).getUnlistedProperties().containsKey(BlockLuxCapacitor.COLOR))
                    colour = ((IExtendedBlockState) state).getValue(BlockLuxCapacitor.COLOR);
        }
        if (colour == null)
            colour = BlockLuxCapacitor.defaultColor;
        ColoredQuadHelperThingie helperThingie = new ColoredQuadHelperThingie(colour, facing);

        try {

            List<BakedQuad> quads = ModelLuxCapacitorHelper.INSTANCE.luxCapColoredQuadMap.get(helperThingie);
            if(quads.size() > 0 && this.particleTexture == null)
                this.particleTexture = quads.get(0).getSprite();
            return quads;

        } catch (ExecutionException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
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
        return particleTexture != null ? particleTexture : MissingTextureSprite.getSprite();
    }

    @SuppressWarnings( "deprecation" )
    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return null;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return this.overrides;
    }

    @Override
    public IBakedModel getBakedModel() {
        return this;
    }

    @Override
    public boolean isAmbientOcclusion(IBlockState state) {
        return false;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        TRSRTransformation transform = modelState.apply(Optional.of(cameraTransformType)).orElse(TRSRTransformation.identity());
        if (transform != TRSRTransformation.identity())
            return Pair.of(this, transform.getMatrixVec());

        return Pair.of(this, transform.getMatrixVec());
    }

    public static ModelResourceLocation getModelResourceLocation(EnumFacing facing) {
        return new ModelResourceLocation(MPSItems.INSTANCE.luxCapaRegName, "facing=" + facing.getName());
    }

    public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(MPSItems.INSTANCE.luxCapaRegName, "inventory");

    public static final IModelState getModelState() {
        ImmutableMap.Builder<IModelPart, TRSRTransformation> builder = ImmutableMap.builder();
        builder.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND,
                MuseModelHelper.get(1.13F, 3.2F, 1.13F, -25F, -90F, 0F, 0.41F));

        builder.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND,
                MuseModelHelper.get(0F, 2F, 3F, 0F, 0F, 45F, 0.5F));

        builder.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND,
                MuseModelHelper.get(1.13F, 3.2F, 1.13F, -25F, -90F, 0F, 0.41F));

        builder.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND,
                MuseModelHelper.get(0F, 2F, 3F, 0F, 0F, 45F, 0.5F));

        builder.put(ItemCameraTransforms.TransformType.GUI,
                MuseModelHelper.get(0F, 2.75F, 0F, -45F, 0F, 45F, 0.75F));

        builder.put(ItemCameraTransforms.TransformType.GROUND,
                MuseModelHelper.get(0F, 2F, 0F, -90F, -0F, 0F, 0.5F));

        builder.put(ItemCameraTransforms.TransformType.FIXED,
                MuseModelHelper.get(0F, 0F, -7.5F, 0F, 180F, 0F, 1F));
        return new SimpleModelState(builder.build());
    }

    private class LuxCapacitorItemOverrideList extends ItemOverrideList {
        private LuxCapacitorItemOverrideList() {
            super(
                    ModelLoaderRegistry.getMissingModel(),
                    ModelLoader.defaultModelGetter(),
                    MuseModelHelper.defaultTextureGetter(),
                    ImmutableList.of());
        }

        @Nullable
        @Override // used to be handleItemState
        public IBakedModel getModelWithOverrides(IBakedModel originalModel, ItemStack itemStack, @Nullable World world, @Nullable EntityLivingBase entityLivingBase) {
            return ModelLuxCapacitor.this.wrapper;
        }
    }
}