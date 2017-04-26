/**
 *
 */
package net.machinemuse.powersuits.client.render.block;

import net.machinemuse.powersuits.block.TileEntityTinkerTable;
import net.machinemuse.powersuits.common.Config;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

/**
 * Rendering handler for the tinkertable
 *
 * @author MachineMuse
 */
public class TinkerTableRenderer extends TileEntitySpecialRenderer<TileEntityTinkerTable> {
ResourceLocation texture = new ResourceLocation( Config.TEXTURE_PREFIX + "models/tinkerTable-solid.png");

    protected final TinkerTableModel model;

    public TinkerTableRenderer () {
        model = new TinkerTableModel();
    }

    @Override
    public void renderTileEntityAt(TileEntityTinkerTable te, double x, double y, double z, float partialTicks, int destroyStage) {
        this.bindTexture(texture);
        GlStateManager.pushMatrix();
        if (te != null) {
            EnumFacing facing = te.getFacing();
            if (facing == null) facing = EnumFacing.NORTH;

            // the rotation creates an offset. Apparently the model is not centered on the axis of rotation.
            switch (te.getFacing()) {
                case EAST:
                    GlStateManager.translate(x, y, z +1);
                    break;
                case WEST:
                    GlStateManager.translate(x+1, y, z);
                    break;
                case SOUTH:
                    GlStateManager.translate(x+1, y, z+1);
                    break;
                default:
                    GlStateManager.translate(x, y, z);
                    break;
            }

            int degrees = facing.getOpposite().getHorizontalIndex() * 90;
            GlStateManager.rotate(degrees, 0.0F, 1.0F, 0.0F);
        } else
            GlStateManager.translate(x, y, z); // y is up, x is east, z is south
        model.doRender(null, x, y, z, partialTicks);
        GlStateManager.popMatrix();
    }
}