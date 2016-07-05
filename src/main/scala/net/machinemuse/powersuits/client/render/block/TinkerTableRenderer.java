package net.machinemuse.powersuits.client.render.block;

import net.machinemuse.powersuits.block.TileEntityTinkerTable;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Rendering handler for the tinkertable
 *
 * @author MachineMuse
 */
public class TinkerTableRenderer extends TileEntitySpecialRenderer<TileEntityTinkerTable> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("powersuits:textures/blocks/models/tinkertable_tx.png");
    private final TinkerTableModel model;

    public TinkerTableRenderer() {
        this.model = new TinkerTableModel();
    }

    @Override
    public void renderTileEntityAt(TileEntityTinkerTable tileEntityTinkerTable, double x, double y, double z, float v3, int i) {
        // Not sure what param i is but seems to always be -1

        float scale = 0.0625F;
        //The PushMatrix tells the renderer to "start" doing something.
        GL11.glPushMatrix();
        //This is setting the initial location.
        GL11.glTranslatef((float) x, (float) y, (float) z);
        //This is the texture of your block. It's pathed to be the same place as your other block here.
        this.bindTexture(TEXTURE);
        //This rotation part is very important! Without it, your model will render upside-down! And for some reason you DO need PushMatrix again!
        GL11.glPushMatrix();
        GL11.glRotatef(0F, 0.0F, 0.0F, 1.0F);
        //A reference to your Model file. Again, very important.
        this.model.doRender(tileEntityTinkerTable, x, y, z, scale, 0.0F); //
        //Tell it to stop rendering for both the PushMatrix's
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }
}