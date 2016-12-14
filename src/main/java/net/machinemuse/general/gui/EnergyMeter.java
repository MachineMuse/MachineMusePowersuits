package net.machinemuse.general.gui;

import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.numina.render.MuseTextureUtils;
import net.machinemuse.numina.render.RenderState;
import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;

public class EnergyMeter extends HeatMeter {
    public void draw(double xpos, double ypos, double value) {
        MuseTextureUtils.pushTexture(MuseTextureUtils.BLOCK_TEXTURE_QUILT);
        IIcon icon = Blocks.water.getIcon(0, 0);
        GL11.glLineWidth(0.5f);
        RenderState.on2D();
        RenderState.blendingOn();
        if (value < 0.0001) {
            Colour.RED.doGL();
        } else if (Math.random() / value < 1) {
            RenderState.texturelessOn();
            MuseRenderer.drawMPDLightning(xpos + xsize * (Math.random() / 2 + 0.25), ypos + ysize * (1 - value), 1, xpos + xsize
                    * (Math.random() / 2 + 0.25), ypos + ysize, 1, Colour.WHITE, 4, 1);
            RenderState.texturelessOff();
        }
        drawFluid(xpos, ypos, value, icon);
        drawGlass(xpos, ypos);
        RenderState.blendingOff();
        RenderState.off2D();
        MuseTextureUtils.popTexture();
    }
}
