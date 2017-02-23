package net.machinemuse.powersuits.client.render.entity;

import net.machinemuse.numina.render.RenderState;
import net.machinemuse.powersuits.client.render.model.LuxCapModelHelper;
import net.machinemuse.powersuits.client.render.model.ModelLuxCapacitor;
import net.machinemuse.powersuits.common.MPSItems;
import net.machinemuse.powersuits.entity.EntityLuxCapacitor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.lwjgl.opengl.GL11;

import static net.machinemuse.powersuits.block.BlockLuxCapacitor.COLOR;
import static net.minecraft.block.BlockDirectional.FACING;
import static net.minecraft.util.EnumFacing.DOWN;
public class EntityRendererLuxCapacitorEntity extends MuseEntityRenderer <EntityLuxCapacitor> {
    public EntityRendererLuxCapacitorEntity(RenderManager renderManager) {
        super(renderManager);
    }
    LuxCapModelHelper modelhelper = LuxCapModelHelper.getInstance();
    ModelResourceLocation luxCapFramelocation = modelhelper.getLocationForFacing(EnumFacing.DOWN);
    IBakedModel luxCapFrame = modelhelper.luxCapCleanModelMap.get(luxCapFramelocation);
    IBakedModel luxCapacitorModel;

    @Override
    public void doRender(EntityLuxCapacitor entity, double x, double y, double z, float entityYaw, float partialTicks) {
        IExtendedBlockState blockState = ((IExtendedBlockState) MPSItems.luxCapacitor.getDefaultState().
                withProperty(FACING, DOWN)).withProperty(COLOR, entity.color);
        luxCapacitorModel = (luxCapacitorModel != null) ? luxCapacitorModel : new ModelLuxCapacitor(luxCapFrame);

        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.pushMatrix();
        RenderState.glowOn();
        GlStateManager.translate(x, y, z);
        Tessellator tessellator = Tessellator.getInstance();

        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
                entity.worldObj,
                luxCapacitorModel,
                blockState,
                new BlockPos(x, y, z),
                Tessellator.getInstance().getBuffer(),
                true);
        tessellator.draw();
        RenderState.glowOff();
        GlStateManager.popMatrix();
    }
}