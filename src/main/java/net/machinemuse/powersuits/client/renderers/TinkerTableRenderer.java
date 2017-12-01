//package net.machinemuse.powersuits.client.renderers;
//
//import net.machinemuse.powersuits.client.models.TinkerTableModel;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
//import net.minecraft.tileentity.TileEntity;
//
//import static net.machinemuse.powersuits.common.MPSConstants.TINKERTABLE_TEXTURE_PATH;
//
//public class TinkerTableRenderer extends TileEntitySpecialRenderer {
//    protected TinkerTableModel model;
//    protected int renderId;
//
////    public TinkerTableRenderer(int renderId) {
//     public TinkerTableRenderer() {
//
//
//        model = new TinkerTableModel();
////        this.renderId = renderId;
//    }
//
//    @Override
//    public void render(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
//
////         System.out.println("rendering something");
//
//
//        this.bindTexture(TINKERTABLE_TEXTURE_PATH);
//        GlStateManager.pushMatrix();
//        GlStateManager.translate(x, y, z);
//        //GL11.glTranslated(x, y, z);
//
//        model.doRender(null, x, y, z, partialTicks);
//        // float texturex = 80 / 256.0f;
//        // float texturey = 32 / 256.0f;
//        // float texturex2 = 96 / 256.0f;
//        // float texturey2 = 48 / 256.0f;
//        // MuseRenderer.drawRectPrism(
//        // x, x + 1,
//        // y + 0.5f, y + 1,
//        // z, z + 1,
//        // texturex, texturey, texturex2, texturey2);
//        GlStateManager.popMatrix();
////        GL11.glPopMatrix();
//    }
//
//
//
//
////
////    @Override
////    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
////        this.bindTextureByName(Config.TINKERTABLE_TEXTURE_PATH);
////        GL11.glPushMatrix();
////        GL11.glTranslated(-0.5, -0.5 + -1.0 / 16.0, -0.5);
////        model.doRender(null, 0, 0, 0, 0);
////        GL11.glPopMatrix();
////
////    }
////
////    // Should do nothing since the tile entity handles all the rendering of the
////    // world block
////    @Override
////    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
////        return true;
////    }
////
////    @Override
////    public boolean shouldRender3DInInventory(int modelId) {
////        return true;
////    }
////
////    @Override
////    public int getRenderId() {
////        return renderId;
////    }
//}
