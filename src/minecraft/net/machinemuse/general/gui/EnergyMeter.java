package net.machinemuse.general.gui;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.block.Block;
import net.minecraft.util.Icon;
import org.lwjgl.opengl.GL11;

public class EnergyMeter extends HeatMeter {
    public void draw(double xpos, double ypos, double value) {
        MuseRenderer.pushTexture(MuseRenderer.BLOCK_TEXTURE_QUILT);
        Icon icon = Block.waterStill.getIcon(0, 0);
        GL11.glLineWidth(0.5f);
        MuseRenderer.on2D();
        MuseRenderer.blendingOn();
        if (value < 0.0001) {
            Colour.RED.doGL();
        } else if (Math.random() / value < 1) {
            MuseRenderer.texturelessOn();
            MuseRenderer.drawMPDLightning(xpos + xsize * (Math.random() / 2 + 0.25), ypos + ysize * (1 - value), 1, xpos + xsize
                    * (Math.random() / 2 + 0.25), ypos + ysize, 1, Colour.WHITE, 4, 1);
            MuseRenderer.texturelessOff();
        }
        drawFluid(xpos, ypos, value, icon);
        drawGlass(xpos, ypos);
        MuseRenderer.blendingOff();
        MuseRenderer.off2D();
        MuseRenderer.popTexture();
    }
}
