/**
 * 
 */
package net.machinemuse.powersuits.block;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.opengl.GL11;

/**
 * @author Claire
 * 
 */
public class TinkerTableRenderer extends TileEntitySpecialRenderer {
	protected TinkerTableModel model;

	public TinkerTableRenderer() {
		model = new TinkerTableModel();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y,
			double z, float partialTickTime) {
		ForgeHooksClient.bindTexture("/tinkertable.png", 0);
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
}
