//package net.machinemuse.powersuits.client.render.block;
//
//import net.machinemuse.numina.geometry.Colour;
//import net.machinemuse.numina.render.MuseTESR;
//import net.machinemuse.powersuits.block.TileEntityLuxCapacitor;
//import net.machinemuse.powersuits.common.MPSItems;
//import net.minecraft.block.Block;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.*;
//import net.minecraft.client.renderer.block.model.IBakedModel;
//import net.minecraft.client.renderer.texture.TextureMap;
//import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
//import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
//import net.minecraft.util.EnumBlockRenderType;
//import net.minecraft.util.EnumFacing;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.world.World;
//import net.minecraftforge.common.property.ExtendedBlockState;
//import net.minecraftforge.common.property.IExtendedBlockState;
//import org.lwjgl.opengl.GL11;
//
//import static net.machinemuse.powersuits.block.BlockLuxCapacitor.COLOR;
//import static net.machinemuse.powersuits.event.ModelBakeEventHandler.luxCapacitorModel;
//import static net.minecraft.block.BlockDirectional.FACING;
//import static net.minecraft.util.EnumFacing.DOWN;
//import static net.minecraft.util.EnumFacing.UP;
//
//public class RenderLuxCapacitorTESR extends TileEntitySpecialRenderer<TileEntityLuxCapacitor> {
//    private BlockRendererDispatcher blockRenderer;
//
//
//    @Override
//    public void renderTileEntityAt(TileEntityLuxCapacitor te, double x, double y, double z, float partialTicks, int destroyStage) {
//        if(blockRenderer == null) blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
//        BlockPos blockpos = te.getPos();
//        World world = this.getWorld();
//        IBlockState iblockstate = world.getBlockState(blockpos);
//        if (iblockstate.getRenderType() == EnumBlockRenderType.ENTITYBLOCK_ANIMATED) {
//            Colour color = te.getColor();
//            EnumFacing facing = iblockstate.getValue(FACING);
//
//            IExtendedBlockState extendedBlockState = ((IExtendedBlockState) iblockstate).withProperty(COLOR, color);
//
//
//            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
//            GlStateManager.pushMatrix();
//            GlStateManager.disableLighting();
//
//
//
////            GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
////            GL11.glDisable(GL11.GL_CULL_FACE);
//
//
//
//            Tessellator tessellator = Tessellator.getInstance();
//            VertexBuffer vertexbuffer = tessellator.getBuffer();
//            vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
//
//
//
//
//            GlStateManager.translate((float)(x - (double)blockpos.getX()), (float)(y - (double)blockpos.getY()), (float)(z - (double)blockpos.getZ()));
//            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
//            blockrendererdispatcher.getBlockModelRenderer().renderModel(
//                    world,
//                    luxCapacitorModel,
//                    extendedBlockState,
//                    blockpos,
//                    vertexbuffer,
//                    false,
//                    MathHelper.getPositionRandom(te.getPos()));
//            tessellator.draw();
//
////            GL11.glPopAttrib();
//
//
//            GlStateManager.enableLighting();
//            GlStateManager.popMatrix();
//        }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
////        System.out.println("-- START ----------------------------- ");
////        System.out.println("block pos is " + te.getPos().toString());
////        System.out.println("[ x: " +  x + " ][ y: " + y + " ][ z: " + z + " ]");
////        System.out.println("-- END ----------------------------- ");
////
////
////
////
////
////
////        Block block = iblockstate.getBlock();
////        Colour color = te.getColor();
////        EnumFacing facing = iblockstate.getValue(FACING);
////        if (facing == UP || facing == DOWN )
////            facing = facing.getOpposite();
////
////
////        Tessellator tessellator = Tessellator.getInstance();
////        VertexBuffer vertexbuffer = tessellator.getBuffer();
////        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
////        RenderHelper.disableStandardItemLighting();
//////        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
//////        GlStateManager.enableBlend();
////        GlStateManager.disableCull();
////
////        if (Minecraft.isAmbientOcclusionEnabled()) {
////            GlStateManager.shadeModel(GL11.GL_SMOOTH);
////        } else {
////            GlStateManager.shadeModel(GL11.GL_FLAT);
////        }
////
////        vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
////
//////: -- START -----------------------------
//////: block pos is BlockPos{x=684, y=8, z=-457}
//////: [ x: 0.6040289060471196 ][ y: 1.0206563287359538 ][ z: -2.2201981620303854 ]
//////: -- END -----------------------------
////        // actual model render location {x=683, y=6, z=-458}
////
////        BlockPos test = new BlockPos (x - blockpos.getX(), y - blockpos.getY(), z - blockpos.getZ());
////
////        System.out.println("test pos is " + test.toString());
////
////
////
////
////        vertexbuffer.setTranslation(x - blockpos.getX(), y - blockpos.getY(), z - blockpos.getZ()); // renders but postion is off
//////        vertexbuffer.setTranslation(blockpos.getX(), blockpos.getY(), blockpos.getZ()); // doesn't even render
//////        vertexbuffer.setTranslation(x + blockpos.getX(), y + blockpos.getY(), z + blockpos.getZ());
////
////
//////        glTranslated(x + 0.5, y + 0.5, z + 0.5);
//////        vertexbuffer.setTranslation(x + 0.5, y + 0.5, z + 0.5);
////
////
////
////        this.blockRenderer.getBlockModelRenderer().renderModel(
////                world,
////                luxCapacitorModel.getBakedLuxCapModel(facing, color),
////                world.getBlockState(te.getPos()),
////                te.getPos(),
////                Tessellator.getInstance().getBuffer(), false);
////
////        vertexbuffer.setTranslation(0.0D, 0.0D, 0.0D);
////        tessellator.draw();
//        RenderHelper.enableStandardItemLighting();
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
////    @Override
////    public void renderTileEntityAt(TileEntityLuxCapacitor te, double x, double y, double z, float partialTicks, int destroyStage) {
//
////
////
////
////
////        GlStateManager.pushAttrib();
////        GlStateManager.pushMatrix();
////
////        // Translate to the location of our tile entity
////        GlStateManager.translate(x, y, z);
////        GlStateManager.disableRescaleNormal();
////
////
////
////
////
//////        GlStateManager.pushMatrix();
////
////        RenderHelper.disableStandardItemLighting();
////        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
////        if (Minecraft.isAmbientOcclusionEnabled()) {
////            GlStateManager.shadeModel(GL11.GL_SMOOTH);
////        } else {
////            GlStateManager.shadeModel(GL11.GL_FLAT);
////        }
////
//////        World world = te.getWorld();
////        // Translate back to local view coordinates so that we can do the acual rendering here
//////        GlStateManager.translate(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ());
////
////        Tessellator tessellator = Tessellator.getInstance();
////        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
//////        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
//////                world,
//////                getBakedModel(),
//////                world.getBlockState(te.getPos()),
//////                te.getPos(),
//////                Tessellator.getInstance().getBuffer(), false);
////
////        IExtendedBlockState blockState = (IExtendedBlockState)te.getWorld().getBlockState(te.getPos());
////        EnumFacing facing = blockState.getValue(FACING);
////        Colour color = te.getColor();
////
////
//////---------------------------------------------------
////        IBakedModel ibakedmodel = luxCapacitorModel.getBakedLuxCapModel(facing, color);
////        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
////                te.getWorld(),
////                ibakedmodel,
////                blockState,
//////                te.getPos(),
////                new BlockPos(x,y,z),
////                Tessellator.getInstance().getBuffer(),
////                true);
////        tessellator.draw();
////
////        RenderHelper.enableStandardItemLighting();
//////        GlStateManager.popMatrix();
////
////
////
////
////
////        GlStateManager.popMatrix();
////        GlStateManager.popAttrib();
//////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
//// -- START -----------------------------
////  block pos is BlockPos{x=-1721, y=4, z=1516}
////  x: 4.64983262197029y: -8.621335251230914z: 3.5038135212548696
//// -- END -----------------------------
//
////        GlStateManager.pushMatrix();
////        GlStateManager.translate(x, y, z);
////
////
////        Tessellator tessellator = Tessellator.getInstance();
////        VertexBuffer vertexBuffer = tessellator.getBuffer();
////        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
////        RenderHelper.disableStandardItemLighting();
////        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//////        GlStateManager.enableBlend();
//////        GlStateManager.disableCull();
////        if (Minecraft.isAmbientOcclusionEnabled())
////            GlStateManager.shadeModel(GL11.GL_SMOOTH);
////        else
////            GlStateManager.shadeModel(GL11.GL_FLAT);
////
////        vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
////
////        IExtendedBlockState blockState = (IExtendedBlockState)te.getWorld().getBlockState(te.getPos());
////        EnumFacing facing = blockState.getValue(FACING);
////        Colour color = te.getColor();
////
////
//////---------------------------------------------------
////        IBakedModel ibakedmodel = luxCapacitorModel.getBakedLuxCapModel(facing, color);
////        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
////                te.getWorld(),
////                ibakedmodel,
////                blockState,
////                new BlockPos(x, y, z),
////                vertexBuffer,
////                true);
//////----------------------------------------------------
//////        vertexBuffer.setTranslation(0, 0, 0);
////
//////        vertexBuffer.setTranslation(0, 0, 0);
////
////        tessellator.draw();
////        RenderHelper.enableStandardItemLighting();
////        GlStateManager.popMatrix();
//
//
//
//
////    @Override
////    public void renderTileEntityAt(TileEntityLuxCapacitor te, double x, double y, double z, float partialTicks, int destroyStage) {
////        IBlockState blockState = te.getWorld().getBlockState(te.getPos());
////
////        EnumFacing facing = blockState.getValue(FACING).getOpposite(); // testing, should produce 2 with default rendering enabled
////        Colour color = te.colour;
////
////        GlStateManager.pushMatrix();
////        GlStateManager.translate(x, y, z);
//////        GlStateManager.scale(0.0625, 0.0625, 0.0625);
////
////        RenderHelper.disableStandardItemLighting();
//////        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
////        if (Minecraft.isAmbientOcclusionEnabled()) {
////            GlStateManager.shadeModel(GL11.GL_SMOOTH);
////
////        } else {
////            GlStateManager.shadeModel(GL11.GL_FLAT);
////        }
////        vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
//
////        RenderHelper.enableStandardItemLighting();
////        GlStateManager.popMatrix();
////    }
//
//
//
//
////TODO!! everything
//
////        implements ISimpleBlockRenderingHandler {
////    protected static WavefrontObject lightmodel;
////    protected static WavefrontObject framemodel;
////    protected final int renderId;
////
////    public RenderLuxCapacitorTESR(int renderId) {
////        this.renderId = renderId;
////    }
////
////    public static MPSTestingMPSTestingOBJModel getLightModel() {
////        if (lightmodel == null) {
////            lightmodel = (MPSTestingMPSTestingOBJModel) OBJLoader.INSTANCE.loadModel(new ResourceLocation(Config.RESOURCE_PREFIX + "models/lightCore.obj"));
////        }
////        return lightmodel;
////    }
////
////    public static MPSTestingMPSTestingOBJModel getFrameModel() {
////        if (framemodel == null) {
////            framemodel = (MPSTestingMPSTestingOBJModel) OBJLoader.INSTANCE.loadModel(new ResourceLocation(Config.RESOURCE_PREFIX + "models/lightBase.obj"));
////        }
////        return framemodel;
////    }
////
////    @Override
////    public void renderAt(TileEntity undifferentiatedtileentity, double x, double y, double z, float partialTickTime) {
////        TileEntityLuxCapacitor tileentity = (TileEntityLuxCapacitor) undifferentiatedtileentity;
////        this.bindTextureByName(Config.TEXTURE_PREFIX + "models/thusters_uvw_2.png");
////        glPushMatrix();
////        glTranslated(x + 0.5, y + 0.5, z + 0.5);
////        double scale = 0.0625;
////        glScaled(scale, scale, scale);
////        switch (tileentity.side) {
////            case DOWN:
////                glTranslated(0, -8, 0);
////                break;
////            case EAST:
////                glRotatef(90, 0, 0, 1);
////                glTranslated(0, -8, 0);
////                break;
////            case NORTH:
////                glRotatef(90, 1, 0, 0);
////                glTranslated(0, -8, 0);
////                break;
////            case SOUTH:
////                glRotatef(-90, 1, 0, 0);
////                glTranslated(0, -8, 0);
////                break;
////            case UP:
////                glRotatef(180, 1, 0, 0);
////                glTranslated(0, -8, 0);
////                break;
////            case WEST:
////                glRotatef(-90, 0, 0, 1);
////                glTranslated(0, -8, 0);
////                break;
////            default:
////                break;
////
////        }
////        getFrameModel().renderAll();
////        RenderState.glowOn();
////        new Colour(tileentity.red, tileentity.green, tileentity.blue, 1.0).doGL();
////        getLightModel().renderAll();
////        RenderState.glowOff();
////        glPopMatrix();
////    }
////
////    @Override
////    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
////        this.bindTextureByName(Config.TEXTURE_PREFIX + "models/thusters_uvw_2.png");
////        GL11.glPushMatrix();
////        GL11.glTranslated(-0.5, -0.5 + -1.0 / 16.0, -0.5);
////
////        glPushMatrix();
////        double scale = 0.0625;
////        glScaled(scale, scale, scale);
////        getFrameModel().renderAll();
////        RenderState.glowOn();
////        getLightModel().renderAll();
////        RenderState.glowOff();
////        glPopMatrix();
////        GL11.glPopMatrix();
////    }
////
////    // Should do nothing since the tile entity handles all the rendering of the
////    // world block
////    @Override
////    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
////
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
