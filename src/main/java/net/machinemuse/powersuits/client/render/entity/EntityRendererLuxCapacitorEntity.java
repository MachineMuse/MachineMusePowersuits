package net.machinemuse.powersuits.client.render.entity;

import net.machinemuse.numina.client.render.entity.MuseEntityRenderer;
import net.machinemuse.numina.math.Colour;
import net.machinemuse.powersuits.basemod.MPSItems;
import net.machinemuse.powersuits.client.model.block.ModelLuxCapacitor;
import net.machinemuse.powersuits.entity.EntityLuxCapacitor;
import net.minecraft.block.BlockDirectional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.lwjgl.opengl.GL11;

import java.util.Random;

import static net.machinemuse.powersuits.block.BlockLuxCapacitor.COLOR;

public class EntityRendererLuxCapacitorEntity extends MuseEntityRenderer<EntityLuxCapacitor> {
    ModelLuxCapacitor luxCapacitorModel = new ModelLuxCapacitor();
    IExtendedBlockState blockState;
    public EntityRendererLuxCapacitorEntity(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityLuxCapacitor entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (luxCapacitorModel != null) {
            blockState = (IExtendedBlockState) ((IExtendedBlockState) MPSItems.INSTANCE.luxCapacitor.getDefaultState().
                    with(BlockDirectional.FACING, EnumFacing.DOWN)).withProperty(COLOR, entity.color);
            GL11.glPushMatrix();
            GL11.glTranslated(x, y, z);
            Minecraft.getInstance().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            Tessellator tess = Tessellator.getInstance();
            BufferBuilder buffer = tess.getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
            for (BakedQuad quad : luxCapacitorModel.getQuads(blockState, null, new Random())) {
                buffer.addVertexData(quad.getVertexData());
                ForgeHooksClient.putQuadColor(buffer, quad, Colour.WHITE.getInt());
            }
            tess.draw();
            GL11.glPopMatrix();
        }
    }
}