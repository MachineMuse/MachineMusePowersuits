package net.machinemuse.powersuits.client.model.item.armor;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Ported to Java by lehjr on 10/21/16.
 */
public interface IArmorModel {
    NBTTagCompound renderSpec = null;
    EntityEquipmentSlot visibleSection = EntityEquipmentSlot.HEAD;

    NBTTagCompound getRenderSpec();

    void setRenderSpec(NBTTagCompound nbt);

    EntityEquipmentSlot getVisibleSection();

    void setVisibleSection(EntityEquipmentSlot value);

    void clearAndAddChildWithInitialOffsets(ModelRenderer mr, float xo, float yo, float zo);

    void init();

    void setInitialOffsets(ModelRenderer r, float x, float y, float z);

    void prep(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale);

    void post(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale);
}