package net.machinemuse.powersuits.client.render.item;

import api.player.model.ModelPlayer;
import net.machinemuse.powersuits.client.render.modelspec.RenderPart;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.smart.render.SmartRenderModel;
import net.smart.render.playerapi.SmartRender;
import net.smart.render.playerapi.SmartRenderModelPlayerBase;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 9:23 PM, 11/07/13
 *
 * Ported to Java by lehjr on 11/7/16.
 */
public class SMovingArmorModel extends ModelPlayer implements IArmorModel {
    public static NBTTagCompound renderSpec = null;
    public static int visibleSection = 0;
    private SmartRenderModelPlayerBase renderPlayerBase = SmartRender.getPlayerBase(this);

    public SMovingArmorModel() {
        super(0);

        SmartRenderModel model = renderPlayerBase.getRenderModel();
        this.bipedBody = model.bipedBody;
        this.bipedRightArm = model.bipedRightArm;
        this.bipedLeftArm = model.bipedLeftArm;
        this.bipedRightLeg = model.bipedRightLeg;
        this.bipedLeftLeg = model.bipedLeftLeg;
        this.bipedHeadwear = model.bipedHeadwear;
        this.bipedEars = model.bipedEars;
        this.bipedCloak = model.bipedCloak;

        init();
    }

    private static SMovingArmorModel INSTANCE;
    public static SMovingArmorModel getInstance() {
        if (INSTANCE == null)
            INSTANCE = new SMovingArmorModel();
        return INSTANCE;
    }

    @Override
    public NBTTagCompound getRenderSpec() {
        return renderSpec;
    }

    @Override
    public void setRenderSpec(NBTTagCompound nbt) {
        renderSpec = nbt;
    }

    @Override
    public int getVisibleSection() {
        return visibleSection;
    }

    @Override
    public void setVisibleSection(int value) {
        visibleSection = value;
    }





    /**
     * Sets the models various rotation angles then renders the model.
     */
    @Override
    public void render(Entity entity, float par2, float par3, float par4, float par5, float par6, float scale) {
        prep(entity, par2, par3, par4, par5, par6, scale);
        renderPlayerBase.setRotationAngles(par2, par3, par4, par5, par6, scale, entity);
        renderPlayerBase.render(entity, par2, par3, par4, par5, par6, scale);
        post(entity, par2, par3, par4, par5, par6, scale);
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
        bipedEars.cubeList.clear();
        bipedCloak.cubeList.clear();
    }

//    private void logModelParts(WavefrontObject model) {
//        MuseLogger.logDebug(model.toString() + ":");
//        for (GroupObject group : model.groupObjects) {
//            MuseLogger.logDebug("-" + group.name);
//        }
//    }

    @Override
    public void setInitialOffsets(ModelRenderer r, float x, float y, float z) {
        r.offsetX = x;
        r.offsetY = y;
        r.offsetZ = z;
    }

    @Override
    public void prep(Entity entity, float par2, float par3, float par4, float par5, float par6, float scale) {
        try {
            EntityLivingBase entLive = (EntityLivingBase) entity;
            ItemStack stack = entLive.getEquipmentInSlot(0);
            heldItemRight = (stack != null) ? 1 : 0;
            isSneak = entLive.isSneaking();
            isRiding = entLive.isRiding();
            EntityPlayer entPlayer = (EntityPlayer) entLive;
            if ((stack != null) && (entPlayer.getItemInUseCount() > 0)) {
                EnumAction enumaction = stack.getItemUseAction();
                if (enumaction == EnumAction.block) {
                    heldItemRight = 3;
                } else if (enumaction == EnumAction.bow) {
                    aimedBow = true;
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
    public void post(Entity entity, float par2, float par3, float par4, float par5, float par6, float scale) {
        aimedBow = false;
        isSneak = false;
        heldItemRight = 0;
    }
}