package net.machinemuse.powersuits.client.render.model;

import com.google.common.collect.ImmutableMap;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.client.render.entity.EntityRendererPlasmaBolt;
import net.machinemuse.powersuits.client.render.item.ToolModel;
import net.machinemuse.powersuits.item.IModularItemBase;
import net.machinemuse.powersuits.powermodule.weapon.PlasmaCannonModule;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHandSide;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.*;


/**
 * Created by lehjr on 12/19/16.
 */
public class ModelPowerFist implements IBakedModel, IPerspectiveAwareModel {
    ItemCameraTransforms.TransformType cameraTransformType;
    ItemStack itemStack;
    Item item;
    Colour glow;
    Colour colour;
    World world;
    EntityLivingBase entity;
    boolean isFiring = false;

    IBakedModel iconModel;
    public ModelPowerFist(IBakedModel bakedModelIn) {
        if (bakedModelIn instanceof ModelPowerFist) {
            iconModel = ((ModelPowerFist)bakedModelIn).iconModel;
        } else {
            iconModel = bakedModelIn;
        }
    }


    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformTypeIn) {
        cameraTransformType = cameraTransformTypeIn;
        Matrix4f matrix;
        switch (cameraTransformType) {
            case FIRST_PERSON_RIGHT_HAND:
                matrix = ModelHelper.get(-13, -1f, -16.2f, -14, -181, 0, 0.5f).getMatrix();
                break;
            case THIRD_PERSON_RIGHT_HAND:
                matrix = ModelHelper.get(-18, -0.9f, -19.15f, -15, 180, 0, 0.630f).getMatrix();
                break;
            case FIRST_PERSON_LEFT_HAND:
                matrix = ModelHelper.get(-4.8f, -1f, -16.2f, -14, -181, 0, 0.5f).getMatrix();
                break;
            case THIRD_PERSON_LEFT_HAND:
                matrix = ModelHelper.get(-8, -0.9f, -19.15f, -15, 180, 0, 0.630f).getMatrix();
                break;
            case GROUND:
                matrix = ModelHelper.get(0, 5, 0, 0, 0, 0, 0.630f).getMatrix();
                break;
            default:
//                ModelHelper.transformCalibration();
//                matrix = ModelHelper.get(ModelHelper.xOffest, ModelHelper.yOffest, ModelHelper.zOffest, ModelHelper.xtap, ModelHelper.ytap, ModelHelper.ztap, 0.630f).getMatrix();

                if (iconModel != null && iconModel instanceof IPerspectiveAwareModel) {
                    matrix = ((IPerspectiveAwareModel) iconModel).handlePerspective(cameraTransformTypeIn).getValue();
                } else {
                    matrix = TRSRTransformation.identity().getMatrix();
                }
                break;
        }
        return Pair.of(this, matrix);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> quadList;
        switch (cameraTransformType) {
            case FIRST_PERSON_RIGHT_HAND:
            case THIRD_PERSON_RIGHT_HAND:
            case GROUND:
                if(isFiring)
                    quadList = ModelHelper.getpowerFistFiringQuadList(colour);
                else
                    quadList = ModelHelper.getPowerFistQuadList(colour);
                break;

            case FIRST_PERSON_LEFT_HAND:
            case THIRD_PERSON_LEFT_HAND:
                if(isFiring)
                    quadList = ModelHelper.getpowerFistLeftFiringQuadList(colour);
                else
                    quadList = ModelHelper.getpowerFistLeftQuadList(colour);
                break;
            default:
                quadList = ModelHelper.getPowerFistIconQuadList(iconModel, colour);
        }
        return quadList;
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
//        return iconModel.isBuiltInRenderer();
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return iconModel.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return iconModel.getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return new PowerFistItemOverrideList();
    }

    public class PowerFistItemOverrideList  extends ItemOverrideList {
        public PowerFistItemOverrideList() {
            super(Collections.EMPTY_LIST);
        }

        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stackIn, World worldIn, EntityLivingBase entityIn) {
            itemStack = stackIn;
            world = worldIn;
            entity = entityIn;
            item = itemStack.getItem();
            colour = ((IModularItemBase) item).getColorFromItemStack(itemStack);
            glow = ((IModularItemBase) item).getGlowFromItemStack(itemStack);

            if (entityIn instanceof EntityPlayer) {
                if (itemStack != null && itemStack == entityIn.getHeldItemMainhand() && entityIn.isHandActive()
                        && ModuleManager.itemHasActiveModule(itemStack, PlasmaCannonModule.MODULE_PLASMA_CANNON)) {
                    isFiring = true;
                }
                else isFiring = false;
            }
            else isFiring = false;

            return originalModel;
        }
    }
}