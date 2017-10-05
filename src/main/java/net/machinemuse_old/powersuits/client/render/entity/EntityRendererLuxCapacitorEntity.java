package net.machinemuse_old.powersuits.client.render.entity;

import net.machinemuse_old.numina.geometry.Colour;
import net.machinemuse_old.powersuits.client.render.model.LuxCapModelHelper;
import net.machinemuse_old.powersuits.client.render.model.ModelLuxCapacitor;
import net.machinemuse_old.powersuits.common.MPSItems;
import net.machinemuse_old.powersuits.entity.EntityLuxCapacitor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.lwjgl.opengl.GL11;

import static net.machinemuse_old.powersuits.block.BlockLuxCapacitor.COLOR;
import static net.minecraft.block.BlockDirectional.FACING;
import static net.minecraft.util.EnumFacing.DOWN;
public class EntityRendererLuxCapacitorEntity extends MuseEntityRenderer <EntityLuxCapacitor> {
    public EntityRendererLuxCapacitorEntity(RenderManager renderManager) {
        super(renderManager);
    }
    LuxCapModelHelper modelhelper = LuxCapModelHelper.getInstance();


    ModelResourceLocation luxCapFramelocation = modelhelper.getLocationForFacing(EnumFacing.DOWN);
    IBakedModel luxCapFrame = modelhelper.getFrameForFacing(EnumFacing.DOWN);
    IBakedModel luxCapacitorModel;
    IExtendedBlockState blockState;

    @Override
    public void doRender(EntityLuxCapacitor entity, double x, double y, double z, float entityYaw, float partialTicks) {
        blockState = ((IExtendedBlockState) MPSItems.luxCapacitor.getDefaultState().
                withProperty(FACING, DOWN)).withProperty(COLOR, entity.color);
        luxCapacitorModel = (luxCapacitorModel != null) ? luxCapacitorModel : new ModelLuxCapacitor(luxCapFrame);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Tessellator tess = Tessellator.getInstance();
        VertexBuffer buffer = tess.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
        for (BakedQuad quad : luxCapacitorModel.getQuads(blockState, null, 0)) {
            buffer.addVertexData(quad.getVertexData());
            ForgeHooksClient.putQuadColor(buffer, quad, Colour.WHITE.getInt());
        }
        tess.draw();
        GlStateManager.popMatrix();
    }
}