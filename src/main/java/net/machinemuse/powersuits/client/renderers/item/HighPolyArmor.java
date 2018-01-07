package net.machinemuse.powersuits.client.renderers.item;

import net.machinemuse.powersuits.client.modelspec.RenderPart;
import net.machinemuse.powersuits.common.MPSConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 9:24 PM, 11/07/13
 * <p>
 * Ported to Java by lehjr on 11/7/16.
 * <p>
 * FIXME: IMPORTANT!!!!: Note that SmartMoving will mess up the rendering here and the armor's yaw will not change with the player's yaw but will be fine with it not installed.
 */
public class HighPolyArmor extends ModelBiped implements IArmorModel {
    public NBTTagCompound renderSpec = null;
    public EntityEquipmentSlot visibleSection = EntityEquipmentSlot.HEAD;
    private static final float modelSize = 0.0F;

    private static HighPolyArmor INSTANCE;

    public static HighPolyArmor getInstance() {
        if (INSTANCE == null) {
            synchronized (HighPolyArmor.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HighPolyArmor();
                }
            }
        }
        return INSTANCE;
    }

    private HighPolyArmor() {
        super(0);
        init();
    }

    @Override
    public NBTTagCompound getRenderSpec() {
        return this.renderSpec;
    }

    @Override
    public void setRenderSpec(NBTTagCompound nbt) {
        renderSpec = nbt;
    }

    @Override
    public EntityEquipmentSlot getVisibleSection() {
        return this.visibleSection;
    }

    @Override
    public void setVisibleSection(EntityEquipmentSlot equipmentSlot) {
        this.visibleSection = equipmentSlot;
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
//        if (entity instanceof AbstractClientPlayer)
//            this.isSkinnyArms = ((AbstractClientPlayer) entity).getSkinType() == "slim";
        prep(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.bipedBody.rotateAngleY = entity.rotationYaw;
        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        post(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    @Override
    public void clearAndAddChildWithInitialOffsets(ModelRenderer mr, float xo, float yo, float zo) {
        mr.cubeList.clear();
        RenderPart rp = new RenderPart(this, mr);
        mr.addChild(rp);
        setInitialOffsets(rp, xo, yo, zo);
    }

    @Override
    public void init() {
        clearAndAddChildWithInitialOffsets(bipedHead, 0.0F, 0.0F, 0.0F);
        clearAndAddChildWithInitialOffsets(bipedBody, 0.0F, 0.0F, 0.0F);
        clearAndAddChildWithInitialOffsets(bipedRightArm, 5, 2.0F, 0.0F);
        clearAndAddChildWithInitialOffsets(bipedLeftArm, -5, 2.0F, 0.0F);
        clearAndAddChildWithInitialOffsets(bipedRightLeg, 2, 12.0F, 0.0F);
        clearAndAddChildWithInitialOffsets(bipedLeftLeg, -2, 12.0F, 0.0F);
        bipedHeadwear.cubeList.clear();
    }

    @Override
    public void setInitialOffsets(ModelRenderer r, float x, float y, float z) {
        r.offsetX = x;
        r.offsetY = y;
        r.offsetZ = z;
    }

    @Override
    public void prep(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        try {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase entityLiving = (EntityLivingBase) entity;

                ItemStack mainHandStack = entityLiving.getHeldItemMainhand();
                ItemStack offHandStack = entityLiving.getHeldItemOffhand();

                ArmPose armPose1 = ArmPose.EMPTY;
                ArmPose armPose2 = ArmPose.EMPTY;

                if (!mainHandStack.isEmpty()) {
                    armPose1 = ArmPose.ITEM;
                    if (entityLiving.getItemInUseCount() > 0) {
                        EnumAction enumaction = mainHandStack.getItemUseAction();
                        if (enumaction == EnumAction.BLOCK) {
                            armPose1 = ArmPose.BLOCK;
                        } else if (enumaction == EnumAction.BOW) {
                            armPose1 = ArmPose.BOW_AND_ARROW;
                        }
                    }
                }

                if (!offHandStack.isEmpty()) {
                    armPose2 = ArmPose.ITEM;
                    if (entityLiving.getItemInUseCount() > 0) {
                        EnumAction enumaction1 = offHandStack.getItemUseAction();

                        if (enumaction1 == EnumAction.BLOCK) {
                            armPose2 = ArmPose.BLOCK;
                        }
                        // FORGE: fix MC-88356 allow offhand to use bow and arrow animation
                        else if (enumaction1 == EnumAction.BOW) {
                            armPose2 = ArmPose.BOW_AND_ARROW;
                        }
                    }
                }

                if (entityLiving.getPrimaryHand() == EnumHandSide.RIGHT) {
                    this.rightArmPose = armPose1;
                    this.leftArmPose = armPose2;
                } else {
                    this.rightArmPose = armPose2;
                    this.leftArmPose = armPose1;
                }

                isSneak = entityLiving.isSneaking();
                isRiding = entityLiving.isRiding();
            }
        } catch (Exception ignored) {

        }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        bipedHead.showModel = visible;
        bipedBody.showModel = visible;
        bipedRightArm.showModel = visible;
        bipedLeftArm.showModel = visible;
        bipedRightLeg.showModel = visible;
        bipedLeftLeg.showModel = visible;
    }

    void showPart(ModelRenderer mr, boolean shown) {
      mr.showModel = shown;
      mr.isHidden = !shown;
    }

    @Override
    public void post(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        leftArmPose = ArmPose.EMPTY;
        rightArmPose = ArmPose.EMPTY;
        isSneak = false;
    }
}