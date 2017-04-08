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
                ModelHelper.transformCalibration();
                matrix = ModelHelper.get(ModelHelper.xOffest, ModelHelper.yOffest, ModelHelper.zOffest, ModelHelper.xtap, ModelHelper.ytap, ModelHelper.ztap, 0.5f).getMatrix();
//                matrix = TRSRTransformation.identity().getMatrix();
                break;
            case THIRD_PERSON_RIGHT_HAND:



//
                matrix = ModelHelper.get(-18, -0.9f, -19.15f, -15, 180, 0, 0.630f).getMatrix();
                break;
            case FIRST_PERSON_LEFT_HAND:
                matrix = TRSRTransformation.identity().getMatrix();
//                matrix = ModelHelper.get(xOffest, yOffest, zOffest, xtap, 180, ztap, 0.630f).getMatrix();
                break;
            case THIRD_PERSON_LEFT_HAND:
                matrix = ModelHelper.get(-8, -0.9f, -19.15f, -15, 180, 0, 0.630f).getMatrix();
                break;
            case GROUND:
                matrix = TRSRTransformation.identity().getMatrix();

                break;
            default:
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
//        if (side == null)
//            return Collections.EMPTY_LIST;





        switch (cameraTransformType) {
            case FIRST_PERSON_RIGHT_HAND:
                return ModelHelper.powerFist.getQuads(state, side, rand);
            case THIRD_PERSON_RIGHT_HAND:
                return ModelHelper.powerFist.getQuads(state, side, rand);





            case FIRST_PERSON_LEFT_HAND:
                return ModelHelper.powerFistLeft.getQuads(state, side, rand);
            case THIRD_PERSON_LEFT_HAND:
                return ModelHelper.powerFistLeft.getQuads(state, side, rand);
            case GROUND:
                return ModelHelper.powerFist.getQuads(state, side, rand);
            default:
                return iconModel.getQuads(state, side, rand);
        }
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
            return originalModel;
        }
    }
}

