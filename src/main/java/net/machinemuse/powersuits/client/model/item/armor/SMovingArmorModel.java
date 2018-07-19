package net.machinemuse.powersuits.client.model.item.armor;

import api.player.model.ModelPlayerAPI;
import api.player.model.ModelPlayerBase;
import net.machinemuse.powersuits.client.render.modelspec.RenderPart;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 9:23 PM, 11/07/13
 * <p>
 * Ported to Java by lehjr on 11/7/16.
 */
public class SMovingArmorModel extends ModelPlayerBase implements IArmorModel {
    public NBTTagCompound renderSpec = null;
    public EntityEquipmentSlot visibleSection = EntityEquipmentSlot.HEAD;
    public ModelBiped INSTANCE;

    public SMovingArmorModel(ModelPlayerAPI modelPlayerAPI) {
        super(modelPlayerAPI);
//        if(this.modelPlayerArmor != null)
//            this.INSTANCE = this.modelPlayerArmor;
//        else
        if(this.modelPlayer != null)
            this.INSTANCE = this.modelPlayer;
        else
            this.INSTANCE = this.modelBiped;
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
    public EntityEquipmentSlot getVisibleSection() {
        return this.visibleSection;
    }

    @Override
    public void setVisibleSection(EntityEquipmentSlot equipmentSlot) {
        this.visibleSection = equipmentSlot;
    }

    @Override
    public void clearAndAddChildWithInitialOffsets(ModelRenderer mr, float xo, float yo, float zo) {
        mr.cubeList.clear();
        RenderPart rp = new RenderPart(this.modelBiped, mr);
        mr.addChild(rp);
        setInitialOffsets(rp, xo, yo, zo);
    }

    @Override
    public void init() {
        clearAndAddChildWithInitialOffsets(this.INSTANCE.bipedHead, 0.0F, 0.0F, 0.0F);
        clearAndAddChildWithInitialOffsets(this.INSTANCE.bipedBody, 0.0F, 0.0F, 0.0F);
        clearAndAddChildWithInitialOffsets(this.INSTANCE.bipedRightArm, 5, 2.0F, 0.0F);
        clearAndAddChildWithInitialOffsets(this.INSTANCE.bipedLeftArm, -5, 2.0F, 0.0F);
        clearAndAddChildWithInitialOffsets(this.INSTANCE.bipedRightLeg, 2, 12.0F, 0.0F);
        clearAndAddChildWithInitialOffsets(this.INSTANCE.bipedLeftLeg, -2, 12.0F, 0.0F);
        this.INSTANCE.bipedHeadwear.cubeList.clear();
    }

    @Override
    public void setInitialOffsets(ModelRenderer r, float x, float y, float z) {
        r.offsetX = x;
        r.offsetY = y;
        r.offsetZ = z;
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    @Override
    public void post(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    }

    @Override
    public void prep(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    }
}