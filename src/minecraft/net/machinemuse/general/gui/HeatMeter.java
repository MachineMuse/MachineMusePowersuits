package net.machinemuse.general.gui;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.block.Block;
import net.minecraft.util.Icon;
import org.lwjgl.opengl.GL11;

public class HeatMeter {
    int xsize = 8;
    int ysize = 32;

    public void draw(double xpos, double ypos, double value) {
        MuseRenderer.pushTexture(MuseRenderer.BLOCK_TEXTURE_QUILT);
        MuseRenderer.blendingOn();
        MuseRenderer.on2D();
        Icon icon = Block.lavaStill.getIcon(0, 0);
        drawFluid(xpos, ypos, value, icon);
        drawGlass(xpos, ypos);
        MuseRenderer.off2D();
        MuseRenderer.blendingOff();
        MuseRenderer.popTexture();
    }

    public void drawFluid(double xpos, double ypos, double value, Icon icon) {

        double bottomY = (ypos + ysize);
        double topY = (ypos + ysize * (1 - value));
        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 0.5);
        while (bottomY - 8 > topY) {
            MuseRenderer.drawIconAt(xpos * 2, (bottomY - 8) * 2, icon, Colour.WHITE);
            bottomY -= 8;
        }
        MuseRenderer.drawIconPartial(xpos * 2, (bottomY - 8) * 2, icon, Colour.WHITE, 0, (topY - bottomY + 8) * 2, 16, 16);
        GL11.glPopMatrix();
    }

    public void drawGlass(double xpos, double ypos) {
        MuseRenderer.pushTexture(Config.GLASS_TEXTURE);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(0, 0);
        GL11.glVertex2d(xpos, ypos);
        GL11.glTexCoord2d(0, 1);
        GL11.glVertex2d(xpos, ypos + ysize);
        GL11.glTexCoord2d(1, 1);
        GL11.glVertex2d(xpos + xsize, ypos + ysize);
        GL11.glTexCoord2d(1, 0);
        GL11.glVertex2d(xpos + xsize, ypos);
        GL11.glEnd();
        MuseRenderer.popTexture();
    }
}
