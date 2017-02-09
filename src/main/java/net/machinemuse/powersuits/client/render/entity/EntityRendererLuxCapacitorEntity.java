package net.machinemuse.powersuits.client.render.entity;

import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.numina.render.RenderState;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.MPSItems;
import net.machinemuse.powersuits.entity.EntityLuxCapacitor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.lwjgl.opengl.GL11;

import java.util.concurrent.ThreadLocalRandom;

import static net.machinemuse.powersuits.block.BlockLuxCapacitor.COLOR;
//import static net.machinemuse.powersuits.event.ModelBakeEventHandler.luxCapacitorModel;
import static net.minecraft.block.BlockDirectional.FACING;
import static net.minecraft.util.EnumFacing.DOWN;
import static net.minecraft.util.EnumFacing.UP;

public class EntityRendererLuxCapacitorEntity extends MuseEntityRenderer <EntityLuxCapacitor> {
    public EntityRendererLuxCapacitorEntity(RenderManager renderManager) {
        super(renderManager);
    }

    ResourceLocation textureLocation = new ResourceLocation(Config.RESOURCE_PREFIX + "textures/blocks/LuxCap.png");



    // debugging stuff
//    int min = new Colour(0, 0, 0).getInt();
//    int max = new Colour(255, 255, 255).getInt();
    // end debugging stuff



    @Override
    public void doRender(EntityLuxCapacitor entity, double x, double y, double z, float entityYaw, float partialTicks) {
//
//
//
//        this.bindTexture(textureLocation);
//
//
////        int colour = ThreadLocalRandom.current().nextInt(min, max + 1);
//        GlStateManager.pushMatrix();
////        RenderState.glowOn();
//
//        GlStateManager.translate(x, y, z);
////        GlStateManager.scale(0.0625, 0.0625, 0.0625);
//
//        Tessellator tessellator = Tessellator.getInstance();
//        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
//
//
//        IExtendedBlockState blockState = ((IExtendedBlockState) MPSItems.luxCapacitor.getDefaultState().
//                withProperty(FACING, UP)).withProperty(COLOR, entity.color);
//        IBakedModel ibakedmodel = luxCapacitorModel.getBakedLuxCapModel(UP, entity.color);
//
//        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
//                entity.worldObj,
//                ibakedmodel,
//                blockState,
//                new BlockPos(x, y, z),
//                Tessellator.getInstance().getBuffer(),
//                true);
//        tessellator.draw();
//
////        RenderHelper.enableStandardItemLighting();
//
//        RenderState.glowOff();
//        GlStateManager.popMatrix();
//

    }
}
