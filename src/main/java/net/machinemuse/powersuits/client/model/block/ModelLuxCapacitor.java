package net.machinemuse.powersuits.client.model.block;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.machinemuse.numina.client.model.helper.MuseModelHelper;
import net.machinemuse.numina.math.Colour;
import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.client.model.helper.ColoredQuadHelperThingie;
import net.machinemuse.powersuits.client.model.helper.ModelLuxCapacitorHelper;
import net.machinemuse.powersuits.common.MPSItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static net.minecraft.block.BlockDirectional.FACING;

@SideOnly(Side.CLIENT)
public class ModelLuxCapacitor implements IBakedModel {
    public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(MPSItems.INSTANCE.luxCapacitor.getRegistryName().toString());
    final IModelState modelState;
    public IBakedModel wrapper;
    protected Function<ResourceLocation, TextureAtlasSprite> textureGetter;
    Colour colour;
    private LuxCapacitorItemOverrideList overrides;

    public ModelLuxCapacitor() {
        this.overrides = new LuxCapacitorItemOverrideList();
        this.wrapper = this;
        this.modelState = getModelState();
    }

    public static ModelResourceLocation getModelResourceLocation(EnumFacing facing) {
        return new ModelResourceLocation(MPSItems.INSTANCE.luxCapacitor.getRegistryName().toString(), "facing=" + facing.getName());
    }

    IModelState getModelState() {
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

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if (side != null)
            return Collections.emptyList();

        EnumFacing facing = EnumFacing.NORTH; // both NORTH and items use TRSRTransformation.Identity because I finally rotated the model
        colour = BlockLuxCapacitor.defaultColor;

        if (state != null) {
            facing = state.getValue(FACING);
            if (state instanceof IExtendedBlockState)
                if (((IExtendedBlockState) state).getUnlistedProperties().containsKey(BlockLuxCapacitor.COLOR))
                    colour = ((IExtendedBlockState) state).getValue(BlockLuxCapacitor.COLOR);
        }
        if (colour == null)
            colour = BlockLuxCapacitor.defaultColor;
        ColoredQuadHelperThingie helperThingie = new ColoredQuadHelperThingie(colour, facing);

        try {
            return ModelLuxCapacitorHelper.INSTANCE.luxCapColoredQuadMap.get(helperThingie);
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
        return MuseIcon.luxCapacitorTexture;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return this.overrides;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        TRSRTransformation transform = modelState.apply(Optional.of(cameraTransformType)).orElse(TRSRTransformation.identity());
        if (transform != TRSRTransformation.identity())
            return Pair.of(this, transform.getMatrix());

        return Pair.of(this, transform.getMatrix());
    }

    private class LuxCapacitorItemOverrideList extends ItemOverrideList {
        private LuxCapacitorItemOverrideList() {
            super(ImmutableList.of());
        }

        @Nonnull
        public IBakedModel handleItemState(@Nonnull IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
            return ModelLuxCapacitor.this.wrapper;
        }
    }
}