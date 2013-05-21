package net.machinemuse.powersuits.client.render.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.machinemuse.powersuits.block.TileEntityLuxCapacitor;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class RenderLuxCapacitorTESR extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {
    protected static WavefrontObject lightmodel;
    protected static WavefrontObject framemodel;
    protected int renderId;

    public RenderLuxCapacitorTESR(int renderId) {
        this.renderId = renderId;
    }

    public static WavefrontObject getLightModel() {
        if (lightmodel == null) {
            lightmodel = (WavefrontObject) AdvancedModelLoader.loadModel(Config.RESOURCE_PREFIX + "models/lightCore.obj");
        }
        return lightmodel;
    }

    public static WavefrontObject getFrameModel() {
        if (framemodel == null) {
            framemodel = (WavefrontObject) AdvancedModelLoader.loadModel(Config.RESOURCE_PREFIX + "models/lightBase.obj");
        }
        return framemodel;
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTickTime) {
        this.bindTextureByName(Config.TEXTURE_PREFIX + "models/thusters_uvw_2.png");
        glPushMatrix();
        glTranslated(x + 0.5, y + 0.5, z + 0.5);
        double scale = 0.0625;
        glScaled(scale, scale, scale);
        switch (((TileEntityLuxCapacitor) tileentity).side) {
            case DOWN:
                glTranslated(0, -8, 0);
                break;
            case EAST:
                glRotatef(90, 0, 0, 1);
                glTranslated(0, -8, 0);
                break;
            case NORTH:
                glRotatef(90, 1, 0, 0);
                glTranslated(0, -8, 0);
                break;
            case SOUTH:
                glRotatef(-90, 1, 0, 0);
                glTranslated(0, -8, 0);
                break;
            case UP:
                glRotatef(180, 1, 0, 0);
                glTranslated(0, -8, 0);
                break;
            case WEST:
                glRotatef(-90, 0, 0, 1);
                glTranslated(0, -8, 0);
                break;
            default:
                break;

        }
        getFrameModel().renderAll();
        MuseRenderer.glowOn();
        getLightModel().renderAll();
        MuseRenderer.glowOff();
        glPopMatrix();
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        this.bindTextureByName(Config.TEXTURE_PREFIX + "models/thusters_uvw_2.png");
        GL11.glPushMatrix();
        GL11.glTranslated(-0.5, -0.5 + -1.0 / 16.0, -0.5);

        glPushMatrix();
        double scale = 0.0625;
        glScaled(scale, scale, scale);
        getFrameModel().renderAll();
        MuseRenderer.glowOn();
        getLightModel().renderAll();
        MuseRenderer.glowOff();
        glPopMatrix();
        GL11.glPopMatrix();

    }

    // Should do nothing since the tile entity handles all the rendering of the
    // world block
    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory() {
        return true;
    }

    @Override
    public int getRenderId() {
        return renderId;
    }
}
