/**
 *
 */
package net.machinemuse.powersuits.client.render.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.machinemuse.powersuits.common.Config;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

/**
 * Rendering handler for the tinkertable
 *
 * @author MachineMuse
 */
public class TinkerTableRenderer extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {
    protected TinkerTableModel model;
    protected int renderId;

    public TinkerTableRenderer(int renderId) {
        model = new TinkerTableModel();
        this.renderId = renderId;
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTickTime) {
        this.bindTextureByName(Config.TINKERTABLE_TEXTURE_PATH);
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);

        model.doRender(null, x, y, z, partialTickTime, partialTickTime);
        // float texturex = 80 / 256.0f;
        // float texturey = 32 / 256.0f;
        // float texturex2 = 96 / 256.0f;
        // float texturey2 = 48 / 256.0f;
        // MuseRenderer.drawRectPrism(
        // x, x + 1,
        // y + 0.5f, y + 1,
        // z, z + 1,
        // texturex, texturey, texturex2, texturey2);

        GL11.glPopMatrix();

    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        this.bindTextureByName(Config.TINKERTABLE_TEXTURE_PATH);
        GL11.glPushMatrix();
        GL11.glTranslated(-0.5, -0.5 + -1.0 / 16.0, -0.5);
        model.doRender(null, 0, 0, 0, 0, 0);
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
