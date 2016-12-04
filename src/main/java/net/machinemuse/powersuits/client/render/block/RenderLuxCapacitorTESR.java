package net.machinemuse.powersuits.client.render.block;

import net.machinemuse.numina.render.MuseTESR;

public class RenderLuxCapacitorTESR extends MuseTESR {
    //TODO!! everything

//        implements ISimpleBlockRenderingHandler {
//    protected static WavefrontObject lightmodel;
//    protected static WavefrontObject framemodel;
//    protected final int renderId;
//
//    public RenderLuxCapacitorTESR(int renderId) {
//        this.renderId = renderId;
//    }
//
//    public static OBJModel getLightModel() {
//        if (lightmodel == null) {
//            lightmodel = (OBJModel) OBJLoader.INSTANCE.loadModel(new ResourceLocation(Config.RESOURCE_PREFIX + "models/lightCore.obj"));
//        }
//        return lightmodel;
//    }
//
//    public static OBJModel getFrameModel() {
//        if (framemodel == null) {
//            framemodel = (OBJModel) OBJLoader.INSTANCE.loadModel(new ResourceLocation(Config.RESOURCE_PREFIX + "models/lightBase.obj"));
//        }
//        return framemodel;
//    }
//
//    @Override
//    public void renderAt(TileEntity undifferentiatedtileentity, double x, double y, double z, float partialTickTime) {
//        TileEntityLuxCapacitor tileentity = (TileEntityLuxCapacitor) undifferentiatedtileentity;
//        this.bindTextureByName(Config.TEXTURE_PREFIX + "models/thusters_uvw_2.png");
//        glPushMatrix();
//        glTranslated(x + 0.5, y + 0.5, z + 0.5);
//        double scale = 0.0625;
//        glScaled(scale, scale, scale);
//        switch (tileentity.side) {
//            case DOWN:
//                glTranslated(0, -8, 0);
//                break;
//            case EAST:
//                glRotatef(90, 0, 0, 1);
//                glTranslated(0, -8, 0);
//                break;
//            case NORTH:
//                glRotatef(90, 1, 0, 0);
//                glTranslated(0, -8, 0);
//                break;
//            case SOUTH:
//                glRotatef(-90, 1, 0, 0);
//                glTranslated(0, -8, 0);
//                break;
//            case UP:
//                glRotatef(180, 1, 0, 0);
//                glTranslated(0, -8, 0);
//                break;
//            case WEST:
//                glRotatef(-90, 0, 0, 1);
//                glTranslated(0, -8, 0);
//                break;
//            default:
//                break;
//
//        }
//        getFrameModel().renderAll();
//        RenderState.glowOn();
//        new Colour(tileentity.red, tileentity.green, tileentity.blue, 1.0).doGL();
//        getLightModel().renderAll();
//        RenderState.glowOff();
//        glPopMatrix();
//    }
//
//    @Override
//    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
//        this.bindTextureByName(Config.TEXTURE_PREFIX + "models/thusters_uvw_2.png");
//        GL11.glPushMatrix();
//        GL11.glTranslated(-0.5, -0.5 + -1.0 / 16.0, -0.5);
//
//        glPushMatrix();
//        double scale = 0.0625;
//        glScaled(scale, scale, scale);
//        getFrameModel().renderAll();
//        RenderState.glowOn();
//        getLightModel().renderAll();
//        RenderState.glowOff();
//        glPopMatrix();
//        GL11.glPopMatrix();
//    }
//
//    // Should do nothing since the tile entity handles all the rendering of the
//    // world block
//    @Override
//    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
//
//        return true;
//    }
//
//    @Override
//    public boolean shouldRender3DInInventory(int modelId) {
//        return true;
//    }
//
//    @Override
//    public int getRenderId() {
//        return renderId;
//    }
}
