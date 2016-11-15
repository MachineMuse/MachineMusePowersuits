package net.machinemuse.powersuits.client.render.item;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Ported to Java by lehjr on 10/21/16.
 */
public interface IArmorModel {
    NBTTagCompound renderSpec = null;
    int visibleSection = 0;

    NBTTagCompound getRenderSpec();

    void setRenderSpec(NBTTagCompound nbt);

    int getVisibleSection();

    void setVisibleSection(int value);

    void clearAndAddChildWithInitialOffsets(ModelRenderer mr, float xo, float yo, float zo);

    void init();

    void setInitialOffsets(ModelRenderer r, float x, float y, float z);

    void prep(Entity entity, float par2, float par3, float par4, float par5, float par6, float scale);

    void post(Entity entity, float par2, float par3, float par4, float par5, float par6, float scale);
}