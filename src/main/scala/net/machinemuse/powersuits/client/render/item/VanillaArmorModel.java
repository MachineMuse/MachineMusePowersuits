//package net.machinemuse.powersuits.client.render.item;
//
//import net.minecraft.client.model.ModelRenderer;
//import net.minecraft.entity.Entity;
//import net.minecraft.nbt.NBTTagCompound;
//
///**
// * Author: MachineMuse (Claire Semple)
// * Created: 9:24 PM, 11/07/13
// *
// * Ported to Java by lehjr on 10/20/16.
// */
//
///*
//    Probably hold off on this for now. looks like some of the traits will need to be converted to Java classes and Interfaces.
//
//
// */
//
//
//
//public class VanillaArmorModel implements ArmorModel {
//    @Override
//    public void clearAndAddChildWithInitialOffsets(ModelRenderer mr, float xo, float yo, float zo) {
//        super.clearAndAddChildWithInitialOffsets(mr, xo, yo, zo);
//    }
//
//    @Override
//    public void init() {
//        super.init();
//    }
//
//    @Override
//    public void setInitialOffsets(ModelRenderer r, float x, float y, float z) {
//        super.setInitialOffsets(r, x, y, z);
//    }
//
//    @Override
//    public void prep(Entity entity, float par2, float par3, float par4, float par5, float par6, float scale) {
//        super.prep(entity, par2, par3, par4, par5, par6, scale);
//    }
//
//    @Override
//    public void post(Entity entity, float par2, float par3, float par4, float par5, float par6, float scale) {
//        super.post(entity, par2, par3, par4, par5, par6, scale);
//    }
//
//    /**
//     * Sets the models various rotation angles then renders the model.
//     */
//    @Override
//    public void render(Entity entity, float par2, float par3, float par4, float par5, float par6, float scale) {
//        prep(entity, par2, par3, par4, par5, par6, scale);
//        setRotationAngles(par2, par3, par4, par5, par6, scale, entity);
//        super.render(entity, par2, par3, par4, par5, par6, scale);
//        post(entity, par2, par3, par4, par5, par6, scale);
//    }
//
//    @Override
//    public NBTTagCompound renderSpec() {
//        return null;
//    }
//
//    @Override
//    public void renderSpec_$eq(NBTTagCompound renderSpec) {
//
//    }
//
//    @Override
//    public int visibleSection() {
//        return 0;
//    }
//
//    @Override
//    public void visibleSection_$eq(int visibleSection) {
//
//    }
//}
//
