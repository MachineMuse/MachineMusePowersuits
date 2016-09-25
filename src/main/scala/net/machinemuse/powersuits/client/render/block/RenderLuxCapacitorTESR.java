package net.machinemuse.powersuits.client.render.block;

import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.block.TileEntityLuxCapacitor;
import net.machinemuse.powersuits.client.MPSModels;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

public class RenderLuxCapacitorTESR extends TileEntitySpecialRenderer<TileEntityLuxCapacitor> {

    @Override
    public void renderTileEntityAt(TileEntityLuxCapacitor te, double x, double y, double z, float partialTicks, int destroyStage) {
        if(!te.getWorld().isBlockLoaded(te.getPos(), false))
            return;

        BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
        BlockPos blockPos = te.getPos();
        IBlockState state = getWorld().getBlockState(blockPos);

        Colour color = te.getColor();


        System.out.println("facing over here here is: " + te.getFacing().toString());


        IBakedModel model = MPSModels.luxCapacitormodel(te.getColor(), te.getFacing());
        if (model == null){
            return;
        }

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();

        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.pushMatrix();

        RenderHelper.disableStandardItemLighting();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();

        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        vertexbuffer.setTranslation((double) ((float) x - (float) te.getPos().getX()), (double) ((float) y - (float) te.getPos().getY()), (double) ((float) z - (float) te.getPos().getZ()));
        blockRenderer.getBlockModelRenderer().renderModel(te.getWorld(), model, state, blockPos, vertexbuffer,true);
        vertexbuffer.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}