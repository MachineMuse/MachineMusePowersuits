package net.machinemuse.powersuits.client.render.model;

import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.client.render.item.ToolModel;
import net.machinemuse.powersuits.item.IModularItemBase;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.Collections;
import java.util.List;

/**
 * Created by lehjr on 12/19/16.
 */
public class ModelPowerFist implements IBakedModel, IPerspectiveAwareModel {
    // TODO: switch to our obj models asap

    public static ToolModel powerFistRightModel = new ToolModel(false);
    public static ToolModel powerFistLeftModel = new ToolModel(true);
    public IBakedModel modelOriginal;
    public ItemStack itemStack;
    public World world;
    public EntityLivingBase entity;

    Colour colour;
    Colour glow;
    Item item;

    public ModelPowerFist(IBakedModel bakedModelIn) {
        if (bakedModelIn instanceof ModelPowerFist) {
            modelOriginal = ((ModelPowerFist)bakedModelIn).modelOriginal;
        } else {
            modelOriginal = bakedModelIn;
        }
    }

    public ItemCameraTransforms.TransformType cameraTransformType = ItemCameraTransforms.TransformType.NONE;

    public void handleItemState(ItemStack itemStackIn, World worldIn, EntityLivingBase entityLivingBaseIn) {
        itemStack = itemStackIn;
        world = worldIn;
        entity = entityLivingBaseIn;
        item = itemStack.getItem();
        colour = ((IModularItemBase) item).getColorFromItemStack(itemStack);
        glow = ((IModularItemBase) item).getColorFromItemStack(itemStack);
    }

    /*
        First person model and third person models are switched for each side due to some rendering oddity that makes the
        model render mirrored in first person.

     */
    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformTypeIn) {
        this.cameraTransformType = cameraTransformTypeIn;
        Matrix4f matrix;

        switch (cameraTransformTypeIn) {
            /* Left Hand Model -------------------------------------------------------------------- */
            case FIRST_PERSON_RIGHT_HAND:
                if (entity instanceof EntityPlayer) {
                    powerFistLeftModel.setPoseForPlayer((EntityPlayer) entity, itemStack);
                } else {
                    powerFistLeftModel.setNeutralPose();
                }
                powerFistLeftModel.render(entity, 1, cameraTransformTypeIn, colour, glow);
                break;

            case THIRD_PERSON_LEFT_HAND:
                if (entity instanceof EntityPlayer) {
                    powerFistLeftModel.setPoseForPlayer((EntityPlayer) entity, itemStack);
                } else {
                    powerFistLeftModel.setNeutralPose();
                }
                powerFistLeftModel.render(entity, 1, cameraTransformTypeIn, colour, glow);
                break;


            /* Right Hand Model ------------------------------------------------------------------- */
            case FIRST_PERSON_LEFT_HAND:
                if (entity instanceof EntityPlayer) {
                    powerFistRightModel.setPoseForPlayer((EntityPlayer) entity, itemStack);
                } else {
                    powerFistRightModel.setNeutralPose();
                }
                powerFistRightModel.render(entity, 1, cameraTransformTypeIn, colour, glow);
                break;

            case THIRD_PERSON_RIGHT_HAND:
                if (entity instanceof EntityPlayer) {
                    powerFistRightModel.setPoseForPlayer((EntityPlayer) entity, itemStack);
                } else {
                    powerFistRightModel.setNeutralPose();
                }
                powerFistRightModel.render(entity, 1, cameraTransformTypeIn, colour, glow);
                break;

            case GROUND: // defaut to right hand model when on the ground;
                powerFistRightModel.setNeutralPose();
                powerFistRightModel.render(null, 1, cameraTransformTypeIn, colour, glow);
                break;


            /* Everything else is GUI *------------------------------------------------------------ */
            default:
        /*
        USE ICON for these:
        ---------------------
        GU
        GROUND
        FIXED
        HEAD, wait, what?
        NONE, ?
         */
                break;
        }



        if (modelOriginal != null && modelOriginal instanceof IPerspectiveAwareModel) {
            matrix = ((IPerspectiveAwareModel) modelOriginal).handlePerspective(cameraTransformTypeIn).getValue();
        } else {
            matrix = TRSRTransformation.identity().getMatrix();
        }

        return Pair.of(this, matrix);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if(cameraTransformType == ItemCameraTransforms.TransformType.GUI)
            return modelOriginal.getQuads(state, side, rand);
        return Collections.emptyList();
    }

    @Override
    public boolean isAmbientOcclusion() {
        if (cameraTransformType == ItemCameraTransforms.TransformType.GUI)
            return modelOriginal.isAmbientOcclusion();
        return true;
    }

    @Override
    public boolean isGui3d() {
        if (cameraTransformType == ItemCameraTransforms.TransformType.GUI)
            return modelOriginal.isGui3d();
        return true;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return modelOriginal.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return modelOriginal.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return modelOriginal.getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides() {
        if (cameraTransformType == ItemCameraTransforms.TransformType.GUI)
            return modelOriginal.getOverrides();
        return new PowerFistItemOverrideList();
    }

    public class PowerFistItemOverrideList extends ItemOverrideList {
        public PowerFistItemOverrideList() {
            super(Collections.EMPTY_LIST);
        }

        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
            if (originalModel instanceof ModelPowerFist) {
                ((ModelPowerFist)originalModel).handleItemState(stack, world, entity);
            }
            return originalModel;
        }
    }
}
