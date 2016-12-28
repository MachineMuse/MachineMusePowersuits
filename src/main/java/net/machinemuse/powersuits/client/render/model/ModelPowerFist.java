package net.machinemuse.powersuits.client.render.model;

import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.client.render.item.ToolModel;
import net.machinemuse.powersuits.item.IModularItemBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lehjr on 12/19/16.
 */
public class ModelPowerFist implements IBakedModel, IPerspectiveAwareModel {
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
















//========================================================================================================



    Minecraft mc = Minecraft.getMinecraft();

    public void renderFirstPersonArm(EntityPlayerSP entityclientplayermp, float sp, ItemCameraTransforms.TransformType transformType) {
        entityclientplayermp.swingArm(EnumHand.MAIN_HAND);
        entityclientplayermp.renderArmPitch =0;
        entityclientplayermp.renderArmYaw = 0;

        float changeItemProgress = 0;

        GL11.glPushMatrix();
        float f4 = 0.8F;
        float swingProgress = entityclientplayermp.swingProgress;
        float swingProgressx = MathHelper.sin(swingProgress * (float) Math.PI);
        float swingProgressy = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI);
        GL11.glTranslatef(-swingProgressy * 0.3F, MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI * 2.0F) * 0.4F, -swingProgressx * 0.4F);
        GL11.glTranslatef(0.8F * f4, -0.75F * f4 - (1.0F - changeItemProgress) * 0.6F, -0.9F * f4);
        GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        swingProgress = entityclientplayermp.swingProgress;
        swingProgressx = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
        swingProgressy = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI);
        GL11.glRotatef(swingProgressy * 70.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-swingProgressx * 20.0F, 0.0F, 0.0F, 1.0F);
//        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTextureForDownloadableImage(mc.thePlayer.skinUrl, mc.thePlayer.getTexture()));
//        mc.renderEngine.resetBoundTexture();
        GL11.glTranslatef(-1.0F, 3.6F, 3.5F);
        GL11.glRotatef(120.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(200.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        GL11.glTranslatef(5.6F, 0.0F, 0.0F);
        Render render = mc.getRenderManager().getEntityRenderObject(mc.thePlayer);
        RenderPlayer renderplayer = (RenderPlayer) render;
        if (transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)
            renderplayer.renderRightArm(entityclientplayermp);
        else
            renderplayer.renderLeftArm(entityclientplayermp);
        GL11.glPopMatrix();
    }
}