//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    ImmutableMap.Builder<ItemCameraTransforms.TransformType, EnumHandSide> transformsEnumHandSideMap = ImmutableMap.<ItemCameraTransforms.TransformType, EnumHandSide>builder()
//            .put(FIRST_PERSON_RIGHT_HAND, EnumHandSide.RIGHT)
//            .put(THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT)
//            .put(FIRST_PERSON_LEFT_HAND, EnumHandSide.LEFT)
//            .put(THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT);
//
//
//
//
//
//    // TODO: switch to our obj models asap
//
////    static IBakedModel powerFist;
////    static IBakedModel powerFistFiring;
////    static IBakedModel powerFistLeft;
////    static IBakedModel powerFistLeftFiring;
//
//
//
//    public static ToolModel powerFistRightModel = new ToolModel(true);
//    public static ToolModel powerFistLeftModel = new ToolModel(false);
//    public IBakedModel iconModel;
//    public ItemStack itemStack;
//    public World world;
//    public EntityLivingBase entity;
//
//    int boltSize = 0;
//    boolean isFiring = false;
//
//    Colour colour; // tint
//    Colour glow; // crystal glow on/off ?
//
//
//    public ModelPowerFist(IBakedModel bakedModelIn) {
//        if (bakedModelIn instanceof ModelPowerFist) {
//            iconModel = ((ModelPowerFist)bakedModelIn).iconModel;
//        } else {
//            iconModel = bakedModelIn;
//        }
//    }
//
//    public ItemCameraTransforms.TransformType cameraTransformType = ItemCameraTransforms.TransformType.NONE;
//
//
//
//    /*
//        First person model and third person models are switched for each side due to some rendering oddity that makes the
//        model render mirrored in first person.
//
//     */
//    @Override
//    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformTypeIn) {
//        this.cameraTransformType = cameraTransformTypeIn;
//        Matrix4f matrix;
//
////        switch (cameraTransformTypeIn) {
////            /* Left Hand Model -------------------------------------------------------------------- */
////            case FIRST_PERSON_RIGHT_HAND:
////                if (entity instanceof EntityPlayer) {
////                    powerFistLeftModel.setPoseForPlayer((EntityPlayer) entity, itemStack);
////                } else {
////                    powerFistLeftModel.setNeutralPose();
////                }
////                powerFistLeftModel.render(entity, 0.625f, cameraTransformTypeIn, colour, glow);
////                break;
////
////            case THIRD_PERSON_LEFT_HAND:
////                if (entity instanceof EntityPlayer) {
////                    powerFistLeftModel.setPoseForPlayer((EntityPlayer) entity, itemStack);
////                } else {
////                    powerFistLeftModel.setNeutralPose();
////                }
////                powerFistLeftModel.render(entity, 0.625f, cameraTransformTypeIn, colour, glow);
////                break;
////
////
////            /* Right Hand Model ------------------------------------------------------------------- */
////            case FIRST_PERSON_LEFT_HAND:
////                if (entity instanceof EntityPlayer) {
////                    powerFistRightModel.setPoseForPlayer((EntityPlayer) entity, itemStack);
////                } else {
////                    powerFistRightModel.setNeutralPose();
////                }
////                powerFistRightModel.render(entity, 0.625f, cameraTransformTypeIn, colour, glow);
////                break;
////
////            case THIRD_PERSON_RIGHT_HAND:
////                if (entity instanceof EntityPlayer) {
////                    powerFistRightModel.setPoseForPlayer((EntityPlayer) entity, itemStack);
////                } else {
////                    powerFistRightModel.setNeutralPose();
////                }
////                powerFistRightModel.render(entity, 0.625f, cameraTransformTypeIn, colour, glow);
////                break;
////
////            case GROUND: // defaut to right hand model when on the ground;
////                powerFistRightModel.setNeutralPose();
////                powerFistRightModel.render(null, 0.625f, cameraTransformTypeIn, colour, glow);
////                break;
////
////
////            /* Everything else is GUI *------------------------------------------------------------ */
////            default:
////        /*
////        USE ICON for these:
////        ---------------------
////        GU
////        GROUND
////        FIXED
////        HEAD, wait, what?
////        NONE, ?
////         */
////                break;
////        }
//
//
//        switch(cameraTransformTypeIn){
//            case THIRD_PERSON_RIGHT_HAND:
////                setUpPowerFistVariables(entity, EnumHandSide.RIGHT);
////                if (isFiring)
//////                    EntityRendererPlasmaBolt.doRender(boltSize, cameraTransformType);
//
//                ModelHelper.transformCalibration();
//                matrix = ModelHelper.get(/*-16.000025f*/ -16, -2.2f, /* -16.700025f */ -16.7f, -14, 180, 0, 0.5001f).getMatrix();
///*
////    /** The default skin for the Steve model. */
////                private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/steve.png");
////                /** The default skin for the Alex model. */
////                private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");
//
//// ALEX
////[09:11:25] [Client thread/INFO]: [net.machinemuse.powersuits.client.render.model.ModelPowerFist:transformCalibration:513]: xrot: -14, yrot: 0, zrot: 0
////[09:11:25] [Client thread/INFO]: [net.machinemuse.powersuits.client.render.model.ModelPowerFist:transformCalibration:515]: xOffest: -16.000025, yOffest: -2.2, zOffest: -16.700027
////[09:11:25] [Client thread/INFO]: [net.machinemuse.powersuits.client.render.model.ModelPowerFist:transformCalibration:517]: scaleModifier: 1.0
//
//
//
////                matrix = ModelHelper.get(xOffest, yOffest, zOffest, xtap, 180, ztap, 0.5001f).getMatrix();
//                break;
//            case THIRD_PERSON_LEFT_HAND:
//
//
//                matrix = ModelHelper.get(0, 3, 1, 0, 180, 0, 0.55f).getMatrix();
//                break;
//            case FIRST_PERSON_RIGHT_HAND:
//                matrix = ModelHelper.get(1.13f, 3.2f, 1.13f, 0, -90, 25, 0.68f).getMatrix();
//                break;
//            case FIRST_PERSON_LEFT_HAND:
//                matrix = ModelHelper.get(1.13f, 3.2f, 1.13f, 0, 90, -25, 0.68f).getMatrix();
//            case GROUND:
//                matrix = ModelHelper.get(0, 2, 0, 0, 0, 0, 0.5f).getMatrix();
//            default:
//                if (iconModel != null && iconModel instanceof IPerspectiveAwareModel) {
//                    matrix = ((IPerspectiveAwareModel) iconModel).handlePerspective(cameraTransformTypeIn).getValue();
//                } else {
//                    matrix = TRSRTransformation.identity().getMatrix();
//                }
//        }
//        return Pair.of(this, matrix);
//    }
//
//    @Override
//    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
//        IBakedModel retModel;
//        List<BakedQuad> quadList;
//
//        switch (cameraTransformType) {
//            case FIRST_PERSON_RIGHT_HAND:
//                setUpPowerFistVariables(entity, EnumHandSide.RIGHT);
//                if (isFiring) {
////                    EntityRendererPlasmaBolt.doRender(boltSize, cameraTransformType);
//
//
//
//                            // setup rendering for plasma bolt
//                    quadList = ModelHelper.powerFistFiring.getQuads(state, side, rand);
//                    if (colour != null)
//                        quadList = ModelHelper.getColoredQuads(quadList, colour);
//                    return quadList;
////                    return ModelHelper.powerFistFiring.getQuads(state, side, rand);
//                } else
//                    return ModelHelper.powerFist.getQuads(state, side, rand);
//
//            case THIRD_PERSON_RIGHT_HAND:
//                setUpPowerFistVariables(entity, EnumHandSide.RIGHT);
//                if (isFiring) {
//                    EntityRendererPlasmaBolt.doRender(boltSize, cameraTransformType);
//
////                    return Collections.emptyList();
//                    quadList = ModelHelper.powerFistFiring.getQuads(state, side, rand);
//                    if (colour != null) {
////                        System.out.println("color is: " + colour.hexColour());
//                        quadList = ModelHelper.getColoredQuads(quadList, colour);
//                    }//else
////                        System.out.println("color is null");
////                    return ModelHelper.powerFistFiring.getQuads(state, side, rand);
//                } else {
//                    quadList = ModelHelper.powerFist.getQuads(state, side, rand);
//                    if (colour != null) {
////                        System.out.println("color is: " + colour.hexColour());
//                        quadList = ModelHelper.getColoredQuads(quadList, colour);
//                    } //else
////                        System.out.println("color is null");
//                }
//                return quadList;
//
////                    return ModelHelper.powerFist.getQuads(state, side, rand);
//
//            case FIRST_PERSON_LEFT_HAND:
//                setUpPowerFistVariables(entity, EnumHandSide.LEFT);
//                if (isFiring) {
//                    EntityRendererPlasmaBolt.doRender(boltSize, cameraTransformType);
//                    return ModelHelper.powerFistLeftFiring.getQuads(state, side, rand);
//                } else
//                    return ModelHelper.powerFistLeft.getQuads(state, side, rand);
//
//            case THIRD_PERSON_LEFT_HAND:
//                setUpPowerFistVariables(entity, EnumHandSide.LEFT);
//                if (isFiring) {
//                    EntityRendererPlasmaBolt.doRender(boltSize, cameraTransformType);
//                    return ModelHelper.powerFistLeftFiring.getQuads(state, side, rand);
//                } else
//                    return ModelHelper.powerFistLeft.getQuads(state, side, rand);
//
//            case GROUND:
//                return ModelHelper.powerFist.getQuads(state, side, rand);
//
//            default:
//                return iconModel.getQuads(state, side, rand);
//        }
//
////
////
////
////        List<BakedQuad> quadList;
////
////        if(cameraTransformType == ItemCameraTransforms.TransformType.GUI) {
////            if (colour != null)
////                quadList = ModelHelper.getColoredQuads(iconModel.getQuads(state, side, rand), colour);
////            else
////                quadList = iconModel.getQuads(state, side, rand);
////            return quadList;
////
////        }
////        return Collections.emptyList();
//    }
//
//
//    @Override
//    public boolean isAmbientOcclusion() {
//        if (cameraTransformType == ItemCameraTransforms.TransformType.GUI)
//            return iconModel.isAmbientOcclusion();
//        return true;
//    }
//
//    @Override
//    public boolean isGui3d() {
//        if (cameraTransformType == ItemCameraTransforms.TransformType.GUI)
//            return iconModel.isGui3d();
//
//
//        return true;
//    }
//
//    @Override
//    public boolean isBuiltInRenderer() {
////
////        if (cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND)
////            return true;
//
//        return iconModel.isBuiltInRenderer();
//    }
//
//    @Override
//    public TextureAtlasSprite getParticleTexture() {
//        return iconModel.getParticleTexture();
//    }
//
//    @SuppressWarnings("deprecation")
//    @Override
//    public ItemCameraTransforms getItemCameraTransforms() {
//        return iconModel.getItemCameraTransforms();
//    }
//
//
//
//
//
//
//    @Override
//    public ItemOverrideList getOverrides() {
//        if (cameraTransformType == ItemCameraTransforms.TransformType.GUI)
//            return iconModel.getOverrides();
//        return new PowerFistItemOverrideList();
//    }
//
//    public class PowerFistItemOverrideList extends ItemOverrideList {
//        public PowerFistItemOverrideList() {
//            super(Collections.EMPTY_LIST);
//        }
//
//        /*
//         * Testing reveals that stackIn is not even the held item instance, but instead the first inventory type match, numerically by slot index.
//         * This means if you have 2 of the same type, no matter how different they are, only the first one is returned. So the entity is the only sure
//         * variable here.
//         */
//        @Override
//        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stackIn, World worldIn, EntityLivingBase entityIn) {
//            entity = entityIn;
////
////
////
////
////
////
////            ItemStack playerHolding = null;
////            ItemStack mainHandStack = null;
////            ItemStack offHandStack = null;
////
////            playerHolding = ((EntityPlayer) entityIn).inventory.getCurrentItem();
////            mainHandStack = entityIn.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
////            offHandStack = entityIn.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
////            if (entityIn instanceof EntityPlayer) {
////                for( int i = 0; i < 36; i++) {
////                    if (stackIn == ((EntityPlayer) entityIn).inventory.getStackInSlot(i)) {
////                        System.out.println("stack in found in slot " + i);
////
////                        if (ItemStack.areItemsEqual(stackIn, mainHandStack) && ItemStack.areItemStackTagsEqual(stackIn, mainHandStack))
////                            System.out.println("mainhand item matches stack in by different method");
////                        if (playerHolding == mainHandStack)
////                            System.out.println("mainhand item matches player holding");
////                        else if (playerHolding == offHandStack)
////                            System.out.println("offhand item matches player holding");
////                        else
////                            System.out.println("no match for held item");
////
////                        if (stackIn == mainHandStack)
////                            System.out.println("mainhand item matches stackIn");
////                        else if (stackIn == offHandStack)
////                            System.out.println("offhand item matches stackIn");
////                        else {
////                            System.out.println("no match for held StackIn");
////                            if (stackIn == null)
////                                System.out.println("StackIn is NULL!!!");
////                            else
////                                System.out.println("StackIn is " + stackIn.getUnlocalizedName());
////                        }
////                    }
////                }
////
////
////
////            /*
////                testing this looks like the code that calls handleItemState only grabs the first hotbar match for the given type rather than the exact instance.
////
////
////             */
////
////
////
//////                IItemColor
////
////
////
////
////
////
////
////                if (((EntityPlayer) entityIn).inventory.getCurrentItem() == itemStack) {
////
////
////
////                    itemStack = stackIn;
////                    world = worldIn;
////                    entity = entityIn;
////                    item = itemStack.getItem();
////                    colour = ((IModularItemBase) item).getColorFromItemStack(itemStack);
////                    glow = ((IModularItemBase) item).getGlowFromItemStack(itemStack);
////                }
////            } else
////
////                System.out.println("EntityLivingIn is not a player");
//            return originalModel;
//        }
//    }
//
//
//
//    public void setUpPowerFistVariables(EntityLivingBase entityLiving, EnumHandSide hand) {
//        if (entityLiving != null && entityLiving instanceof EntityPlayer) {
//            EntityPlayer player = (EntityPlayer) entityLiving;
//            if (player.getPrimaryHand() == hand) {
//                itemStack = player.getHeldItemMainhand();
//                if (itemStack != null && player.isHandActive() && ModuleManager.itemHasActiveModule(itemStack, PlasmaCannonModule.MODULE_PLASMA_CANNON)) {
//                    isFiring = true;
//                    int actualCount = (-player.getItemInUseCount() + 72000);
//                    this.boltSize = actualCount > 50 ? 50 : actualCount;
//                } else {
//                    isFiring = false;
//                    this.boltSize = 0;
//                }
//            } else {
//                itemStack = entity.getHeldItemOffhand();
//                isFiring = false;
//                this.boltSize = 0;
//            }
//            if (itemStack != null) {
//                if (itemStack.getItem() instanceof  IModularItemBase) {
//                    IModularItemBase item = (IModularItemBase) itemStack.getItem();
//                    colour = ((IModularItemBase) itemStack.getItem()).getColorFromItemStack(itemStack);
//                    glow = ((IModularItemBase) itemStack.getItem()).getGlowFromItemStack(itemStack);
//                }
//            } else {
//                colour = null;
//                glow = null;
//            }
//        }
//    }
//}
