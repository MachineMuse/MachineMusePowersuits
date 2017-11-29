package net.machinemuse.powersuits.client.models;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import net.machinemuse.powersuits.client.events.MuseIcon;
import net.machinemuse.powersuits.client.helpers.ColoredQuadHelperThingie;
import net.machinemuse.powersuits.client.helpers.EnumColour;
import net.machinemuse.powersuits.common.blocks.BlockLuxCapacitor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static net.minecraft.block.BlockDirectional.FACING;

@SideOnly(Side.CLIENT)
public class ModelLuxCapacitor implements IBakedModel {
    public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(BlockLuxCapacitor.getInstance().getRegistryName().toString());
    public IBakedModel wrapper;
    protected Function<ResourceLocation, TextureAtlasSprite> textureGetter;
    EnumColour colour;
    Matrix4f defaultTransform;
    private LuxCapacitorItemOverrideList overrides;
    private Map<ItemCameraTransforms.TransformType, Matrix4f> cameraTransforms;
    public ModelLuxCapacitor() {
        this.overrides = new LuxCapacitorItemOverrideList();
        this.wrapper = this;
        this.cameraTransforms = new HashMap<ItemCameraTransforms.TransformType, Matrix4f>() {{
            put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND,
                    ModelHelper.get(1.13F, 3.2F, 1.13F, -25F, -90F, 0F, 0.41F).getMatrix());

            put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND,
                    ModelHelper.get(0F, 2F, 3F, 0F, 0F, 45F, 0.5F).getMatrix());

            put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND,
                    ModelHelper.get(1.13F, 3.2F, 1.13F, -25F, -90F, 0F, 0.41F).getMatrix());

            put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND,
                    ModelHelper.get(0F, 2F, 3F, 0F, 0F, 45F, 0.5F).getMatrix());

            put(ItemCameraTransforms.TransformType.GUI,
                    ModelHelper.get(0F, 2.75F, 0F, -45F, 0F, 45F, 0.75F).getMatrix());

            put(ItemCameraTransforms.TransformType.GROUND,
                    ModelHelper.get(0F, 2F, 0F, -90F, -0F, 0F, 0.5F).getMatrix());

            put(ItemCameraTransforms.TransformType.FIXED,
                    ModelHelper.get(0F, 0F, -7.5F, 0F, 180F, 0F, 1F).getMatrix());
        }};
        defaultTransform = ModelHelper.get(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F).getMatrix();
    }

    public static ModelResourceLocation getModelResourceLocation(EnumFacing facing) {
        return new ModelResourceLocation(BlockLuxCapacitor.getInstance().getRegistryName().toString(), "facing=" + facing.getName());
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        // AFAICT quads are only returned for null side
        if (side != null)
            return Collections.emptyList();

        EnumFacing facing = EnumFacing.NORTH; // both NORTH and items use TRSRTransformation.Identity because I finally rotated the model
        colour = BlockLuxCapacitor.defaultColor;

        if (state != null) {
            facing = state.getValue(FACING);
            if (state instanceof IExtendedBlockState)
                if (((IExtendedBlockState) state).getUnlistedProperties().containsKey(BlockLuxCapacitor.COLOUR))
                    colour = ((IExtendedBlockState) state).getValue(BlockLuxCapacitor.COLOUR);
        }
        if (colour == null)
            colour = BlockLuxCapacitor.defaultColor;
        ColoredQuadHelperThingie helperThingie = new ColoredQuadHelperThingie(colour, facing);

        try {
            return ModelLuxCapacitorHelper.getInstance().luxCapColoredQuadMap.get(helperThingie);
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
        Matrix4f transform = cameraTransforms.getOrDefault(cameraTransformType, defaultTransform);
//                ModelHelper.transformCalibration();
//                transform = ModelHelper.get(ModelHelper.xOffest, ModelHelper.yOffest, ModelHelper.zOffest, ModelHelper.xtap, ModelHelper.ytap, ModelHelper.ztap, ModelHelper.scalemodifier);
        return Pair.of(this, transform);
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