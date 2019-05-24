package net.machinemuse.powersuits.client.model.item.armor;

import net.machinemuse.powersuits.client.render.modelspec.RenderPart;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHandSide;

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

    public HighPolyArmor() {
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
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        prep(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.bipedBody.rotateAngleY = entityIn.rotationYaw;
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        post(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
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
//        bipedEars.cubeList.clear();
//        bipedCloak.cubeList.clear();
    }

    @Override
    public void setInitialOffsets(ModelRenderer r, float x, float y, float z) {
        r.offsetX = x;
        r.offsetY = y;
        r.offsetZ = z;
    }

    @Override
    public void prep(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        try {
            EntityLivingBase entLive = (EntityLivingBase) entityIn;

            isSneak = entLive.isSneaking();
            isRiding = entLive.isRiding();

            ArmPose mainHandPose = ArmPose.EMPTY;
            ArmPose offHandPose = ArmPose.EMPTY;

            ItemStack itemStackMainHand = entLive.getHeldItemMainhand();
            if (!itemStackMainHand.isEmpty()) {
                mainHandPose = ArmPose.ITEM;

                if (entLive.getItemInUseCount() > 0) {
                    EnumAction enumaction = itemStackMainHand.getItemUseAction();

                    if (enumaction == EnumAction.BLOCK) {
                        mainHandPose = ArmPose.BLOCK;
                    } else if (enumaction == EnumAction.BOW) {
                        mainHandPose = ArmPose.BOW_AND_ARROW;
                    }
                }
            }

            ItemStack itemstackOffHand = entLive.getHeldItemOffhand();
            if (!itemstackOffHand.isEmpty()) {
                offHandPose = ArmPose.ITEM;

                if (entLive.getItemInUseCount() > 0) {
                    EnumAction enumaction1 = itemstackOffHand.getItemUseAction();

                    if (enumaction1 == EnumAction.BLOCK) {
                        offHandPose = ArmPose.BLOCK;
                    }
                    // FORGE: fix MC-88356 allow offhand to use bow and arrow animation
                    else if (enumaction1 == EnumAction.BOW) {
                        offHandPose = ArmPose.BOW_AND_ARROW;
                    }
                }
            }

            if (entLive.getPrimaryHand() == EnumHandSide.RIGHT) {
                this.rightArmPose = mainHandPose;
                this.leftArmPose = offHandPose;
            } else {
                this.rightArmPose = offHandPose;
                this.leftArmPose = mainHandPose;
            }
        } catch (Exception ignored) {
        }

        bipedHead.isHidden = false;
        bipedBody.isHidden = false;
        bipedRightArm.isHidden = false;
        bipedLeftArm.isHidden = false;
        bipedRightLeg.isHidden = false;
        bipedLeftLeg.isHidden = false;

        bipedHead.showModel = true;
        bipedBody.showModel = true;
        bipedRightArm.showModel = true;
        bipedLeftArm.showModel = true;
        bipedRightLeg.showModel = true;
        bipedLeftLeg.showModel = true;
    }

    @Override
    public void post(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
//        leftArmPose = ArmPose.EMPTY;
//        rightArmPose = ArmPose.EMPTY;
//        isSneak = false;
    }

    @Override
    protected EnumHandSide getMainHand(Entity entityIn) {
        return super.getMainHand(entityIn);
    }
}