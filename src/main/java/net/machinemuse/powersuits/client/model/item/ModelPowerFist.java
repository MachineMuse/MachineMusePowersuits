package net.machinemuse.powersuits.client.model.item;

import com.google.common.collect.ImmutableList;
import net.machinemuse.numina.common.constants.NuminaNBTConstants;
import net.machinemuse.numina.utils.math.Colour;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.ModelBakeEventHandler;
import net.machinemuse.powersuits.client.helper.ModelHelper;
import net.machinemuse.powersuits.client.helper.ModelTransformCalibration;
import net.machinemuse.powersuits.client.render.modelspec.ModelPartSpec;
import net.machinemuse.powersuits.client.render.modelspec.ModelRegistry;
import net.machinemuse.powersuits.client.render.modelspec.ModelSpec;
import net.machinemuse.powersuits.client.render.modelspec.PartSpecBase;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.item.IModularItemBase;
import net.machinemuse.powersuits.utils.nbt.MPSNBTUtils;
import net.machinemuse.powersuits.utils.nbt.NBTTagAccessor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.Collections;
import java.util.List;

/**
 * Created by lehjr on 12/19/16.
 */
@SideOnly(Side.CLIENT)
public class ModelPowerFist implements IBakedModel {
    static ItemCameraTransforms.TransformType modelcameraTransformType;
    static ItemStack itemStack;
    static Item item;
    static Colour colour;
    static World world;
    static EntityLivingBase entity;
    static boolean isFiring = false;
    static IBakedModel iconModel;
    NBTTagCompound renderTag = new NBTTagCompound();
    ModelTransformCalibration calibration;
    NBTTagCompound renderSpec;

    public ModelPowerFist(IBakedModel bakedModelIn) {
        this.iconModel = (bakedModelIn instanceof ModelPowerFist) ? ((ModelPowerFist) bakedModelIn).iconModel : bakedModelIn;
        calibration = new ModelTransformCalibration();
    }

    /**
     * this is great for single models or those that share the exact same transforms for the different camera transform
     * type. However, when dealing with quads from different models, it's useless.
     */
    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        modelcameraTransformType = cameraTransformType;
        switch (cameraTransformType) {
            case FIRST_PERSON_LEFT_HAND:
            case THIRD_PERSON_LEFT_HAND:
            case FIRST_PERSON_RIGHT_HAND:
            case THIRD_PERSON_RIGHT_HAND:
                return Pair.of(this, TRSRTransformation.blockCornerToCenter(TRSRTransformation.identity()).getMatrix());
            default:
                return iconModel.handlePerspective(cameraTransformType);
        }
    }

    @Override
    public boolean isAmbientOcclusion() {
        return iconModel.isAmbientOcclusion();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return iconModel.getParticleTexture();
    }

    @Override
    public boolean isGui3d() {
        if (iconModel == null)
            iconModel = ModelBakeEventHandler.INSTANCE.powerFistIconModel;
        return iconModel.isGui3d();
    }


//    @Override
//    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
//        if (side != null) return Collections.EMPTY_LIST;
//        try {
//            return ModelPowerFistHelper.INSTANCE.colouredPowerFistQuadMap.get(
//                    new ModelPowerFistHelper.PowerFistQuadMapKey(colour, modelcameraTransformType, isFiring));
//        } catch (Exception e) {
//            MuseLogger.logException("failed to loadButton get quads from cache: ", e);
//            return Collections.EMPTY_LIST;
//        }
//    }


    // todo: implement cache

//    private final LoadingCache<ModelPowerFist.CachKey, List<BakedQuad>> cache = CacheBuilder
//            .newBuilder()
//            .maximumSize(40)
//            .build(new CacheLoader<ModelPowerFist.CachKey, List<BakedQuad>>() {
//                @Override
//                public List<BakedQuad> loadButton(ModelPowerFist.CachKey key) throws Exception {
//                    return null;
//                }
//            });
//
//
//    static class CachKey {
//        public final String partName;
//        public final ItemCameraTransforms.TransformType transformType;
//        public final Colour colour;
//        public final boolean glow;
//
//        CachKey(final String partName,
//                final ItemCameraTransforms.TransformType transformType,
//                final Colour colour,
//                final boolean glow){
//            this.transformType=transformType;
//            this.partName=partName;
//            this.colour=colour;
//            this.glow=glow;
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(partName, transformType, colour, glow);
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            if (this == obj) {
//                return true;
//            }
//            if (obj == null || getClass() != obj.getClass()) {
//                return false;
//            }
//            final CachKey other = (CachKey) obj;
//            return Objects.equals(this.partName, other.partName)
//                    && Objects.equals(this.transformType, other.transformType)
//                    && Objects.equals(this.colour, other.colour)
//                    && Objects.equals(this.glow, other.glow);
//        }
//    }

    /**
     * Since this is where the quads are actually
     */
    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if (side != null)
            return ImmutableList.of();

        switch (modelcameraTransformType) {
            case GUI:
            case FIXED:
            case NONE:
                return iconModel.getQuads(state, side, rand);
        }

        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
        int[] colours = renderSpec.getIntArray(NuminaNBTConstants.TAG_COLOURS);
        Colour partColor;
        TRSRTransformation transform;

        for (NBTTagCompound nbt : NBTTagAccessor.getValues(renderSpec)) {
            PartSpecBase partSpec = ModelRegistry.getInstance().getPart(nbt);
            if (partSpec instanceof ModelPartSpec) {

                // only process this part if it's for the correct hand
                if (partSpec.getBinding().getTarget().name().toUpperCase().equals(
                        modelcameraTransformType.equals(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND) ||
                                modelcameraTransformType.equals(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND) ?
                                "LEFTHAND" : "RIGHTHAND")) {

                    transform = ((ModelSpec) partSpec.spec).getTransform(modelcameraTransformType);
                    String itemState = partSpec.getBinding().getItemState();

                    int ix = partSpec.getColourIndex(nbt);
                    if (ix < colours.length && ix >= 0)
                        partColor = new Colour(colours[ix]);
                    else
                        partColor = Colour.WHITE;
                    boolean glow = ((ModelPartSpec) partSpec).getGlow(nbt);

                    if ((!isFiring && (itemState.equals("all") || itemState.equals("normal"))) ||
                            (isFiring && (itemState.equals("all") || itemState.equals("firing"))))
                        builder.addAll(ModelHelper.getColouredQuadsWithGlowAndTransform(((ModelPartSpec) partSpec).getQuads(), partColor, transform, glow));
                }
            }
        }
        return builder.build();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return new PowerFistItemOverrideList();
    }

    public class PowerFistItemOverrideList extends ItemOverrideList {
        public PowerFistItemOverrideList() {
            super(Collections.EMPTY_LIST);
        }

        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stackIn, World worldIn, EntityLivingBase entityIn) {
            itemStack = stackIn;
            renderSpec = MPSNBTUtils.getMuseRenderTag(stackIn);
            world = worldIn;
            entity = entityIn;
            item = itemStack.getItem();
            // Todo: eliminate
            colour = ((IModularItemBase) item).getColorFromItemStack(itemStack);

            if (entityIn instanceof EntityPlayer) {
                if (!itemStack.isEmpty() && itemStack == entityIn.getHeldItemMainhand() && entityIn.isHandActive()
                        && ModuleManager.INSTANCE.itemHasActiveModule(itemStack, MPSModuleConstants.MODULE_PLASMA_CANNON__DATANAME)) {
                    isFiring = true;
                } else
                    isFiring = false;
            } else isFiring = false;

            return originalModel;
        }
    }
}