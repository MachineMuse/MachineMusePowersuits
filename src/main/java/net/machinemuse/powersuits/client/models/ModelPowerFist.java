package net.machinemuse.powersuits.client.models;

import com.google.common.collect.ImmutableList;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.item.IModularItemBase;
import net.machinemuse.general.NBTTagAccessor;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.client.helpers.ModelTransformCalibration;
import net.machinemuse.powersuits.client.modelspec.ModelPartSpec;
import net.machinemuse.powersuits.client.modelspec.ModelRegistry;
import net.machinemuse.powersuits.client.modelspec.ModelSpec;
import net.machinemuse.powersuits.client.modelspec.PartSpec;
import net.machinemuse.powersuits.common.MPSConstants;
import net.machinemuse.powersuits.common.events.EventRegisterItems;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SideOnly(Side.CLIENT)
public class ModelPowerFist implements IBakedModel {
    public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(EventRegisterItems.powerTool.getRegistryName().toString());
    static ItemCameraTransforms.TransformType modelcameraTransformType;
    static ItemStack itemStack;
    static Item item;
    static Colour colour;
    static World world;
    static EntityLivingBase entity;
    static boolean isFiring = false;
    NBTTagCompound renderTag = new NBTTagCompound();
    static IBakedModel iconModel;
    IModelState modelState;
    ModelTransformCalibration calibration;
    NBTTagCompound renderSpec;

    public ModelPowerFist(IBakedModel bakedModelIn) {
        this.iconModel = (bakedModelIn instanceof ModelPowerFist) ? ((ModelPowerFist) bakedModelIn).iconModel : bakedModelIn;
        calibration = new ModelTransformCalibration();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if (side != null) return ImmutableList.of();
        if (modelcameraTransformType == ItemCameraTransforms.TransformType.GUI)
            return iconModel.getQuads(state, side, rand);

        // TODO: get quads for default powerfist model without custom settings
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
        int[] colours = renderSpec.getIntArray("colours");
        int partColor;
        for (NBTTagCompound nbt : NBTTagAccessor.getValues(renderSpec)) {
            PartSpec partSpec = ModelRegistry.getInstance().getPart(nbt);
            if (partSpec instanceof ModelPartSpec) {
                if (modelState == null)
                    modelState = ((ModelSpec)partSpec.spec).getModel().getState();


                // TODO: Enumcolour stuff
                String itemState = partSpec.binding.getItemState();

                int ix = partSpec.getColourIndex(nbt);
                if (ix < colours.length && ix >= 0) partColor = colours[ix];
                else partColor = Colour.WHITE.getInt();
                boolean glow = ((ModelPartSpec) partSpec).getGlow(nbt);



                if ((!isFiring && (itemState.equals("all") || itemState.equals("normal"))) ||
                        (isFiring && (itemState.equals("all") || itemState.equals("firing"))))
                    builder.addAll(ModelHelper.getColoredQuadsWithGlow(((ModelPartSpec) partSpec).getQuads(), new Colour(partColor), glow));
            }
        }
        return builder.build();

//        return iconModel.getQuads(state, side, rand);
    }

    @Override
    public boolean isAmbientOcclusion() {
        return iconModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return iconModel.isGui3d();
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
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        modelcameraTransformType = cameraTransformType;
        if(cameraTransformType == ItemCameraTransforms.TransformType.GUI)
            return iconModel.handlePerspective(cameraTransformType);
        if (modelState != null) {
            TRSRTransformation tr = modelState.apply(Optional.of(cameraTransformType)).orElse(calibration.getTransform());
            if(tr != TRSRTransformation.identity())
                return Pair.of(this, TRSRTransformation.blockCornerToCenter(tr).getMatrix());
            return Pair.of(this, tr.getMatrix());

        }
        return Pair.of(this, TRSRTransformation.blockCornerToCenter(TRSRTransformation.identity()).getMatrix());
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
            renderSpec =  MuseItemUtils.getMuseRenderTag(stackIn);
            world = worldIn;
            entity = entityIn;
            item = itemStack.getItem();
            colour = ((IModularItemBase) item).getColorFromItemStack(itemStack);
            // TODO: nbt based normal/firing tag

            if (entityIn instanceof EntityPlayer) {
                if (itemStack != null && itemStack == entityIn.getHeldItemMainhand() && entityIn.isHandActive()
                        && ModuleManager.itemHasActiveModule(itemStack, MPSConstants.MODULE_PLASMA_CANNON)) {
                    isFiring = true;
                } else isFiring = false;
            } else isFiring = false;

            return originalModel;
        }
    }
}