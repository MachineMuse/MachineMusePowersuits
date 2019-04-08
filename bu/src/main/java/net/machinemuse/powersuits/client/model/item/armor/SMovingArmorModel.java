package net.machinemuse.powersuits.client.model.item.armor;

import api.player.model.ModelPlayerArmor;
import net.machinemuse.powersuits.client.render.modelspec.RenderPart;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHandSide;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 9:23 PM, 11/07/13
 * <p>
 * Ported to Java by lehjr on 11/7/16.
 */
public class SMovingArmorModel extends ModelPlayerArmor implements IArmorModel {
    public NBTTagCompound renderSpec = null;
    public EntityEquipmentSlot visibleSection = EntityEquipmentSlot.HEAD;

    public SMovingArmorModel() {
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
            ItemStack stack = entLive.getActiveItemStack();

            // set pose for main hand, whichever hand that is
            if (entLive.getHeldItemMainhand().isEmpty()) {
                if (getMainHand(entLive) == EnumHandSide.RIGHT)
                    this.rightArmPose = ArmPose.EMPTY;
                else
                    this.leftArmPose = ArmPose.EMPTY;
            } else {
                if (getMainHand(entLive) == EnumHandSide.RIGHT)
                    this.rightArmPose = ArmPose.ITEM;
                else
                    this.leftArmPose = ArmPose.ITEM;
            }

            // the "offhand" is the other hand
            if (entLive.getHeldItemOffhand().isEmpty()) {
                if (getMainHand(entLive) == EnumHandSide.RIGHT)
                    this.rightArmPose = ArmPose.EMPTY;
                else
                    this.leftArmPose = ArmPose.EMPTY;
            } else {
                if (getMainHand(entLive) == EnumHandSide.RIGHT)
                    this.rightArmPose = ArmPose.ITEM;
                else
                    this.leftArmPose = ArmPose.ITEM;
            }

            isSneak = entLive.isSneaking();
            isRiding = entLive.isRiding();
            EntityPlayer entPlayer = (EntityPlayer) entLive;
            if ((!stack.isEmpty()) && (entPlayer.getItemInUseCount() > 0)) {
                EnumAction enumaction = stack.getItemUseAction();
                if (enumaction == EnumAction.BLOCK) {
                    if (getMainHand(entLive) == EnumHandSide.LEFT)
                        this.leftArmPose = ArmPose.BLOCK;
                    else
                        this.rightArmPose = ArmPose.BLOCK;
                } else if (enumaction == EnumAction.BOW) {
                    if (getMainHand(entLive) == EnumHandSide.LEFT)
                        this.leftArmPose = ArmPose.BOW_AND_ARROW;
                    else
                        this.rightArmPose = ArmPose.BOW_AND_ARROW;
                }
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
        leftArmPose = ArmPose.EMPTY;
        rightArmPose = ArmPose.EMPTY;
        isSneak = false;
    }

    @Override
    protected EnumHandSide getMainHand(Entity entityIn) {
        return super.getMainHand(entityIn);
    }
}