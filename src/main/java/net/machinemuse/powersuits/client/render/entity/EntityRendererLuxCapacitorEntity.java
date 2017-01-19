package net.machinemuse.powersuits.client.render.entity;

import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.entity.EntityLuxCapacitor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.obj.OBJModel;
import org.lwjgl.opengl.GL11;

//import net.minecraftforge.client.model.AdvancedModelLoader;
//import net.minecraftforge.client.model.obj.WavefrontObject;

public class EntityRendererLuxCapacitorEntity extends MuseEntityRenderer <EntityLuxCapacitor> {
    public EntityRendererLuxCapacitorEntity(RenderManager renderManager) {
        super(renderManager);
    }

//    IBakedModel luxCapModel =

    @Override
    public void doRender(EntityLuxCapacitor entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        Tessellator tessellator = Tessellator.getInstance();
        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        // FIXME:

//        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().
//                renderModel(entity.worldObj,
//                        MPSModels.luxCapacitormodel(entity.color, EnumFacing.UP),
//                        BlockLuxCapacitor.getDefaultState(),
//                        new BlockPos(x, y, z),
//                        Tessellator.getInstance().getBuffer(),
//                        true);
        tessellator.draw();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();







        System.out.println("trying to render something here");

//        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }


//    protected static WavefrontObject lightmodel;
//    protected static WavefrontObject framemodel;
//
//    public static WavefrontObject getLightModel() {
//        if (lightmodel == null) {
//            lightmodel = (WavefrontObject) AdvancedModelLoader.loadModel(new ResourceLocation(Config.RESOURCE_PREFIX + "models/lightCore.obj"));
//        }
//        return lightmodel;
//    }
//
//    public static WavefrontObject getFrameModel() {
//        if (framemodel == null) {
//            framemodel = (WavefrontObject) AdvancedModelLoader.loadModel(new ResourceLocation(Config.RESOURCE_PREFIX + "models/lightBase.obj"));
//        }
//        return framemodel;
//    }
//
//    @Override
//    public void doRender(Entity undifferentiatedentity, double x, double y, double z, float yaw, float partialTickTime) {
//        EntityLuxCapacitor entity = (EntityLuxCapacitor) undifferentiatedentity;
//        MuseTextureUtils.pushTexture(Config.TEXTURE_PREFIX + "models/thusters_uvw_2.png");
//        glPushMatrix();
//        glTranslated(x, y, z);
//        double scale = 0.0625;
//        glScaled(scale, scale, scale);
//        getFrameModel().renderAll();
//        RenderState.glowOn();
//        new Colour(entity.red, entity.green, entity.blue, 1.0).doGL();
//        getLightModel().renderAll();
//        RenderState.glowOff();
//        glPopMatrix();
//        MuseTextureUtils.popTexture();
//    }
}
