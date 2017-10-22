package net.machinemuse.powersuits.client.renderers.entity;


import net.machinemuse.powersuits.entity.EntityLuxCapacitor;
import net.minecraft.client.renderer.entity.RenderManager;

public class EntityRendererLuxCapacitorEntity extends MuseEntityRenderer <EntityLuxCapacitor> {
    public EntityRendererLuxCapacitorEntity(RenderManager renderManager) {
        super(renderManager);
    }
//    LuxCapModelHelper modelhelper = LuxCapModelHelper.getInstance();
//
//
//    ModelResourceLocation luxCapFramelocation = modelhelper.getLocationForFacing(EnumFacing.DOWN);
//    IBakedModel luxCapFrame = modelhelper.getFrameForFacing(EnumFacing.DOWN);
//    IBakedModel luxCapacitorModel;
//    IExtendedBlockState blockState;

    @Override
    public void doRender(EntityLuxCapacitor entity, double x, double y, double z, float entityYaw, float partialTicks) {
//        blockState = ((IExtendedBlockState) BlockLuxCapacitor.getInstance().getDefaultState().
//                withProperty(FACING, DOWN)).withProperty(COLOUR, entity.color);
//        luxCapacitorModel = (luxCapacitorModel != null) ? luxCapacitorModel : new ModelLuxCapacitorOLD(luxCapFrame);
//
//        GlStateManager.pushMatrix();
//        GlStateManager.translate(x, y, z);
//        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
//        Tessellator tess = Tessellator.getInstance();
//        BufferBuilder buffer = tess.getBuffer();
//        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
//        for (BakedQuad quad : luxCapacitorModel.getQuads(blockState, null, 0)) {
//            buffer.addVertexData(quad.getVertexData());
//            ForgeHooksClient.putQuadColor(buffer, quad, Colour.WHITE.getInt());
//        }
//        tess.draw();
//        GlStateManager.popMatrix();
    }
}