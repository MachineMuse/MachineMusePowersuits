package net.machinemuse.powersuits.client.render.entity;

import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.client.render.model.ModelLuxCapacitor;
import net.machinemuse.powersuits.entity.EntityLuxCapacitor;
import net.minecraft.block.BlockDirectional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.lwjgl.opengl.GL11;

import static net.machinemuse.powersuits.block.BlockLuxCapacitor.COLOR;
public class EntityRendererLuxCapacitorEntity extends MuseEntityRenderer <EntityLuxCapacitor> {
    public EntityRendererLuxCapacitorEntity(RenderManager renderManager) {
        super(renderManager);
    }

    ModelLuxCapacitor luxCapacitorModel = new ModelLuxCapacitor();
    IExtendedBlockState blockState;

    @Override
    public void doRender(EntityLuxCapacitor entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (luxCapacitorModel != null) {
            blockState = ((IExtendedBlockState) BlockLuxCapacitor.getInstance().getDefaultState().
                    withProperty(BlockDirectional.FACING, EnumFacing.DOWN)).withProperty(COLOR, entity.color);
            GL11.glPushMatrix();
            GL11.glTranslated(x, y, z);
            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            Tessellator tess = Tessellator.getInstance();
            BufferBuilder buffer = tess.getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
            for (BakedQuad quad : luxCapacitorModel.getQuads(blockState, null, 0)) {
                buffer.addVertexData(quad.getVertexData());
                ForgeHooksClient.putQuadColor(buffer, quad, Colour.WHITE.getInt());
            }
            tess.draw();
            GL11.glPopMatrix();
        }
    }
}