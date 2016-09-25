package net.machinemuse.powersuits.client.render.item;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * Created by leon on 8/23/16.
 */
public class PowerFistModel2 extends LayerBipedArmor {
    public PowerFistModel2(RenderLivingBase<?> rendererIn) {
        super(rendererIn);
    }


    @Override
    protected void initArmor() {
        super.initArmor();
    }

    @Override
    protected void setModelSlotVisible(ModelBiped p_188359_1_, EntityEquipmentSlot slotIn) {
        super.setModelSlotVisible(p_188359_1_, slotIn);
    }

    @Override
    protected void setModelVisible(ModelBiped model) {
        super.setModelVisible(model);
    }

    @Override
    protected ModelBiped getArmorModelHook(EntityLivingBase entity, ItemStack itemStack, EntityEquipmentSlot slot, ModelBiped model) {
        return super.getArmorModelHook(entity, itemStack, slot, model);
    }

    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
    }

    @Override
    public boolean shouldCombineTextures() {
        return super.shouldCombineTextures();
    }

    @Nullable
    @Override
    public ItemStack getItemStackFromSlot(EntityLivingBase living, EntityEquipmentSlot slotIn) {
        return super.getItemStackFromSlot(living, slotIn);
    }

    @Override
    public ModelBiped getModelFromSlot(EntityEquipmentSlot slotIn) {
        return super.getModelFromSlot(slotIn);
    }

    @Override
    public ResourceLocation getArmorResource(Entity entity, ItemStack stack, EntityEquipmentSlot slot, String type) {
        return super.getArmorResource(entity, stack, slot, type);
    }
}
