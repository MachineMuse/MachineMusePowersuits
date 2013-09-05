package net.machinemuse.powersuits.client.render.block;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:28 PM, 8/3/13
 */
public abstract class MuseTESR extends TileEntitySpecialRenderer {
    public void bindTextureByName(String tex) {
        this.bindTexture(new ResourceLocation(tex));
    }
}
