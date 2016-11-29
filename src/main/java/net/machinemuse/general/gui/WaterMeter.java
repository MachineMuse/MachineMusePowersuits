package net.machinemuse.general.gui;

import net.machinemuse.numina.render.MuseTextureUtils;
import net.machinemuse.numina.render.RenderState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fluids.FluidRegistry;


/**
 * Created by User: Andrew2448
 * 4:30 PM 6/21/13
 */
public class WaterMeter extends HeatMeter {
    TextureAtlasSprite icon = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(FluidRegistry.WATER.getStill().toString());
    public void draw(double xpos, double ypos, double value) {
        MuseTextureUtils.pushTexture(MuseTextureUtils.TEXTURE_QUILT);
        RenderState.blendingOn();
        RenderState.on2D();
        drawFluid(xpos, ypos, value, icon);
        drawGlass(xpos, ypos);
        RenderState.off2D();
        RenderState.blendingOff();
        MuseTextureUtils.popTexture();
    }
}