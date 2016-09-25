package net.machinemuse.powersuits.client.render.entity;

import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.client.MPSModels;
import net.machinemuse.powersuits.entity.EntityLuxCapacitor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

public class EntityRenderLuxCapacitorEntity extends MuseEntityRender<EntityLuxCapacitor> {
    public EntityRenderLuxCapacitorEntity(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityLuxCapacitor entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        Tessellator tessellator = Tessellator.getInstance();
        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().
                renderModel(entity.worldObj,
                        MPSModels.luxCapacitormodel(entity.color, EnumFacing.UP),
                            BlockLuxCapacitor.instance.getDefaultState(),
                            new BlockPos(x, y, z),
                            Tessellator.getInstance().getBuffer(),
                            true);
        tessellator.draw();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}