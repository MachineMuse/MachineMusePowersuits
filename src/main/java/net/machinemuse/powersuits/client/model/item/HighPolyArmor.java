package net.machinemuse.powersuits.client.model.item;

import net.machinemuse.powersuits.client.render.item.armor.RenderPart;
import net.minecraft.client.renderer.entity.model.ModelBiped;
import net.minecraft.client.renderer.entity.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 9:24 PM, 11/07/13
 * <p>
 * Ported to Java by lehjr on 11/7/16.
 * <p>
 * FIXME: IMPORTANT!!!!: Note that SmartMoving will mess up the rendering here and the armor's yaw will not change with the player's yaw but will be fine with it not installed.
 */
@OnlyIn(Dist.CLIENT)
public class HighPolyArmor extends ModelBiped {
    public NBTTagCompound renderSpec = null;
    public EntityEquipmentSlot visibleSection = EntityEquipmentSlot.HEAD;

    public HighPolyArmor() {
        super(0);
        init();
    }

    public NBTTagCompound getRenderSpec() {
        return this.renderSpec;
    }

    public void setRenderSpec(NBTTagCompound nbt) {
        renderSpec = nbt;
    }

    public EntityEquipmentSlot getVisibleSection() {
        return this.visibleSection;
    }

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

    public void clearAndAddChildWithInitialOffsets(ModelRenderer mr, float xo, float yo, float zo) {
        mr.cubeList.clear();
        RenderPart rp = new RenderPart(this, mr);
        mr.addChild(rp);
        setInitialOffsets(rp, xo, yo, zo);
    }

    public void init() {
        clearAndAddChildWithInitialOffsets(bipedHead, 0.0F, 0.0F, 0.0F);
        clearAndAddChildWithInitialOffsets(bipedBody, 0.0F, 0.0F, 0.0F);
        clearAndAddChildWithInitialOffsets(bipedRightArm, 5, 2.0F, 0.0F);
        clearAndAddChildWithInitialOffsets(bipedLeftArm, -5, 2.0F, 0.0F);
        clearAndAddChildWithInitialOffsets(bipedRightLeg, 2, 12.0F, 0.0F);
        clearAndAddChildWithInitialOffsets(bipedLeftLeg, -2, 12.0F, 0.0F);
        bipedHeadwear.cubeList.clear();
    }

    public void setInitialOffsets(ModelRenderer r, float x, float y, float z) {
        r.offsetX = x;
        r.offsetY = y;
        r.offsetZ = z;
    }

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
            EntityPlayer entPlayer = (EntityPlayer) entLive;
            if ((!stack.isEmpty()) && (entPlayer.getItemInUseCount() > 0)) {
                EnumAction enumaction = stack.getUseAction();
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